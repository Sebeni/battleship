package GameMechanic;

import UI.Game;
import UI.GridPaneButtonMethods;

import java.util.*;

public class CpuChoiceMaker {

    private Game game;
    private GridPaneButtonMethods gridMethods;
    private HitFireDirection currentFireDirection = HitFireDirection.NONE;

    private Map<Integer, HitState> cpuAllShots = new LinkedHashMap<>();

    private List<Integer> whiteFields = new ArrayList<>();
    private List<Integer> blackFields = new ArrayList<>();
    
    private List<Integer> currentChoiceList;
    

    private Random random = new Random();


    public CpuChoiceMaker(Game game, GridPaneButtonMethods gridMethods) {
        this.game = game;
        this.gridMethods = gridMethods;

        for (int i = 0; i < 100; i++) {
            if ((i / 10) % 2 == 0) {

                if (i % 2 == 0) {
                    whiteFields.add(i);
                } else {
                    blackFields.add(i);
                }

            } else {

                if (i % 2 == 0) {
                    blackFields.add(i);
                } else {
                    whiteFields.add(i);
                }
            }
        }
        
        currentChoiceList = random.nextBoolean() ? whiteFields : blackFields;
    }

    public int getCpuChoice() {
        
        if (!cpuAllShots.containsValue(HitState.HIT)) {
            currentFireDirection = HitFireDirection.NONE;
            return searchMode();

        } else {
      
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
        } else {
            cpuAllShots.put(choice, HitState.MISS);
        }
        return choice;
    }

    private int checkerboardAlgorithm() {
        int choice; 
        if(!currentChoiceList.isEmpty()){
            choice = currentChoiceList.get(random.nextInt(currentChoiceList.size()));
            currentChoiceList.remove((Integer) choice);
        } else {
            choice = getAnotherList().get(random.nextInt(currentChoiceList.size()));
            getAnotherList().remove((Integer) choice);
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

            } else if (cpuAllShots.containsValue(HitState.DEPLETED)) {
                cpuAllShots.entrySet().stream()
                        .filter(integerHitStateEntry -> integerHitStateEntry.getValue().equals(HitState.DEPLETED))
                        .forEach(integerHitStateEntry -> integerHitStateEntry.setValue(HitState.HIT));
                currentFireDirection = HitFireDirection.NONE;

                return fourDirections(getHitEntry());
            } else {
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
            } else if (cpuAllShots.containsValue(HitState.DEPLETED)) {
            
                cpuAllShots.entrySet().stream()
                        .filter(integerHitStateEntry -> integerHitStateEntry.getValue().equals(HitState.DEPLETED))
                        .forEach(integerHitStateEntry -> integerHitStateEntry.setValue(HitState.HIT));
                currentFireDirection = HitFireDirection.NONE;


                return fourDirections(getHitEntry());
            } else {
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

        if (hitCheckDelegate(nextShot)) {
            if (horizontal) {
                currentFireDirection = HitFireDirection.HORIZONTAL;
            } else {
                currentFireDirection = HitFireDirection.VERTICAL;
            }
            cpuAllShots.put(nextShot, HitState.HIT);
        } else {
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
    
    private List<Integer> getAnotherList(){
        return currentChoiceList == whiteFields ? blackFields : whiteFields;
    }
    
    
    
}
