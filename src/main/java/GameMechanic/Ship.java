package GameMechanic;

import java.util.*;

public class Ship {
    private final static Map<ShipName, Integer> allShips = new LinkedHashMap<>();
    static {
        allShips.put(ShipName.CARRIER, 5);
        allShips.put(ShipName.BATTLESHIP, 4);
        allShips.put(ShipName.CRUISER, 3);
        allShips.put(ShipName.SUBMARINE, 3);
        allShips.put(ShipName.DESTROYER, 2);
    }
    private static int allShipsParts = allShips.values().stream().mapToInt(i -> i).sum(); 
    
    private final int shipMaxSize;
    private int shipPartsInGameCount = 0;
    private boolean horizontalPlacement;
    
    private ShipName name;
    private List<String> coordinates;

    public Ship(ShipName name) {
        this.name = name;
        this.shipMaxSize = allShips.get(name);
        this.coordinates = new ArrayList<>();
    }
    
    public void setCoordinate(Integer x, Integer y) {
        coordinates.add(x.toString() + y.toString());
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

    public List<String> getCoordinates() {
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
                "name=" + name +
                ", coordinates=" + coordinates.stream().reduce((s, s2) -> s += " " + s2) +
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
        return name == ship.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
