package timepiece.watchface;

import org.junit.jupiter.api.Test;
import timepiece.TimeNamesEnglish;

import java.util.LinkedList;
import java.util.List;

public class GenAlgIntegrationTest {

    @Test
    void fullTest() {
        GenAlg gen = new GenAlg();

        System.out.println("creating patterns");
        gen.createPatterns(TimeNamesEnglish.getTimeStrings());

        System.out.println("loading solution");
        gen.loadSolution();

        List<GenThread> threads = new LinkedList<>();
        for (int i = 0; i < 25; i++) {
            GenThread gt = new GenThread(gen);
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


    @Test
    void runUntilStableBest() {
        GenAlg gen = new GenAlg();
        System.out.println("creating patterns");
        gen.createPatterns(TimeNamesEnglish.getTimeStrings());

        System.out.println("loading solution");
        gen.loadSolution();

        Candidate currentBest = null;
        int stableFor = 0;
        do {
            run(gen);

            if (gen.getSolution().getFittest().getFitnessResult().getCheckedTimesNOK() == 0 && gen.getSolution().getFittest() == currentBest) {
                stableFor++;
            } else {
                stableFor = 0;
            }

            currentBest = gen.getSolution().getFittest();
        } while (stableFor < 10);
    }

    private void run(GenAlg genAlg) {
        genAlg.incGeneration();

        Candidate newCandidate;
        int action = genAlg.getRand().nextInt(16);

        if (action < 2) {
            newCandidate = genAlg.mixTogether(genAlg.getRandom(true), genAlg.getRandom(true));
        } else if (action < 10) {
            newCandidate = genAlg.addRandomWord(genAlg.getRandom(true));
        } else if (action < 11) {
            newCandidate = genAlg.changeRandom(genAlg.getRandom(true));
        } else {
            newCandidate = genAlg.createRandom();
        }

        genAlg.calcFitness(newCandidate);

        if (genAlg.doAdd(newCandidate)) {
            genAlg.addToSolution(newCandidate);
        }
    }
}
