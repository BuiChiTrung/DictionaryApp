package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import backend.*;

import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        String path= "src/main/java/sample/sample.fxml";
        Parent root = FXMLLoader.load(Paths.get(path).toUri().toURL());

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        // test
        Word word = new Word("ha", "hi");
        System.out.println(word.toString());
        launch(args);
    }
}
