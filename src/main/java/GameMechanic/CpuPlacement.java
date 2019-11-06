package GameMechanic;

import UI.GridPaneButtonMethods;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CpuPlacement {
    private Stage window = new Stage();
    private int width = 500;
    private int height = 300;
    
    private List<Ship> cpuShips = new ArrayList<>();
    
    private GridPane cpuShipPlacement = new GridPane();
    private List<Button> buttonList = GridPaneButtonMethods.create100ButtonList(cpuShipPlacement, "cpuPlacement", true, Event::consume);
            
    public CpuPlacement(){
        window.setTitle("Debug visualisation of cpu ship placement");
        window.setMinWidth(width);
        window.setMinHeight(height);
        
        
        BorderPane layout = new BorderPane(cpuShipPlacement);
        
        
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("gameStyles.css");
        
        window.setScene(scene);
        window.show();
    }
    
    
    
    
    
        
    
}
