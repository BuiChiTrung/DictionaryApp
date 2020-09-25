import java.util.TreeMap;

public class Dictionary {
    private Word[] words = new Word[100000];
    private TreeMap<String, Word> wordsMap = new TreeMap<String, Word>();
    private int wordCnt = 0;

    public Word[] getWords(){
        return words;
    }

    public int getWordCnt(){
        return wordCnt;
    }

    public void setWordCnt(int _WordCnt){ wordCnt = _WordCnt;}

    public TreeMap<String, Word> getWordsMap(){return wordsMap;}

    public static void main(String[] args) {
        Dictionary instance = new Dictionary();
        //instance.addNewWord(new Word("trung", "suni ha linh"));
    }
}
