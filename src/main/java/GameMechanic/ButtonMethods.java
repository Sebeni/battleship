package GameMechanic;

import UI.AlertBox;
import UI.Game;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                button.setOnAction(event -> {
                    //only working when player selected ship
                    if (Game.getCurrentShip() != null) {


                        Ship current = Game.getCurrentShip();
                        //check if all spaces were used
                        if (current.getShipMaxSize() > current.getShipFieldCount()) {
                            Integer xParam = GridPane.getColumnIndex(button);
                            Integer yParam = GridPane.getRowIndex(button);
                            if (Game.debug) {
                                System.out.println("X " + xParam + " Y " + yParam + " button " + event.getSource());
                            }
                            
                            
                            //placement validation
                            //check is needed with any other than first placement
                            if(current.getShipFieldCount() > 0) {
                                if(shipPlacementCheck(xParam, yParam)){
                                    current.setCoordinates(xParam, yParam);
                                    button.setStyle("-fx-background-color: yellow");
                                    button.setDisable(true);
                                } else {
                                    AlertBox.display("Warning", "Ship parts must be placed in adjacent cells");
                                }
                                
                            } else {
                                //TODO
                                //first placement doesn't need check (later can be implemented to check if horizontally and vertically 
                                // current ship can be placed)
                                current.setCoordinates(xParam, yParam);
                                button.setStyle("-fx-background-color: yellow");
                                button.setDisable(true);
                            }
                            
                            
                        } else {

                            Game.setCurrentShip(null);
                        }

                    }
                });
            }
        }
        return locationButtonList;
    }
    
    
    private static boolean shipPlacementCheck(int xToCheck, int yToCheck){
        boolean result = false;
        int[][] currentShipCoordinates = Game.getCurrentShip().getCoordinates();
//        List<List<Integer>> possibleChoices = new ArrayList<>();

        //second placement
        if(Game.getCurrentShip().getShipFieldCount() == 1){
            //possible choices: +- 1 
            int xFirstShipPart = currentShipCoordinates[0][0];
            int yFirstShipPart = currentShipCoordinates[0][1];
            
            int xDiff = xFirstShipPart - xToCheck;
            int yDiff = yFirstShipPart - yToCheck;
            
            //check if in boundary +- 
            boolean possibleXChoice = (xDiff <= 1 && xDiff >= -1);
            boolean possibleYChoice = (yDiff <= 1 && yDiff >= -1);
            
            //checking for further placement if player is placing horizontally(true) or vertically (false - default)
            if(yDiff != 0){
                Game.getCurrentShip().setHorizontalPlacement(true);
            }
            
            result = (possibleXChoice && possibleYChoice);
        }
        
        return result;
    }
    

}
    
