package backend;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


public class DictionaryManager {
    private final Connection con;
    private final Statement st;

    /**
     * connect dictionary to postgresql db.
     * @throws SQLException connected fail
     */
    public DictionaryManager() throws SQLException {
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/straw", "straw", "Trung123");
        System.out.println("Connected to PostgreSQL database!");

        st = con.createStatement();
    }

    /**
     * check whether a word is in database or not.
     *
     * @param enWord english meaning of a word
     * @return true/false
     * @throws SQLException exception
     */
    public boolean wordInDict (String enWord) throws SQLException {
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
    public String[] getSingleWord (String enWord) throws SQLException, IOException {
        String[] response = new String[2];

        // get info in database
        String command = "SELECT * FROM words WHERE enWord =" + "'" + enWord + "';";
        ResultSet rs = st.executeQuery(command);
        rs.next();

        // get more info with oxford api
        String dbResponse = rs.getString("viWord");
        String[] apiResponse = OxfordApi.parseJsonString(OxfordApi.getOxford(enWord));


        response[0] = apiResponse[0]; // audio link
        response[1] = dbResponse + apiResponse[1]; // definitions, examples,...

        return response;
    }

    /**
     * get all words have prefix subEnWord.
     *
     * @param subEnWord prefix
     * @throws SQLException exception
     */
    public ArrayList<String> selectMultipleWords(String subEnWord) throws SQLException{
        ArrayList<String> response = new ArrayList<>();

        String firstWord = subEnWord;
        String lastWord = subEnWord + "zz";

        String command = "SELECT * FROM words WHERE enWord >='" + firstWord + "'AND enWord <= '" + lastWord + "';";
        ResultSet resultSet = st.executeQuery(command);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String enWord = resultSet.getString("enWord");
            String viWord = resultSet.getString("viWord");
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
    public void addNewWord(Word newWord) throws SQLException {
        if (this.wordInDict(newWord.getEnWord())) return;

        String command = "INSERT INTO words (enWord, viWord) VALUES" + "('" + newWord.getEnWord() + "','" + newWord.getViWord() + "');";
        st.executeUpdate(command);
    }

    /**
     * delete a word from database.
     * @param enWord english meaning of the word
     * @throws SQLException exception
     */
    public void deleteWord(String enWord) throws SQLException {
        String command = "DELETE FROM words WHERE enWord =" + "'" + enWord + "'";
        st.executeUpdate(command);
    }

    /**
     * test methods.
     * @param args no args
     * @throws SQLException exception
     */
    public static void main(String[] args) throws SQLException, IOException {
        DictionaryManager instance = new DictionaryManager();
        //instance.deleteWord("aba");
        //System.out.println(instance.wordInDict("hate"));
        //instance.addNewWord(new Word("love", "tìnhyêu"));

        ArrayList<String> response1 = instance.selectMultipleWords("goo");
        for (String str : response1) {
            System.out.println(str);
        }

        String[] response = instance.getSingleWord("happy");
        System.out.println(response[0] + '\n' + response[1]);

        instance.con.close();
        instance.st.close();
    }
}
