package GameMechanic;

import UI.Game;

import java.util.HashMap;
import java.util.Map;

public class Ship {
    /**
     * By default ships should be this size:
     *  	Carrier 	5
     *  	Battleship 	4
     *  	Cruiser 	3
     *  	Submarine 	3
     *  	Destroyer 	2 
     */
    private static Map<ShipName, Integer> allShipsMaxSize = new HashMap<>();
    static {
        allShipsMaxSize.put(ShipName.CARRIER, 5);
        allShipsMaxSize.put(ShipName.BATTLESHIP, 4);
        allShipsMaxSize.put(ShipName.CRUISER, 3);
        allShipsMaxSize.put(ShipName.SUBMARINE, 3);
        allShipsMaxSize.put(ShipName.DESTROYER, 2);
    }
    
    
    private int shipMaxSize;
    private int shipFieldCount = 0;
    private boolean horizontalPlacement;
    
    private ShipName name;
    private String[] coordinates;

    public Ship(ShipName name) {
        this.name = name;
        this.shipMaxSize = allShipsMaxSize.get(name);
        this.coordinates = new String[shipMaxSize];
        if(Game.debug){
            System.out.println("Ship created! size " + shipMaxSize + " ship name " + name);
        }
    }

    public void setCoordinates(int x, int y) {
        if(Game.debug){
            System.out.println(shipFieldCount);
        }
        coordinates[shipFieldCount] = ("" + x) + ("" + y);
        
        if(Game.debug){
            System.out.println("Ship" +  name + "coordinates set:" + coordinates);
        }
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

    public String[] getCoordinates() {
        return coordinates;
    }

    public ShipName getName() {
        return name;
    }

    public static Map<ShipName, Integer> getAllShipsMaxSize() {
        return allShipsMaxSize;
    }
}
