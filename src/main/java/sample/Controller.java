package sample;
import backend.DictionaryManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;

public class Controller {
    @FXML
    private TextField text_field;

    @FXML
    private TextArea translateArea;

    @FXML
    private Button translate_button;

    @FXML
    private ListView Dictionary;

    @FXML
    private Button deleteButton;

    @FXML
    private Button addButton;

    @FXML
    private TextField addText;
    public void initialize() {
        Dictionary.getItems().addAll("A", "B", "C", "D", "E", "F", "g");
        Dictionary.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        translateArea.setEditable(false);
        translateArea.setWrapText(true);
    }

    public void Submit(ActionEvent event) throws SQLException, IOException {
        if(event.getSource() == translate_button) {
            String word = text_field.getText();
            if(word.length() > 0) {
                text_field.deleteText(0, word.length());
                translateArea.deleteText(0, translateArea.getText().length());

                String[] response = DictionaryManager.getSingleWord(word);
                //System.out.println(response[0] + '\n' + response[1]);

                translateArea.setText(response[1]);
            }
        }
        if(event.getSource() == deleteButton) {
            Object selectedItem = Dictionary.getSelectionModel().getSelectedItem();
            Dictionary.getItems().remove(selectedItem);
        }
        if(event.getSource() == addButton) {
            String addWord = addText.getText();
            addText.deleteText(0, addWord.length());
            if(addWord.length() > 0) {
                Dictionary.getItems().add(addWord);
            }
        }
    }
}