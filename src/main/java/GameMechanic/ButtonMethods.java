package GameMechanic;

import UI.AlertBox;
import UI.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.*;

/**
 * Class created to clean up Game class, provides methods connected with creating and setting actions on buttons
 */
public class ButtonMethods {


    /**
     * Creates 100 buttons to populate player Fire Pane Grid (place where player puts his fire choices)
     * also adds them to the specific grid pane playerFirePaneTop (top)
     *
     * @return populated list of fire buttons - buttons are added in order; for example #35 button equals to coordinates (3, 5)
     */
    public static List<Button> createFireButtonList() {
        List<Button> fireButtonList = new ArrayList<>();
        GridPane playerFirePane = Game.getPlayerFirePaneTop();
        int fireButtonCounter = 0;

        for (int column = 0; column < 10; column++) {
            for (int row = 0; row < 10; row++) {
                //text on buttons only for debug
                Button button = new Button();
                //removing blue border from next button
                button.setStyle("-fx-focus-traversable: false;");

                if (Game.debug) {
                    button.setText("" + fireButtonCounter);
                    fireButtonCounter++;
                }

                button.setMinSize(30, 30);
                button.setPrefSize(40, 40);
                playerFirePane.add(button, column, row);
                fireButtonList.add(button);

                button.setOnAction(event -> {
                    Integer xParam = GridPane.getColumnIndex(button);
                    Integer yParam = GridPane.getRowIndex(button);
                    if (Game.debug) {
                        System.out.println("X " + xParam + " Y " + yParam + " button " + event.getSource());
                    }

                    button.setDisable(true);
                    //TODO
                    //changing color accordingly to cpu ship placement for now red
                    button.setStyle("-fx-background-color: red");

                });
            }
        }
        return fireButtonList;
    }

    /**
     * Creates 100 buttons to populate player Player Location Pane (place where player puts his ships
     * and where are marked cpu's choices)
     * also adds them to the specific grid pane (bottom) playerLocationBoardBottom
     *
     * @return populated list of location buttons - buttons are added in order; for example #35 button equals to coordinates (3, 5)
     */
    public static List<Button> createLocationButtonList() {
        List<Button> locationButtonList = new ArrayList<>();
        GridPane playerLocationBoard = Game.getPlayerLocationBoardBottom();

        int placementButtonCounter = 0;

        for (int column = 0; column < 10; column++) {
            for (int row = 0; row < 10; row++) {
                //text on buttons only for debug
                Button button = new Button();
                if (Game.debug) {
                    button.setText("" + placementButtonCounter);
                    placementButtonCounter++;
                }

                //removing blue border from next button
                button.setStyle("-fx-focus-traversable: false;");


                button.setMinSize(30, 30);
                button.setPrefSize(40, 40);
                playerLocationBoard.add(button, column, row);
                locationButtonList.add(button);

                //reading X and Y coordinates
                button.setOnAction(new PlacementButtonHandler());
            }
        }
        return locationButtonList;
    }

    static class PlacementButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Button button = (Button) event.getSource();

            //only working when player selected ship
            if (Game.getCurrentShip() != null) {
                Ship currentShip = Game.getCurrentShip();

                //check if all spaces were used
                if (currentShip.getShipMaxSize() > currentShip.getShipFieldCount()) {
                    Integer xParam = GridPane.getColumnIndex(button);
                    Integer yParam = GridPane.getRowIndex(button);


                    if (Game.debug) {
                        System.out.println("X " + xParam + " Y " + yParam + " button " + event.getSource());
                    }

                    //placement validation
                    //check is needed with any other than first placement
                    if (currentShip.getShipFieldCount() > 0) {
                        if (shipPlacementCheck(xParam, yParam)) {
                            currentShip.setCoordinates(xParam, yParam);
                            button.setStyle("-fx-background-color: yellow");
                            button.setDisable(true);
                            Game.changingShipPartLabel();
                        } else {
                            AlertBox.display("Warning", "Ship parts must be placed in adjacent cells");
                        }

                    } else {
                        //TODO
                        //first placement doesn't need check (later can be implemented to check if horizontally and vertically 
                        // current ship can be placed)
                        currentShip.setCoordinates(xParam, yParam);
                        button.setStyle("-fx-background-color: yellow");
                        button.setDisable(true);
                        Game.changingShipPartLabel();
                    }
                } else {
                    //if all ship parts are set current ship must be set to null
                    Game.setCurrentShip(null);
                }
            }
        }
    }
    
    private static boolean shipPlacementCheck(int xToCheck, int yToCheck){
        if(Game.getCurrentShip().getShipFieldCount() == 1){
            //second placement
            String firstPlacementCoordinate = Game.getCurrentShip().getCoordinates()[0];
            
            //possible choices: +- 1 
            int xFirstShipPart = extractCoordinateFromString(firstPlacementCoordinate, "x");
            int yFirstShipPart = extractCoordinateFromString(firstPlacementCoordinate, "y");
            
            int xDiff = xFirstShipPart - xToCheck;
            int yDiff = yFirstShipPart - yToCheck;
            
            //check if in boundary +- 
            boolean possibleXChoice = (xDiff == 1 || xDiff == -1) && yDiff == 0;
            boolean possibleYChoice = (yDiff == 1 || yDiff == -1) && xDiff == 0;
            
            //checking for further placement if player is placing horizontally(true) or vertically (false - default)
            if(yDiff == 0){
                Game.getCurrentShip().setHorizontalPlacement(true);
            }
            
            return possibleXChoice || possibleYChoice;
        } else {
            //third and subsequent placement
            List<String> currentCoordinates = new ArrayList<>(Arrays.asList(Game.getCurrentShip().getCoordinates()));
            currentCoordinates.removeIf(Objects::isNull);
            Collections.sort(currentCoordinates);
            
            String startBorder = currentCoordinates.get(0);
            
            String endBorder = currentCoordinates.get(currentCoordinates.size()-1);
            
            //horizontal check
            if(Game.getCurrentShip().isHorizontalPlacement()){
                // x +- 1
                int startBorderX = extractCoordinateFromString(startBorder, "x");
                int endBorderX = extractCoordinateFromString(endBorder, "x");
                
                boolean possibleXBeforeStart = (startBorderX - 1) == xToCheck;
                boolean possibleXAfterEnd = (endBorderX + 1) == xToCheck;
                //no matter form where (start or end) extracted;
                boolean possibleY = extractCoordinateFromString(startBorder, "y") == yToCheck;
                
                return (possibleXBeforeStart || possibleXAfterEnd) && possibleY;
            } else {
                // y +- 1
                int startBorderY = extractCoordinateFromString(startBorder, "y");
                int endBorderY = extractCoordinateFromString(endBorder, "y");

                boolean possibleYBeforeStart = (startBorderY - 1) == yToCheck;
                boolean possibleYAfterEnd = (endBorderY + 1) == yToCheck;
                //no matter form where (start or end) extracted;
                boolean possibleX = extractCoordinateFromString(startBorder, "x") == xToCheck;
                
                return (possibleYBeforeStart || possibleYAfterEnd) && possibleX;
            }
            
        }
        
    }

    /**
     * Extracting and parsing parameters from two "digit" String to one digit int;
     * @param toExtract String with parameters (two "digit" String);
     * @param coordinate Which coordinate extract (x or y)
     * @return chosen int coordinate 
     */
    private static int extractCoordinateFromString(String toExtract, String coordinate){
        
        if(coordinate.toLowerCase().equals("x")){
            return Character.getNumericValue(toExtract.charAt(0));
        } else {
            return Character.getNumericValue(toExtract.charAt(1));
        }
    }
    
}
    
