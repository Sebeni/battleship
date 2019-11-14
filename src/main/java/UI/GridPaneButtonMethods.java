package UI;

import GameMechanic.Player;
import GameMechanic.Ship;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

import java.util.*;

public class GridPaneButtonMethods {
    private Game game;
    private double minButtonSize = 39;

    private List<Integer> cpuChoices = new ArrayList<>();
    
    private String playerFires = "You fire at: ";
    private String cpuFires = "Cpu fires at: ";


    //only for debug
    private List<Integer> allCoordinates = new ArrayList<>();
    private int counter = 0;

    public GridPaneButtonMethods(Game game) {
        this.game = game;
    }

    public List<Button> create100ButtonList(GridPane toPopulate, String cssId, boolean disableGridButtons, EventHandler<ActionEvent> eventHandlerGridButtons) {
        List<Button> resultButtonList = new ArrayList<>();

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
                button.setMinSize(minButtonSize, minButtonSize);


//                panes are parents for buttons so when they are disabled (ship part is placed) can shown ship name
                Pane pane = new Pane(button);
                toPopulate.add(pane, column, row);
                resultButtonList.add(button);

                //reading X and Y coordinates
                button.setOnAction(eventHandlerGridButtons);
            }
        }
        return resultButtonList;
    }


    public void placementButtonHandler(ActionEvent event) {
        Button button = (Button) event.getSource();
        
        if (Game.getCurrentShip() != null) {
            Ship currentShip = Game.getCurrentShip();

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
                        Game.setCurrentShip(null);
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
        if (Game.getCurrentShip().getShipPartsInGameCount() == 1) {
            return secondPlacementCheck(xToCheck, yToCheck);

        } else {
            return thirdAndMorePlacementCheck(xToCheck, yToCheck);
        }
    }

    private boolean thirdAndMorePlacementCheck(int xToCheck, int yToCheck) {
        //third and subsequent placement
        List<Integer> currentCoordinates = Game.getCurrentShip().getCoordinates();
        Collections.sort(currentCoordinates);

        Integer startBorder = currentCoordinates.get(0);

        Integer endBorder = currentCoordinates.get(currentCoordinates.size() - 1);

        if (Game.getCurrentShip().isHorizontalPlacement()) {
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

    private boolean secondPlacementCheck(int xToCheck, int yToCheck) {
        Integer firstPlacementCoordinate = Game.getCurrentShip().getCoordinates().get(0);

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
            Game.getCurrentShip().setHorizontalPlacement(true);
        }

        return possibleXChoice || possibleYChoice;
    }


    public void fireButtonHandler(ActionEvent event) {
        Button button = (Button) event.getSource();

        Integer xParam = GridPane.getColumnIndex(button.getParent());
        Integer yParam = GridPane.getRowIndex(button.getParent());
        
        Integer coordinate = xParam * 10 + yParam;

        if (hitCheck(game.getCpu(), coordinate)) {
            shipHitByHumanMethod(button, xParam, yParam);
            
        } else {
            game.setBattleLog(playerFires + numberToLetter(xParam) + (yParam + 1) + " miss");
            button.setId("miss");
        }
        button.setDisable(true);
        checkWin();
        cpuTurn();
        checkWin();
        game.increaseRoundCounter();
    }

    private boolean hitCheck(Player player, Integer coordinate) {
        return player.getShipsList().stream()
                .flatMap(ship -> ship.getCoordinates().stream())
                .anyMatch(s -> s.equals(coordinate));
    }

    private void shipHitByHumanMethod(Button buttonToChangeColor, Integer xParam, Integer yParam) {
        List<Button> listWithButtonToChange = game.getFireButtonListTop();
        Ship shipHit = shipFromGridButton(listWithButtonToChange, game.getCpu().getShipsList(), buttonToChangeColor);

        changingBoardAndLog(buttonToChangeColor, xParam, yParam, listWithButtonToChange, shipHit, playerFires);
        
        
    }

    private void shipHitByCpu(Button buttonToChangeColor, Integer xParam, Integer yParam) {
        List<Button> listWithButtonToChange = game.getSeaButtonsListBottom();
        Ship shipHit = shipFromGridButton(listWithButtonToChange, game.getHuman().getShipsList(), buttonToChangeColor);

        changingBoardAndLog(buttonToChangeColor, xParam, yParam, listWithButtonToChange, shipHit, cpuFires);
    }

    private void changingBoardAndLog(Button buttonToChangeColor, Integer xParam, Integer yParam, List<Button> listWithButtonToChange, Ship shipHit, String whoFires) {
        shipHit.setShipPartsInGameCount(shipHit.getShipPartsInGameCount() - 1);
        
        Integer yParamFix = yParam + 1;

        if (shipHit.getShipPartsInGameCount() > 0) {
            buttonToChangeColor.setId("hit");
            game.setBattleLog(whoFires + numberToLetter(xParam) + yParamFix + " hit");
        } else {
            changeAllButtonsToSunk(listWithButtonToChange, shipHit);
            game.setBattleLog(whoFires + numberToLetter(xParam) + yParamFix + " sunk");
        }
    }


    private void changeAllButtonsToSunk(List<Button> listWithButtonToChange, Ship shipHit) {
        shipHit.getCoordinates().forEach(s -> listWithButtonToChange.get(s).setId("sunk"));
    }

    private void cpuTurn() {
        Random random = new Random();

        Integer choice = random.nextInt(100);
        System.out.println(choice);

        while (cpuChoices.contains(choice)) {
            choice = random.nextInt(100);
        }
        cpuChoices.add(choice);

//        for debug
//        game.getHuman().getShipsList().stream().flatMap(ship -> ship.getCoordinates().stream()).forEach(s -> allCoordinates.add(s));
//        
//        Integer choice = Integer.parseInt(allCoordinates.get(counter));
//        counter++;
        Integer xParam = choice / 10;
        Integer yParam = choice % 10;

        Button buttonToChange = game.getSeaButtonsListBottom().get(choice);
        if (hitCheck(game.getHuman(), choice)) {
            shipHitByCpu(buttonToChange, xParam, yParam);
        } else {
            buttonToChange.setId("miss");
            game.setBattleLog(cpuFires + numberToLetter(xParam) + (yParam + 1) + " miss");
        }
    }


    public void mouseShipNameEH(MouseEvent event) {

        Pane paneEntered = (Pane) event.getSource();
        Button buttonEntered = (Button) paneEntered.getChildren().get(0);

        if (buttonEntered.isDisable() && !game.getHuman().getShipsList().isEmpty()) {
            Ship shipHovered = shipFromGridButton(game.getSeaButtonsListBottom(), game.getHuman().getShipsList(), buttonEntered);
            Game.setMiddleLabel(shipHovered.getName().toString());
        }
    }

    public void mouseExitShipNameEH(MouseEvent event) {
        Pane paneEntered = (Pane) event.getSource();
        Button buttonEntered = (Button) paneEntered.getChildren().get(0);

        if (buttonEntered.isDisable()) {
            game.middleLabelUpdateText();
        }
    }
    
    public void mouseFireFieldName(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        int index = game.getFireButtonListTop().indexOf(button);
        
        String letter = numberToLetter(index/10);
        String y = (index%10 + 1) + "";
        
        Game.setMiddleLabel(letter+y);
        
    }
    
    public void mouseExitFireFieldName(MouseEvent event){
        game.middleLabelUpdateText();
    }
    

    private Ship shipFromGridButton(List<Button> gridButtons, List<Ship> shipList, Button buttonEntered) {
        int index = gridButtons.indexOf(buttonEntered);
        return shipList.stream()
                .filter(ship -> ship.getCoordinates().contains(index))
                .findAny().get();
    }

    private void checkWin() {
        //win condition
        if (game.getCpu().getShipsList().stream().filter(ship -> ship.getShipPartsInGameCount() == 0).count() == Ship.getAllShips().size()) {
            AlertBox.display("Result", "You win!");
            game.setBattleLog("GAME OVER! YOU WON!");
        } else if (game.getHuman().getShipsList().stream().filter(ship -> ship.getShipPartsInGameCount() == 0).count() == Ship.getAllShips().size()) {
            AlertBox.display("Result", "You loose!");
            game.setBattleLog("GAME OVER! YOU LOOSE!");
        }
    }
    
    public void columnRowMarkers(GridPane topRootForGrid, GridPane bottomRootForGrid) {
        List<Label> markerLabels = new ArrayList<>();
        for(int i = 1; i <= 10; i++){
            Label topMarkerLetters = new Label(numberToLetter(i - 1));
            Label topMarkerNumbers = new Label(""+i);
            Label bottomMarkerLetters = new Label(numberToLetter(i - 1));
            Label bottomMarkerNumbers = new Label(""+i);

            markerLabels.add(topMarkerLetters);
            markerLabels.add(topMarkerNumbers);
            markerLabels.add(bottomMarkerLetters);
            markerLabels.add(bottomMarkerNumbers);

            topRootForGrid.add(topMarkerLetters, i, 0);
            topRootForGrid.add(topMarkerNumbers, 0, i);
            bottomRootForGrid.add(bottomMarkerLetters, i, 0);
            bottomRootForGrid.add(bottomMarkerNumbers, 0, i);

        }

        markerLabels.forEach(label -> {
            label.setMinHeight(minButtonSize);
            label.setMinWidth(minButtonSize);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setAlignment(Pos.CENTER);
            label.setId("boardMarkers");
        });
    }

    private String numberToLetter(int number){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return Character.toString(alphabet.charAt(number));
    }
}
    
