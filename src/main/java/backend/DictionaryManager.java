package backend;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


public class DictionaryManager {
    private static Connection con;
    private static Statement st;
    static {
        try {
            //con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/straw", "straw", "Trung123");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ad", "ad", "555666");
            st = con.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
        String[] response = new String[2];

        // get info in database
        String command = "SELECT * FROM words WHERE enWord =" + "'" + enWord + "';";
        ResultSet rs = st.executeQuery(command);
        rs.next();

        // get more info with oxford api
        String dbResponse = rs.getString("viWord");
        String[] apiResponse = OxfordApi.parseJsonString(OxfordApi.getOxford(enWord));


        response[0] = apiResponse[0]; // audio link
        response[1] = dbResponse + "OXFORD DICTIONARY:\n" + apiResponse[1]; // definitions, examples,...

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

        String command = "INSERT INTO words (enWord, viWord) VALUES" + "('" + newWord.getEnWord() + "','" + newWord.getViWord() + "');";
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

    /**
     * test methods.
     * @param args no args
     * @throws SQLException exception
     */
    public static void main(String[] args) throws SQLException, IOException {
        //instance.deleteWord("aba");
        System.out.println(DictionaryManager.wordInDict("hate"));
        //DictionaryManager.addNewWord(new Word("love", "tìnhyêu"));

        ArrayList<String> response1 = DictionaryManager.selectMultipleWords("goof");
        for (String str : response1) {
            System.out.println(str);
        }

        String[] response = DictionaryManager.getSingleWord("happy");
        System.out.println(response[0] + '\n' + response[1]);

        DictionaryManager.con.close();
        DictionaryManager.st.close();
    }
}
