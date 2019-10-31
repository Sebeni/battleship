package UI;

import GameMechanic.ButtonListCreator;
import GameMechanic.Ship;
import GameMechanic.ShipNames;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    
    private List<Ship> playerShips = new ArrayList<>();
    
    //center top - player firing board
    private static GridPane playerFirePaneTop = new GridPane();
    private List<Button> fireButtonList = ButtonListCreator.createFireButtonList();

    //center bottom - player ships location
    private static GridPane playerLocationBoardBottom = new GridPane();
    private List<Button> locationButtonList = ButtonListCreator.createLocationButtonList();
    
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
        
        //TODO
        //playerFirePane should be added ONLY after all ships have been placed
        centerPane.getChildren().addAll(playerFirePaneTop, playerLocationBoardBottom);
        centerPane.setAlignment(Pos.CENTER);
        
        layout.setCenter(centerPane);
        
//      left pane
        leftPane.setPadding(new Insets(10));
        
        
        Button carrierButton = new Button("Carrier - 5 spaces");
        carrierButton.setOnAction(event -> {
            Ship ship = new Ship(5, ShipNames.CARRIER);
            currentShip = ship;
            playerShips.add(ship);
            ((Button) event.getSource()).setDisable(true);
        });
        shipNameButtonListWest.add(carrierButton);
        
        Button battleshipButton = new Button("Battleship - 4 spaces");
        battleshipButton.setOnAction(event -> {
            Ship ship = new Ship(4, ShipNames.BATTLESHIP);
            currentShip = ship;
            playerShips.add(ship);
            ((Button) event.getSource()).setDisable(true);
        });
        shipNameButtonListWest.add(battleshipButton);
     
        Button cruiserButton = new Button("Cruiser - 3 spaces");
        cruiserButton.setOnAction(event -> {
            Ship ship = new Ship(3, ShipNames.CRUISER);
            currentShip = ship;
            playerShips.add(ship);
            ((Button) event.getSource()).setDisable(true);
        });
        shipNameButtonListWest.add(cruiserButton);
        
        Button submarineButton = new Button("Submarine - 3 spaces");
        submarineButton.setOnAction(event -> {
            Ship ship = new Ship(3, ShipNames.SUBMARINE);
            currentShip = ship;
            playerShips.add(ship);
            ((Button) event.getSource()).setDisable(true);
        });
        shipNameButtonListWest.add(submarineButton);
        
        Button destroyerButton = new Button("Destroyer - 2 spaces");
        destroyerButton.setOnAction(event -> {
            Ship ship = new Ship(2, ShipNames.DESTROYER);
            currentShip = ship;
            playerShips.add(ship);
            ((Button) event.getSource()).setDisable(true);
        });
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
        
        //setting scene
        scene = new Scene(layout, width, height);
        scene.getStylesheets().add("gameStyles.css");

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
}