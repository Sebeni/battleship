package UI;

import GameMechanic.Ship;
import GameMechanic.ShipName;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

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
        Game.updatingShipPartLabel();
    }

    public void resetPlacementButtonEH(ActionEvent event, ShipName shipToHandleName) {

        if (shipPlacementCheck(shipToHandleName)) {
            // set current ship to null
            // activate all cells with coordinates and change their colors
            // activate ship button list
            // delete from list given ship
            Game.setCurrentShip(null);
            Game.updatingShipPartLabel();


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
                    Button toReset = game.getLocationButtonList().get(Integer.parseInt(s));
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
                game.getShipPlacementButtonList().get(0).setDisable(setDisable);
                break;
            case BATTLESHIP:
                game.getShipPlacementButtonList().get(1).setDisable(setDisable);
                break;
            case CRUISER:
                game.getShipPlacementButtonList().get(2).setDisable(setDisable);
                break;
            case SUBMARINE:
                game.getShipPlacementButtonList().get(3).setDisable(setDisable);
                break;
            case DESTROYER:
                game.getShipPlacementButtonList().get(4).setDisable(setDisable);
                break;
        }
    }


    public void resetAllButtonEH(ActionEvent event) {

        if (!game.getPlayerShips().isEmpty()) {
            Game.setCurrentShip(null);
            Game.updatingShipPartLabel();

            game.getPlayerShips().forEach(s -> resetPlacementBoard(s));

            game.getPlayerShips().clear();
            game.getShipPlacementButtonList().stream().forEach(button -> button.setDisable(false));
        }
    }

    public void newGameButtonEH(ActionEvent event) {
        int shipPartsPlaced = game.getPlayerShips().stream()
                .mapToInt(ship -> ship.getCoordinates().size())
                .sum();

        int allShipsParts = Ship.getAllShipsMaxSize().values().stream()
                .mapToInt(l -> l)
                .sum();
        
        if (shipPartsPlaced == allShipsParts) {
            game.getFireButtonList().forEach(button -> button.setDisable(false));
            game.getShipPlacementButtonList().forEach(button -> button.setDisable(true));
            game.getResetShipButtonList().forEach(button -> button.setDisable(true));
            Button source = (Button) event.getSource();
            source.setDisable(true);
            game.setCpuShips();

        } else {
            AlertBox.display("Place all ships", "Before starting a game you must place all your ships!");
        }
    }

    public void randomPlacementButtonEH(ActionEvent event) {
        if (!game.getPlayerShips().isEmpty()) {
            if(ConfirmBox.display("Warning!", "This action will reset all ships you have already placed. Continue?")){
                Game.setCurrentShip(null);
                Game.updatingShipPartLabel();
                game.getPlayerShips().stream().forEach(this::resetPlacementBoard);
                game.getShipPlacementButtonList().forEach(button -> button.setDisable(false));
                game.getPlayerShips().clear();
            } else {
                return;
            }
        }
        game.setHumanShips();
    }

}
