package sample;
import backend.DictionaryManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.scene.layout.*;
import java.io.IOException;
import java.sql.SQLException;

public class Controller {
    @FXML
    private TextField translateTextField;

    @FXML
    private Button translateButton;

    @FXML
    private TextArea textArea;

    @FXML
    private ListView<String> personalDictionary;

    @FXML
    private Button deleteButton;

    @FXML
    private Button addButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button modifyButton;

    @FXML
    private TextField addWordTextField;

    @FXML
    private Label addWordLabel;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private HBox confirmOrCancelButton;

    @FXML
    private HBox chooseAddWord;

    @FXML
    private HBox translateSide;

    @FXML
    private Button addToPersonalDictButton;
    private String currentMode;
    public void setTranslateMode() {
        currentMode = "";
        translateTextField.setText("");
        textArea.setPromptText("");
        textArea.setText("");
        textArea.setEditable(false);
        chooseAddWord.setVisible(false);
        confirmOrCancelButton.setVisible(false);
        translateSide.setVisible(true);
        searchButton.setVisible(true);
        deleteButton.setVisible(true);
        addWordTextField.setText("");
        addButton.setVisible(true);
        modifyButton.setVisible(true);
        addToPersonalDictButton.setVisible(true);
    }
    public void setAddPersonalDictionaryMode() {
        currentMode = "Add";
        translateTextField.setText("");
        textArea.setText("");
        textArea.setPromptText("Enter your definition of your added word");
        textArea.setEditable(true);
        chooseAddWord.setVisible(true);
        confirmOrCancelButton.setVisible(true);
        translateTextField.setText("");
        translateSide.setVisible(false);
        searchButton.setVisible(false);
        deleteButton.setVisible(false);
        addWordTextField.setText("");
        addButton.setVisible(false);
        modifyButton.setVisible(false);
        addToPersonalDictButton.setVisible(false);
    }
    public void setModifyPersonalDictionaryMode() {
        currentMode = "Modify";
        translateTextField.setText("");
        textArea.setText("");
        textArea.setEditable(true);
        translateTextField.setText("");
        translateSide.setVisible(false);
        searchButton.setVisible(false);
        deleteButton.setVisible(false);
        addButton.setVisible(false);
        modifyButton.setVisible(false);
        addWordTextField.setText("");
        chooseAddWord.setVisible(false);
        addToPersonalDictButton.setVisible(false);
        confirmOrCancelButton.setVisible(true);
    }
    public void initialize() {
        personalDictionary.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        textArea.setWrapText(true);
    }
    public void Submit(ActionEvent event) throws SQLException, IOException {
        if(event.getSource() == translateButton) {
            String word = translateTextField.getText();
            if(word.length() > 0) {
                textArea.deleteText(0, textArea.getText().length());
                //String[] response = DictionaryManager.getSingleWord(word);
                //System.out.println(response[0] + '\n' + response[1]);
                //textArea.setText(response[1]);
            }
        }
        if(event.getSource() == deleteButton) {
            Object selectedItem = personalDictionary.getSelectionModel().getSelectedItem();
            personalDictionary.getItems().remove(selectedItem);
        }
        if(event.getSource() == addButton) {
            setAddPersonalDictionaryMode();
        }
        if(event.getSource() == confirmButton) {
            if(currentMode == "Add") {
                String word = addWordTextField.getText();
                String definition = textArea.getText();
                /*
                add (word, definition) to TreeMap
                 */
                personalDictionary.getItems().add(word);
            }
            else {
                /*
                update (word, definition) to TreeMap
                 */
            }
            setTranslateMode();
        }
        if(event.getSource() == cancelButton) {
            setTranslateMode();
        }
        if(event.getSource() == addToPersonalDictButton) {
            String word = translateTextField.getText();
            String definition = textArea.getText();
            if(word.length() > 0 && definition.length() > 0 && !personalDictionary.getItems().contains(word)) {
                personalDictionary.getItems().add(word);
                /*
                Update (word, definition) to TreeMap
                 */
            }
        }
        if(event.getSource() == modifyButton) {
            String word = personalDictionary.getSelectionModel().getSelectedItem();
            if(word != null) {
                setModifyPersonalDictionaryMode();
                //textArea.setText(enWord in treeMap)
                textArea.setText("1231425rrqarqarr");
                textArea.setEditable(true);
            }
        }
    }
}