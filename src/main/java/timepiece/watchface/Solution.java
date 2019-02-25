package timepiece.watchface;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
public class Solution {
    private int generation = 0;
    private List<Candidate> candidates = new LinkedList<>();
    private Candidate fittest = null;
    private Candidate worst = null;

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public Candidate getFittest() {
        return fittest;
    }

    public void setFittest(Candidate fittest) {
        this.fittest = fittest;
    }

    public Candidate getWorst() {
        return worst;
    }

    public void setWorst(Candidate worst) {
        this.worst = worst;
    }
}
