package backend;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.net.*;
import javazoom.jl.player.Player;
import java.io.BufferedInputStream;
import java.util.Collections;

public class DictionaryManager {
    private static Connection con;
    private static Statement st;
    static {
        try {
            //con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/straw", "straw", "Trung123");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ad", "ad", "555666");
            st = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * check whether a word is in database or not.
     *
     * @param enWord english meaning of a word
     * @return true/false
     * @throws SQLException exception
     */
    public static boolean wordInDict (String enWord) throws SQLException {
        String command = "SELECT * FROM words WHERE enWord =" + "'" + enWord + "';";
        ResultSet rs = st.executeQuery(command);

        return rs.next();
    }

    /**
     * get detail info when use click in a specific word.
     * @param enWord clicked word
     * @return audio link and detail info of that word
     * @throws SQLException exception
     * @throws IOException exception
     */
    public static String[] getSingleWord (String enWord) throws SQLException, IOException {
        String[] response = {"", ""};
        String word = enWord.toString();
        if (!wordInDict(word)) {
            word = word.toLowerCase();
            if(!wordInDict(word)) {
                return response;
            }
        }
        // get info in database
        String command = "SELECT * FROM words WHERE enWord =" + "'" + word + "';";
        ResultSet rs = st.executeQuery(command);
        rs.next();

        // get more info with oxford api
        String dbResponse = rs.getString("viWord");
        String[] apiResponse = OxfordApi.parseJsonString(OxfordApi.getOxford(word));


        response[0] = apiResponse[0]; // audio link
        response[1] = dbResponse + "\nOXFORD DICTIONARY:\n" + apiResponse[1]; // definitions, examples,...

        rs.close();
        return response;
    }

    /**
     * get all words have prefix subEnWord.
     *
     * @param subEnWord prefix
     * @throws SQLException exception
     */
    public static ArrayList<String> selectMultipleWords(String subEnWord) throws SQLException{
        ArrayList<String> response = new ArrayList<>();

        String firstWord = subEnWord;
        String lastWord = subEnWord + "zz";

        String command = "SELECT * FROM words WHERE enWord >='" + firstWord + "'AND enWord <= '" + lastWord + "';";
        ResultSet resultSet = st.executeQuery(command);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String enWord = resultSet.getString("enWord");
            //String viWord = resultSet.getString("viWord");
            response.add(enWord);

        }
        resultSet.close();
        return response;
    }

    /**
     * Add a new word to database.
     * @param newWord a Word
     * @throws SQLException exception
     */
    public static void addNewWord(Word newWord) throws SQLException {
        if (wordInDict(newWord.getEnWord())) return;

        String command = "INSERT INTO words (enWord, viWord, inFavorite) VALUES" + "('" + newWord.getEnWord() + "','" + newWord.getViWord() + "',false);";
        st.executeUpdate(command);
    }

    /**
     * delete a word from database.
     * @param enWord english meaning of the word
     * @throws SQLException exception
     */
    public static void deleteWord(String enWord) throws SQLException {
        String command = "DELETE FROM words WHERE enWord =" + "'" + enWord + "'";
        st.executeUpdate(command);
    }


    public static void modifyWord(String enWord, String viWord) throws SQLException {
        String command = "UPDATE words SET viWord='" + viWord + "'WHERE enWord='" + enWord + "';";
        st.executeUpdate(command);
    }

    /**
     * get all favorite words of user.
     *
     * @return a list of favorite words
     * @throws SQLException exception
     */
    public static ArrayList<String> selectFavoriteWords() throws SQLException {
        ArrayList<String> res = new ArrayList<>();
        String command = "SELECT * FROM words WHERE inFavorite=true;";
        ResultSet rs = st.executeQuery(command);

        while(rs.next()) {
            String enWord = rs.getString("enWord");
            res.add(enWord);
        }
        Collections.sort(res);
        return res;
    }

    /**
     * add a word to favorite list.
     *
     * @param enWord english word
     * @throws SQLException exception
     */
    public static void addToFavorite(String enWord) throws SQLException {
        String command = "UPDATE words SET inFavorite = true WHERE enWord='" + enWord + "';";
        st.executeUpdate(command);
    }

    /**
     * remove a word from favorite list.
     *
     * @param enWord english word
     * @throws SQLException exception
     */
    public static void removeFromFavorite(String enWord) throws SQLException {
        String command = "UPDATE words SET inFavorite = false WHERE enWord='" + enWord + "';";
        st.executeUpdate(command);
    }


    public static void playSound(String path) {
        try {
            URL url = new URL(path);
            URLConnection connection = url.openConnection();
            connection.connect();
            BufferedInputStream reader = new BufferedInputStream(connection.getInputStream());
            Player player = new Player(reader);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * test methods.
     * @param args no args
     * @throws SQLException exception
     */
    public static void main(String[] args) throws SQLException, IOException {

//        DictionaryManager.addNewWord(new Word("aaaa", "tìnhyêu"));
//        DictionaryManager.modifyWord("aaaa", "son tung mtp");
//        DictionaryManager.deleteWord("aaaa");


//        ArrayList<String> response1 = DictionaryManager.selectMultipleWords("goof");
//        for (String str : response1) {
//            System.out.println(str);
//        }

        String[] response = DictionaryManager.getSingleWord("zzzzzz");
        System.out.println(response[0] + '\n' + response[1]);

        String[] response4 = DictionaryManager.getSingleWord("cat");
        System.out.println(response4[0] + '\n' + response4[1]);


//        DictionaryManager.addToFavorite("aba");
//        DictionaryManager.addToFavorite("home");
//
//        ArrayList<Word> response2 = DictionaryManager.selectFavoriteWords();
//        for (Word word: response2) {
//            System.out.println(word.toString());
//        }
//        DictionaryManager.removeFromFavorite("home");
//        DictionaryManager.removeFromFavorite("aba");

        DictionaryManager.con.close();
        DictionaryManager.st.close();
    }
}
