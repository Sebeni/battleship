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
import java.util.Random;

public class CpuPlacement {
    private final Stage window = new Stage();
    private final int width = 500;
    private final int height = 300;
    private final Random random = new Random();

    private final List<Ship> cpuShips = new ArrayList<>();

    private final GridPane cpuShipPlacement = new GridPane();
    private final List<Button> buttonList = GridPaneButtonMethods.create100ButtonList(cpuShipPlacement, "cpuPlacement", false, Event::consume);

    public CpuPlacement() {
        window.setTitle("Debug visualisation of cpu ship placement");
        window.setMinWidth(width);
        window.setMinHeight(height);


        BorderPane layout = new BorderPane(cpuShipPlacement);
        
        Button button = new Button("placeShip");
        button.setOnAction(event -> {
            cpuShips.clear();
            buttonList.forEach(button1 -> button1.setDisable(false));
            
            for(ShipName s : Ship.getAllShipsMaxSize().keySet()){
                Ship ship = new Ship(s);
                placeCpuShip(ship);
                cpuShips.add(ship);
                System.out.println(ship);
            }
            
            cpuShips.stream().flatMap(ship -> ship.getCoordinates().stream()).forEach(s -> buttonList.get(Integer.parseInt(s)).setDisable(true));
        });
        layout.setRight(button);


        Scene scene = new Scene(layout);
        scene.getStylesheets().add("gameStyles.css");

        window.setScene(scene);
        window.show();
    }
    
    
    

    private void placeCpuShip(Ship ship) {
        ship.setHorizontalPlacement(random.nextBoolean());

        if (ship.isHorizontalPlacement()) {
            //y 0 : 9; x 0 :(9-ship max size)
            Integer x = random.nextInt(10 - ship.getShipMaxSize());
            Integer y = random.nextInt(10);
            placeShipPart(x, y, ship);


        } else {
            //y 0 : (9-ship max size), x 0 : 9
            Integer x = random.nextInt(10);
            Integer y = random.nextInt(10 - ship.getShipMaxSize());
            placeShipPart(x, y, ship);
        }
    }
    
    private void placeShipPart(Integer x, Integer y, Ship ship){
        if(!coordinateIsOccupied(x, y)){
            ship.setCoordinates(x, y);
            
            if(ship.getShipFieldCount() < ship.getShipMaxSize()){
                if(ship.isHorizontalPlacement()){
                    placeShipPart(x+1, y, ship);
                } else {
                    placeShipPart(x, y+1, ship);
                }
            }
        } else {
            ship.getCoordinates().clear();
            ship.setShipFieldCount(0);
            
            placeCpuShip(ship);
        }
    }
    
    private boolean coordinateIsOccupied(Integer x, Integer y) {
        String coordinateToCheck = x.toString() + y.toString();
        boolean coordinateOccupied = false;
        if (!cpuShips.isEmpty()) {
            coordinateOccupied = cpuShips.stream().flatMap(ship -> ship.getCoordinates().stream()).anyMatch(s -> s.equals(coordinateToCheck));
        }
        return coordinateOccupied;
    }

}
