package UI;

import GameMechanic.Ship;
import GameMechanic.ShipName;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static Game game;
    private Stage window;
    private Scene scene;
    private double width = 800;
    private double height = 1000;
    public static boolean debug;
    private static Ship currentShip;
    private static Label labelShipPartsPlaced;
    
    private List<Ship> playerShips = new ArrayList<>();
    
    //center top - player firing board
    private static GridPane playerFirePaneTop = new GridPane();
    private List<Button> fireButtonList = ButtonMethods.createFireButtonList();

    //center bottom - player ships location
    private static GridPane playerLocationBoardBottom = new GridPane();
    private List<Button> locationButtonList = ButtonMethods.createLocationButtonList();
    
    //left buttons - name of the ships
    private static VBox leftPane = new VBox(10);
    private static List<Button> shipNameButtonListWest = new ArrayList<>();
    
    public static Game getInstance(Stage primaryStage){
        if(game == null) {
            game = new Game(primaryStage);
        }
        return game;
    }

    private Game(Stage primaryStage) {
        window = primaryStage;

        window.setOnCloseRequest(e -> {
            e.consume();
            AfterClick.closeProgram(window);
        });
        
//      root pane
        BorderPane layout = new BorderPane();
        
        layout.setPadding(new Insets(50, 50, 50, 50));
        
        
        
//      center pane
        VBox centerPane = new VBox(20);
        
        labelShipPartsPlaced = new Label();
        changingShipPartLabel();

        //TODO
        //playerFirePane should be added ONLY after all ships have been placed
        centerPane.getChildren().addAll(playerFirePaneTop, labelShipPartsPlaced, playerLocationBoardBottom);
        
        layout.setCenter(centerPane);
        BorderPane.setAlignment(centerPane, Pos.CENTER);
        
        
//      left pane
        leftPane.setPadding(new Insets(10));
        
        Button carrierButton = new Button("Carrier - 5 spaces");
        carrierButton.setOnAction(new ShipButtonEventHandler(ShipName.CARRIER));
        shipNameButtonListWest.add(carrierButton);
        
        Button battleshipButton = new Button("Battleship - 4 spaces");
        battleshipButton.setOnAction(new ShipButtonEventHandler(ShipName.BATTLESHIP));
        shipNameButtonListWest.add(battleshipButton);
     
        Button cruiserButton = new Button("Cruiser - 3 spaces");
        cruiserButton.setOnAction(new ShipButtonEventHandler(ShipName.CRUISER));
        shipNameButtonListWest.add(cruiserButton);
        
        Button submarineButton = new Button("Submarine - 3 spaces");
        submarineButton.setOnAction(new ShipButtonEventHandler(ShipName.SUBMARINE));
        shipNameButtonListWest.add(submarineButton);
        
        Button destroyerButton = new Button("Destroyer - 2 spaces");
        destroyerButton.setOnAction(new ShipButtonEventHandler(ShipName.DESTROYER));
        shipNameButtonListWest.add(destroyerButton);
        
        for(Button b : shipNameButtonListWest){
            b.setId("shipButtonsActive");
        }
        
        leftPane.getChildren().addAll(carrierButton, battleshipButton, cruiserButton, submarineButton, destroyerButton);
        leftPane.setAlignment(Pos.BOTTOM_CENTER);
        layout.setLeft(leftPane);
        
        
//        down pane
        HBox downPane = new HBox(10);
        
        //exit button
        Button exit = new Button("Exit");
        exit.setPrefSize(130,20);
        exit.setOnAction(event -> AfterClick.closeProgram(window));
        
        downPane.getChildren().addAll(exit);
        downPane.setAlignment(Pos.CENTER);

        layout.setBottom(downPane);
        
        
//        setting scene
        scene = new Scene(layout, width, height);
        scene.getStylesheets().add("gameStyles.css");

    }

    private class ShipButtonEventHandler implements EventHandler<ActionEvent>{
        private ShipName shipToHandleName;

        public ShipButtonEventHandler(ShipName shipToHandle) {
            this.shipToHandleName = shipToHandle;
        }

        @Override
        public void handle(ActionEvent event) {
            if(debug){
                System.out.println(event.getSource());
                if(currentShip != null){
                    System.out.println(currentShip.getName());
                }
            }
            
            if(currentShip == null ||  ((currentShip.getShipMaxSize() - currentShip.getShipFieldCount()) == 0)){
                Ship shipToHandle = new Ship(shipToHandleName);

                currentShip = shipToHandle;
                playerShips.add(shipToHandle);
                changingShipPartLabel();
                ((Button) event.getSource()).setDisable(true);
            }
        }
    }

    /**
     * Changing label above playerLocationBoardBottom
     */
    public static void changingShipPartLabel(){
        if(currentShip != null){
            labelShipPartsPlaced.setText("Current ship: " + currentShip.getName() +
                    ". Remaining ship parts: " + (currentShip.getShipMaxSize() - currentShip.getShipFieldCount()));
            labelShipPartsPlaced.setTextAlignment(TextAlignment.CENTER);
        } else {
            labelShipPartsPlaced.setText("No ship selected");
        }
    }

    public Scene getScene() {
        return this.scene;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Stage getWindow() {
        return window;
    }

    public static GridPane getPlayerLocationBoardBottom() {
        return playerLocationBoardBottom;
    }

    public static GridPane getPlayerFirePaneTop() {
        return playerFirePaneTop;
    }

    public static void setDebug(boolean debug) {
        Game.debug = debug;
    }

    public static Ship getCurrentShip() {
        return currentShip;
    }

    public static void setCurrentShip(Ship currentShip) {
        Game.currentShip = currentShip;
    }

    public static Label getLabelShipPartsPlaced() {
        return labelShipPartsPlaced;
    }
    
}