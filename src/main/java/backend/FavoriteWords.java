package backend;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class FavoriteWords {
    private static TreeMap<String, String> wordsMap = new TreeMap<>();

    public static void addNewWord(String enWord, String viWord) {
        wordsMap.put(enWord, viWord);
    }

    public static void deleteFromMap(String enWord) {
        wordsMap.remove(enWord);
    }

    public static ArrayList<Word> getWordsInMap() {
        ArrayList<Word> res = new ArrayList<>();
        for (Map.Entry<String, String> it : wordsMap.entrySet()) {
            res.add(new Word(it.getKey(), it.getValue()));
        }

        return res;
    }

    public static void main(String[] args) {

    }
}
