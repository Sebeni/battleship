package UI;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ResultBox {
//    
//    public static void display(String title, String playerChoice, String cpuChoice, String result, int roundNum, Stage primaryStage, int playerWins, int cpuWins){
//        Stage window = new Stage();
//
//        window.initModality(Modality.APPLICATION_MODAL);
//        
//        window.setTitle(title);
//        window.setMinWidth(500);
//        window.setMinHeight(200);
//
//        Label playerChoiceLabel = new Label(playerChoice);
//        Label cpuChoiceLabel = new Label(cpuChoice);
//        HBox top = new HBox(50);
//        top.getChildren().addAll(playerChoiceLabel, cpuChoiceLabel);
//        top.setAlignment(Pos.CENTER);
//        
//        Label resultLabel = new Label(result);
//        if(result.contains("You won")){
//            resultLabel.setStyle("-fx-text-fill: green; " +
//                    "-fx-font-size:20");
//        } else if(result.contains("You lost")){
//            resultLabel.setStyle("-fx-text-fill: red;" +
//                    "-fx-font-size:20");
//        } else {
//            resultLabel.setStyle("-fx-text-fill: blue;" +
//                    "-fx-font-size:20");
//        }
//        
//        VBox root = new VBox(10);
//        
//        Button buttonContinueReturn = new Button();
//        buttonContinueReturn.setMinSize(200,20);
//
//        //final pane
//        VBox finalResult = new VBox(10);
//        
//        
//        //checking num of rounds
//        if(roundNum <= Options.getNumOfRounds()){
//            buttonContinueReturn.setText("Continue");
//            buttonContinueReturn.setOnAction(e -> window.close());
//        } else {
//            window.initStyle(StageStyle.UNDECORATED);
//            window.setOnCloseRequest(Event::consume);
//            
//            root.setStyle("-fx-border-color: darkgrey;" +
//                    "-fx-border-width: 5");
//            buttonContinueReturn.setText("Return to options");
//            buttonContinueReturn.setOnAction(e -> {
//                AfterClick instance = Options.getInstance(primaryStage);
//                AfterClick.centerWindow(instance);
//                window.close();
//            });
//
//            //first pane
//            Label gameOver = new Label("GAME OVER");
//            gameOver.setStyle("-fx-font-size:20;" +
//                    "-fx-font-family:Verdana");
//            
//            
//            //second pane
//            HBox winsCompare = new HBox(10);
//            Label yourWinsLabel = new Label("Your wins: " + playerWins);
//            Label cpuWinsLabel = new Label("CPU wins: " + cpuWins);
//            winsCompare.getChildren().addAll(yourWinsLabel, cpuWinsLabel);
//            winsCompare.setAlignment(Pos.CENTER);
//            
//            //last pane
//            Label finalResultCompare = new Label();
//            
//            if(playerWins == cpuWins){
//                finalResultCompare.setText("DRAW");
//                finalResultCompare.setStyle("-fx-font-size:20;" +
//                        "-fx-font-family:Verdana;" +
//                        "-fx-font-weight: bold;" +
//                        "-fx-text-fill: blue");
//            }
//            else if(playerWins > cpuWins){
//                finalResultCompare.setText("CONGRATULATIONS! YOU WON THE GAME!");
//                finalResultCompare.setStyle("-fx-font-size:20;" +
//                        "-fx-font-family:Verdana;" +
//                        "-fx-font-weight: bold;" +
//                        "-fx-text-fill: green");
//            } else {
//                finalResultCompare.setText("YOU LOST THE GAME! BETTER LUCK NEXT TIME");
//                finalResultCompare.setStyle("-fx-font-size:20;" +
//                        "-fx-font-family:Verdana;" +
//                        "-fx-font-weight: bold;" +
//                        "-fx-text-fill: red");
//            }
//            
//            Button buttonRestart = new Button("Restart");
//            buttonRestart.setMinSize(200,20);
//            buttonRestart.setOnAction(e -> {
//                AfterClick instance = new Game(primaryStage);
//                AfterClick.centerWindow(instance);
//                window.close();
//            });
//            
//            Button buttonQuit = new Button("Quit");
//            buttonQuit.setMinSize(200,20);
//            buttonQuit.setOnAction(event -> {
//                AfterClick.closeProgram(primaryStage, window);
//            });
//            
//            finalResult.getChildren().addAll(gameOver, winsCompare, finalResultCompare, buttonRestart, buttonQuit);
//            finalResult.setAlignment(Pos.CENTER);
//        }
//        
//        root.getChildren().addAll(top, resultLabel, finalResult, buttonContinueReturn);
//        root.setAlignment(Pos.CENTER);
//        root.setPadding(new Insets(50));
//        
//        Scene layout = new Scene(root);
//        window.setScene(layout);
//
//        window.showAndWait();
//    }
}
