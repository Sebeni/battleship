package GameMechanic;

import GameUI.Game;
import GameUI.GridHelperMethods;

import java.util.*;

public class CpuChoiceMaker {

    private Game game;
   
    private HitFireDirection currentFireDirection = HitFireDirection.NONE;

    private Map<Integer, HitState> cpuAllShots = new LinkedHashMap<>();

    private List<Integer> whiteFields = new ArrayList<>();
    private List<Integer> blackFields = new ArrayList<>();

    private List<Integer> currentChoiceList;


    private Random random = new Random();


    public CpuChoiceMaker(Game game) {
        this.game = game;
  
        populateWhiteAndBlackFields();

        currentChoiceList = random.nextBoolean() ? whiteFields : blackFields;
    }

    private void populateWhiteAndBlackFields() {
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
    }

    public int getCpuChoice() {
        removeImpossibilitiesFromChoices();
        
        if (!cpuAllShots.containsValue(HitState.HIT)) {
            currentFireDirection = HitFireDirection.NONE;
            return searchMode();
        } else {
            return fireMode();
        }
    }

    private int searchMode() {
        int choice = checkerboardAlgorithm();
        
        if (hitCheckDelegate(choice)) {
            cpuAllShots.put(choice, HitState.HIT);
        } else {
            cpuAllShots.put(choice, HitState.MISS);
        }
        return choice;
    }

    private void removeImpossibilitiesFromChoices() {
        if (!cpuAllShots.isEmpty()) {
            cpuAllShots.keySet().forEach(integer -> {
                currentChoiceList.remove(integer);
                getAnotherList().remove(integer);
            });
        }

        currentChoiceList.removeIf(integer -> !canShortestShipFit(integer));
        getAnotherList().removeIf(integer -> !canShortestShipFit(integer));
    }

    private int checkerboardAlgorithm() {
        int choice;
        if (!currentChoiceList.isEmpty()) {
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
            return onlyHorizontalHits(getHitEntry());
        } else {
            return onlyVerticalHits(getHitEntry());
        }
    }

    private int fourDirections(Map.Entry<Integer, HitState> entryWithHit) {
        int cellHit = entryWithHit.getKey();
        boolean goingVerticalFirst = random.nextBoolean();
        
        if (goingVerticalFirst && !canFitShipUpOrDown(cellHit)) {
            goingVerticalFirst = false;
        }

        if (!goingVerticalFirst && !canFitShipLeftOrRight(cellHit)) {
            goingVerticalFirst = true;
        }

        int nextShot;

        if (goingVerticalFirst) {
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

    private int onlyHorizontalHits(Map.Entry<Integer, HitState> entryWithHit) {
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
                return onlyHorizontalHits(getHitEntry());
            } else if (cpuAllShots.containsValue(HitState.DEPLETED)) {
                resetDepletedToHit();
                return fourDirections(getHitEntry());
            } else {
                return searchMode();
            }
        }
    }

    private int onlyVerticalHits(Map.Entry<Integer, HitState> entryWithHit) {
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
                return onlyVerticalHits(getHitEntry());
            } else if (cpuAllShots.containsValue(HitState.DEPLETED)) {
                resetDepletedToHit();
                return fourDirections(getHitEntry());
            } else {
                return searchMode();
            }
        }
    }

    private void resetDepletedToHit() {
        cpuAllShots.entrySet().stream()
                .filter(integerHitStateEntry -> integerHitStateEntry.getValue().equals(HitState.DEPLETED))
                .forEach(integerHitStateEntry -> integerHitStateEntry.setValue(HitState.HIT));
        currentFireDirection = HitFireDirection.NONE;
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
        return GridHelperMethods.hitCheck(game.getHuman(), choiceToCheck);
    }

    public Map<Integer, HitState> getCpuAllShots() {
        return cpuAllShots;
    }


    private enum HitFireDirection {
        NONE,
        HORIZONTAL,
        VERTICAL;
    }

    private List<Integer> getAnotherList() {
        return currentChoiceList == whiteFields ? blackFields : whiteFields;
    }

    private int getShortestLivingShipNum() {
        return game.getHuman().getShipsList().stream()
                .filter(ship -> ship.getShipPartsInGameCount() == Ship.getAllShips().get(ship.getName()))
                .map(ship -> ship.getShipPartsInGameCount())
                .mapToInt(value -> value)
                .min()
                .orElse(2);
    }

    private boolean canShortestShipFit(int cellToCheck) {
        int shortestLivingShip = getShortestLivingShipNum();

        List<Integer> horizontal = new ArrayList<>();
        horizontal.add(cellToCheck);
        List<Integer> vertical = new ArrayList<>();
        vertical.add(cellToCheck);

        horizontalPossibilities(horizontal);
        verticalPossibilities(vertical);

        return horizontal.size() >= shortestLivingShip || vertical.size() >= shortestLivingShip;

    }

    private void horizontalPossibilities(List<Integer> horizontal) {

        int theMostLeft = horizontal.get(0);

        if (canShootLeft(theMostLeft)) {
            horizontal.add(0, theMostLeft - 10);
            horizontalPossibilities(horizontal);
        }

        int theMostRight = horizontal.get(horizontal.size() - 1);

        if (canShootRight(theMostRight)) {
            horizontal.add(theMostRight + 10);
            horizontalPossibilities(horizontal);
        }


    }

    private void verticalPossibilities(List<Integer> vertical) {
        int theMostUp = vertical.get(0);

        if (canShootUp(theMostUp)) {
            vertical.add(0, theMostUp - 1);
            verticalPossibilities(vertical);
        }

        int theMostDown = vertical.get(vertical.size() - 1);

        if (canShootDown(theMostDown)) {
            vertical.add(theMostDown + 1);
            verticalPossibilities(vertical);
        }
    }

    private boolean canFitShipUpOrDown(int cellHit) {
        boolean currentChoicesUpOrDown = currentChoiceList.contains(cellHit - 1) || currentChoiceList.contains(cellHit + 1);
        boolean otherChoicesUpOrDown = getAnotherList().contains(cellHit - 1) || getAnotherList().contains(cellHit + 1);

        return currentChoicesUpOrDown || otherChoicesUpOrDown;
    }

    private boolean canFitShipLeftOrRight(int cellHit) {
        boolean currentChoicesLeftOrRight = currentChoiceList.contains(cellHit - 10) || currentChoiceList.contains(cellHit + 10);
        boolean otherChoicesLeftOrRight = getAnotherList().contains(cellHit - 10) || getAnotherList().contains(cellHit + 10);

        return currentChoicesLeftOrRight || otherChoicesLeftOrRight;
    }
}
