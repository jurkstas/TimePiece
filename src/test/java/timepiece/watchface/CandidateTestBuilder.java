package timepiece.watchface;

public class CandidateTestBuilder {
    private String schema;


    public CandidateTestBuilder withSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public CandidateTestBuilder withSchemaEmpty() {
        this.schema = //11x11
                "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX|" +
                        "XXXXXXXXXXX";

        return this;
    }

    public CandidateTestBuilder withSchemaEnglishSolution() {
        this.schema = "elevenntsix|sevenfourty|twelvehotwo|eightoneten|threertnine|fivefifteen|twentypastl|fourthytenv|fiftythirty|clockfourty|gtwofiveten";

        return this;
    }

    public CandidateTestBuilder withSchemaGermanSolution() {
        this.schema = "fünffzehnu|dreihnacht|vorelfhalb|uhrviertel|siebenacht|dreisechsz|neundzwölf|zweinsluhr|elfünfzehn|undvierzig";

        return this;
    }

    public CandidateTestBuilder withWordAt(int row, int column, String word) {
        String[] split = this.schema.split("\\|");

        for (int i = 0; i < split.length; i++) {
            String newColumn;
            if (i == row) {
                newColumn = split[i].substring(0, column) + word + split[i].substring(column + word.length());
            } else {
                newColumn = split[i];
            }

            split[i] = newColumn;
        }

        this.schema = String.join("|", split);

        return this;
    }

    public Candidate build() {
        Candidate candidate = new Candidate();

        candidate.setCandidate(this.schema);

        return candidate;
    }
}
