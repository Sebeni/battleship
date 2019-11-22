package GameUI.Handlers;

import GameMechanic.Ship;
import GameMechanic.ShipName;
import GameUI.Boxes.AlertBox;
import GameUI.Boxes.ConfirmBox;
import GameUI.Boxes.Game;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

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

        game.setCurrentShip(shipToHandle);
        game.middleLabelUpdateText();
    }

    public void resetPlacementButtonEH(ActionEvent event, ShipName shipToHandleName) {

        if (shipPlacementCheck(shipToHandleName)) {
            // set current ship to null
            // activate all cells with coordinates and change their colors
            // activate ship button list
            // delete from list given ship
            game.setCurrentShip(null);
            game.middleLabelUpdateText();


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
                    Button toReset = game.getSeaButtonsListBottom().get(s);
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
            game.setCurrentShip(null);
            game.middleLabelUpdateText();

            game.getHuman().getShipsList().forEach(s -> resetPlacementBoard(s));

            game.getHuman().getShipsList().clear();
            game.getPlacementShipButtonListLeft().stream().forEach(button -> button.setDisable(false));
        }
    }

    public void startGameButtonEH(ActionEvent event) {
        int shipPartsPlaced = game.getHuman().getShipsList().stream()
                .mapToInt(ship -> ship.getCoordinates().size())
                .sum();

        int allShipsParts = Ship.getAllShipsParts();

        if (shipPartsPlaced == allShipsParts) {
            game.setFirePhase(true);
            game.middleLabelUpdateText();
            game.setBattleLog("Turn 1");

            game.getSunkEnemyShips().getChildren().add(new Label("You have already sunk:"));

            game.getFireButtonListTop().forEach(button -> button.setDisable(false));
            game.getPlacementShipButtonListLeft().forEach(button -> button.setDisable(true));
            game.getResetShipButtonListRight().forEach(button -> button.setDisable(true));
            Button source = (Button) event.getSource();
            source.setDisable(true);
            game.setCpuShips();

        } else {
            String shipsWithMissingShipParts = game.getHuman().getShipsList().stream()
                    .filter(ship -> ship.getShipPartsInGameCount() != Ship.getAllShips().get(ship.getName()))
                    .map(ship -> ship.getName().toString())
                    .collect(Collectors.joining(" "));

            List<ShipName> namesOfShipsPlaced = game.getHuman().getShipsList().stream().map(ship -> ship.getName()).collect(Collectors.toList());

            String shipsNotPlacedYet = Ship.getAllShips().keySet().stream()
                    .filter(shipName -> !namesOfShipsPlaced.contains(shipName))
                    .map(shipName -> shipName.toString())
                    .collect(Collectors.joining(" "));

            String message = "Before starting a game you must place all your ships!";

            if (!shipsNotPlacedYet.isEmpty()) {
                message += "\n Ships which weren't placed yet: " + shipsNotPlacedYet;
            }

            if (!shipsWithMissingShipParts.isEmpty()) {
                message += "\n Ships with remaining parts to place: " + shipsWithMissingShipParts;
            }


            AlertBox.display("Place all ships", message);
        }
    }

    public void randomPlacementButtonEH(ActionEvent event) {
        if (!game.getHuman().getShipsList().isEmpty()) {
            if (ConfirmBox.display("Warning!", "This action will reset all ships that might have been already placed. Continue?")) {
                game.setCurrentShip(null);
                game.middleLabelUpdateText();
                game.getHuman().getShipsList().stream().forEach(shipToReset -> resetPlacementBoard(shipToReset));
                game.getPlacementShipButtonListLeft().forEach(button -> button.setDisable(true));
                game.getHuman().getShipsList().clear();
            } else {
                return;
            }
        }
        game.getPlacementShipButtonListLeft().forEach(button -> button.setDisable(true));
        game.setHumanShips();
    }

    public void resetGameButtonEH(ActionEvent event) {
        game.getCpuVisualBox().getWindow().close();
        Stage window = game.getWindow();
        Game newGame = new Game(window);
        window.setScene(newGame.getScene());
    }
}
