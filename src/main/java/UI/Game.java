package UI;

import GameMechanic.Player;
import GameMechanic.RandomPlacement;
import GameMechanic.Ship;
import GameMechanic.ShipName;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static boolean debug;
    
    private Stage window;
    private Scene scene;
    private final double width = 1200;
    private final double height = 1000;
    
    private boolean firePhase = false;

    private static Ship currentShip;
    private static Label middleLabel;

    private final ButtonHandlers gameHandler = new ButtonHandlers(this);
    private final GridPaneButtonMethods gridMethods = new GridPaneButtonMethods(this);

    //center top - human firing board
    private final GridPane playerFireBoardTop = new GridPane();
    private final List<Button> fireButtonListTop;

    //center bottom - human ships location
    private final GridPane playerLocationBoardBottom = new GridPane();
    private final List<Button> seaButtonsListBottom;

    //left pane and buttons - name of the ships
    private final VBox leftPane = new VBox(150);
    private final ScrollPane instructionPane = new ScrollPane();
    private final List<Button> placementShipButtonListLeft = new ArrayList<>();

    //right pane and buttons - reset ships
    private final VBox rightPane = new VBox(150);
    private final ScrollPane updateStatus = new ScrollPane();
    private final List<Button> resetShipButtonListRight = new ArrayList<>();

    //bottom pane
    private final HBox bottomPane = new HBox(10);
    private final List<Button> bottomPaneButtonList = new ArrayList<>();

    private final Player human = new Player(true);

    private CpuVisual cpuVisual;

    private final Player cpu = new Player(false);

    public Game(Stage primaryStage) {
        window = primaryStage;

        window.setOnCloseRequest(e -> {
            e.consume();
            AfterClick.closeProgram(window);
        });
        
        cpuVisual = new CpuVisual(this);

        fireButtonListTop = gridMethods.create100ButtonList(playerFireBoardTop, "fireButton", true, gridMethods::fireButtonHandler);
        seaButtonsListBottom = gridMethods.create100ButtonList(playerLocationBoardBottom, "boardButton", false, gridMethods::placementButtonHandler);
        
        playerLocationBoardBottom.getChildren().forEach(node -> {
            Pane pane = (Pane) node;
            pane.setOnMouseEntered(gridMethods::mouseEnteredEH);
            pane.setOnMouseExited(gridMethods::mouseExitedEH);

        });

//      root pane for all other panes
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(50, 50, 50, 50));


//      center pane
        VBox centerPane = new VBox(20);

        middleLabel = new Label();
        updatingMiddleLabel();

        centerPane.getChildren().addAll(playerFireBoardTop, middleLabel, playerLocationBoardBottom);
        centerPane.setAlignment(Pos.CENTER);
        playerFireBoardTop.setAlignment(Pos.CENTER);
        playerFireBoardTop.setStyle("-fx-cursor: crosshair;");
        playerLocationBoardBottom.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(10));


        layout.setCenter(centerPane);
        BorderPane.setAlignment(centerPane, Pos.CENTER);


//      left pane
        leftPane.setPadding(new Insets(10));
        instructionPane.setPrefWidth(250);
        instructionPane.setPrefHeight(600);
        instructionPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

//        upper left
        VBox upperLeft = new VBox(10);

        Label instructionLabel = new Label();
        instructionLabel.setText("Before play begins, you must arranges all your ships on your bottom grid." +
                "\nEach ship occupies a number of consecutive squares on the grid, arranged either horizontally or vertically." +
                "\nThe number of squares for each ship is determined by the type of the ship. " +
                "\nThe ships cannot overlap (i.e., only one ship can occupy any given square in the grid). " +
                "Ships however can touch each other.");
        instructionLabel.setWrapText(true);
        instructionLabel.setTextAlignment(TextAlignment.JUSTIFY);
        instructionLabel.setMaxWidth(250);
        instructionLabel.setPadding(new Insets(10));

        instructionPane.setContent(instructionLabel);
        
        upperLeft.getChildren().add(instructionPane);
        
//        bottom left
        VBox bottomLeft = new VBox(10);


        Button carrierButton = new Button("Carrier - 5 spaces");
        carrierButton.setOnAction(event -> gameHandler.shipPlacementButtonEH(event, ShipName.CARRIER));
        placementShipButtonListLeft.add(carrierButton);

        Button battleshipButton = new Button("Battleship - 4 spaces");
        battleshipButton.setOnAction(event -> gameHandler.shipPlacementButtonEH(event, ShipName.BATTLESHIP));
        placementShipButtonListLeft.add(battleshipButton);

        Button cruiserButton = new Button("Cruiser - 3 spaces");
        cruiserButton.setOnAction(event -> gameHandler.shipPlacementButtonEH(event, ShipName.CRUISER));
        placementShipButtonListLeft.add(cruiserButton);

        Button submarineButton = new Button("Submarine - 3 spaces");
        submarineButton.setOnAction(event -> gameHandler.shipPlacementButtonEH(event, ShipName.SUBMARINE));
        placementShipButtonListLeft.add(submarineButton);

        Button destroyerButton = new Button("Destroyer - 2 spaces");
        destroyerButton.setOnAction(event -> gameHandler.shipPlacementButtonEH(event, ShipName.DESTROYER));
        placementShipButtonListLeft.add(destroyerButton);

        Button randomPlacement = new Button("Random placement");
        randomPlacement.setOnAction(gameHandler::randomPlacementButtonEH);
        placementShipButtonListLeft.add(randomPlacement);

        for (Button b : placementShipButtonListLeft) {
            b.setId("shipButtonsPlacement");
        }
        
        bottomLeft.getChildren().addAll(randomPlacement, carrierButton, battleshipButton, cruiserButton, submarineButton, destroyerButton);
        bottomLeft.setAlignment(Pos.CENTER);
        
        leftPane.getChildren().addAll(upperLeft, bottomLeft);
        layout.setLeft(leftPane);
        


//        right pane
        rightPane.setPadding(new Insets(10));

        updateStatus.setPrefWidth(250);
        updateStatus.setPrefHeight(600);
        updateStatus.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
//        bottom right
        VBox bottomRight = new VBox(10);
        Button resetCarrierButton = new Button("Reset Carrier");
        resetCarrierButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.CARRIER));
        resetShipButtonListRight.add(resetCarrierButton);

        Button resetBattleshipButton = new Button("Reset Battleship");
        resetBattleshipButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.BATTLESHIP));
        resetShipButtonListRight.add(resetBattleshipButton);

        Button resetCruiserButton = new Button("Reset Cruiser");
        resetCruiserButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.CRUISER));
        resetShipButtonListRight.add(resetCruiserButton);
        
        Button resetSubmarineButton = new Button("Reset Submarine");
        resetSubmarineButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.SUBMARINE));
        resetShipButtonListRight.add(resetSubmarineButton);

        Button resetDestroyerButton = new Button("Reset Destroyer");
        resetDestroyerButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.DESTROYER));
        resetShipButtonListRight.add(resetDestroyerButton);

        Button resetAll = new Button("Reset all");
        resetAll.setOnAction(gameHandler::resetAllButtonEH);
        resetShipButtonListRight.add(resetAll);

        for (Button b : resetShipButtonListRight) {
            b.setId("shipButtonsReset");
        }

        bottomRight.getChildren().addAll(resetAll, resetCarrierButton, resetBattleshipButton, resetCruiserButton, resetSubmarineButton, resetDestroyerButton);
        bottomRight.setAlignment(Pos.CENTER);
        
        rightPane.getChildren().addAll(updateStatus, bottomRight);
        layout.setRight(rightPane);


//        down pane
        Button exit = new Button("Exit");
        exit.setOnAction(event -> AfterClick.closeProgram(window));
        bottomPaneButtonList.add(exit);

        Button newGame = new Button("Start game");
        newGame.setOnAction(event1 -> {
            gameHandler.newGameButtonEH(event1);
            firePhase = true;
        });
        bottomPaneButtonList.add(newGame);

        for (Button b : bottomPaneButtonList) {
            b.setId("bottomButtons");
        }

        bottomPane.getChildren().addAll(exit, newGame);

        if (debug) {
            Button showShips = new Button("show ship lists");
            showShips.setOnAction(event -> {
                System.out.println("***HUMAN***");
                human.getShipsList().forEach(System.out::println);
                System.out.println("***CPU***");
                cpu.getShipsList().forEach(System.out::println);
            });

            bottomPane.getChildren().addAll(showShips);
        }
        bottomPane.setAlignment(Pos.CENTER);

        layout.setBottom(bottomPane);


//        setting scene
        scene = new Scene(layout, width, height);
        scene.getStylesheets().add("gameStyles.css");
    }
    
    public static void updatingMiddleLabel() {
        if (currentShip != null) {
            middleLabel.setText("Current ship: " + currentShip.getName() +
                    ". Remaining ship parts: " + (currentShip.getShipMaxSize() - currentShip.getShipPartsInGameCount()));
            middleLabel.setTextAlignment(TextAlignment.CENTER);
        } else {
            middleLabel.setText("No ship is selected");
        }
    }
    
    public static void setMiddleLabel(String textToPlace){
        middleLabel.setText(textToPlace);
    }

    public Scene getScene() {
        return this.scene;
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

    public static Label getMiddleLabel() {
        return middleLabel;
    }
    
    public List<Button> getPlacementShipButtonListLeft() {
        return placementShipButtonListLeft;
    }

    public List<Button> getSeaButtonsListBottom() {
        return seaButtonsListBottom;
    }

    public List<Button> getFireButtonListTop() {
        return fireButtonListTop;
    }

    public List<Button> getResetShipButtonListRight() {
        return resetShipButtonListRight;
    }

    public void setHumanShips() {
        human.setPlayerShips(RandomPlacement.randomShipPlacement(seaButtonsListBottom));
    }

    public void setCpuShips() {
        cpu.setPlayerShips(RandomPlacement.randomShipPlacement(cpuVisual.getButtonList()));
    }

    public Player getCpu() {
        return cpu;
    }

    public Player getHuman() {
        return human;
    }

    public boolean isFirePhase() {
        return firePhase;
    }
}