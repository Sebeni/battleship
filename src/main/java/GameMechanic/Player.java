package GameMechanic;

import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Ship> playerShips = new ArrayList<Ship>();
    private final boolean isHuman;
    
    
    public Player(boolean isHuman) {
        this.isHuman = isHuman;
    }

    public List<Ship> getShipsList() {
        return playerShips;
    }

    public void setPlayerShips(List<Ship> playerShips) {
        this.playerShips = playerShips;
    }

    public boolean isHuman() {
        return isHuman;
    }
}