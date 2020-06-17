package pl.seb.czech.GameUI.Handlers;

import pl.seb.czech.GameMechanic.*;
import pl.seb.czech.GameUI.Boxes.Game;
import pl.seb.czech.GameUI.GridHelperMethods;
import pl.seb.czech.GameUI.Boxes.ResultBox;
import pl.seb.czech.Statistics.Stats;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.*;

public class FireButtonHandlers {
    private final Game game;

    private final CpuChoiceMaker cpuChoiceMaker;
    private final String playerFires = "You fired at: ";
    private final String cpuFires = "Cpu fired at: ";
    private final static Map<ShipName, ImageView> SHIP_PICS = new HashMap<>();
    
    private final Set<Integer> humanAllSuccessfulShots = new HashSet<>();
    private final Set<Integer> cpuAllSuccessfulShots = new HashSet<>();
    private final Set<Integer> humanAllMissedShots = new HashSet<>();
    private final Set<Integer> cpuAllMissedShots = new HashSet<>();
    
    static {
        SHIP_PICS.put(ShipName.BATTLESHIP, new ImageView("battleship.png"));
        SHIP_PICS.put(ShipName.CARRIER, new ImageView("carrier.png"));
        SHIP_PICS.put(ShipName.CRUISER, new ImageView("cruiser.png"));
        SHIP_PICS.put(ShipName.DESTROYER, new ImageView("destroyer.png"));
        SHIP_PICS.put(ShipName.SUBMARINE, new ImageView("submarine.png"));

        SHIP_PICS.forEach((key, iv) -> {
            iv.setFitWidth(240);
            iv.setFitHeight(60);
        });
    }
    
    public FireButtonHandlers(Game game) {
        this.game = game;
        this.cpuChoiceMaker = new CpuChoiceMaker(game);
    }

    public void fireButtonHandler(ActionEvent event) {
        Button button = (Button) event.getSource();

        Integer xParam = GridPane.getColumnIndex(button.getParent());
        Integer yParam = GridPane.getRowIndex(button.getParent());
        
        Integer coordinate = xParam * 10 + yParam;

        if (GridHelperMethods.hitCheck(game.getCpu(), coordinate)) {
            humanAllSuccessfulShots.add(coordinate);
            shipHitByHumanMethod(button, xParam, yParam);
        } else {
            humanAllMissedShots.add(coordinate);
            game.setBattleLog(playerFires + GridHelperMethods.numberToLetter(xParam) + (yParam + 1) + " miss");
            game.getCpuVisualBox().getButtonList().get(coordinate).setId("miss");
            button.setId("miss");
        }
        button.setDisable(true);
        if(!gameIsFinished()){
            cpuTurn();
            gameIsFinished();
            game.increaseRoundCounter();
        }
    }

    private void shipHitByHumanMethod(Button buttonToChangeColor, Integer xParam, Integer yParam) {
        List<Button> listWithButtonToChange = game.getFireButtonListTop();
        Ship shipHit = GridHelperMethods.shipFromGridButton(listWithButtonToChange, game.getCpu().getShipsList(), buttonToChangeColor);
        
        changingShipPartBoardAndLog(buttonToChangeColor, xParam, yParam, listWithButtonToChange, shipHit, playerFires);

        if(shipHit.getShipPartsInGameCount() > 0){
            game.getCpuVisualBox().getButtonList().get(xParam*10 + yParam).setId("hit");
        } else {
            changeAllButtonsToSunk(game.getCpuVisualBox().getButtonList(), shipHit);
            addSunkShipPic(shipHit);
        }
    }

    private void shipHitByCpuMethod(Button buttonToChangeColor, Integer xParam, Integer yParam) {
        List<Button> listWithButtonToChange = game.getSeaButtonsListBottom();
        Ship shipHit = GridHelperMethods.shipFromGridButton(listWithButtonToChange, game.getHuman().getShipsList(), buttonToChangeColor);
        
        changingShipPartBoardAndLog(buttonToChangeColor, xParam, yParam, listWithButtonToChange, shipHit, cpuFires);
        
        if(shipHit.getShipPartsInGameCount() == 0){
            shipHit.getCoordinates().forEach(integer -> cpuChoiceMaker.getCpuAllShots().replace(integer, HitState.SUNK));
            cpuChoiceMaker.getCpuAllShots().entrySet().stream()
                    .filter(integerHitStateEntry -> integerHitStateEntry.getValue().equals(HitState.DEPLETED))
                    .forEach(integerHitStateEntry -> integerHitStateEntry.setValue(HitState.HIT));
            
            if(!Game.shipsCanTouch()){
                cpuChoiceMaker.deleteTouchingCells(shipHit);
            }
        }
    }

    private void changingShipPartBoardAndLog(Button buttonToChangeColor, Integer xParam, Integer yParam, List<Button> listWithButtonToChange, Ship shipHit, String whoFires) {
        shipHit.setShipPartsInGameCount(shipHit.getShipPartsInGameCount() - 1);
        
        Integer yParamFix = yParam + 1;

        if (shipHit.getShipPartsInGameCount() > 0) {
            buttonToChangeColor.setId("hit");
            game.setBattleLog(whoFires + GridHelperMethods.numberToLetter(xParam) + yParamFix + " hit");
        } else {
            changeAllButtonsToSunk(listWithButtonToChange, shipHit);
            game.setBattleLog(whoFires + GridHelperMethods.numberToLetter(xParam) + yParamFix + " sunk");
           
        }
    }
    
    private void changeAllButtonsToSunk(List<Button> listWithButtonToChange, Ship shipHit) {
        shipHit.getCoordinates().forEach(s -> listWithButtonToChange.get(s).setId("sunk"));
    }

    private void cpuTurn() {
        Integer choice = cpuChoiceMaker.getCpuChoice();

        Integer xParam = choice / 10;
        Integer yParam = choice % 10;

        Button buttonToChange = game.getSeaButtonsListBottom().get(choice);
        if (GridHelperMethods.hitCheck(game.getHuman(), choice)) {
            cpuAllSuccessfulShots.add(choice);
            shipHitByCpuMethod(buttonToChange, xParam, yParam);
        } else {
            cpuAllMissedShots.add(choice);
            buttonToChange.setId("miss");
            game.setBattleLog(cpuFires + GridHelperMethods.numberToLetter(xParam) + (yParam + 1) + " miss");
            buttonToChange.setDisable(true);
        }
    }
    
    
    public void mouseFireFieldName(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        int index = game.getFireButtonListTop().indexOf(button);
        
        String letter = GridHelperMethods.numberToLetter(index / 10);
        String y = (index % 10 + 1) + "";
        
        game.setMiddleLabel(letter+y);
        
    }
    
    public void mouseExitFireFieldName(MouseEvent event){
        game.middleLabelUpdateText();
        event.consume();
    }

    private boolean gameIsFinished() {
        //win condition
        if (game.getCpu().getShipsList().stream().filter(ship -> ship.getShipPartsInGameCount() == 0).count() == Ship.getAllShips().size()) {
            saveStats(true);
            game.setBattleLog("GAME OVER! YOU WON!");
            ResultBox.display(true, game);
            return true;
        } else if (game.getHuman().getShipsList().stream().filter(ship -> ship.getShipPartsInGameCount() == 0).count() == Ship.getAllShips().size()) {
            saveStats(false);
            game.setBattleLog("GAME OVER! YOU LOOSE!");
            ResultBox.display(false, game);
            return true;
        } else {
            return false;
        }
    }

    private void saveStats(boolean playerWon) {
        Stats.addSuccessfulShotsToCpuMap(cpuAllSuccessfulShots);
        Stats.addMissedShotsToCpuMap(cpuAllMissedShots);
        
        Stats.addSuccessfulShotsToHumanMap(humanAllSuccessfulShots);
        Stats.addMissedShotsToHumanMap(humanAllMissedShots);
        
        Stats.setRoundCounter(Stats.getRoundCounter() + 1);
        
        if(playerWon){
            Stats.setPlayerWonCounter(Stats.getPlayerWonCounter() + 1);
        } else {
            Stats.setPlayerLoseCounter(Stats.getPlayerLoseCounter() + 1);
        }
        
        Stats.saveStats();
    }

    private void addSunkShipPic(Ship sunkShip) {
        game.getSunkEnemyShips().getChildren().add(SHIP_PICS.get(sunkShip.getName()));
    }
}
    
