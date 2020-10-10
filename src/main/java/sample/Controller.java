package sample;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
public class Controller {
    @FXML
    private TextField text_field;

    @FXML
    private TextArea translateArea;

    @FXML
    private Button translate_button;

    @FXML
    private ListView Dictionary;
    public void initialize() {
        Dictionary.getItems().addAll("A", "B", "C", "D", "E", "F", "g");
        translateArea.setEditable(false);
    }

    public void Submit(ActionEvent event) {
        if(event.getSource() == translate_button) {
            String word = text_field.getText();
            if(word.length() > 0) {
                text_field.deleteText(0, word.length());
                translateArea.deleteText(0, translateArea.getText().length());
                translateArea.setWrapText(true);
                translateArea.setText(word);
            }
        }
    }
}
