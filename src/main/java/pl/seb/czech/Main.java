package pl.seb.czech;

import pl.seb.czech.GameUI.SceneChanger;
import javafx.application.Application;
import javafx.stage.Stage;
import pl.seb.czech.MenuUI.MainMenu;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Naval battles");
        SceneChanger mainMenu = MainMenu.getInstance(primaryStage);
        primaryStage.setScene(mainMenu.getScene());
        primaryStage.show();
    }
}
