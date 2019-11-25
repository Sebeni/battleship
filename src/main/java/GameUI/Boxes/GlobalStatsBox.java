package GameUI.Boxes;

import GameUI.GridHelperMethods;

import Statistics.Stats;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GlobalStatsBox {
    private static List<Label> statisticsLabel = new ArrayList<>();
    static {
        for(int i = 0; i <= 100; i++){
            Label label = new Label("0");
            label.setTextAlignment(TextAlignment.CENTER);
            label.setAlignment(Pos.CENTER);
            label.setId("labelStats");
            label.setMinSize(50, 50);
            statisticsLabel.add(label);
        }
    }


    public GlobalStatsBox() {
        Stats.loadStats();
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Global statistics (updated after every round)");

        window.setMinWidth(600);


        GridPane gridPaneRoot = new GridPane();

        Label titleLabel = new Label("Global statistics");
        titleLabel.setStyle("-fx-font-size: 30");

        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setAlignment(Pos.CENTER);

        HBox title = new HBox(titleLabel);
        title.setAlignment(Pos.CENTER);

        gridPaneRoot.add(title, 0, 0, 2, 1);

        Label allRoundsNumLeft = new Label("Rounds played: ");
        Label allRoundsNumRight = new Label(Stats.getRoundCounter() + "");


        Label roundsWinLeft = new Label("Rounds win: ");
        Label roundsWinRight = new Label(Stats.getPlayerWonCounter() + "");


        Label roundsLostLeft = new Label("Rounds lost: ");
        Label roundsLostRight = new Label(Stats.getPlayerLoseCounter() + "");


        Label allShotsFiredLeft = new Label("Number of shots fired: ");
        Label allShotsFiredRight = new Label(Stats.getHumanAllShotsNum() + "");


        Label shotsHitLeft = new Label("Number of shots hit: ");
        Label shotsHitRight = new Label(Stats.getHumanAllSuccessfulShots() + "");


        Label shotsMissedLeft = new Label("Number of shots missed: ");
        Label shotsMissedRight = new Label(Stats.getHumanAllMissedShots() + "");

        Label accuracyLeft = new Label("Accuracy: ");
        double accuracy = (double) Stats.getHumanAllSuccessfulShots() / Stats.getHumanAllShotsNum() * 100;
        Label accuracyRight = new Label(String.format("%.2f %%", accuracy));


        Label allCpuShotsLeft = new Label("Number of CPU's shots fired: ");
        Label allCpuShotsRight = new Label(Stats.getCpuAllShotsNum() + "");

        Label shotsCpuHitLeft = new Label("Number of CPU's shots hit: ");
        Label shotsCpuHitRight = new Label(Stats.getCpuAllSuccessfulShots() + "");

        Label shotsCpuMissedLeft = new Label("Number of CPU's shots missed: ");
        Label shotsCpuMissedRight = new Label(Stats.getCpuAllMissedShots() + "");

        Label accuracyCpuLeft = new Label("CPU's accuracy: ");
        double cpuAccuracy = (double) Stats.getCpuAllSuccessfulShots() / Stats.getCpuAllShotsNum() * 100;
        Label accuracyCpuRight = new Label(String.format("%.2f %%", cpuAccuracy));

        Label gridChoiceBoxLeft = new Label("Show cells: ");
        ChoiceBox<String> gridChoiceBoxRight = new ChoiceBox<>();
        
        final String hitByYou = "successfully hit by you";
        final String missedByYou = "missed by you";
        final String hitByCpu = "successfully hit by cpu";
        final String missedByCpu = "missed by cpu";
        
        gridChoiceBoxRight.getItems().addAll(hitByYou, missedByYou, hitByCpu, missedByCpu);
        gridChoiceBoxRight.setMinWidth(200);
        
        gridChoiceBoxRight.setOnAction(event -> {
            String currentChoice = gridChoiceBoxRight.getValue();
            
            switch(currentChoice) {
                case hitByYou :
                    changeStatsGrid(Stats.getHumanSuccessfulShots());
                    break;
                case missedByYou :
                    changeStatsGrid(Stats.getHumanMissedShots());
                    break;
                case hitByCpu :
                    changeStatsGrid(Stats.getCpuSuccessfulShots());
                    break;
                case missedByCpu :
                    changeStatsGrid(Stats.getCpuMissedShots());
                    break;
            }
        });

        Node[] left = {allRoundsNumLeft, roundsWinLeft, roundsLostLeft, allShotsFiredLeft, shotsHitLeft, shotsMissedLeft, accuracyLeft, allCpuShotsLeft, shotsCpuHitLeft, shotsCpuMissedLeft, accuracyCpuLeft, gridChoiceBoxLeft};
        Node[] right = {allRoundsNumRight, roundsWinRight, roundsLostRight, allShotsFiredRight, shotsHitRight, shotsMissedRight, accuracyRight, allCpuShotsRight, shotsCpuHitRight, shotsCpuMissedRight, accuracyCpuRight, gridChoiceBoxRight};


        for (int i = 0; i < left.length; i++) {
            gridPaneRoot.add(left[i], 0, i + 1);
            gridPaneRoot.setAlignment(Pos.CENTER);
        }

        for (int i = 0; i < right.length; i++) {
            gridPaneRoot.add(right[i], 1, i + 1);
            gridPaneRoot.setAlignment(Pos.CENTER);
        }
        
        GridPane statistics = makeStatisticsGrid();
        gridPaneRoot.add(statistics, 0, left.length + 1, 2, 1);
        gridPaneRoot.setAlignment(Pos.CENTER);
        
        gridPaneRoot.setHgap(10);
        gridPaneRoot.setVgap(10);

        ScrollPane scrollPane = new ScrollPane(gridPaneRoot);
        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        scrollPane.setPadding(new Insets(10));

        Scene scene = new Scene(scrollPane);
        scene.getStylesheets().add("gameStyles.css");

        window.setScene(scene);
        window.showAndWait();
    }


    private GridPane makeStatisticsGrid() {
        GridPane statistics = new GridPane();
        int counter = 0;
        for (int column = 0; column < 10; column++) {
            for (int row = 0; row < 10; row++) {
                statistics.add(statisticsLabel.get(counter), column, row);
                counter++;
            }
        }
        statistics.setAlignment(Pos.CENTER);
        
        GridPane root = new GridPane();
        root.add(statistics, 1, 1, 10, 10);
        Double minSize = statisticsLabel.get(0).getMinHeight();
        GridHelperMethods.gridMarkers(root, minSize, minSize);
        
        return root;
    }
    
    private void changeStatsGrid(Map<Integer, Integer> mapToLabel){
        mapToLabel.entrySet().forEach(mapEntry -> statisticsLabel.get(mapEntry.getKey()).setText(mapEntry.getValue() + ""));
        changeColors();
    }
    
    
    private void changeColors(){
        int maxValue = currentMaxValue();
        
        statisticsLabel.forEach(label -> {
            double percentage = Double.parseDouble(label.getText()) / maxValue * 100;
            if(percentage <= 25){
                label.setId("25");
            } else if (percentage <= 50){
                label.setId("50");
            } else if (percentage <= 75){
                label.setId("75");
            } else {
                label.setId("100");
            }
        });
        
    }
    
    private static int currentMaxValue(){
        return statisticsLabel.stream()
                .map(label -> label.getText())
                .mapToInt(value -> Integer.parseInt(value))
                .max()
                .orElse(1);
    }



    
}
