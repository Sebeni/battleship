package GameUI;

import GameMechanic.Ship;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Collections;
import java.util.List;

public class SeaButtonHandlers {
   
    private Game game;

    public SeaButtonHandlers(Game game) {
        this.game = game;
    }

    public void placementButtonHandler(ActionEvent event) {
        Button button = (Button) event.getSource();

        if (game.getCurrentShip() != null) {
            Ship currentShip = game.getCurrentShip();

            //check if all spaces were used
            if (currentShip.getShipMaxSize() > currentShip.getShipPartsInGameCount()) {
                Integer xParam = GridPane.getColumnIndex(button.getParent());
                Integer yParam = GridPane.getRowIndex(button.getParent());

                //placement validation
                if (currentShip.getShipPartsInGameCount() > 0) {
                    if (shipPlacementAdjacentCheck(xParam, yParam)) {
                        currentShip.setCoordinate(xParam, yParam);
                        button.setDisable(true);
                        game.middleLabelUpdateText();
                    } else {
                        AlertBox.display("Warning", "Ship parts must be placed in adjacent cells in one line!");
                    }

                    if (currentShip.getShipMaxSize() == currentShip.getShipPartsInGameCount()) {
                        ButtonHandlers.changeShipPlacementButtonState(game, currentShip, true);
                        game.setCurrentShip(null);
                        game.middleLabelUpdateText();
                    }
                } else {
                    currentShip.setCoordinate(xParam, yParam);
                    button.setDisable(true);
                    game.middleLabelUpdateText();
                }
            }
        }
    }

    private boolean shipPlacementAdjacentCheck(int xToCheck, int yToCheck) {
        if (game.getCurrentShip().getShipPartsInGameCount() == 1) {
            return secondPlacementCheck(xToCheck, yToCheck);
        } else {
            return thirdAndMorePlacementCheck(xToCheck, yToCheck);
        }
    }

    private boolean secondPlacementCheck(int xToCheck, int yToCheck) {
        Integer firstPlacementCoordinate = game.getCurrentShip().getCoordinates().get(0);

        //possible choices: +- 1 
        int xFirstShipPart = firstPlacementCoordinate / 10;
        int yFirstShipPart = firstPlacementCoordinate % 10;

        int xDiff = xFirstShipPart - xToCheck;
        int yDiff = yFirstShipPart - yToCheck;

        //check if in boundary +- 
        boolean possibleXChoice = (xDiff == 1 || xDiff == -1) && yDiff == 0;
        boolean possibleYChoice = (yDiff == 1 || yDiff == -1) && xDiff == 0;

        //checking for further placement if player is placing horizontally(true) or vertically (false - default)
        if (yDiff == 0) {
            game.getCurrentShip().setHorizontalPlacement(true);
        }

        return possibleXChoice || possibleYChoice;
    }

    private boolean thirdAndMorePlacementCheck(int xToCheck, int yToCheck) {
        //third and subsequent placement
        List<Integer> currentCoordinates = game.getCurrentShip().getCoordinates();
        Collections.sort(currentCoordinates);

        Integer startBorder = currentCoordinates.get(0);

        Integer endBorder = currentCoordinates.get(currentCoordinates.size() - 1);

        if (game.getCurrentShip().isHorizontalPlacement()) {
            // x +- 1
            int startBorderX = startBorder / 10;
            int endBorderX = endBorder / 10;

            boolean possibleXBeforeStart = (startBorderX - 1) == xToCheck;
            boolean possibleXAfterEnd = (endBorderX + 1) == xToCheck;

            boolean possibleY = startBorder % 10 == yToCheck;

            return (possibleXBeforeStart || possibleXAfterEnd) && possibleY;

        } else {
            // y +- 1
            int startBorderY = startBorder % 10;
            int endBorderY = endBorder % 10;

            boolean possibleYBeforeStart = (startBorderY - 1) == yToCheck;
            boolean possibleYAfterEnd = (endBorderY + 1) == yToCheck;

            boolean possibleX = startBorder / 10 == xToCheck;

            return (possibleYBeforeStart || possibleYAfterEnd) && possibleX;
        }
    }

    public void mouseShipNameEH(MouseEvent event) {

        Pane paneEntered = (Pane) event.getSource();
        Button buttonEntered = (Button) paneEntered.getChildren().get(0);

        Integer xParam = GridPane.getColumnIndex(paneEntered);
        Integer yParam = GridPane.getRowIndex(paneEntered);

        Integer coordinate = xParam * 10 + yParam;

        boolean coordinateIsInShipList = game.getHuman().getShipsList().stream()
                .flatMap(ship -> ship.getCoordinates().stream())
                .anyMatch(integer -> integer.equals(coordinate));

        if (buttonEntered.isDisable() && coordinateIsInShipList) {
            Ship shipHovered = GridHelperMethods.shipFromGridButton(game.getSeaButtonsListBottom(), game.getHuman().getShipsList(), buttonEntered);
            game.setMiddleLabel(shipHovered.getName().toString());
        }
    }

    public void mouseExitShipNameEH(MouseEvent event) {
        Pane paneEntered = (Pane) event.getSource();
        Button buttonEntered = (Button) paneEntered.getChildren().get(0);

        if (buttonEntered.isDisable()) {
            game.middleLabelUpdateText();
        }
    }
}