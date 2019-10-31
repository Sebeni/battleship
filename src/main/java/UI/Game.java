package UI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static Game game;
    private Stage window;
    private Scene scene;
    private double width = 800;
    private double height = 1000;
    
    //buttons are added in order; so 35 button equals to coordinates (3, 5)
    private List<Button> fireButtonList = new ArrayList<>();

    public static Game getInstance(Stage primaryStage){
        if(game == null) {
            game = new Game(primaryStage);
        }
        return game;
    }

    private Game(Stage primaryStage) {
        window = primaryStage;

        window.setOnCloseRequest(e -> {
            e.consume();
            AfterClick.closeProgram(window);
        });
        
        //root pane
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(50, 50, 50, 50));
        
        GridPane playerFirePane = new GridPane();
        
        int counter = 0;
        
        
        for(int y = 0; y < 10; y++){
            for (int x = 0; x < 10; x++){
                //text on buttons only for debug
                Button button = new Button("" + counter++);
                button.setMinSize(30,30);
                button.setPrefSize(40,40);
                playerFirePane.add(button, y, x);
                fireButtonList.add(button);
                
                button.setOnAction(event -> {
                    Integer xParam = GridPane.getColumnIndex(button);
                    Integer yParam = GridPane.getRowIndex(button);
                    System.out.println("X " + xParam + " Y " + yParam + " button " + event.getSource());
                });
            }
        }

        System.out.println(fireButtonList.get(35));
        
        

        layout.setCenter(playerFirePane);
        
        
        
        

        //exit button
        Button exit = new Button("Exit");
        exit.setPrefSize(130,20);
        exit.setOnAction(event -> AfterClick.closeProgram(window));
        layout.setBottom(exit);
        
        
        
        //setting scene
        
        scene = new Scene(layout, width, height);

    }

    public Scene getScene() {
        return this.scene;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Stage getWindow() {
        return window;
    }

}