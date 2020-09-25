import java.util.Scanner;

public class DictionaryCommandLine {
    private DictionaryManagement dictManager = new DictionaryManagement();

    public void dictionaryAdvanced(){
//        System.out.println("1. Insert New Words");
//        System.out.println("2. Show All Words");
//        System.out.println("3. Look for a word");
//        System.out.println("Type in the number of what you want to do: ");

        dictManager.insertFromFile();
        dictManager.showAllWords();

        System.out.print("Search for word: ");
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();

        dictManager.dictionaryLookUp(word);
    }

    public static void main(String[] args) {
        DictionaryCommandLine instance = new DictionaryCommandLine();
        instance.dictionaryAdvanced();
    }
}
