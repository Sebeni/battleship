package UI;

import GameMechanic.Player;
import GameMechanic.RandomPlacement;
import GameMechanic.Ship;
import GameMechanic.ShipName;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Game {
    //game elements
    public static boolean debug;
    private int roundCounter = 1;
    private boolean firePhase = false;

    private Ship currentShip;
    private Label middleLabel;

    private final Player human = new Player(true);
    private final Player cpu = new Player(false);
    private CpuVisual cpuVisual;
    
    //javaFX elements
    private Stage window;
    private Scene scene;
    
    // parameters of elements
    private final double windowWidth = 1400;
    private final double windowHeight = 1000;
    private final double scrollPaneWidth = 350;
    private final double scrollPaneHeight = 500;
    private final double labelWidth = scrollPaneWidth - 20;
    

    //handlers for javaFX elements
    private final ButtonHandlers gameHandler = new ButtonHandlers(this);
    private final GridPaneButtonMethods gridMethods = new GridPaneButtonMethods(this);

    //center top - human firing board
    private final GridPane playerFireBoardTop = new GridPane();
    private final List<Button> fireButtonListTop;

    //center bottom - human ships location
    private final GridPane playerLocationBoardBottom = new GridPane();
    private final List<Button> seaButtonsListBottom;

    //left pane and buttons - name of the ships and new game buttons
    private final VBox leftPane = new VBox(200);
    private final List<Button> newGameButtons = new ArrayList<>();
    private final List<Button> placementShipButtonListLeft = new ArrayList<>();

    //right pane and buttons bottom - reset ships
    private final VBox rightPane = new VBox(150);
    private final ScrollPane updateStatus = new ScrollPane();
    private final List<Button> resetShipButtonListRight = new ArrayList<>();
    
    //right pane top - battleLog
    private final Label battleLog = new Label();
    

    public Game(Stage primaryStage) {
        window = primaryStage;
        cpuVisual = new CpuVisual(this);

        window.setOnCloseRequest(e -> {
            e.consume();
            AfterClick.closeProgram(window, cpuVisual.getWindow());
        });
        

        fireButtonListTop = gridMethods.create100ButtonList(playerFireBoardTop, "fireButton", true, gridMethods::fireButtonHandler);
        seaButtonsListBottom = gridMethods.create100ButtonList(playerLocationBoardBottom, "boardButton", false, gridMethods::placementButtonHandler);

//      root pane for all other panes
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20, 50, 20, 50));
        
//      center pane
        VBox centerPane = new VBox(20);

        middleLabel = new Label();
        middleLabelUpdateText();

        playerLocationBoardBottom.getChildren().forEach(node -> {
            Pane pane = (Pane) node;
            pane.setOnMouseEntered(gridMethods::mouseShipNameEH);
            pane.setOnMouseExited(gridMethods::mouseExitShipNameEH);

        });
        
        fireButtonListTop.forEach(button -> {
            button.setOnMouseEntered(gridMethods::mouseFireFieldName);
            button.setOnMouseExited(gridMethods::mouseExitFireFieldName);
        });
        
        GridPane topRootForGrid = new GridPane();
        topRootForGrid.add(playerFireBoardTop, 1, 1, 10, 10);
        topRootForGrid.setAlignment(Pos.CENTER);
        
        GridPane bottomRootForGrid = new GridPane();
        bottomRootForGrid.add(playerLocationBoardBottom, 1, 1, 10, 10);
        bottomRootForGrid.setAlignment(Pos.CENTER);
        
        gridMethods.gridMarkers(topRootForGrid);
        gridMethods.gridMarkers(bottomRootForGrid);



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
        newGameButton.setOnAction(gameHandler::startGameButtonEH);
        newGameButtons.add(newGameButton);
        
        Button resetButton = new Button("Reset game");
        resetButton.setOnAction(event -> {
            if(ConfirmBox.display("Warning", "Your current game will be lost. Continue?")){
                gameHandler.resetGameButtonEH(event);
            }
        });
        newGameButtons.add(resetButton);
        
        Button helpButton = new Button("Help");
        helpButton.setOnAction(event -> {
            InstructionBox.display(firePhase);
        });
        newGameButtons.add(helpButton);
        
        
        
        Button exit = new Button("Exit");
        exit.setOnAction(event -> AfterClick.closeProgram(window, cpuVisual.getWindow()));
        newGameButtons.add(exit);
        
        for (Button b : newGameButtons) {
            b.setId("newGameButtons");
        }
        
        upperLeft.getChildren().addAll(newGameButton, resetButton, helpButton, exit);
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


        Button resetCarrierButton = new Button("Reset");
        resetCarrierButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.CARRIER));
        resetShipButtonListRight.add(resetCarrierButton);

        Button resetBattleshipButton = new Button("Reset");
        resetBattleshipButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.BATTLESHIP));
        resetShipButtonListRight.add(resetBattleshipButton);

        Button resetCruiserButton = new Button("Reset");
        resetCruiserButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.CRUISER));
        resetShipButtonListRight.add(resetCruiserButton);

        Button resetSubmarineButton = new Button("Reset");
        resetSubmarineButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.SUBMARINE));
        resetShipButtonListRight.add(resetSubmarineButton);

        Button resetDestroyerButton = new Button("Reset");
        resetDestroyerButton.setOnAction(event -> gameHandler.resetPlacementButtonEH(event, ShipName.DESTROYER));
        resetShipButtonListRight.add(resetDestroyerButton);

        Button resetAll = new Button("Reset all");
        resetAll.setOnAction(gameHandler::resetAllButtonEH);
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
        
        
        bottomLeft.getChildren().addAll(randomLine, carrierLine, battleShipLine, cruiserLine, submarineLine, destroyerLine);
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
        VBox bottomRight = new VBox(10);
        
        
        

        bottomRight.getChildren().addAll();
        bottomRight.setAlignment(Pos.CENTER);
        
        rightPane.getChildren().addAll(updateStatus, bottomRight);
        layout.setRight(rightPane);


//        debug

        if (debug) {
            Button showShips = new Button("show ship lists");
            showShips.setOnAction(event -> {
                System.out.println("***HUMAN***");
                human.getShipsList().forEach(System.out::println);
                System.out.println("***CPU***");
                cpu.getShipsList().forEach(System.out::println);
            });
            
//            upperLeft.getChildren().addAll(showShips);
        }
        
//        setting scene
        scene = new Scene(layout, windowWidth, windowHeight);
        window.setMinHeight(windowHeight);
        window.setMinWidth(windowWidth);
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
    
    public static void setDebug(boolean debug) {
        Game.debug = debug;
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

    public int getRoundCounter() {
        return roundCounter;
    }

    public void setRoundCounter(int roundCounter) {
        this.roundCounter = roundCounter;
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

    public CpuVisual getCpuVisual() {
        return cpuVisual;
    }

    public Stage getWindow() {
        return window;
    }
}