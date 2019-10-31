package GameMechanic;

public class Ship {
    /**
     * By default ships should be this size:
     *  	Carrier 	5
     *  	Battleship 	4
     *  	Cruiser 	3
     *  	Submarine 	3
     *  	Destroyer 	2 
     */
    
    private int shipSize;
    private int shipFieldCount = 0;
    
    
    private ShipNames name;
    
    
    private int[][] coordinates = new int[shipSize][2];

    public Ship(int shipSize, ShipNames name) {
        this.shipSize = shipSize;
        this.name = name;
    }

    public void setCoordinates(int x, int y) {
        coordinates[shipFieldCount][0] = x;
        coordinates[shipFieldCount][1] = y;
        shipFieldCount++;
    }
}
