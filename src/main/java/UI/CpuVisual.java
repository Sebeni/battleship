package UI;


import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;

public class CpuVisual {
    private final Stage window = new Stage();
    private final int width = 400;
    private final int height = 400;
    private final List<Button> buttonList;

    public CpuVisual(Game game) {
        GridPane root = new GridPane();

        GridPane cpuShipPlacement = new GridPane();
        GridPaneButtonMethods gridMethods = new GridPaneButtonMethods(game);
        buttonList = gridMethods.create100ButtonList(cpuShipPlacement, "boardButton", false, Event::consume);


        window.setTitle("CPU's position");
        window.setMinWidth(width);
        window.setMinHeight(height);


        root.add(cpuShipPlacement, 1, 1, 10, 10);
        gridMethods.gridMarkers(root);
        
        BorderPane layout = new BorderPane(cpuShipPlacement);
        layout.setPadding(new Insets(10));


        Scene scene = new Scene(layout);
        scene.getStylesheets().add("gameStyles.css");

        window.setScene(scene);
        window.show();

    }

    public List<Button> getButtonList() {
        return buttonList;
    }

    public Stage getWindow() {
        return window;
    }
}
