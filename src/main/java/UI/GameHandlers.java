package UI;

import GameMechanic.Ship;
import GameMechanic.ShipName;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class GameHandlers {
    private final Game game;

    public GameHandlers(Game game) {
        this.game = game;
    }

    public void shipPlacementButtonEH(ActionEvent event, ShipName shipToHandleName) {
        Ship shipToHandle;

        if (!shipPlacementCheck(shipToHandleName)) {
            shipToHandle = new Ship(shipToHandleName);
            game.getPlayerShips().add(shipToHandle);
        } else {
            shipToHandle = game.getPlayerShips().stream().filter(ship -> ship.getName().equals(shipToHandleName)).findFirst().get();
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


            Ship shipToReset = game.getPlayerShips().stream()
                    .filter(ship -> ship.getName().equals(shipToHandleName))
                    .findFirst()
                    .get();

            resetPlacementBoard(shipToReset);

            changeShipPlacementButtonState(game, shipToReset, false);

            game.getPlayerShips().remove(shipToReset);
        }
    }

    /**
     * Resets all board buttons to default state equals to parameters of given ship
     *
     * @param shipToReset ship which parameters must be reset
     */
    private void resetPlacementBoard(Ship shipToReset) {
        shipToReset.getCoordinates().stream()
                .forEach(s -> {
                    Button toReset = game.getSeaButtonsListBottom().get(Integer.parseInt(s));
                    toReset.setDisable(false);
                });
    }


    /**
     * Checks if given ship was already placed
     *
     * @param shipToHandleName ship to check
     * @return boolean true if placed, false if it wasn't placed
     */
    private boolean shipPlacementCheck(ShipName shipToHandleName) {
        return game.getPlayerShips().stream()
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

        if (!game.getPlayerShips().isEmpty()) {
            Game.setCurrentShip(null);
            Game.updatingMiddleLabel();

            game.getPlayerShips().forEach(s -> resetPlacementBoard(s));

            game.getPlayerShips().clear();
            game.getPlacementShipButtonListLeft().stream().forEach(button -> button.setDisable(false));
        }
    }

    public void newGameButtonEH(ActionEvent event) {
        int shipPartsPlaced = game.getPlayerShips().stream()
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
        if (!game.getPlayerShips().isEmpty()) {
            if(ConfirmBox.display("Warning!", "This action will reset all ships that have been already placed. Continue?")){
                Game.setCurrentShip(null);
                Game.updatingMiddleLabel();
                game.getPlayerShips().stream().forEach(this::resetPlacementBoard);
                game.getPlacementShipButtonListLeft().forEach(button -> button.setDisable(false));
                game.getPlayerShips().clear();
            } else {
                return;
            }
        }
        game.setHumanShips();
    }


    

}
