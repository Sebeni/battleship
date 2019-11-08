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

    private final GridPane cpuShipPlacement = new GridPane();
    private final List<Button> buttonList = GridPaneButtonMethods.create100ButtonList(cpuShipPlacement, "cpuPlacement", false, Event::consume);

    public CpuVisual() {
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
