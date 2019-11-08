package GameMechanic;

import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPlacement {
    private static List<Ship> result;
    
    public static List<Ship> randomShipPlacement(List<Button> buttonListToDisable) {
        result = new ArrayList<>();
        
        for (ShipName s : Ship.getAllShipsMaxSize().keySet()) {
            Ship ship = new Ship(s);
            horizontalCheck(ship);
            result.add(ship);
        }
        
        result.stream()
                .flatMap(ship -> ship.getCoordinates().stream())
                .forEach(s -> buttonListToDisable.get(Integer.parseInt(s)).setDisable(true));

        return result;
    }

    private static void horizontalCheck(Ship ship) {
        Random random = new Random();

        ship.setHorizontalPlacement(random.nextBoolean());

        if (ship.isHorizontalPlacement()) {
            //y 0 : 9; x 0 :(9-ship max size)
            Integer x = random.nextInt(10 - ship.getShipMaxSize());
            Integer y = random.nextInt(10);
            placeShipPart(x, y, ship);


        } else {
            //y 0 : (9-ship max size), x 0 : 9
            Integer x = random.nextInt(10);
            Integer y = random.nextInt(10 - ship.getShipMaxSize());
            placeShipPart(x, y, ship);
        }
    }

    private static void placeShipPart(Integer x, Integer y, Ship ship) {
        if (!coordinateIsOccupied(x, y)) {
            ship.setCoordinates(x, y);

            if (ship.getShipFieldCount() < ship.getShipMaxSize()) {
                if (ship.isHorizontalPlacement()) {
                    placeShipPart(x + 1, y, ship);
                } else {
                    placeShipPart(x, y + 1, ship);
                }
            }
        } else {
            ship.getCoordinates().clear();
            ship.setShipFieldCount(0);

            horizontalCheck(ship);
        }
    }

    private static boolean coordinateIsOccupied(Integer x, Integer y) {
        String coordinateToCheck = x.toString() + y.toString();
        boolean coordinateOccupied = false;
        if (!result.isEmpty()) {
            coordinateOccupied = result.stream().flatMap(ship -> ship.getCoordinates().stream()).anyMatch(s -> s.equals(coordinateToCheck));
        }
        return coordinateOccupied;
    }
}