package UI;

import GameMechanic.Player;
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
 
    private List<Integer> cpuChoices = new ArrayList<>();
    
    private List<String> allCoordinates = new ArrayList<>();
    private int counter = 0;

    public GridPaneButtonMethods(Game game) {
        this.game = game;
    }
    
    public List<Button> create100ButtonList(GridPane toPopulate, String cssId, boolean disableGridButtons, EventHandler<ActionEvent> eventHandlerGridButtons) {
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
                button.setDisable(disableGridButtons);


                button.setMinSize(30, 30);
                button.setPrefSize(40, 40);

//                panes are parents for buttons so when they are disabled (ship part is placed) can shown ship name
                Pane pane = new Pane(button);
                gridPane.add(pane, column, row);
                resultButtonList.add(button);

                //reading X and Y coordinates
                button.setOnAction(eventHandlerGridButtons);
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
            if (currentShip.getShipMaxSize() > currentShip.getShipPartsInGameCount()) {
                Integer xParam = GridPane.getColumnIndex(button.getParent());
                Integer yParam = GridPane.getRowIndex(button.getParent());

                //placement validation
                //check is needed with any other than first placement
                if (currentShip.getShipPartsInGameCount() > 0) {
                    if (shipPlacementCheck(xParam, yParam)) {
                        currentShip.setCoordinate(xParam, yParam);
                        button.setDisable(true);
                        Game.updatingMiddleLabel();
                    } else {
                        AlertBox.display("Warning", "Ship parts must be placed in adjacent cells in one line!");
                    }

                    if (currentShip.getShipMaxSize() == currentShip.getShipPartsInGameCount()) {
                        //if all ship parts are set current ship must be set to null, disable ship button placement

                        ButtonHandlers.changeShipPlacementButtonState(game, currentShip, true);
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
        if (Game.getCurrentShip().getShipPartsInGameCount() == 1) {
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

        if (hitCheck(game.getCpu(), coordinate)) {
            shipHitMethod(button, true);
        } else {
            button.setId("miss");
        }
        button.setDisable(true);
        checkWin();
        cpuTurn();
        checkWin();
    }

    private boolean hitCheck(Player player, String coordinate) {
        return player.getShipsList().stream()
                .flatMap(ship -> ship.getCoordinates().stream())
                .anyMatch(s -> s.equals(coordinate));
    }

    private void shipHitMethod(Button button, boolean humanFires) {
        Ship shipHit;
        if (humanFires) {
            shipHit = shipFromGridButton(game.getFireButtonListTop(), game.getCpu().getShipsList(), button);
        } else {
            shipHit = shipFromGridButton(game.getSeaButtonsListBottom(), game.getHuman().getShipsList(), button);
        }
        
        shipHit.setShipPartsInGameCount(shipHit.getShipPartsInGameCount() - 1);

        if (shipHit.getShipPartsInGameCount() > 0) {
            button.setId("hit");
        } else {
            changeAllButtonsToSunk(humanFires, shipHit);
        }
    }

    private void changeAllButtonsToSunk(boolean humanFires, Ship shipHit) {
        if (humanFires) {
            shipHit.getCoordinates().forEach(s -> game.getFireButtonListTop().get(Integer.parseInt(s)).setId("sunk"));
        } else {
            shipHit.getCoordinates().forEach(s -> game.getSeaButtonsListBottom().get(Integer.parseInt(s)).setId("sunk"));
        }
    }

    private void cpuTurn() {
        Random random = new Random();

        Integer choice = random.nextInt(100);

        while (cpuChoices.contains(choice)) {
            choice = random.nextInt(100);
        }
        
//        for debug
//        game.getHuman().getShipsList().stream().flatMap(ship -> ship.getCoordinates().stream()).forEach(s -> allCoordinates.add(s));
//        
//        Integer choice = Integer.parseInt(allCoordinates.get(counter));
//        counter++;
        
        String coordinate;
        if (choice >= 0 && choice < 10) {
            coordinate = "0" + choice;
        } else {
            coordinate = "" + choice;
        }

        Button buttonToChange = game.getSeaButtonsListBottom().get(choice);


        if (hitCheck(game.getHuman(), coordinate)) {
            shipHitMethod(buttonToChange, false);
        } else {
            buttonToChange.setId("miss");
        }

    }
    
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

        if (buttonEntered.isDisable() && !game.getHuman().getShipsList().isEmpty()) {
            Ship shipHovered = shipFromGridButton(game.getSeaButtonsListBottom(), game.getHuman().getShipsList(), buttonEntered);
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

    //    for player
    private Ship shipFromGridButton(List<Button> gridButtons, List<Ship> shipList, Button buttonEntered) {
        int index = gridButtons.indexOf(buttonEntered);
        String toFind;

        if (index >= 0 && index < 10) {
            toFind = "0" + index;
        } else {
            toFind = "" + index;
        }
        return shipList.stream().filter(ship -> ship.getCoordinates().contains(toFind)).findAny().get();
    }


    private void checkWin() {
        //win condition
        if (game.getCpu().getShipsList().stream().filter(ship -> ship.getShipPartsInGameCount() == 0).count() == 5) {
            AlertBox.display("Result", "You win!");
        } else if (game.getHuman().getShipsList().stream().filter(ship -> ship.getShipPartsInGameCount() == 0).count() == 5) {
            AlertBox.display("Result", "You loose!");
        }
    }
}
    
