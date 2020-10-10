package backend;

public class Word {
    private String enWord;
    private String viWord;

    public String getEnWord() {
        return enWord;
    }

    public void setEnWord(String enWord) {
        this.enWord = enWord;
    }

    public String getViWord() {
        return viWord;
    }

    public void setViWord(String viWord) {
        this.viWord = viWord;
    }

    public Word(String _enWord, String _viWord){
        this.enWord = _enWord;
        this.viWord = _viWord;
    }

    public void printWord(){
        System.out.println(this.enWord + " # " + this.viWord);
    }

    public String toString() {
        return enWord + '\n' + viWord;
    }

    public static void main(String[] args) {

    }
}
