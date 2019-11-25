package GameMechanic;

import java.util.*;
import java.util.stream.Collectors;

public class Ship {
    private final static Map<ShipName, Integer> allShips = new LinkedHashMap<>();
    static {
        allShips.put(ShipName.CARRIER, 5);
        allShips.put(ShipName.BATTLESHIP, 4);
        allShips.put(ShipName.CRUISER, 3);
        allShips.put(ShipName.SUBMARINE, 3);
        allShips.put(ShipName.DESTROYER, 2);
    }
    private static final int allShipsParts = allShips.values().stream().mapToInt(i -> i).sum(); 
    
    private final int shipMaxSize;
    private int shipPartsInGameCount = 0;
    private boolean horizontalPlacement;
    
    private ShipName name;
    private List<Integer> coordinates;

    public Ship(ShipName name) {
        this.name = name;
        this.shipMaxSize = allShips.get(name);
        this.coordinates = new ArrayList<>();
    }
    
    public void setCoordinate(Integer x, Integer y) {
        coordinates.add(x*10 + y);
        shipPartsInGameCount++;
    }
    
    public int getShipMaxSize() {
        return shipMaxSize;
    }

    public int getShipPartsInGameCount() {
        return shipPartsInGameCount;
    }

    public boolean isHorizontalPlacement() {
        return horizontalPlacement;
    }

    public void setHorizontalPlacement(boolean horizontalPlacement) {
        this.horizontalPlacement = horizontalPlacement;
    }

    public List<Integer> getCoordinates() {
        return coordinates;
    }

    public ShipName getName() {
        return name;
    }

    public static Map<ShipName, Integer> getAllShips() {
        return allShips;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "shipMaxSize=" + shipMaxSize +
                ", shipPartsInGameCount=" + shipPartsInGameCount +
                ", horizontalPlacement=" + horizontalPlacement +
                ", name=" + name +
                ", coordinates= " + coordinates.stream().map(Object::toString).collect(Collectors.joining(", ")) +
                '}';
    }

    public void setShipPartsInGameCount(int shipPartsInGameCount) {
        this.shipPartsInGameCount = shipPartsInGameCount;
    }

    public static int getAllShipsParts() {
        return allShipsParts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ship)) return false;
        Ship ship = (Ship) o;
        return horizontalPlacement == ship.horizontalPlacement &&
                name == ship.name &&
                coordinates.equals(ship.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(horizontalPlacement, name, coordinates);
    }
}
