package UI;


import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

public class CpuVisual {
    private final Stage window = new Stage();
    private final int width = 500;
    private final int height = 300;
    private final List<Button> buttonList;

    public CpuVisual(Game game) {
        GridPane cpuShipPlacement = new GridPane();
        GridPaneButtonMethods gridMethods = new GridPaneButtonMethods(game);
        buttonList = gridMethods.create100ButtonList(cpuShipPlacement, "boardButton", false, Event::consume);
        
        if(Game.debug){
            window.setTitle("Debug visualisation of cpu ship placement");
            window.setMinWidth(width);
            window.setMinHeight(height);
            
            BorderPane layout = new BorderPane(cpuShipPlacement);
            
            Scene scene = new Scene(layout);
            scene.getStylesheets().add("gameStyles.css");

            window.setScene(scene);
            
            if(Game.debug){
                window.show();
            }
        }
    }

    public List<Button> getButtonList() {
        return buttonList;
    }
}
