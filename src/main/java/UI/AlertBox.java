package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    
    public static void display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(500);
        window.setMinHeight(200);

        Label label = new Label(message);
      
        label.setTextAlignment(TextAlignment.CENTER);
        label.setWrapText(true);
      
        
        VBox pane = new VBox(10);
        Button button = new Button("OK");
        button.setPrefSize(130, 20);
        
        pane.getChildren().addAll(label, button);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(20));
        pane.setSpacing(20);
        button.setOnAction(e -> window.close());

        Scene layout = new Scene(pane);
        window.setScene(layout);

        window.showAndWait();
    }
}
