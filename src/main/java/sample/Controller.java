package sample;
import backend.DictionaryManager;
import backend.Word;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import java.io.File;
import javafx.beans.value.ChangeListener;
public class Controller{
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

    @FXML
    private ListView<String> listSuggestWord;

    @FXML
    private VBox leftSide;

    @FXML
    private Button speakButton;

    @FXML
    private Button chooseButton;

    @FXML
    private BorderPane window;

    @FXML
    private Label footer;


    private String currentMode;
    private String currentWord;
    private String currentDefinition;
    public void setTranslateMode() {
        leftSide.setVisible(true);
        currentMode = "Translate";
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
        speakButton.setVisible(true);
    }
    public void setAddPersonalDictionaryMode() {
        leftSide.setVisible(false);
        currentMode = "Add";
        translateTextField.setText("");
        textArea.setText("");
        textArea.setPromptText("Enter your definition of your added word");
        textArea.setEditable(true);
        chooseAddWord.setVisible(true);
        translateTextField.setText("");
        translateSide.setVisible(false);
        searchButton.setVisible(false);
        deleteButton.setVisible(false);
        addWordTextField.setText("");
        addButton.setVisible(false);
        modifyButton.setVisible(false);
        confirmOrCancelButton.setVisible(true);
        addToPersonalDictButton.setVisible(false);
        speakButton.setVisible(false);
    }
    public void setModifyPersonalDictionaryMode() {
        leftSide.setVisible(true);
        currentMode = "Modify";
        translateTextField.setText("");
        addWordTextField.setText("");
        textArea.setEditable(true);
        translateTextField.setText("");
        translateSide.setVisible(false);
        searchButton.setVisible(false);
        deleteButton.setVisible(false);
        addButton.setVisible(false);
        modifyButton.setVisible(false);
        chooseAddWord.setVisible(false);
        confirmOrCancelButton.setVisible(true);
        addToPersonalDictButton.setVisible(false);
        speakButton.setVisible(false);
    }
    public void initialize() throws SQLException {
        personalDictionary.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listSuggestWord.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        textArea.setWrapText(true);
        setTranslateMode();
        ArrayList<String> favoriteWord = DictionaryManager.selectFavoriteWords();
        personalDictionary.getItems().addAll(favoriteWord);
        currentMode = "Translate";
        translateTextField.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                String word = translateTextField.getText();
                translateTextField.setText(word);
                ArrayList<String> suggestWords = null;
                try {
                    suggestWords = DictionaryManager.selectMultipleWords(word);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                listSuggestWord.getItems().clear();
                for(int i = 0; i < Math.min(suggestWords.size(), 20); i++) {
                    listSuggestWord.getItems().add(suggestWords.get(i));
                }
            }
        });
    }
    public void Submit(ActionEvent event) throws SQLException, IOException {
        if(event.getSource() == translateButton) {
            String word = translateTextField.getText();
            translateTextField.setText(word);
            if(word.length() > 0) {
                textArea.deleteText(0, textArea.getText().length());
                String[] response = DictionaryManager.getSingleWord(word);
                textArea.setText(response[1]);
                currentWord = word;
                currentDefinition = response[1];
            }
        }
        if(event.getSource() == deleteButton) {
            Object selectedItem = personalDictionary.getSelectionModel().getSelectedItem();
            personalDictionary.getItems().remove(selectedItem);
            String word = selectedItem.toString();
            DictionaryManager.removeFromFavorite(word);
            personalDictionary.getSelectionModel().clearSelection();
            String[] response = DictionaryManager.getSingleWord(word);
            textArea.setText(response[1]);
        }
        if(event.getSource() == searchButton) {
            String word = personalDictionary.getSelectionModel().getSelectedItem();
            if(word != null) {
                translateTextField.setText(word);
                textArea.deleteText(0, textArea.getText().length());
                String[] response = DictionaryManager.getSingleWord(word);
                textArea.setText(response[1]);
                currentWord = word;
                currentDefinition = response[1];
            }
            personalDictionary.getSelectionModel().clearSelection();
        }
        if(event.getSource() == addButton) {
            setAddPersonalDictionaryMode();
        }
        if(event.getSource() == confirmButton) {
            if(currentMode.equals("Add")) {
                String word = addWordTextField.getText();
                String definition = textArea.getText();
                if(word.length() > 0 && definition.length() > 0 && !DictionaryManager.wordInDict(word)) {
                    Word newWord = new Word(word, definition);
                    personalDictionary.getItems().add(word);
                    DictionaryManager.addNewWord(newWord);
                    DictionaryManager.addToFavorite(word);
                }
            }
            if(currentMode.equals("Modify")) {
                String newDefinition = textArea.getText();
                DictionaryManager.modifyWord(currentWord, newDefinition);
            }
            setTranslateMode();
        }
        if(event.getSource() == cancelButton) {
            setTranslateMode();
        }
        if(event.getSource() == addToPersonalDictButton) {
            if(currentWord.length() > 0 && !personalDictionary.getItems().contains(currentWord)) {
                personalDictionary.getItems().add(currentWord);
                DictionaryManager.addToFavorite(currentWord);
            }
        }
        if(event.getSource() == modifyButton) {
            if(currentWord.length() > 0) {
                setModifyPersonalDictionaryMode();
                textArea.setText(DictionaryManager.getDefinition(currentWord));
                translateTextField.setText(currentWord);
            }
        }

        if(event.getSource() == speakButton) {
            String word = translateTextField.getText();
            translateTextField.setText(word);
            if(word.length() > 0 && DictionaryManager.wordInDict(word)) {
                String[] response = DictionaryManager.getSingleWord(word);
                DictionaryManager.playSound(response[0]);
            }
        }
        if(event.getSource() == chooseButton) {
            String word = listSuggestWord.getSelectionModel().getSelectedItem();
            translateTextField.setText(word);
            listSuggestWord.getSelectionModel().clearSelection();
        }
    }
    @FXML
    private void handleOnKeyPressed(KeyEvent event) throws SQLException, IOException {
        if(currentMode.equals("Translate")) {
            String word = translateTextField.getText();
            switch (event.getCode()) {
                case ENTER:
                    translateTextField.setText(word);
                    if(word.length() > 0) {
                        textArea.deleteText(0, textArea.getText().length());
                        String[] response = DictionaryManager.getSingleWord(word);
                        textArea.setText(response[1]);
                        currentWord = word;
                        currentDefinition = response[1];
                    }
                    break;
                case CONTROL:
                    translateTextField.setText(word);
                    if(word.length() > 0 && DictionaryManager.wordInDict(word)) {
                        String[] response = DictionaryManager.getSingleWord(word);
                        DictionaryManager.playSound(response[0]);
                    }
            }
        }
    }
}