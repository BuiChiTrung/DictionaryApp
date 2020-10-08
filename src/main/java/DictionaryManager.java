import java.sql.*;

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
     * get all words in database.
     * @throws SQLException exception
     */
    public void selectAll() throws SQLException{
        ResultSet resultSet = st.executeQuery("SELECT * FROM words");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String enWord = resultSet.getString("enWord");
            String viWord = resultSet.getString("viWord");
            //System.out.println(id + ":" + enWord + "#" + viWord);
            System.out.println(enWord);
            System.out.println(viWord);
        }
        resultSet.close();
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
     * test
     * @param args no args
     * @throws SQLException exception
     */
    public static void main(String[] args) throws SQLException {
        DictionaryManager instance = new DictionaryManager();
        //instance.deleteWord("aba");
        //System.out.println(instance.wordInDict("hate"));
        //instance.addNewWord(new Word("love", "tìnhyêu"));
        instance.selectAll();

        instance.con.close();
        instance.st.close();
    }
}
