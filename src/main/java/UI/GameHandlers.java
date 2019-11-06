package UI;

import GameMechanic.Ship;
import GameMechanic.ShipName;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Map;

public class GameHandlers {
    private final Game game;

    public GameHandlers(Game game) {
        this.game = game;
    }

    public void shipPlacementButtonEH(ActionEvent event, ShipName shipToHandleName) {
        if (Game.debug) {
            System.out.println(event.getSource());
            if (Game.getCurrentShip() != null) {
                System.out.println(Game.getCurrentShip().getName());
            }
        }

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
        if (Game.debug) {
            System.out.println(event.getSource());
        }
        
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

            shipToReset.getCoordinates().stream()
                    .forEach(s -> {
                        Button toReset = game.getLocationButtonList().get(Integer.parseInt(s));
                        toReset.setDisable(false);
                        toReset.setId("boardButton");
                    });
            
            changeShipPlacementButtonState(game, shipToReset, false);

            game.getPlayerShips().remove(shipToReset);
        }
    }
    

    private boolean shipPlacementCheck(ShipName shipToHandleName) {
        return game.getPlayerShips().stream()
                .map(Ship::getName)
                .anyMatch(shipName -> shipName.equals(shipToHandleName));
    }
    
    public static void changeShipPlacementButtonState(Game game, Ship shipToReset, boolean setDisable){
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
        if (Game.debug) {
            System.out.println(event.getSource());
        }

        if (!game.getPlayerShips().isEmpty()) {
            Game.setCurrentShip(null);
            Game.updatingShipPartLabel();

            game.getPlayerShips().stream()
                    .flatMap(ship -> ship.getCoordinates().stream())
                    .forEach(s -> {
                        Button toReset = game.getLocationButtonList().get(Integer.parseInt(s));
                        toReset.setId("boardButton");
                        toReset.setDisable(false);
                    });

            game.getPlayerShips().clear();
            game.getShipPlacementButtonList().stream().forEach(button -> button.setDisable(false));
        }
    }
    
    public void newGameButtonEH(ActionEvent event) {
        long shipPartsPlaced = game.getPlayerShips().stream()
                .flatMap(ship -> ship.getCoordinates().stream())
                .count();

        long allShipsParts = 0;

        for (Map.Entry<ShipName, Integer> entry : Ship.getAllShipsMaxSize().entrySet()) {
            allShipsParts += entry.getValue();
        }

        if (shipPartsPlaced == allShipsParts) {
            game.getFireButtonList().forEach(button -> button.setDisable(false));
            game.getLocationButtonList().forEach(button -> button.setDisable(true));
            game.getResetShipButtonList().forEach(button -> button.setDisable(true));
            new Stage();
        } else {
            AlertBox.display("Place all ships", "Before starting a game you must place all your ships!");
        }
    }
}
