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
    private double width = 1000;
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
    
    //left pane and buttons - name of the ships
    private static VBox leftPane = new VBox(10);
    private static List<Button> shipPlacementButtonList = new ArrayList<>();
    
    //right pane and buttons - reset ships
    private static VBox rightPane = new VBox(10);
    private static List<Button> resetShipButtonList = new ArrayList<>();
    
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
        updatingShipPartLabel();

        //TODO
        //playerFirePane should be added ONLY after all ships have been placed
        centerPane.getChildren().addAll(playerFirePaneTop, labelShipPartsPlaced, playerLocationBoardBottom);
        centerPane.setAlignment(Pos.CENTER);
        playerFirePaneTop.setAlignment(Pos.CENTER);
        playerLocationBoardBottom.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(10));
        
        
        layout.setCenter(centerPane);
        BorderPane.setAlignment(centerPane, Pos.CENTER);
        
        
//      left pane
        leftPane.setPadding(new Insets(10));
        
        Button carrierButton = new Button("Carrier - 5 spaces");
        carrierButton.setOnAction(new ShipPlacementButtonEH(ShipName.CARRIER));
        shipPlacementButtonList.add(carrierButton);
        
        Button battleshipButton = new Button("Battleship - 4 spaces");
        battleshipButton.setOnAction(new ShipPlacementButtonEH(ShipName.BATTLESHIP));
        shipPlacementButtonList.add(battleshipButton);
     
        Button cruiserButton = new Button("Cruiser - 3 spaces");
        cruiserButton.setOnAction(new ShipPlacementButtonEH(ShipName.CRUISER));
        shipPlacementButtonList.add(cruiserButton);
        
        Button submarineButton = new Button("Submarine - 3 spaces");
        submarineButton.setOnAction(new ShipPlacementButtonEH(ShipName.SUBMARINE));
        shipPlacementButtonList.add(submarineButton);
        
        Button destroyerButton = new Button("Destroyer - 2 spaces");
        destroyerButton.setOnAction(new ShipPlacementButtonEH(ShipName.DESTROYER));
        shipPlacementButtonList.add(destroyerButton);
        
        for(Button b : shipPlacementButtonList){
            b.setId("shipButtonsPlacement");
        }
        
        leftPane.getChildren().addAll(carrierButton, battleshipButton, cruiserButton, submarineButton, destroyerButton);
        leftPane.setAlignment(Pos.BOTTOM_CENTER);
        layout.setLeft(leftPane);
        
        
//        right pane
        rightPane.setPadding(new Insets(10));


        Button resetCarrierButton = new Button("Reset Carrier");
        resetCarrierButton.setOnAction(new ResetPlacementButtonEH(ShipName.CARRIER));
        resetShipButtonList.add(resetCarrierButton);

        Button resetBattleshipButton = new Button("Reset Battleship");
        resetBattleshipButton.setOnAction(new ResetPlacementButtonEH(ShipName.BATTLESHIP));
        resetShipButtonList.add(resetBattleshipButton);

        Button resetCruiserButton = new Button("Reset Cruiser");
        resetCruiserButton.setOnAction(new ResetPlacementButtonEH(ShipName.CRUISER));
        resetShipButtonList.add(resetCruiserButton);

        Button resetSubmarineButton = new Button("Reset Submarine");
        resetSubmarineButton.setOnAction(new ResetPlacementButtonEH(ShipName.SUBMARINE));
        resetShipButtonList.add(resetSubmarineButton);

        Button resetDestroyerButton = new Button("Reset Destroyer");
        resetDestroyerButton.setOnAction(new ResetPlacementButtonEH(ShipName.DESTROYER));
        resetShipButtonList.add(resetDestroyerButton);
        
        Button resetAll = new Button("Reset all");
        resetAll.setOnAction(new ResetAllButtonEH());
        resetShipButtonList.add(resetAll);

        for(Button b : resetShipButtonList){
            b.setId("shipButtonsReset");
        }

        rightPane.getChildren().addAll(resetAll, resetCarrierButton, resetBattleshipButton, resetCruiserButton, resetSubmarineButton, resetDestroyerButton);
        rightPane.setAlignment(Pos.BOTTOM_CENTER);
        layout.setRight(rightPane);
        
        
//        down pane
        HBox downPane = new HBox(10);
        
        //exit button
        Button exit = new Button("Exit");
        exit.setPrefSize(130,20);
        exit.setOnAction(event -> AfterClick.closeProgram(window));


        downPane.getChildren().addAll(exit);
        if(debug){
            Button showPlayerShipList = new Button("show");
            showPlayerShipList.setOnAction(event -> 
                playerShips.stream().forEach(ship -> System.out.println(ship.getName()))
            );
            downPane.getChildren().add(showPlayerShipList);
        }
        downPane.setAlignment(Pos.CENTER);

        layout.setBottom(downPane);
        
        
//        setting scene
        scene = new Scene(layout, width, height);
        scene.getStylesheets().add("gameStyles.css");

    }

    private class ShipPlacementButtonEH implements EventHandler<ActionEvent>{
        private ShipName shipToHandleName;

        public ShipPlacementButtonEH(ShipName shipToHandle) {
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
                updatingShipPartLabel();
                ((Button) event.getSource()).setDisable(true);
            }
        }
    }

    private class ResetPlacementButtonEH implements EventHandler<ActionEvent>{
        private ShipName shipToHandleName;

        public ResetPlacementButtonEH(ShipName shipToHandleName) {
            this.shipToHandleName = shipToHandleName;
        }

        @Override
        public void handle(ActionEvent event) {
            if(debug){
                System.out.println(event.getSource());
            }
            
            boolean shipWasPlaced = playerShips.stream()
                    .map(Ship::getName)
                    .anyMatch(shipName -> shipName.equals(shipToHandleName));
            
            if(shipWasPlaced){
                // set current ship to null
                // activate all cells with coordinates and change their colors
                // activate ship button list
                // delete from list given ship
                currentShip = null;
                updatingShipPartLabel();


                Ship shipToReset = playerShips.stream()
                        .filter(ship -> ship.getName().equals(shipToHandleName))
                        .findFirst()
                        .get();
                
                
                List<String> coordinatesToReset = shipToReset.getCoordinates();
                
                for(String s : coordinatesToReset){
                    Button toReset = locationButtonList.get(Integer.parseInt(s));
                    toReset.setDisable(false);
                    toReset.setId("boardButton");
                }
                
                switch(shipToReset.getName()){
                    case CARRIER:
                        shipPlacementButtonList.get(0).setDisable(false);
                        break;
                    case BATTLESHIP:
                        shipPlacementButtonList.get(1).setDisable(false);
                        break;
                    case CRUISER:
                        shipPlacementButtonList.get(2).setDisable(false);
                        break;
                    case SUBMARINE:
                        shipPlacementButtonList.get(3).setDisable(false);
                        break;
                    case DESTROYER:
                        shipPlacementButtonList.get(4).setDisable(false);
                        break;
                }
                
                playerShips.remove(shipToReset);
            }
        }
    }

    private class ResetAllButtonEH implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            if(debug){
                System.out.println(event.getSource());
            }
            
            if(!playerShips.isEmpty()){
                currentShip = null;
                updatingShipPartLabel();
                
                playerShips.stream()
                        .flatMap(ship -> ship.getCoordinates().stream())
                        .map(s -> Integer.parseInt(s))
                        .forEach(integer -> {
                            Button toReset = locationButtonList.get(integer);
                            toReset.setId("boardButton");
                            toReset.setDisable(false);
                        });
                
                playerShips.clear();
                shipPlacementButtonList.stream().forEach(button -> button.setDisable(false));
                
            }
        }
    }
    
    
    
    

    /**
     * Changing label above playerLocationBoardBottom
     */
    public static void updatingShipPartLabel(){
        if(currentShip != null){
            labelShipPartsPlaced.setText("Current ship: " + currentShip.getName() +
                    ". Remaining ship parts: " + (currentShip.getShipMaxSize() - currentShip.getShipFieldCount()));
            labelShipPartsPlaced.setTextAlignment(TextAlignment.CENTER);
        } else {
            labelShipPartsPlaced.setText("No ship is selected");
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