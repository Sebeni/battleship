package GameMechanic;

import GameUI.Boxes.Game;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomPlacement {
    private static List<Ship> result;
    
    public static List<Ship> randomShipPlacementList(List<Button> buttonListToPlaceShips) {
        result = new ArrayList<>();
        
        for (ShipName s : Ship.getAllShips().keySet()) {
            Ship ship = new Ship(s);
            horizontalCheck(ship);
            result.add(ship);
        }
        
        result.stream()
                .flatMap(ship -> ship.getCoordinates().stream())
                .forEach(s -> buttonListToPlaceShips.get(s).setDisable(true));

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

    private static void placeShipPart(Integer x, Integer y, Ship currentShip) {
        if (!coordinateIsOccupied(x, y, currentShip)) {
            currentShip.setCoordinate(x, y);

            if (currentShip.getShipPartsInGameCount() < currentShip.getShipMaxSize()) {
                if (currentShip.isHorizontalPlacement()) {
                    placeShipPart(x + 1, y, currentShip);
                } else {
                    placeShipPart(x, y + 1, currentShip);
                }
            }
        } else {
            currentShip.getCoordinates().clear();
            currentShip.setShipPartsInGameCount(0);

            horizontalCheck(currentShip);
        }
    }

    private static boolean coordinateIsOccupied(Integer x, Integer y, Ship currentShip) {
        boolean coordinateOccupied = false;
        if (!result.isEmpty()) {
            coordinateOccupied = result.stream().flatMap(ship -> ship.getCoordinates().stream()).anyMatch(s -> s.equals(x*10+y));
            if(!Game.shipsCanTouch() && !coordinateOccupied){
                coordinateOccupied = coordinateIsTouching(x, y, currentShip);
            }
        }
        
        return coordinateOccupied;
    }
    
    private static boolean coordinateIsTouching(Integer x, Integer y, Ship currentShip){
        List<Integer> allTouchingCells = getTouchingParams(x * 10 + y);

        List<Integer> coordinatesAlreadyPlaced = result.stream()
                .filter(ship -> !ship.equals(currentShip))
                .flatMap(ship -> ship.getCoordinates().stream())
                .collect(Collectors.toList());
        
        return allTouchingCells.stream().anyMatch(coordinatesAlreadyPlaced::contains);
    }

    public static List<Integer> getTouchingParams(int coordinate) {

        int left = coordinate - 10;
        int bottomLeft = left + 1;
        int upperLeft = left - 1;

        int right = coordinate + 10;
        int bottomRight = right + 1;
        int upperRight = right - 1;

        int up = coordinate - 1;
        int down = coordinate + 1;

        List<Integer> allTouchingCells = new ArrayList<>();
        allTouchingCells.add(left);
        allTouchingCells.add(right);
        
        if(coordinate % 10 != 0){
            allTouchingCells.add(upperLeft);
            allTouchingCells.add(upperRight);
            allTouchingCells.add(up);
        }
        
        if(coordinate % 10 != 9){
            allTouchingCells.add(bottomLeft);
            allTouchingCells.add(bottomRight);
            allTouchingCells.add(down);
        }
        
        return allTouchingCells;
    }
}