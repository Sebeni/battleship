package pl.seb.czech.GameUI.Boxes;

import pl.seb.czech.GameMechanic.Player;
import pl.seb.czech.GameMechanic.RandomPlacement;
import pl.seb.czech.GameMechanic.Ship;
import pl.seb.czech.GameMechanic.ShipName;
import pl.seb.czech.GameUI.GridHelperMethods;
import pl.seb.czech.GameUI.Handlers.ButtonHandlers;
import pl.seb.czech.GameUI.Handlers.FireButtonHandlers;
import pl.seb.czech.GameUI.Handlers.SeaButtonHandlers;
import pl.seb.czech.GameUI.SceneChanger;
import pl.seb.czech.MenuUI.Options;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Game implements SceneChanger {
    //game elements
    private int roundCounter = 1;
    private boolean firePhase = false;
    private static boolean shipsCanTouch;

    private Ship currentShip;
    private final Label middleLabel;

    private final Player human = new Player();
    private final Player cpu = new Player();
    private final CpuVisualBox cpuVisualBox;
    
    //javaFX elements
    private final Stage window;
    private final Scene scene;
    
    // parameters of elements
    private final double windowWidth = 1400;
    private final double windowHeight = 1000;
    private final double scrollPaneWidth = 350;
    private final double scrollPaneHeight = 425;
    private final double labelWidth = scrollPaneWidth - 20;
    
    //center top - human firing board
    private final GridPane playerFireBoardTop = new GridPane();
    private final List<Button> fireButtonListTop;

    //center bottom - human ships location
    private final GridPane playerLocationBoardBottom = new GridPane();
    private final List<Button> seaButtonsListBottom;

    //left pane
    private final VBox leftPane = new VBox(200);
    private final List<Button> newGameButtons = new ArrayList<>();
    private final List<Button> placementShipButtonListLeft = new ArrayList<>();
    private final List<Button> resetShipButtonListRight = new ArrayList<>();

    //right pane 
    private final VBox rightPane = new VBox(50);
    private final ScrollPane updateStatus = new ScrollPane();
    private final VBox sunkEnemyShips;
    
    //right pane top - battleLog
    private final Label battleLog = new Label();
    

    public Game(Stage primaryStage) {
        window = primaryStage;
        cpuVisualBox = new CpuVisualBox();

        window.setOnCloseRequest(e -> {
            e.consume();
            SceneChanger.closeProgram(window, cpuVisualBox.getWindow());
        });
        
        SeaButtonHandlers seaHandler = new SeaButtonHandlers(this);
        FireButtonHandlers fireHandler = new FireButtonHandlers(this);
        ButtonHandlers buttonHandlers = new ButtonHandlers(this);
        
        fireButtonListTop = GridHelperMethods.create100ButtonList(playerFireBoardTop, "fireButton", true, fireHandler::fireButtonHandler);
        seaButtonsListBottom = GridHelperMethods.create100ButtonList(playerLocationBoardBottom, "boardButton", false, seaHandler::placementButtonHandler);

//      root pane for all other panes
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20, 50, 20, 50));
        
//      center pane
        VBox centerPane = new VBox(20);

        middleLabel = new Label();
        middleLabelUpdateText();

        playerLocationBoardBottom.getChildren().forEach(node -> {
            Pane pane = (Pane) node;
            pane.setOnMouseEntered(seaHandler::mouseShipNameEH);
            pane.setOnMouseExited(seaHandler::mouseExitShipNameEH);

        });
        
        fireButtonListTop.forEach(button -> {
            button.setOnMouseEntered(fireHandler::mouseFireFieldName);
            button.setOnMouseExited(fireHandler::mouseExitFireFieldName);
        });
        
        GridPane topRootForGrid = new GridPane();
        topRootForGrid.add(playerFireBoardTop, 1, 1, 10, 10);
        topRootForGrid.setAlignment(Pos.CENTER);
        
        GridPane bottomRootForGrid = new GridPane();
        bottomRootForGrid.add(playerLocationBoardBottom, 1, 1, 10, 10);
        bottomRootForGrid.setAlignment(Pos.CENTER);
        
        GridHelperMethods.gridMarkers(topRootForGrid, GridHelperMethods.getMinButtonSize(), GridHelperMethods.getMinButtonSize());
        GridHelperMethods.gridMarkers(bottomRootForGrid, GridHelperMethods.getMinButtonSize(), GridHelperMethods.getMinButtonSize());
        
        centerPane.getChildren().addAll(topRootForGrid, middleLabel, bottomRootForGrid);
        centerPane.setAlignment(Pos.CENTER);
        
        playerFireBoardTop.setAlignment(Pos.CENTER);
        playerFireBoardTop.setStyle("-fx-cursor: crosshair;");
        playerLocationBoardBottom.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(10));
        
        layout.setCenter(centerPane);
        BorderPane.setAlignment(centerPane, Pos.CENTER);
        
//      left pane
        leftPane.setPadding(new Insets(10));
        
//        upper left
        VBox upperLeft = new VBox(10);
        upperLeft.setPadding(new Insets(100, 10, 10, 10));
        
        Button newGameButton = new Button("Start game");
        newGameButton.setOnAction(buttonHandlers::startGameButtonEH);
        newGameButtons.add(newGameButton);
        
        Button resetButton = new Button("Reset game");
        resetButton.setOnAction(event -> {
            if(ConfirmBox.display("Warning", "Your current game will be lost. Continue?")){
                buttonHandlers.resetGameButtonEH();
            }
        });
        newGameButtons.add(resetButton);
        
        Button returnButton = new Button("Return to options");
        returnButton.setOnAction(event -> {
            SceneChanger.centerWindow(Options.getInstance(window));
        });
        newGameButtons.add(returnButton);
        
        Button helpButton = new Button("Help");
        helpButton.setOnAction(event -> InstructionBox.display(firePhase));
        newGameButtons.add(helpButton);
        
        Button globalStats = new Button("Statistics");
        globalStats.setOnAction(event -> new GlobalStatsBox());
        newGameButtons.add(globalStats);
        
        Button exit = new Button("Exit");
        exit.setOnAction(event -> SceneChanger.closeProgram(window, cpuVisualBox.getWindow()));
        newGameButtons.add(exit);
        
        for (Button b : newGameButtons) {
            b.setId("newGameButtons");
        }
        
        upperLeft.getChildren().addAll(newGameButton, returnButton, resetButton, helpButton, globalStats, exit);
        upperLeft.setAlignment(Pos.CENTER);
        
//        bottom left
        VBox bottomLeft = new VBox(10);

        double hBoxSpacing = 10;

        HBox carrierLine = new HBox(hBoxSpacing);
        carrierLine.setAlignment(Pos.CENTER);
        
        HBox battleShipLine = new HBox(hBoxSpacing);
        battleShipLine.setAlignment(Pos.CENTER);
        
        HBox cruiserLine = new HBox(hBoxSpacing);
        cruiserLine.setAlignment(Pos.CENTER);
        
        HBox submarineLine = new HBox(hBoxSpacing);
        submarineLine.setAlignment(Pos.CENTER);
        
        HBox destroyerLine = new HBox(hBoxSpacing);
        destroyerLine.setAlignment(Pos.CENTER);
        
        HBox randomLine = new HBox(hBoxSpacing);
        randomLine.setAlignment(Pos.CENTER);
        
        Button carrierButton = new Button("Carrier - 5 decker");
        carrierButton.setOnAction(event -> buttonHandlers.shipPlacementButtonEH(ShipName.CARRIER));
        placementShipButtonListLeft.add(carrierButton);
        
        Button battleshipButton = new Button("Battleship - 4 decker");
        battleshipButton.setOnAction(event -> buttonHandlers.shipPlacementButtonEH(ShipName.BATTLESHIP));
        placementShipButtonListLeft.add(battleshipButton);

        Button cruiserButton = new Button("Cruiser - 3 decker");
        cruiserButton.setOnAction(event -> buttonHandlers.shipPlacementButtonEH(ShipName.CRUISER));
        placementShipButtonListLeft.add(cruiserButton);

        Button submarineButton = new Button("Submarine - 3 decker");
        submarineButton.setOnAction(event -> buttonHandlers.shipPlacementButtonEH(ShipName.SUBMARINE));
        placementShipButtonListLeft.add(submarineButton);

        Button destroyerButton = new Button("Destroyer - 2 decker");
        destroyerButton.setOnAction(event -> buttonHandlers.shipPlacementButtonEH(ShipName.DESTROYER));
        placementShipButtonListLeft.add(destroyerButton);

        Button randomPlacement = new Button("Random placement");
        randomPlacement.setOnAction(buttonHandlers::randomPlacementButtonEH);
        placementShipButtonListLeft.add(randomPlacement);

        for (Button b : placementShipButtonListLeft) {
            b.setId("shipButtonsPlacement");
        }
        
        Button resetCarrierButton = new Button("Reset");
        resetCarrierButton.setOnAction(event -> buttonHandlers.resetPlacementButtonEH(ShipName.CARRIER));
        resetShipButtonListRight.add(resetCarrierButton);

        Button resetBattleshipButton = new Button("Reset");
        resetBattleshipButton.setOnAction(event -> buttonHandlers.resetPlacementButtonEH(ShipName.BATTLESHIP));
        resetShipButtonListRight.add(resetBattleshipButton);

        Button resetCruiserButton = new Button("Reset");
        resetCruiserButton.setOnAction(event -> buttonHandlers.resetPlacementButtonEH(ShipName.CRUISER));
        resetShipButtonListRight.add(resetCruiserButton);

        Button resetSubmarineButton = new Button("Reset");
        resetSubmarineButton.setOnAction(event -> buttonHandlers.resetPlacementButtonEH(ShipName.SUBMARINE));
        resetShipButtonListRight.add(resetSubmarineButton);

        Button resetDestroyerButton = new Button("Reset");
        resetDestroyerButton.setOnAction(event -> buttonHandlers.resetPlacementButtonEH(ShipName.DESTROYER));
        resetShipButtonListRight.add(resetDestroyerButton);

        Button resetAll = new Button("Reset all");
        resetAll.setOnAction(buttonHandlers::resetAllButtonEH);
        resetShipButtonListRight.add(resetAll);

        for (Button b : resetShipButtonListRight) {
            b.setId("shipButtonsReset");
           
        }

        randomLine.getChildren().addAll(randomPlacement, resetAll);
        carrierLine.getChildren().addAll(carrierButton, resetCarrierButton);
        battleShipLine.getChildren().addAll(battleshipButton, resetBattleshipButton);
        cruiserLine.getChildren().addAll(cruiserButton, resetCruiserButton);
        submarineLine.getChildren().addAll(submarineButton, resetSubmarineButton);
        destroyerLine.getChildren().addAll(destroyerButton, resetDestroyerButton);
        
        String shipsTouching = shipsCanTouch() ? "yes" : "no";
        Label isCurrentTouch = new Label("Ships can touch: " + shipsTouching);
        
        bottomLeft.getChildren().addAll(isCurrentTouch, randomLine, carrierLine, battleShipLine, cruiserLine, submarineLine, destroyerLine);
        bottomLeft.setAlignment(Pos.CENTER);
        
        leftPane.getChildren().addAll(upperLeft, bottomLeft);
        layout.setLeft(leftPane);

//        right pane
        rightPane.setPadding(new Insets(10));

        updateStatus.setPrefWidth(scrollPaneWidth);
        updateStatus.setPrefHeight(scrollPaneHeight);
        updateStatus.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        updateStatus.setId("labels");
        
        battleLog.setWrapText(true);
        battleLog.setTextAlignment(TextAlignment.JUSTIFY);
        battleLog.setMaxWidth(labelWidth);
        battleLog.setPadding(new Insets(10));
        battleLog.setId("labels");
        
        updateStatus.setContent(battleLog);
        
//        bottom right
        sunkEnemyShips = new VBox(10);

        sunkEnemyShips.setAlignment(Pos.CENTER);
        
        rightPane.getChildren().addAll(updateStatus, sunkEnemyShips);
        layout.setRight(rightPane);
        
//        setting scene
        scene = new Scene(layout, windowWidth, windowHeight);
        scene.getStylesheets().add("gameStyles.css");
    }
    
    public void middleLabelUpdateText() {
        if(!firePhase){
            if (currentShip != null) {
                middleLabel.setText("Current ship: " + currentShip.getName() +
                        ". Remaining ship parts: " + (currentShip.getShipMaxSize() - currentShip.getShipPartsInGameCount()));
                middleLabel.setTextAlignment(TextAlignment.CENTER);
            } else {
                middleLabel.setText("No ship is selected");
            }
        } else {
            middleLabel.setText("Turn " + roundCounter);
        }
    }
    
    public void setMiddleLabel(String textToPlace){
        middleLabel.setText(textToPlace);
    }

    public Scene getScene() {
        return this.scene;
    }

    public Ship getCurrentShip() {
        return currentShip;
    }

    public void setCurrentShip(Ship currentShip) {
        this.currentShip = currentShip;
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
        human.setPlayerShips(RandomPlacement.randomShipPlacementList(seaButtonsListBottom));
    }

    public void setCpuShips() {
        cpu.setPlayerShips(RandomPlacement.randomShipPlacementList(cpuVisualBox.getButtonList()));
    }

    public Player getCpu() {
        return cpu;
    }

    public Player getHuman() {
        return human;
    }
    
    int getRoundCounter() {
        return roundCounter;
    }
    
    public void setBattleLog(String messageToSet){
        battleLog.setText(battleLog.getText() + messageToSet + "\n");
        updateStatus.vvalueProperty().bind(battleLog.heightProperty());
    }

    public void setFirePhase(boolean firePhase) {
        this.firePhase = firePhase;
    }

    public void increaseRoundCounter() {
        roundCounter = roundCounter + 1;
        middleLabelUpdateText();
        setBattleLog("Turn " + roundCounter);
    }

    public CpuVisualBox getCpuVisualBox() {
        return cpuVisualBox;
    }

    public Stage getWindow() {
        return window;
    }

    public VBox getSunkEnemyShips() {
        return sunkEnemyShips;
    }


    @Override
    public double getWindowWidth() {
        return windowWidth;
    }

    @Override
    public double getWindowHeight() {
        return windowHeight;
    }

    public static boolean shipsCanTouch() {
        return shipsCanTouch;
    }

    public static void setShipsCanTouch(boolean canShipsTouchEachOther) {
        shipsCanTouch = canShipsTouchEachOther;
    }
}