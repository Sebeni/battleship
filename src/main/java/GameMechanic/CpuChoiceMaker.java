package GameMechanic;

import UI.Game;
import UI.GridPaneButtonMethods;

import java.util.*;

public class CpuChoiceMaker {

    //    private int firstHit;
    private boolean shootHorizontal;

    private Game game;
    private GridPaneButtonMethods gridMethods;


    private Map<Integer, HitState> cpuAllShots = new LinkedHashMap<>();


    private Random random = new Random();


    public CpuChoiceMaker(Game game, GridPaneButtonMethods gridMethods) {
        this.game = game;
        this.gridMethods = gridMethods;
    }

    public int getCpuChoice() {
        if (!cpuAllShots.containsValue(HitState.HIT)) {
            System.out.println("SEARCH MODE");
            return searchMode();

        } else {
            System.out.println("FIRE MODE");
            return fireMode();
        }
    }

    private int searchMode() {
        Integer choice = checkerboardAlgorithm();
        while (cpuAllShots.containsKey(choice)) {
            choice = checkerboardAlgorithm();
        }

        if (hitCheckDelegate(choice)) {
            cpuAllShots.put(choice, HitState.HIT);
//            firstHit = choice;
        } else {
            cpuAllShots.put(choice, HitState.MISS);
        }
        return choice;
    }

    private int checkerboardAlgorithm() {
        int choice = random.nextInt(100);
        if ((choice / 10) % 2 == 0) {
            if (choice % 2 != 0 && choice > 0) {
                choice--;
            }
        } else {
            if (choice % 2 == 0) {
                choice++;
            }
        }
        return choice;
    }

    private int fireMode() {
        return fourDirections(getEntryByValue(HitState.HIT));
    }

    private int fourDirections(Map.Entry<Integer, HitState> entryWithHit) {
        int cellHit = entryWithHit.getKey();

        int nextShot;
        if (canShootUp(cellHit)) {
            nextShot = cellHit - 1;
            checkAndPutNextShot(nextShot);
            return nextShot;
        } else if (canShootDown(cellHit)) {
            nextShot = cellHit + 1;
            checkAndPutNextShot(nextShot);
            return nextShot;
        } else if (canShootLeft(cellHit)) {
            nextShot = cellHit - 10;
            checkAndPutNextShot(nextShot);
            return nextShot;
        } else if (canShootRight(cellHit)) {
            nextShot = cellHit + 10;
            checkAndPutNextShot(nextShot);
            return nextShot;
        } else {
            entryWithHit.setValue(HitState.MISS);
            if (cpuAllShots.containsValue(HitState.HIT)) {
                return fourDirections(getEntryByValue(HitState.HIT));
            } else {
                System.out.println("REACHED SAFE SEARCH MODE!");
                return searchMode();
            }
            
        }
    }

    private int onlyHorizontal(int cellHit) {
        if (canShootUp(cellHit)) {
            return cellHit - 1;
        } else if (canShootDown(cellHit)) {
            return cellHit + 1;
        } else {
            return searchMode();
        }
    }

    private int onlyVertical(int cellHit) {
        if (canShootLeft(cellHit)) {
            return cellHit - 10;
        } else if (canShootRight(cellHit)) {
            return cellHit + 10;
        } else {
            return searchMode();
        }
    }

    private boolean canShootUp(int startingCell) {
        boolean upLimitReached = startingCell % 10 == 0;
        boolean cellAlreadyShot = cpuAllShots.containsKey(startingCell - 1);
        return !upLimitReached && !cellAlreadyShot;
    }

    private boolean canShootDown(int startingCell) {
        boolean downLimitReached = startingCell % 10 == 9;
        boolean cellAlreadyShot = cpuAllShots.containsKey(startingCell + 1);
        return !downLimitReached && !cellAlreadyShot;
    }

    private boolean canShootLeft(int startingCell) {
        boolean leftLimitReached = startingCell / 10 == 0;
        boolean cellAlreadyShot = cpuAllShots.containsKey(startingCell - 10);
        return !leftLimitReached && !cellAlreadyShot;
    }

    private boolean canShootRight(int startingCell) {
        boolean rightLimitReached = startingCell / 10 == 9;
        boolean cellAlreadyShot = cpuAllShots.containsKey(startingCell + 10);
        return !rightLimitReached && !cellAlreadyShot;
    }


    private Map.Entry<Integer, HitState> getEntryByValue(HitState hitStateToGet) {
        return cpuAllShots.entrySet().stream()
                .filter(integerHitStateEntry -> integerHitStateEntry.getValue().equals(HitState.HIT))
                .findAny()
                .get();
    }

    private void checkAndPutNextShot(int nextShot) {
        System.out.println("NEXT SHOT " + nextShot);

        if (hitCheckDelegate(nextShot)) {
            System.out.println("HIT!");
            cpuAllShots.put(nextShot, HitState.HIT);
        } else {
            System.out.println("MISS!");
            cpuAllShots.put(nextShot, HitState.MISS);
        }
    }

    private boolean hitCheckDelegate(int choiceToCheck) {
        return gridMethods.hitCheck(game.getHuman(), choiceToCheck);
    }

    public Map<Integer, HitState> getCpuAllShots() {
        return cpuAllShots;
    }
}
