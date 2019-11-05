package UI;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        debugOn(true);
        Game currentGame = new Game(primaryStage);
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(currentGame.getScene());
        
        primaryStage.show();
    }
    
    private void debugOn(boolean debugMode){
        Game.setDebug(debugMode);
    }
    
}
