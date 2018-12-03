package view;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;



public class Quicklook extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        Button btOK = new Button("OK");
        Scene scene = new Scene(btOK, 200, 250);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
