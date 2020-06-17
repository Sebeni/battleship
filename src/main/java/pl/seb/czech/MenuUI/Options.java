package pl.seb.czech.MenuUI;

import pl.seb.czech.GameUI.SceneChanger;
import pl.seb.czech.GameUI.Boxes.Game;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Options implements SceneChanger {
    private final Scene scene;
    private final Stage window;
    private final double windowWidth = 450;
    private final double windowHeight = 400;
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

        Label canTouchLeft = new Label("Can ships touch each other?");
        canTouchLeft.setId("aboutLabel");
        
        ChoiceBox<String> canTouchChoiceRight = new ChoiceBox<>();
        String yes = "yes";
        String no = "no";
        canTouchChoiceRight.setMinWidth(100);
        
        canTouchChoiceRight.getItems().addAll(yes, no);
        canTouchChoiceRight.setValue(yes);
        
        HBox canTouchContainer = new HBox(10, canTouchLeft, canTouchChoiceRight);
        canTouchContainer.setAlignment(Pos.CENTER);
        
        Button startGameButton = new Button("Start");
        startGameButton.setPrefSize(130,20);
        startGameButton.setOnAction(event -> {
            Game.setShipsCanTouch(canTouchChoiceRight.getValue().equals(yes));
            Game game = new Game(window);
            SceneChanger.centerWindow(game);
        });
        
        Button returnButton = new Button("Return");
        returnButton.setPrefSize(130,20);
        returnButton.setOnAction(event -> SceneChanger.centerWindow(MainMenu.getInstance(window)));
        
        VBox layout = new VBox(10, canTouchContainer, startGameButton, returnButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-image: url('submarinePhoto.jpg');" +
                "-fx-background-position: center center;" +
                " -fx-background-size: stretch;");
        

        scene = new Scene(layout, windowWidth, windowHeight);
        scene.getStylesheets().add("gameStyles.css");
        

        
        
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
