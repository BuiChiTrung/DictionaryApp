package sample;

import backend.Word;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.text.Text;
import java.nio.file.Paths;

public class Main extends Application {
    public Word A;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        String path= "src/main/java/sample/sample.fxml";
        Parent root = FXMLLoader.load(Paths.get(path).toUri().toURL());
        StackPane layout = new StackPane();
        layout.getChildren().add(root);

        Scene scene = new Scene(layout, 1200, 600);
        scene.getStylesheets().add("style.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}