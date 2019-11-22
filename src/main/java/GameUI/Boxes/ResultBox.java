package GameUI.Boxes;

import GameMechanic.Ship;
import GameUI.Handlers.ButtonHandlers;
import GameUI.SceneChanger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class ResultBox {
    
    public static void display(boolean humanWin, Game currentGame){
        int remainingShipParts = currentGame.getCpu().getShipsList().stream()
                .map(ship -> ship.getShipPartsInGameCount())
                .mapToInt(value -> value)
                .sum();
        
        int successfulShotsNum = Ship.getAllShipsParts() - remainingShipParts;
        
        double accuracy = (double) successfulShotsNum / currentGame.getRoundCounter() * 100; 
        
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Results");
        window.initStyle(StageStyle.UNDECORATED);
        
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(30, 20, 30, 20));
        
        Label resultLabel = new Label();
        resultLabel.setPadding(new Insets(5));

        if(humanWin){
            resultLabel.setText("CONGRATULATIONS! \nYOU WON!");
            resultLabel.setStyle("-fx-text-fill: green; " +
                    "-fx-font-size:20;" +
                    "-fx-font-family: 'Courier New'");
        } else {
            resultLabel.setText("SORRY! \nYOU LOST!");
            resultLabel.setStyle("-fx-text-fill: red; " +
                    "-fx-font-size:20;" +
                    "-fx-font-family: 'Courier New'");
        }
        
        

        resultLabel.setTextAlignment(TextAlignment.CENTER);
        resultLabel.setAlignment(Pos.CENTER);
        
        HBox top = new HBox(10, resultLabel);
        top.setAlignment(Pos.CENTER);

        layout.add(top, 0, 0, 2, 1);
        
        List<Label> leftLabels = new ArrayList<>();
        List<Label> rightLabels = new ArrayList<>();
        
        Label roundsLabel = new Label("Number of rounds:");
        leftLabels.add(roundsLabel);
        
        Label numbOfRounds = new Label(currentGame.getRoundCounter() + "");
        rightLabels.add(numbOfRounds);
        
        Label humanShotsSuccessLabel = new Label("Shots hit target:");
        leftLabels.add(humanShotsSuccessLabel);
        
        Label successfulShotsNumLabel = new Label(successfulShotsNum + "");
        rightLabels.add(successfulShotsNumLabel);
        
        Label successRateLabel = new Label("Accuracy:");
        leftLabels.add(successRateLabel);
        
        Label accuracyNum = new Label(String.format("%.2f%%", accuracy));
        rightLabels.add(accuracyNum);
        
        for(int i = 0; i < leftLabels.size(); i++){
            leftLabels.get(i).setStyle("-fx-font-family: 'Courier New'");
            layout.add(leftLabels.get(i), 0, i + 1);
        }
        
        for(int i = 0; i < rightLabels.size(); i++){
            rightLabels.get(i).setStyle("-fx-font-family: 'Courier New'");
            layout.add(rightLabels.get(i), 1, i + 1);
        }

        ButtonHandlers bh = new ButtonHandlers(currentGame);
        
        List<Button> buttonList = new ArrayList<>();
        
        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(event -> {
            window.close();
            bh.resetGameButtonEH(event);
        });
        buttonList.add(newGameButton);
        
        Button showCpuVisual = new Button("Show CPU's board");
        showCpuVisual.setOnAction(event -> {
            currentGame.getCpuVisualBox().getWindow().show();
        });
        buttonList.add(showCpuVisual);
        
        Button exit = new Button("Exit");
        exit.setOnAction(event -> {
            SceneChanger.closeProgram(currentGame.getWindow(), currentGame.getCpuVisualBox().getWindow(), window);
        });
        buttonList.add(exit);

        VBox buttonsPane = new VBox(5);
        buttonsPane.setPadding(new Insets(10));
        
        buttonList.forEach(button -> {
            button.setPrefSize(180, 30);
            button.setStyle("-fx-font-family: 'Courier New'");
            buttonsPane.getChildren().add(button);
            button.setAlignment(Pos.CENTER);
        });
        
        buttonsPane.setAlignment(Pos.CENTER);
        
        layout.add(buttonsPane, 0, 6, 2, 1);
        
        layout.setAlignment(Pos.BASELINE_CENTER);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);

        window.showAndWait();
    }
    
    
}