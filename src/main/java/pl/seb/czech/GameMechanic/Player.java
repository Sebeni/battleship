package pl.seb.czech.GameMechanic;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Ship> playerShips = new ArrayList<>();
    
    public List<Ship> getShipsList() {
        return playerShips;
    }

    public void setPlayerShips(List<Ship> playerShips) {
        this.playerShips = playerShips;
    }
    
}