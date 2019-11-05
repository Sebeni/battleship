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
import java.util.Map;

public class Game {
    private Stage window;
    private Scene scene;
    private double width = 1000;
    private double height = 1000;
    public static boolean debug;
    private static Ship currentShip;
    private static Label labelShipPartsPlaced;

    private List<Ship> playerShips = new ArrayList<>();

    //center top - player firing board
    private GridPane playerFirePaneTop = new GridPane();
    private List<Button> fireButtonList = ButtonMethods.create100ButtonList(playerFirePaneTop, "fireButtons", true, new ButtonMethods.FireButtonHandler());

    //center bottom - player ships location
    private GridPane playerLocationBoardBottom = new GridPane();
    private List<Button> locationButtonList = ButtonMethods.create100ButtonList(playerLocationBoardBottom, "boardButton", false, new ButtonMethods.PlacementButtonHandler());

    //left pane and buttons - name of the ships
    private VBox leftPane = new VBox(10);
    private List<Button> shipPlacementButtonList = new ArrayList<>();

    //right pane and buttons - reset ships
    private VBox rightPane = new VBox(10);
    private List<Button> resetShipButtonList = new ArrayList<>();

    //bottom pane
    private HBox bottomPane = new HBox(10);
    private List<Button> bottomPaneButtonList = new ArrayList<>();

    public Game(Stage primaryStage) {
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
        playerFirePaneTop.setStyle("-fx-cursor: crosshair;");
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

        for (Button b : shipPlacementButtonList) {
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
        newGame.setOnAction(new NewGameButtonEH());
        bottomPaneButtonList.add(newGame);

        for (Button b : bottomPaneButtonList) {
            b.setId("bottomButtons");
        }

        bottomPane.getChildren().addAll(exit, newGame);

        if (debug) {
            Button showPlayerShipList = new Button("show player ship list");
            showPlayerShipList.setOnAction(event ->
                    playerShips.forEach(ship -> System.out.println(ship.getName()))
            );
            
            Button cpuPlacement = new Button("cpu placement window");
            

            bottomPane.getChildren().addAll(showPlayerShipList);
        }
        bottomPane.setAlignment(Pos.CENTER);

        layout.setBottom(bottomPane);


//        setting scene
        scene = new Scene(layout, width, height);
        scene.getStylesheets().add("gameStyles.css");
    }

    //    handlers too big to fit in lambda or anonymous class
    private class ShipPlacementButtonEH implements EventHandler<ActionEvent> {
        private ShipName shipToHandleName;

        ShipPlacementButtonEH(ShipName shipToHandle) {
            this.shipToHandleName = shipToHandle;
        }

        @Override
        public void handle(ActionEvent event) {
            if (debug) {
                System.out.println(event.getSource());
                if (currentShip != null) {
                    System.out.println(currentShip.getName());
                }
            }

            if (currentShip == null || ((currentShip.getShipMaxSize() - currentShip.getShipFieldCount()) == 0)) {
                Ship shipToHandle = new Ship(shipToHandleName);

                currentShip = shipToHandle;
                playerShips.add(shipToHandle);
                updatingShipPartLabel();
                ((Button) event.getSource()).setDisable(true);
            }
        }
    }

    private class ResetPlacementButtonEH implements EventHandler<ActionEvent> {
        private ShipName shipToHandleName;

        ResetPlacementButtonEH(ShipName shipToHandleName) {
            this.shipToHandleName = shipToHandleName;
        }

        @Override
        public void handle(ActionEvent event) {
            if (debug) {
                System.out.println(event.getSource());
            }

            boolean shipWasPlaced = playerShips.stream()
                    .map(Ship::getName)
                    .anyMatch(shipName -> shipName.equals(shipToHandleName));

            if (shipWasPlaced) {
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

                shipToReset.getCoordinates().stream()
                        .forEach(s -> {
                            Button toReset = locationButtonList.get(Integer.parseInt(s));
                            toReset.setDisable(false);
                            toReset.setId("boardButton");
                        });

                switch (shipToReset.getName()) {
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

    private class ResetAllButtonEH implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            if (debug) {
                System.out.println(event.getSource());
            }

            if (!playerShips.isEmpty()) {
                currentShip = null;
                updatingShipPartLabel();

                playerShips.stream()
                        .flatMap(ship -> ship.getCoordinates().stream())
                        .forEach(s -> {
                            Button toReset = locationButtonList.get(Integer.parseInt(s));
                            toReset.setId("boardButton");
                            toReset.setDisable(false);
                        });

                playerShips.clear();
                shipPlacementButtonList.stream().forEach(button -> button.setDisable(false));
            }
        }
    }

    private class NewGameButtonEH implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            long shipPartsPlaced = playerShips.stream()
                    .flatMap(ship -> ship.getCoordinates().stream())
                    .count();

            long allShipsParts = 0;

            for (Map.Entry<ShipName, Integer> entry : Ship.getAllShipsMaxSize().entrySet()) {
                allShipsParts += entry.getValue();
            }

            if (shipPartsPlaced == allShipsParts) {
                fireButtonList.forEach(button -> button.setDisable(false));
                locationButtonList.forEach(button -> button.setDisable(true));
                resetShipButtonList.forEach(button -> button.setDisable(true));
                new Stage();
            } else {
                AlertBox.display("Place all ships", "Before starting a new game you must place all your ships!");
            }
        }
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
}