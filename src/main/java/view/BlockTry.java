package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BlockTry extends Application {

    @SuppressWarnings("Duplicates")
    @Override
    public void start(Stage stage) throws Exception {
        GridPane gridPane = new GridPane();

        Text text = new Text("1");
        Rectangle rec = new Rectangle();
        rec.setWidth(100);
        rec.setHeight(100);
        rec.setFill(Color.RED);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(rec, text);

        gridPane.add(stackPane, 1, 1);


        Scene scene = new Scene(gridPane, 1024, 628);
        stage.setScene(scene);
        stage.show();
    }
}
