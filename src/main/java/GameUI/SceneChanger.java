package GameUI;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public interface SceneChanger {
    //this interface gives methods common to all scenes and stages

    //changes AND center on the screen new scene
    static void centerWindow(SceneChanger newSceneToShow){
        Stage window = newSceneToShow.getWindow();
        Rectangle2D screenBoundsHome = Screen.getPrimary().getVisualBounds();
        window.setX(((screenBoundsHome.getWidth()) - newSceneToShow.getWindowWidth()) / 2);
        window.setY(((screenBoundsHome.getHeight()) - newSceneToShow.getWindowHeight()) / 2);
        window.setScene(newSceneToShow.getScene());
    }
    
    double getWindowWidth();
    double getWindowHeight();
    Stage getWindow();
    Scene getScene();

    //shows exit popup window
    static void closeProgram(Stage... windows) {
        boolean closing = ConfirmBox.display("Warning", "Do you wish to quit?");
        if(closing){
            for(Stage window : windows){
                window.close();
            }
        }
    }
    
}

 
