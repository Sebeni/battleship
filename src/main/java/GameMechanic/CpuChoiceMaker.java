package GameMechanic;

import UI.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CpuChoiceMaker {
    private boolean fireMode = false;
    private Game currentGame;
    private List<Integer> cpuChoices = new ArrayList<>();
    private Random random = new Random();


    public CpuChoiceMaker(Game currentGame) {
        this.currentGame = currentGame;
    }

    public int getCpuChoice() {
        if (!fireMode) {
            return huntMode();
        } else {
            return 0;
        }
    }

    private int huntMode() {
        int choice = checkBoardPicker();
        while (cpuChoices.contains(choice)) {
            choice = checkBoardPicker();
        }
        cpuChoices.add(choice);
        return choice;

    }

    private int checkBoardPicker() {
        int choice = random.nextInt(100);
        if ((choice / 10) % 2 == 0) {
            if (choice % 2 != 0 && choice > 0) {
                choice--;
            }
        } else {
            if (choice % 2 == 0){
                choice++;
            }
        }
        return choice;
    }
}
