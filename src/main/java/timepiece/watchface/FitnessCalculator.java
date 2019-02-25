package timepiece.watchface;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FitnessCalculator {
    private static final int NONE_FOUND_PENALTY = -10000;
    private static final int NOT_MATCHED_PENALTY = -10;
    private static final int MATCH_BONUS = 1000;
    private static final int WORD_MATCH_BONUS = 10;
    private static final int COST_OF_THIS_ALGORITHM_BECOMING_SKYNET = 999999999;
    private static final int LOW_SPLIT_BONUS = 100;
    private static final int VARIANCE_PENALTY = 1;

    public Fitness calculate(Candidate candidate, List<Pattern>[][] patterns, List<Pattern> wordPatterns) {
        int fitness = 0;
        int checkedOK = 0;
        int checkedNOK = 0;
        int checkedTimesOK = 0;
        int checkedTimesNOK = 0;

        HashSet<Integer> splitPos = new HashSet<>();
        for (List<Pattern>[] pattern : patterns) {
            for (List<Pattern> aPattern : pattern) {
                boolean oneFound = false;
                for (Pattern timeRegEx : aPattern) {
                    Matcher m = timeRegEx.matcher(candidate.getCandidate());
                    if (m.matches()) {
                        oneFound = true;
                        fitness += MATCH_BONUS;
                        checkedOK++;
                        for (int i = 1; i <= m.groupCount(); i++) {
                            splitPos.add(m.start(i));
                        }
                    } else {
                        fitness += NOT_MATCHED_PENALTY;
                        checkedNOK++;
                    }
                }
                if (!oneFound) {
                    fitness += NONE_FOUND_PENALTY;
                    checkedTimesNOK++;
                } else {
                    checkedTimesOK++;
                }
            }
        }

        for (Pattern word : wordPatterns) {
            Matcher m = word.matcher(candidate.getCandidate());
            if (m.matches()) {
                fitness += WORD_MATCH_BONUS;
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

        if (checkedNOK == 0) {
            fitness = fitness + (100 - splitPos.size()) * LOW_SPLIT_BONUS;
            fitness = (int)(fitness - variance * VARIANCE_PENALTY);
        }

        Fitness fitnessResult = Fitness.createFitness(
                fitness,
                checkedOK,
                checkedNOK,
                checkedTimesOK,
                checkedTimesNOK,
                splitPos.size(),
                variance,
                avgLen);

        candidate.setFitnessResult(fitnessResult);

        return fitnessResult;
    }
}
