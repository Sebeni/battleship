package GameMechanic;

import UI.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ship {
    /**
     * Ships sizes:
     *  	Carrier 	5
     *  	Battleship 	4
     *  	Cruiser 	3
     *  	Submarine 	3
     *  	Destroyer 	2 
     */
    private final static Map<ShipName, Integer> allShipsMaxSize = new HashMap<>();
    static {
        allShipsMaxSize.put(ShipName.CARRIER, 5);
        allShipsMaxSize.put(ShipName.BATTLESHIP, 4);
        allShipsMaxSize.put(ShipName.CRUISER, 3);
        allShipsMaxSize.put(ShipName.SUBMARINE, 3);
        allShipsMaxSize.put(ShipName.DESTROYER, 2);
    }
    
    private final int shipMaxSize;
    private int shipFieldCount = 0;
    private boolean horizontalPlacement;
    
    private ShipName name;
    private List<String> coordinates;

    public Ship(ShipName name) {
        this.name = name;
        this.shipMaxSize = allShipsMaxSize.get(name);
        this.coordinates = new ArrayList<>();
    }

    public void setCoordinates(int x, int y) {
        coordinates.add(("" + x) + ("" + y));
        shipFieldCount++;
    }

    public int getShipMaxSize() {
        return shipMaxSize;
    }

    public int getShipFieldCount() {
        return shipFieldCount;
    }

    public boolean isHorizontalPlacement() {
        return horizontalPlacement;
    }

    public void setHorizontalPlacement(boolean horizontalPlacement) {
        this.horizontalPlacement = horizontalPlacement;
    }

    public List<String> getCoordinates() {
        return coordinates;
    }

    public ShipName getName() {
        return name;
    }

    public static Map<ShipName, Integer> getAllShipsMaxSize() {
        return allShipsMaxSize;
    }
    
    @Override
    public String toString() {
        return "Ship{" +
                "name=" + name +
                ", coordinates=" + coordinates.stream().reduce((s, s2) -> s += " " + s2) +
                '}';
    }

    public void setShipFieldCount(int shipFieldCount) {
        this.shipFieldCount = shipFieldCount;
    }
}
