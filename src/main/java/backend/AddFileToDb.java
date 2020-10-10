package backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Scanner;

public class AddFileToDb {
    public static void main(String[] args) throws FileNotFoundException, SQLException {
         // Modify path
         File inp_file = new File("/home/straw/Java/DictionaryApp/src/main/java/database.txt");
         Scanner sc = new Scanner(inp_file);

         String enWord = "";
         StringBuilder viWord = new StringBuilder();

         DictionaryManager instance = new DictionaryManager();

         while (sc.hasNext()) {
             String line = sc.nextLine();
             // a word start with @
             if (!line.equals("") && line.charAt(0) == '@') {
                 //System.out.println(enWord + "#" + viWord);

                 // add prev word to db
                 if (!enWord.equals("") && !viWord.equals("")) {
                     instance.addNewWord(new Word(enWord, viWord.toString()));
                 }

                 // extract a word from file
                 String[] words = line.split(" ");
                 enWord = words[0].replace("@", "");
                 if (words.length >= 2) {
                     viWord = new StringBuilder(words[1] + '\n');
                 }
                 else {
                     viWord = new StringBuilder('\n');
                 }
             }
             else {
                viWord.append(line + '\n');
             }
         }
    }
}
