import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

public class DictionaryManagement {
    private Dictionary userDict = new Dictionary();
    private void addNewWord(Word newWord){
        Word[] words = userDict.getWords();
        int wordCnt = userDict.getWordCnt();
        TreeMap<String, Word> wordsMap = userDict.getWordsMap();

        // add to list, map
        words[wordCnt] = newWord;
        wordsMap.put(newWord.getEnWord(), words[wordCnt]);
        userDict.setWordCnt(wordCnt + 1);
    }

    private void appendToFile(Word newWord){
        try{
            FileWriter file = new FileWriter(WORDS_FILE, true);
            file.write(newWord.getEnWord() + "#" + newWord.getViWord() + "\n");
            file.close();
        }catch(IOException e){
            System.out.println("ERROR on writeToFile");
            e.printStackTrace();
        }
    }

    public static final String WORDS_FILE = "/home/straw/Java/Dictionary/src/dictionaries.txt";
    public void insertFromFile(){
        try{
            File inp = new File(WORDS_FILE);
            Scanner scanner = new Scanner(inp);
            while (scanner.hasNext()){
                String[] arrOfString = scanner.nextLine().split("#"); // máy ko nhận diện đc tab khi bấm tab trên bàn phím nên dùng "#"
                Word newWord = new Word(arrOfString[0], arrOfString[1]);
                this.addNewWord(newWord);
            }
        }catch(FileNotFoundException e){
            System.out.println("ERROR");
            e.printStackTrace();
        }
    }

    public void insertFromCommandLine(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add new words to your dictionary");
        System.out.print("Words number: ");

        int wordsNumber = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= wordsNumber; ++i){
            System.out.print("English word: ");
            String enWord = scanner.nextLine();

            System.out.print("Vietnamese meaning: ");
            String viWord = scanner.nextLine(); // tiếng việt có dấu cách nên tính là 2 str, phải đọc cả dòng

            Word newWord = new Word(enWord, viWord);

            this.addNewWord(newWord);
            this.appendToFile(newWord);
        }
    }

    public void removeWord(String enWord){
        TreeMap<String, Word> wordsMap = userDict.getWordsMap();
        Word[] words = userDict.getWords();
        int wordCnt = userDict.getWordCnt();

        Word removedWord = wordsMap.get(enWord);

    }

    public void dictionaryLookUp(String EnWord){
        Word res =  userDict.getWordsMap().get(EnWord);
        if (res == null) System.out.println("There is no such word");
        else res.printWord();
    }

    public void showAllWords(){
        Word[] words = userDict.getWords();
        int wordCnt = userDict.getWordCnt();

        for (int i = 0; i < wordCnt; ++i){
            System.out.print(words[i].getEnWord() + " # ");
            System.out.println(words[i].getViWord());
        }
    }

    public static void main(String[] args) {
        DictionaryManagement instance = new DictionaryManagement();
        //instance.insertFromCommandLine();
        instance.insertFromFile();
        instance.insertFromCommandLine();
        //instance.dictionaryLookUp("capsule");
        instance.showAllWords();
    }
}
