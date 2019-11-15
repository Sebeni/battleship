package GameMechanic;

import UI.Game;
import UI.GridPaneButtonMethods;

import java.util.*;

public class CpuChoiceMaker {

    private Game game;
    private GridPaneButtonMethods gridMethods;
    private HitFireDirection currentFireDirection = HitFireDirection.NONE;
    
    private int outOfOptionsSafetyCounter = 0;
    
    private Map<Integer, HitState> cpuAllShots = new LinkedHashMap<>();
    

    private Random random = new Random();


    public CpuChoiceMaker(Game game, GridPaneButtonMethods gridMethods) {
        this.game = game;
        this.gridMethods = gridMethods;
        
        
    }

    public int getCpuChoice() {
        if (!cpuAllShots.containsValue(HitState.HIT)) {
            System.out.println("SEARCH MODE");
            currentFireDirection = HitFireDirection.NONE;
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
            outOfOptionsSafetyCounter++;
            if(outOfOptionsSafetyCounter > 1000){
//                probably never reached but just in case
                choice = random.nextInt(100);
            }
        }
        outOfOptionsSafetyCounter = 0;

        if (hitCheckDelegate(choice)) {
            cpuAllShots.put(choice, HitState.HIT);
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
        if (currentFireDirection.equals(HitFireDirection.NONE)) {
            return fourDirections(getHitEntry());
        } else if (currentFireDirection.equals(HitFireDirection.HORIZONTAL)) {
            return onlyHorizontal(getHitEntry());
        } else {
            return onlyVertical(getHitEntry());
        }
    }

    private int fourDirections(Map.Entry<Integer, HitState> entryWithHit) {
        boolean goingUpFirst = random.nextBoolean();
        int cellHit = entryWithHit.getKey();
        int nextShot;

        if (goingUpFirst) {
            if (canShootUp(cellHit)) {
                nextShot = cellHit - 1;
                checkAndPutNextShot(nextShot, false);
                return nextShot;
            } else if (canShootDown(cellHit)) {
                nextShot = cellHit + 1;
                checkAndPutNextShot(nextShot, false);
                return nextShot;
            } else if (canShootLeft(cellHit)) {
                nextShot = cellHit - 10;
                checkAndPutNextShot(nextShot, true);
                return nextShot;
            } else if (canShootRight(cellHit)) {
                nextShot = cellHit + 10;
                checkAndPutNextShot(nextShot, true);
                return nextShot;
            } else {
                entryWithHit.setValue(HitState.MISS);
                if (cpuAllShots.containsValue(HitState.HIT)) {
                    return fourDirections(getHitEntry());
                } else {
                    System.out.println("REACHED FOUR DIRECTIONS SAFE SEARCH MODE!");
                    return searchMode();
                }
            }

        } else {
            if (canShootLeft(cellHit)) {
                nextShot = cellHit - 10;
                checkAndPutNextShot(nextShot, true);
                return nextShot;
            } else if (canShootRight(cellHit)) {
                nextShot = cellHit + 10;
                checkAndPutNextShot(nextShot, true);
                return nextShot;
            } else if (canShootUp(cellHit)) {
                nextShot = cellHit - 1;
                checkAndPutNextShot(nextShot, false);
                return nextShot;
            } else if (canShootDown(cellHit)) {
                nextShot = cellHit + 1;
                checkAndPutNextShot(nextShot, false);
                return nextShot;
            } else {
                entryWithHit.setValue(HitState.MISS);
                if (cpuAllShots.containsValue(HitState.HIT)) {
                    return fourDirections(getHitEntry());
                } else {
                    System.out.println("REACHED FOUR DIRECTIONS SAFE SEARCH MODE!");
                    return searchMode();
                }
            }
        }

    }

    private int onlyHorizontal(Map.Entry<Integer, HitState> entryWithHit) {
        
        
        int cellHit = entryWithHit.getKey();

        int nextShot;

        if (canShootLeft(cellHit)) {
            nextShot = cellHit - 10;
            checkAndPutNextShot(nextShot, true);
            return nextShot;
        } else if (canShootRight(cellHit)) {
            nextShot = cellHit + 10;
            checkAndPutNextShot(nextShot, true);
            return nextShot;
        } else {
            entryWithHit.setValue(HitState.DEPLETED);
            if (cpuAllShots.containsValue(HitState.HIT)) {
                return onlyHorizontal(getHitEntry());
                
            } else if (cpuAllShots.containsValue(HitState.DEPLETED)){
                System.out.println("HORIZONTAL DEPLETED");
                cpuAllShots.entrySet().stream()
                        .filter(integerHitStateEntry -> integerHitStateEntry.getValue().equals(HitState.DEPLETED))
                        .forEach(integerHitStateEntry -> integerHitStateEntry.setValue(HitState.HIT));
                currentFireDirection = HitFireDirection.NONE;
                
                return fourDirections(getHitEntry());
            } else {
                System.out.println("REACHED HORIZONTAL SAFETY");
                //                just in case
                return searchMode();
            }
        }
    }

    private int onlyVertical(Map.Entry<Integer, HitState> entryWithHit) {
        
        
        int cellHit = entryWithHit.getKey();

        int nextShot;
        if (canShootUp(cellHit)) {
            nextShot = cellHit - 1;
            checkAndPutNextShot(nextShot, false);
            return nextShot;
        } else if (canShootDown(cellHit)) {
            nextShot = cellHit + 1;
            checkAndPutNextShot(nextShot, false);
            return nextShot;
        } else {
            entryWithHit.setValue(HitState.DEPLETED);
            if (cpuAllShots.containsValue(HitState.HIT)) {
                return onlyVertical(getHitEntry());
            } else if (cpuAllShots.containsValue(HitState.DEPLETED)){
                System.out.println("VERTICAL DEPLETED");
                cpuAllShots.entrySet().stream()
                        .filter(integerHitStateEntry -> integerHitStateEntry.getValue().equals(HitState.DEPLETED))
                        .forEach(integerHitStateEntry -> integerHitStateEntry.setValue(HitState.HIT));
                currentFireDirection = HitFireDirection.NONE;
                
                
                return fourDirections(getHitEntry());
            } else {
                System.out.println("REACHED VERTICAL SAFETY");
                //                just in case
                return searchMode();
            }
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


    private Map.Entry<Integer, HitState> getHitEntry() {
        return cpuAllShots.entrySet().stream()
                .filter(integerHitStateEntry -> integerHitStateEntry.getValue().equals(HitState.HIT))
                .findAny()
                .get();
    }

    private void checkAndPutNextShot(int nextShot, boolean horizontal) {
        System.out.println("NEXT SHOT " + nextShot);

        if (hitCheckDelegate(nextShot)) {
            System.out.println("HIT!");
            if (horizontal) {
                currentFireDirection = HitFireDirection.HORIZONTAL;
            } else {
                currentFireDirection = HitFireDirection.VERTICAL;
            }
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


    private enum HitFireDirection {
        NONE,
        HORIZONTAL,
        VERTICAL;
    }
}
