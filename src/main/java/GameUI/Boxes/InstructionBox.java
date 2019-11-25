package GameUI.Boxes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

class InstructionBox {
    
    static void display(boolean firePhase){
        String instructionPhaseOne = "PHASE ONE - SHIP PLACEMENT" +
                "\n\nBefore play begins, you must arranges all your ships on your bottom grid." +
                " Each ship occupies a number of consecutive squares on the grid, arranged either horizontally or vertically." +
                " The number of squares for each ship is determined by the type of the ship. " +
                " The ships cannot overlap (i.e., only one ship can occupy any given square in the grid)." +
                " To start placing your ships click on one of the green buttons and then start placing ship parts on " +
                "the blue grid by clicking on chosen cell." +
                " You can also click on Random Placement button to randomly place all your ships.";

        String instructionPhaseTwo = "PHASE TWO - FIRING" +
                "\n\nTo fire click on the top grid." +
                " If you hit enemy's ship selected cell will change color to yellow." +
                " If you sunk enemy's ship all cells, where this ship was placed will change color to red." +
                " If you miss cell will become white." +
                "\nHappy hunting!";
        
        String windowTitle = firePhase ? "PHASE TWO - FIRING" : "PHASE ONE - SHIP PLACEMENT";
        
        
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(windowTitle);
        window.setMinWidth(500);
        window.setMinHeight(200);

        Label label = new Label();
        if(!firePhase){
            label.setText(instructionPhaseOne);
        } else {
            label.setText(instructionPhaseTwo);
        }

        label.setTextAlignment(TextAlignment.JUSTIFY);
        label.setPrefWidth(450);
        label.setWrapText(true);


        VBox pane = new VBox(10);
        Button button = new Button("OK");
        button.setId("newGameButtons");

        pane.getChildren().addAll(label, button);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(20));
        pane.setSpacing(20);
        button.setOnAction(e -> window.close());

        Scene layout = new Scene(pane);
        layout.getStylesheets().add("gameStyles.css");
        window.setScene(layout);

        window.showAndWait();
    }
    
}
