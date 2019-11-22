package MenuUI;

import GameUI.SceneChanger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu implements SceneChanger {
    private Stage window;
    private Scene scene;
    private double windowWidth = 400;
    private double windowHeight = 600;
    
    private static MainMenu mainMenu;
    
    public static MainMenu getInstance(Stage primaryStage){
        if(mainMenu == null){
            mainMenu = new MainMenu(primaryStage);
        }
        return mainMenu;
    }


    private MainMenu(Stage primaryStage){
        window = primaryStage;

        window.setOnCloseRequest(e -> {
            e.consume();
            SceneChanger.closeProgram(window);
        });
        
        VBox layout = new VBox(20);

        layout.setPadding(new Insets(10, 50, 50, 50));

        Button newGame = new Button("New Game");
        newGame.setPrefSize(130,20);
        newGame.setOnAction(event -> SceneChanger.centerWindow(Options.getInstance(window)));

        Button about = new Button("About");
        about.setPrefSize(130,20);
        about.setOnAction(event -> SceneChanger.centerWindow(About.getInstance(window)));
        

        Button exit = new Button("Exit");
        exit.setPrefSize(130,20);
        exit.setOnAction(event -> SceneChanger.closeProgram(window));

        layout.getChildren().addAll(newGame, about, exit);
        layout.setAlignment(Pos.CENTER);


        layout.setStyle("-fx-background-image: url('submarinePhoto.jpg');" +
                "-fx-background-position: center center;" +
                " -fx-background-size: stretch;");

        scene = new Scene(layout, windowWidth, windowHeight);
        scene.getStylesheets().add("gameStyles.css");

        
        window.setScene(scene);
        window.show();

    }

    public Scene getScene() {
        return this.scene;
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public double getWindowHeight() {
        return windowHeight;
    }

    public Stage getWindow() {
        return window;
    }
}
