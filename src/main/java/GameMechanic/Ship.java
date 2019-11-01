package GameMechanic;

import UI.Game;

public class Ship {
    /**
     * By default ships should be this size:
     *  	Carrier 	5
     *  	Battleship 	4
     *  	Cruiser 	3
     *  	Submarine 	3
     *  	Destroyer 	2 
     */
    
    private int shipMaxSize;
    private int shipFieldCount = 0;
    private boolean horizontalPlacement;
    
    private ShipNames name;
    private int[][] coordinates;

    public Ship(int shipMaxSize, ShipNames name) {
        this.shipMaxSize = shipMaxSize;
        this.name = name;
        coordinates = new int[shipMaxSize][2];
        if(Game.debug){
            System.out.println("Ship created! size " + shipMaxSize + " ship name " + name);
        }
    }

    public void setCoordinates(int x, int y) {
        if(Game.debug){
            System.out.println(shipFieldCount);
        }
        
        coordinates[shipFieldCount][0] = x;
        coordinates[shipFieldCount][1] = y;
        if(Game.debug){
            System.out.println("Ship" +  name + "coordinates set x: " + x + " y: " + y);
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

    public int[][] getCoordinates() {
        return coordinates;
    }
}
