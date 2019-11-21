package MenuUI;

import GameUI.AfterClick;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class About implements AfterClick {
    private Scene scene;
    private Stage window;
    private static About about;
    private double windowWidth = 700;
    private double windowHeight = 800;

    public static About getInstance(Stage primaryStage){
        if(about == null){
            about = new About(primaryStage);
        }
        return about;
    }
    
    private About(Stage primaryStage){
        window = primaryStage;


        Label about = new Label("text source: Wikipedia\n" +
                "Battleship (also Battleships or Sea Battle) is a strategy type guessing game for two players. " +
                "It is played on ruled grids (paper or board) on which each player's fleet of ships " +
                "(including battleships) are marked. The locations of the fleets are concealed from " +
                "the other player. Players alternate turns calling \"shots\" at the other player's ships, " +
                "and the objective of the game is to destroy the opposing player's fleet.\n" +
                "\n" +
                "Battleship is known worldwide as a pencil and paper game which dates from World War I. " +
                "It was published by various companies as a pad-and-pencil game in the 1930s, and was released " +
                "as a plastic board game by Milton Bradley in 1967. The game has spawned electronic versions, " +
                "video games, smart device apps and a film.");

        about.setId("aboutLabel");
        about.setWrapText(true);
        about.setPadding(new Insets(10));
        about.setTextAlignment(TextAlignment.JUSTIFY);
        about.setMaxSize(windowWidth - 40, windowHeight - 40);
        
        Button returnButton = new Button("Return");
        returnButton.setMinSize(130,20);
        
        returnButton.setOnAction(event -> AfterClick.centerWindow(MainMenu.getInstance(window)));



        VBox layout = new VBox(20, about, returnButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(  "-fx-background-image: url('submarinePhoto.jpg');" +
        "-fx-background-repeat: stretch;" +
        "-fx-background-position: center center;");
        
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

    @Override
    public double getWindowWidth() {
        return windowWidth;
    }

    @Override
    public double getWindowHeight() {
        return windowHeight;
    }
}
