package timepiece.watchface;

import timepiece.TimeNamesEnglish;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenAlg {

    private static final int POPULATION_SIZE = 1000;
    private static final int NONE_FOUND_PENALTY = -10000;
    private static final int NOT_MATCHED_PENALTY = -10;
    private static final int MATCH_BONUS = 1000;
    private static final int WORD_MATCH_BONUS = 10;
    public static final int COST_OF_THIS_ALGORITHM_BECOMING_SKYNET = 999999999;
    private static final int LOW_SPLIT_BONUS = 100;
    private static final int VARIANCE_PENALTY = 1;

    private char[] includedChar = null;
    private Solution solution = null;
    Random rand = new Random();
    private List<Pattern>[][] patterns = null;
    private List<Pattern> wordPatterns = null;
    private HashSet<String> inclWords = new HashSet<>();

    public static void main(String[] args) {
        GenAlg gen = new GenAlg();
        gen.run();
    }

    private void run() {

        System.out.println("creating patterns");
        createPatterns(TimeNamesEnglish.getTimeStrings());

        System.out.println("loading solution");
        loadSolution();

        List<GenThread> threads = new LinkedList<>();
        for (int i = 0; i < 25; i++) {
            GenThread gt = new GenThread(this);
            gt.start();
            threads.add(gt);
        }

        for (GenThread genThread : threads) {
            try {
                genThread.join();
            } catch (InterruptedException ignored) {
            }
        }

    }

    public void incGeneration() {
        synchronized (solution) {
            solution.generation++;

            if (solution.generation % 1000 == 0) {
                System.out.printf("best:  %4d: %s\n", solution.generation, solution.fittest.toString());
//				System.out.printf("worst: %4d: %s\n",solution.generation,solution.worst.toString());
            }

            if (solution.generation % 10000 == 0) {
                System.out.println("saving solution");
                saveSolution();

                try {
                    FileWriter out = new FileWriter("fittnes.txt", true);
                    out.write("" + solution.generation);
                    out.write("," + solution.fittest.fittnes);
                    out.write("," + solution.fittest.checkedTimesNOK);
                    out.write("\n");
                    out.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public boolean doAdd(Candidate candidate) {
        int max = solution.fittest.fittnes;
        int min = solution.worst.fittnes;
        if (max == min) return true;
        if (candidate.fittnes < min) return rand.nextInt(100) < 10;

        double prob = (candidate.fittnes - min) / (max - min) + 0.1;
        return rand.nextDouble() < prob;
    }

    public Candidate getRandom(boolean good) {
        synchronized (solution) {
            int r = -1;
            do {
                r = (int) (Math.abs(rand.nextGaussian() * 0.5) * solution.candidates.size());
                if (!good) r = solution.candidates.size() - r + 1;
            } while (r < 0 || r >= solution.candidates.size());
            if (!good && r == 0) r++;
            return solution.candidates.get(r);
        }
    }

    @SuppressWarnings("unchecked")
    private void createPatterns(List<String>[][] strings) {
        HashSet<Character> inclChar = new HashSet<>();

        patterns = new List[strings.length][];

        for (int hour = 0; hour < strings.length; hour++) {
            patterns[hour] = new List[strings[hour].length];
            for (int minute = 0; minute < strings[hour].length; minute++) {
                patterns[hour][minute] = new LinkedList<>();
                for (String time : strings[hour][minute]) {

                    StringBuilder regex = new StringBuilder();
                    String[] words = time.split(" ");
                    for (String word : words) {

                        if (regex.length() == 0) {
                            regex.append("(.*)");
                        } else {
                            regex.append("(.+)");
                        }

                        StringBuilder subRegex = new StringBuilder();
                        String[] subWords = word.split("\\+");
                        for (String string : subWords) {
                            if (subRegex.length() > 0) {
                                subRegex.append("(.*)");
                            }
                            subRegex.append("(");
                            subRegex.append(string);
                            subRegex.append(")");
                            inclWords.add(string);

                            char[] chars = string.toCharArray();
                            for (char c : chars) {
                                inclChar.add(c);
                            }
                        }

                        regex.append(subRegex.toString());

                    }
                    regex.append("(.*)");
                    patterns[hour][minute].add(Pattern.compile(regex.toString()));
                    //System.out.printf("%02d:%02d : %s\n", hour + 1, minute * 5, regex.toString());
                    System.out.printf("\t/%s/,\n", regex.toString());
                }
            }
        }

        StringBuilder charSet = new StringBuilder();
        for (Character character : inclChar) {
            if (character != ' ') charSet.append(character);
        }
        includedChar = charSet.toString().toCharArray();
        System.out.println(charSet.toString());

        wordPatterns = new LinkedList<>();
        for (String string : inclWords) {
            wordPatterns.add(Pattern.compile(".*" + string + ".*"));
        }
    }

    public Candidate createRandom() {
        Candidate cand = new Candidate();
        char[] c = cand.candidate.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] != '|') {
                c[i] = getRandomChar();
            }
        }
        cand.candidate = new String(c);
        for (int j = 0; j < 100; j++) {
            cand = addRandomWord(cand);
        }
        return cand;
    }

    private void loadSolution() {
        try {
            JAXBContext jc = JAXBContext.newInstance(Solution.class);
            Unmarshaller um = jc.createUnmarshaller();
            this.solution = (Solution) um.unmarshal(new File("solution.xml"));
            for (int i = solution.candidates.size(); i < POPULATION_SIZE; i++) {
                this.solution.candidates.add(createRandom());
            }
            for (Candidate cand : solution.candidates) {
                calcFittness(cand);
            }
        } catch (Exception e) {
            System.out.println("solution not found");
            this.solution = new Solution();

            for (int i = 0; i < POPULATION_SIZE; i++) {

                if (i % 10 == 0) System.out.print(".");
                Candidate c = createRandom();
                if (i < 0 /*POPULATION_SIZE / 2*/) {
//					c.candidate = "fünffzehnX|dreiXnachX|vorelfhalb|uhrviertel|siebenacht|dreisechsX|neundzwölf|zweinsuhrX|elfünfzehn|vierXXXXXX";
//					c.candidate = "fünffzehnu|dreihnacht|vorelfhalb|uhrviertel|siebenacht|dreisechsz|neundzwölf|zweinsluhr|elfünfzehn|undvierzig";

                    //11x11
                    //c.candidate = "XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX|XXXXXXXXXXX";

                    //10x10
                    //c.candidate = "XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX|XXXXXXXXXX";

                    //circle
                    c.candidate = "ONER|SETHIRTY|FIFTYPTYHALF|FTENETWENTYR|FIVENTPASTNINE|TIELEVENTHREEH|XSIXTFIFTEENTWOEN|TWELVEEIGHTSEVENR|YWEQUARTERFOURTYE|FOFIVENTCLOCKTENY|NINEEPASTENTOU|FIFTEENFOURTHY|TWOHFIFTYEWR|THIRTYTWENTY|FIVENONE|YTEN";
                }
                calcFittness(c);
                this.solution.candidates.add(c);
            }
            System.out.println();
        }
        sortSolution();
        this.solution.fittest = this.solution.candidates.get(0);
        this.solution.worst = this.solution.candidates.get(this.solution.candidates.size() - 1);
    }

    private void saveSolution() {
        try {
            JAXBContext jc = JAXBContext.newInstance(Solution.class);
            Marshaller ma = jc.createMarshaller();
            ma.marshal(this.solution, new File("solution.xml"));
        } catch (Exception ignored) {
        }
    }

    public void addToSolution(Candidate candidate) {
        synchronized (solution) {
            if (this.solution.candidates.size() == POPULATION_SIZE) this.removeRandom();
            this.solution.candidates.add(candidate);
            if (candidate.fittnes >= this.solution.fittest.fittnes) this.solution.fittest = candidate;
            if (candidate.fittnes <= this.solution.worst.fittnes) this.solution.worst = candidate;
            this.sortSolution();
        }
    }

    private void removeRandom() {
        this.solution.candidates.remove(getRandom(false));
    }

    private void sortSolution() {
        this.solution.candidates.sort((o1, o2) -> {
            Integer i = o1.checkedTimesNOK;
            return i.compareTo(o2.checkedTimesNOK);
        });
    }

    public Candidate mixTogether(Candidate left, Candidate right) {
        Candidate res = new Candidate();
        int pos = rand.nextInt(left.candidate.length());
        res.candidate = left.candidate.substring(0, pos) + right.candidate.substring(pos);
        return res;
    }

    public Candidate changeRandom(Candidate source) {
        Candidate res = new Candidate();

        char[] cand = source.candidate.toCharArray();

        int pos = -1;
        do {
            pos = rand.nextInt(cand.length);
        } while (cand[pos] == '|');

        cand[pos] = getRandomChar();

        res.candidate = new String(cand);
        return res;
    }

    public Candidate addRandomWord(Candidate source) {
        Candidate res = new Candidate();

        Object[] words = inclWords.toArray();
        char[] word = words[rand.nextInt(words.length)].toString().toCharArray();
        char[] cand = source.candidate.toCharArray();

        int pos = -1;
        boolean possible = false;
        do {
            possible = true;
            pos = rand.nextInt(cand.length);
            for (int i = pos; i < pos + word.length && possible; i++) {
                if (i >= cand.length) {
                    possible = false;
                } else if (cand[i] == '|') {
                    possible = false;
                }
            }
        } while (!possible);

        for (int i = 0; i < word.length && possible; i++) {
            cand[i + pos] = word[i];
        }

        res.candidate = new String(cand);
        return res;
    }

    private char getRandomChar() {
        int pos = rand.nextInt(includedChar.length);
        return includedChar[pos];
    }

    public void calcFittness(Candidate candidate) {
        int fittness = 0;
        int checkedOK = 0;
        int checkedNOK = 0;
        int checkedTimesOK = 0;
        int checkedTimesNOK = 0;

        HashSet<Integer> splitPos = new HashSet<>();
        for (List<Pattern>[] pattern : patterns) {
            for (List<Pattern> aPattern : pattern) {
                boolean oneFound = false;
                for (Pattern timeRegEx : aPattern) {
                    Matcher m = timeRegEx.matcher(candidate.candidate);
                    if (m.matches()) {
                        oneFound = true;
                        fittness += MATCH_BONUS;
                        checkedOK++;
                        for (int i = 1; i <= m.groupCount(); i++) {
                            splitPos.add(m.start(i));
                        }
                    } else {
                        fittness += NOT_MATCHED_PENALTY;
                        checkedNOK++;
                    }
                }
                if (!oneFound) {
                    fittness += NONE_FOUND_PENALTY;
                    checkedTimesNOK++;
                } else {
                    checkedTimesOK++;
                }
            }
        }

        for (Pattern word : wordPatterns) {
            Matcher m = word.matcher(candidate.candidate);
            if (m.matches()) {
                fittness += WORD_MATCH_BONUS;
            }
        }

        Integer lastPos = null;
        double avgLen = 0;
        for (Integer pos : splitPos) {
            if (lastPos != null) {
                avgLen += pos - lastPos;
            }
            lastPos = pos;
        }
        avgLen /= (splitPos.size() - 1);

        double variance = 0;
        lastPos = null;
        for (Integer pos : splitPos) {
            if (lastPos != null) {
                variance += Math.pow(avgLen - (pos - lastPos), 2);
            }
            lastPos = pos;
        }
        variance = Math.sqrt(variance);

        candidate.fittnes = fittness;
        candidate.splitPos = splitPos.size();
        candidate.variance = variance;
        candidate.avgLen = avgLen;
        candidate.checkedOK = checkedOK;
        candidate.checkedNOK = checkedNOK;
        candidate.checkedTimesOK = checkedTimesOK;
        candidate.checkedTimesNOK = checkedTimesNOK;

        if (checkedNOK == 0) {
            candidate.fittnes += (100 - candidate.splitPos) * LOW_SPLIT_BONUS;
            candidate.fittnes -= variance * VARIANCE_PENALTY;
        }
    }
}
