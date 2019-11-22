package GameUI.Boxes;


import GameUI.GridHelperMethods;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

public class CpuVisualBox {
    private final Stage window = new Stage();
    private final int width = 500;
    private final int height = 500;
    private final List<Button> buttonList;

    public CpuVisualBox(Game game) {
        GridPane root = new GridPane();

        GridPane cpuShipPlacement = new GridPane();
       
        buttonList = GridHelperMethods.create100ButtonList(cpuShipPlacement, "boardButton", false, Event::consume);


        window.setTitle("CPU's position");
        window.setMinWidth(width);
        window.setMinHeight(height);


        root.add(cpuShipPlacement, 1, 1, 10, 10);
        GridHelperMethods.gridMarkers(root);
        
        BorderPane layout = new BorderPane(root);
        layout.setPadding(new Insets(10));
       


        Scene scene = new Scene(layout);
        scene.getStylesheets().add("gameStyles.css");

        window.setScene(scene);

    }

    public List<Button> getButtonList() {
        return buttonList;
    }

    public Stage getWindow() {
        return window;
    }
}
