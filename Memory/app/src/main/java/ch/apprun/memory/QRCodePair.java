package ch.apprun.memory;

public class QRCodePair {
    private String firstCodeImagePath;
    private String firstCodeSolutionWord;

    private String secondCodeImagePath;
    private String secondCodeSolutionWord;

    public String getFirstCodeImagePath() {
        return firstCodeImagePath;
    }

    public void setFirstCodeImagePath(String firstCodeImagePath) {
        this.firstCodeImagePath = firstCodeImagePath;
    }

    public String getFirstCodeSolutionWord() {
        return firstCodeSolutionWord;
    }

    public void setFirstCodeSolutionWord(String firstCodeSolutionWord) {
        this.firstCodeSolutionWord = firstCodeSolutionWord;
    }

    public String getSecondCodeImagePath() {
        return secondCodeImagePath;
    }

    public void setSecondCodeImagePath(String secondCodeImagePath) {
        this.secondCodeImagePath = secondCodeImagePath;
    }

    public String getSecondCodeSolutionWord() {
        return secondCodeSolutionWord;
    }

    public void setSecondCodeSolutionWord(String secondCodeSolutionWord) {
        this.secondCodeSolutionWord = secondCodeSolutionWord;
    }
}
