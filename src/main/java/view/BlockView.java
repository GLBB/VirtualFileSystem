package view;

import bean.Block;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import service.BlockService;

import java.util.List;


public class BlockView extends Application {
    static BlockService blockService = BlockService.blockService;

    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        GridPane grid = createBlocksShow(blockService.list);
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(0);
        grid.setHgap(0);
        grid.setGridLinesVisible(true);

        Scene scene = new Scene(grid, 1300, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void setNewScene(){
        GridPane grid = createBlocksShow(blockService.list);
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(0);
        grid.setHgap(0);
        grid.setGridLinesVisible(true);

        Scene scene = new Scene(grid, 1300, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * 32 行 ，32 列
     * @param blocks
     */
    private static GridPane createBlocksShow(List<Block> blocks){
        GridPane grid = new GridPane();

        int row = 0,column=0;
        for (int i = 0; i < blocks.size(); i++) {
            StackPane block = createBlock(blocks.get(i));
            grid.add(block, column, row);
            column++;
            if (column==32){
                column=0;
                row++;
            }
        }
        return grid;
    }

    /**
     * 创建一个block块的可视化， used true 红色，false 绿色
     * @param block
     */
    private static StackPane createBlock(Block block){
        Text text = new Text(""+block.id);
        Rectangle rec = new Rectangle();
        rec.setWidth(40);
        rec.setHeight(10);
        if (block.used) {
            rec.setFill(Color.RED);
        }else {
            rec.setFill(Color.GREEN);
        }
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(rec, text);
        return stackPane;
    }
}
