package UI;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Game game = Game.getInstance(primaryStage);
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(game.getScene());
        primaryStage.show();
    }
}
