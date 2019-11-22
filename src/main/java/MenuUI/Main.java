package MenuUI;

import GameUI.SceneChanger;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Battleship");
        SceneChanger mainMenu = MainMenu.getInstance(primaryStage);
        primaryStage.setScene(mainMenu.getScene());
        primaryStage.show();
    }
    
    
}
