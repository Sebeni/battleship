package UI;

import GameMechanic.CpuPlacement;
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
import java.util.Map;

public class Game {
    private Stage window;
    private Scene scene;
    private final double width = 1000;
    private final double height = 1000;
    public static boolean debug;
    private static Ship currentShip;
    private static Label labelShipPartsPlaced;
    
    private final GameHandlers gameHandler = new GameHandlers(this);

    private final List<Ship> playerShips = new ArrayList<>();

    //center top - player firing board
    private final GridPane playerFirePaneTop = new GridPane();
    private final List<Button> fireButtonList = GridPaneButtonMethods.create100ButtonList(playerFirePaneTop, "fireButton", true, event -> GridPaneButtonMethods.fireButtonHandler(event));

    //center bottom - player ships location
    private final GridPane playerLocationBoardBottom = new GridPane();
    private final List<Button> locationButtonList = GridPaneButtonMethods.create100ButtonList(playerLocationBoardBottom, "boardButton", false, event -> GridPaneButtonMethods.placementButtonHandler(event, this));

    //left pane and buttons - name of the ships
    private final VBox leftPane = new VBox(10);
    private final List<Button> shipPlacementButtonList = new ArrayList<>();

    //right pane and buttons - reset ships
    private final VBox rightPane = new VBox(10);
    private final List<Button> resetShipButtonList = new ArrayList<>();

    //bottom pane
    private final HBox bottomPane = new HBox(10);
    private final List<Button> bottomPaneButtonList = new ArrayList<>();

    public Game(Stage primaryStage) {
        window = primaryStage;

        window.setOnCloseRequest(e -> {
            e.consume();
            AfterClick.closeProgram(window);
        });

//      root pane for all other panes
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(50, 50, 50, 50));


//      center pane
        VBox centerPane = new VBox(20);

        labelShipPartsPlaced = new Label();
        updatingShipPartLabel();
        
        centerPane.getChildren().addAll(playerFirePaneTop, labelShipPartsPlaced, playerLocationBoardBottom);
        centerPane.setAlignment(Pos.CENTER);
        playerFirePaneTop.setAlignment(Pos.CENTER);
        playerFirePaneTop.setStyle("-fx-cursor: crosshair;");
        playerLocationBoardBottom.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(10));


        layout.setCenter(centerPane);
        BorderPane.setAlignment(centerPane, Pos.CENTER);


//      left pane
        leftPane.setPadding(new Insets(10));

        Button carrierButton = new Button("Carrier - 5 spaces");
        carrierButton.setOnAction(event -> gameHandler.shipPlacementButtonEH(event, ShipName.CARRIER));
        shipPlacementButtonList.add(carrierButton);

//        new ShipPlacementButtonEH(ShipName.BATTLESHIP)
        
        Button battleshipButton = new Button("Battleship - 4 spaces");
        battleshipButton.setOnAction(event -> gameHandler.shipPlacementButtonEH(event, ShipName.BATTLESHIP));
        shipPlacementButtonList.add(battleshipButton);

        Button cruiserButton = new Button("Cruiser - 3 spaces");
        cruiserButton.setOnAction(event -> gameHandler.shipPlacementButtonEH(event, ShipName.CRUISER));
        shipPlacementButtonList.add(cruiserButton);

        Button submarineButton = new Button("Submarine - 3 spaces");
        submarineButton.setOnAction(event -> gameHandler.shipPlacementButtonEH(event, ShipName.SUBMARINE));
        shipPlacementButtonList.add(submarineButton);

        Button destroyerButton = new Button("Destroyer - 2 spaces");
        destroyerButton.setOnAction(event -> gameHandler.shipPlacementButtonEH(event, ShipName.DESTROYER));
        shipPlacementButtonList.add(destroyerButton);

        for (Button b : shipPlacementButtonList) {
            b.setId("shipButtonsPlacement");
        }

        leftPane.getChildren().addAll(carrierButton, battleshipButton, cruiserButton, submarineButton, destroyerButton);
        leftPane.setAlignment(Pos.BOTTOM_CENTER);
        layout.setLeft(leftPane);


//        right pane
        rightPane.setPadding(new Insets(10));


        Button resetCarrierButton = new Button("Reset Carrier");
        resetCarrierButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.CARRIER));
        resetShipButtonList.add(resetCarrierButton);

        Button resetBattleshipButton = new Button("Reset Battleship");
        resetBattleshipButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.BATTLESHIP));
        resetShipButtonList.add(resetBattleshipButton);

        Button resetCruiserButton = new Button("Reset Cruiser");
        resetCruiserButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.CRUISER));
        resetShipButtonList.add(resetCruiserButton);

        Button resetSubmarineButton = new Button("Reset Submarine");
        resetSubmarineButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.SUBMARINE));
        resetShipButtonList.add(resetSubmarineButton);

        Button resetDestroyerButton = new Button("Reset Destroyer");
        resetDestroyerButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.DESTROYER));
        resetShipButtonList.add(resetDestroyerButton);

        Button resetAll = new Button("Reset all");
        resetAll.setOnAction(event -> gameHandler.resetAllButtonEH(event));
        resetShipButtonList.add(resetAll);

        for (Button b : resetShipButtonList) {
            b.setId("shipButtonsReset");
        }

        rightPane.getChildren().addAll(resetAll, resetCarrierButton, resetBattleshipButton, resetCruiserButton, resetSubmarineButton, resetDestroyerButton);
        rightPane.setAlignment(Pos.BOTTOM_CENTER);
        layout.setRight(rightPane);


//        down pane
        Button exit = new Button("Exit");
        exit.setOnAction(event -> AfterClick.closeProgram(window));
        bottomPaneButtonList.add(exit);

        Button newGame = new Button("New Game");
        newGame.setOnAction(event -> gameHandler.newGameButtonEH(event));
        bottomPaneButtonList.add(newGame);

        for (Button b : bottomPaneButtonList) {
            b.setId("bottomButtons");
        }

        bottomPane.getChildren().addAll(exit, newGame);

        if (debug) {
            Button showPlayerShipList = new Button("show player ship list");
            showPlayerShipList.setOnAction(event ->
                    playerShips.forEach(System.out::println)
            );
            
            Button cpuPlacement = new Button("cpu placement window");
            cpuPlacement.setOnAction(event -> new CpuPlacement());
            

            bottomPane.getChildren().addAll(showPlayerShipList, cpuPlacement);
        }
        bottomPane.setAlignment(Pos.CENTER);

        layout.setBottom(bottomPane);


//        setting scene
        scene = new Scene(layout, width, height);
        scene.getStylesheets().add("gameStyles.css");
    }
    

    /**
     * Changing label above playerLocationBoardBottom
     */
    public static void updatingShipPartLabel() {
        if (currentShip != null) {
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

    public GridPane getPlayerLocationBoardBottom() {
        return playerLocationBoardBottom;
    }

    public GridPane getPlayerFirePaneTop() {
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

    public List<Ship> getPlayerShips() {
        return playerShips;
    }


    public List<Button> getShipPlacementButtonList() {
        return shipPlacementButtonList;
    }

    public List<Button> getLocationButtonList() {
        return locationButtonList;
    }

    public List<Button> getFireButtonList() {
        return fireButtonList;
    }

    public List<Button> getResetShipButtonList() {
        return resetShipButtonList;
    }
}