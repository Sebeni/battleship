package UI;

import GameMechanic.Ship;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.*;

public class GridPaneButtonMethods {
    private Game game;
    private List<Button> gridButtonsHit = new ArrayList<>();

    public GridPaneButtonMethods(Game game) {
        this.game = game;
    }

    /**
     * Create 100 buttons, place them in grid and return them in order
     *
     * @param toPopulate   grid pane in which buttons should be placed
     * @param cssId        buttons Id specified in CSS
     * @param disable      whether buttons should be disable (true)
     * @param eventHandler for each button
     * @return populated list of buttons - buttons are added in order; for example #35 button equals to coordinates (3, 5)
     */
    public List<Button> create100ButtonList(GridPane toPopulate, String cssId, boolean disable, EventHandler<ActionEvent> eventHandler) {
        List<Button> resultButtonList = new ArrayList<>();
        GridPane gridPane = toPopulate;

        int buttonCounter = 0;

        for (int column = 0; column < 10; column++) {
            for (int row = 0; row < 10; row++) {
                //text on buttons only for debug
                Button button = new Button();
                if (Game.debug) {
                    button.setText("" + buttonCounter);
                    buttonCounter++;
                }

                button.setId(cssId);
                button.setDisable(disable);


                button.setMinSize(30, 30);
                button.setPrefSize(40, 40);

//                panes are parents for buttons so when they are disabled (ship part is placed) can shown ship name
                Pane pane = new Pane(button);
                
                gridPane.add(pane, column, row);
                resultButtonList.add(button);

                //reading X and Y coordinates
                button.setOnAction(eventHandler);
            }
        }
        return resultButtonList;
    }


    public void placementButtonHandler(ActionEvent event) {
        Button button = (Button) event.getSource();

        //only working when player selected ship
        if (Game.getCurrentShip() != null) {
            Ship currentShip = Game.getCurrentShip();

            //check if all spaces were used
            if (currentShip.getShipMaxSize() > currentShip.getShipFieldCount()) {
                Integer xParam = GridPane.getColumnIndex(button.getParent());
                Integer yParam = GridPane.getRowIndex(button.getParent());

                //placement validation
                //check is needed with any other than first placement
                if (currentShip.getShipFieldCount() > 0) {
                    if (shipPlacementCheck(xParam, yParam)) {
                        currentShip.setCoordinate(xParam, yParam);
                        button.setDisable(true);
                        Game.updatingMiddleLabel();
                    } else {
                        AlertBox.display("Warning", "Ship parts must be placed in adjacent cells in one line!");
                    }

                    if (currentShip.getShipMaxSize() == currentShip.getShipFieldCount()) {
                        //if all ship parts are set current ship must be set to null, disable ship button placement

                        GameHandlers.changeShipPlacementButtonState(game, currentShip, true);
                        Game.setCurrentShip(null);
                        Game.updatingMiddleLabel();
                    }

                } else {
                    currentShip.setCoordinate(xParam, yParam);
                    button.setDisable(true);
                    Game.updatingMiddleLabel();
                }
            }
        }
    }


    private boolean shipPlacementCheck(int xToCheck, int yToCheck) {
        if (Game.getCurrentShip().getShipFieldCount() == 1) {
            //second placement
            String firstPlacementCoordinate = Game.getCurrentShip().getCoordinates().get(0);

            //possible choices: +- 1 
            int xFirstShipPart = extractCoordinateFromString(firstPlacementCoordinate, "x");
            int yFirstShipPart = extractCoordinateFromString(firstPlacementCoordinate, "y");

            int xDiff = xFirstShipPart - xToCheck;
            int yDiff = yFirstShipPart - yToCheck;

            //check if in boundary +- 
            boolean possibleXChoice = (xDiff == 1 || xDiff == -1) && yDiff == 0;
            boolean possibleYChoice = (yDiff == 1 || yDiff == -1) && xDiff == 0;

            //checking for further placement if player is placing horizontally(true) or vertically (false - default)
            if (yDiff == 0) {
                Game.getCurrentShip().setHorizontalPlacement(true);
            }

            return possibleXChoice || possibleYChoice;
        } else {
            //third and subsequent placement
            List<String> currentCoordinates = Game.getCurrentShip().getCoordinates();
            Collections.sort(currentCoordinates);

            String startBorder = currentCoordinates.get(0);

            String endBorder = currentCoordinates.get(currentCoordinates.size() - 1);

            //horizontal check
            if (Game.getCurrentShip().isHorizontalPlacement()) {
                // x +- 1
                int startBorderX = extractCoordinateFromString(startBorder, "x");
                int endBorderX = extractCoordinateFromString(endBorder, "x");

                boolean possibleXBeforeStart = (startBorderX - 1) == xToCheck;
                boolean possibleXAfterEnd = (endBorderX + 1) == xToCheck;
                //no matter form where (start or end) extracted;
                boolean possibleY = extractCoordinateFromString(startBorder, "y") == yToCheck;

                return (possibleXBeforeStart || possibleXAfterEnd) && possibleY;
            } else { //vertical check
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


    public void fireButtonHandler(ActionEvent event) {
        Button button = (Button) event.getSource();

        Integer xParam = GridPane.getColumnIndex(button.getParent());
        Integer yParam = GridPane.getRowIndex(button.getParent());
        String coordinate = xParam.toString() + yParam.toString();
        
        boolean hit = game.getCpu().getShipsList().stream()
                .flatMap(ship -> ship.getCoordinates().stream())
                .anyMatch(s -> s.equals(coordinate));

        if(hit){
            sunkMethod(button, xParam, yParam);
        } else {
            button.setId("miss");
        }
        button.setDisable(true);


    }

    private void sunkMethod(Button button, Integer xParam, Integer yParam) {
        gridButtonsHit.add(button);
        Ship shipHit = shipFromGridButton(game.getFireButtonListTop(), game.getCpu().getShipsList(), button);
        shipHit.deleteCoordinate(xParam, yParam);

        if(shipHit.getShipFieldCount() > 0){
            button.setId("hit");
            
        } else {
            gridButtonsHit.forEach(button1 -> button1.setId("sunk"));
            game.getCpu().getShipsList().remove(shipHit);
            
            //win condition
            if(game.getCpu().getShipsList().isEmpty()){
                AlertBox.display("Result", "You won");
            }
        }
    }


    /**
     * Extracting and parsing parameters from two "digit" String to one digit int;
     *
     * @param toExtract  String with parameters (two "digit" String);
     * @param coordinate Which coordinate extract (x or y)
     * @return chosen int coordinate
     */
    public int extractCoordinateFromString(String toExtract, String coordinate) {

        if (coordinate.toLowerCase().equals("x")) {
            return Character.getNumericValue(toExtract.charAt(0));
        } else {
            return Character.getNumericValue(toExtract.charAt(1));
        }
    }


    public void mouseEnteredEH(MouseEvent event) {

        Pane paneEntered = (Pane) event.getSource();
        Button buttonEntered = (Button) paneEntered.getChildren().get(0);

        if (buttonEntered.isDisable() && !game.getPlayerShips().isEmpty()) {
            Ship shipHovered = shipFromGridButton(game.getSeaButtonsListBottom(), game.getPlayerShips(), buttonEntered);
            Game.setMiddleLabel(shipHovered.getName().toString());
        }
    }

    public void mouseExitedEH(MouseEvent event) {
        Pane paneEntered = (Pane) event.getSource();
        Button buttonEntered = (Button) paneEntered.getChildren().get(0);

        if (buttonEntered.isDisable()) {
            Game.updatingMiddleLabel();
        }
    }
    
    private Ship shipFromGridButton(List<Button> gridButtons, List<Ship> shipList, Button buttonEntered){
        int index = gridButtons.indexOf(buttonEntered);
        String toFind;

        if(index >= 0 && index <= 10){
            toFind = "0" + index;
        } else {
            toFind = "" + index;
        }
        return shipList.stream().filter(ship -> ship.getCoordinates().contains(toFind)).findAny().get();
    }
}
    
