package MenuUI;

import GameUI.SceneChanger;
import GameUI.Game;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Options implements SceneChanger {
    private Scene scene;
    private Stage window;
    private double windowWidth = 450;
    private double windowHeight = 400;
    private static Options options;
    
    public static Options getInstance(Stage primaryStage){
        if(options == null){
            options = new Options(primaryStage);
        }
        return options;
    }
    
    
    private Options(Stage primaryStage){
        window = primaryStage;

        window.setOnCloseRequest(e -> {
            e.consume();
            SceneChanger.closeProgram(window);
        });

        Button startGameButton = new Button("Start");
        startGameButton.setPrefSize(130,20);
        startGameButton.setOnAction(event -> SceneChanger.centerWindow(new Game(window)));


        Button returnButton = new Button("Return");
        returnButton.setPrefSize(130,20);
        returnButton.setOnAction(event -> SceneChanger.centerWindow(MainMenu.getInstance(window)));

        VBox layout = new VBox(10, startGameButton, returnButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-image: url('submarinePhoto.jpg');" +
                "-fx-background-position: center center;" +
                " -fx-background-size: stretch;");
        

        scene = new Scene(layout, windowWidth, windowHeight);
        
        window.setScene(scene);
        window.show();
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public Stage getWindow() {
        return window;
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public double getWindowHeight() {
        return windowHeight;
    }
}
