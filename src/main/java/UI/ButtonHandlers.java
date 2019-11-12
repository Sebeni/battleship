package UI;

import GameMechanic.Ship;
import GameMechanic.ShipName;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ButtonHandlers {
    private final Game game;

    public ButtonHandlers(Game game) {
        this.game = game;
    }

    public void shipPlacementButtonEH(ActionEvent event, ShipName shipToHandleName) {
        Ship shipToHandle;

        if (!shipPlacementCheck(shipToHandleName)) {
            shipToHandle = new Ship(shipToHandleName);
            game.getHuman().getShipsList().add(shipToHandle);
        } else {
            shipToHandle = game.getHuman().getShipsList().stream().filter(ship -> ship.getName().equals(shipToHandleName)).findFirst().get();
        }

        Game.setCurrentShip(shipToHandle);
        Game.updatingMiddleLabel();
    }

    public void resetPlacementButtonEH(ActionEvent event, ShipName shipToHandleName) {

        if (shipPlacementCheck(shipToHandleName)) {
            // set current ship to null
            // activate all cells with coordinates and change their colors
            // activate ship button list
            // delete from list given ship
            Game.setCurrentShip(null);
            Game.updatingMiddleLabel();


            Ship shipToReset = game.getHuman().getShipsList().stream()
                    .filter(ship -> ship.getName().equals(shipToHandleName))
                    .findFirst()
                    .get();

            resetPlacementBoard(shipToReset);

            changeShipPlacementButtonState(game, shipToReset, false);

            game.getHuman().getShipsList().remove(shipToReset);
        }
    }
    
    private void resetPlacementBoard(Ship shipToReset) {
        shipToReset.getCoordinates().stream()
                .forEach(s -> {
                    Button toReset = game.getSeaButtonsListBottom().get(Integer.parseInt(s));
                    toReset.setDisable(false);
                });
    }

    
    private boolean shipPlacementCheck(ShipName shipToHandleName) {
        return game.getHuman().getShipsList().stream()
                .map(Ship::getName)
                .anyMatch(shipName -> shipName.equals(shipToHandleName));
    }

    public static void changeShipPlacementButtonState(Game game, Ship shipToReset, boolean setDisable) {
        switch (shipToReset.getName()) {
            case CARRIER:
                game.getPlacementShipButtonListLeft().get(0).setDisable(setDisable);
                break;
            case BATTLESHIP:
                game.getPlacementShipButtonListLeft().get(1).setDisable(setDisable);
                break;
            case CRUISER:
                game.getPlacementShipButtonListLeft().get(2).setDisable(setDisable);
                break;
            case SUBMARINE:
                game.getPlacementShipButtonListLeft().get(3).setDisable(setDisable);
                break;
            case DESTROYER:
                game.getPlacementShipButtonListLeft().get(4).setDisable(setDisable);
                break;
        }
    }


    public void resetAllButtonEH(ActionEvent event) {

        if (!game.getHuman().getShipsList().isEmpty()) {
            Game.setCurrentShip(null);
            Game.updatingMiddleLabel();

            game.getHuman().getShipsList().forEach(s -> resetPlacementBoard(s));

            game.getHuman().getShipsList().clear();
            game.getPlacementShipButtonListLeft().stream().forEach(button -> button.setDisable(false));
        }
    }

    public void newGameButtonEH(ActionEvent event) {
        int shipPartsPlaced = game.getHuman().getShipsList().stream()
                .mapToInt(ship -> ship.getCoordinates().size())
                .sum();

        int allShipsParts = Ship.getAllShipsParts();
        
        if (shipPartsPlaced == allShipsParts) {
            game.getFireButtonListTop().forEach(button -> button.setDisable(false));
            game.getPlacementShipButtonListLeft().forEach(button -> button.setDisable(true));
            game.getResetShipButtonListRight().forEach(button -> button.setDisable(true));
            Button source = (Button) event.getSource();
            source.setDisable(true);
            game.setCpuShips();

        } else {
            AlertBox.display("Place all ships", "Before starting a game you must place all your ships!");
        }
    }

    public void randomPlacementButtonEH(ActionEvent event) {
        if (!game.getHuman().getShipsList().isEmpty()) {
            if(ConfirmBox.display("Warning!", "This action will reset all ships that have been already placed. Continue?")){
                Game.setCurrentShip(null);
                Game.updatingMiddleLabel();
                game.getHuman().getShipsList().stream().forEach(this::resetPlacementBoard);
                game.getPlacementShipButtonListLeft().forEach(button -> button.setDisable(false));
                game.getHuman().getShipsList().clear();
            } else {
                return;
            }
        }
        game.setHumanShips();
    }
}
