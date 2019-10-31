package GameMechanic;

import UI.Game;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Class created to clean up Game class, provides methods that returns lists of buttons
 */
public class ButtonListCreator {

    /**
     * This method creates 100 buttons to populate player Fire Pane Grid (place where player puts his fire choices)
     * also adds them to the specific grid pane playerFirePaneTop (top)
     *
     * @return populated list of fire buttons - buttons are added in order; for example #35 button equals to coordinates (3, 5)
     */
    public static List<Button> createFireButtonList() {
        List<Button> fireButtonList = new ArrayList<>();
        GridPane playerFirePane = Game.getPlayerFirePaneTop();
        int fireButtonCounter = 0;

        for (int column = 0; column < 10; column++) {
            for (int row = 0; row < 10; row++) {
                //text on buttons only for debug
                Button button = new Button();
                //removing blue border from next button
                button.setStyle("-fx-focus-traversable: false;");

                if (Game.debug) {
                    button.setText("" + fireButtonCounter);
                    fireButtonCounter++;
                }

                button.setMinSize(30, 30);
                button.setPrefSize(40, 40);
                playerFirePane.add(button, column, row);
                fireButtonList.add(button);

                button.setOnAction(event -> {
                    Integer xParam = GridPane.getColumnIndex(button);
                    Integer yParam = GridPane.getRowIndex(button);
                    if (Game.debug) {
                        System.out.println("X " + xParam + " Y " + yParam + " button " + event.getSource());
                    }

                    button.setDisable(true);
                    //TODO
                    //changing color accordingly to cpu ship placement for now red
                    button.setStyle("-fx-background-color: red");

                });
            }
        }
        return fireButtonList;
    }

    /**
     * This method creates 100 buttons to populate player Player Location Pane (place where player puts his ships
     * and where are marked cpu's choices)
     * also adds them to the specific grid pane (bottom) playerLocationBoardBottom
     *
     * @return populated list of location buttons - buttons are added in order; for example #35 button equals to coordinates (3, 5)
     */
    public static List<Button> createLocationButtonList() {
        List<Button> locationButtonList = new ArrayList<>();
        GridPane playerLocationBoard = Game.getPlayerLocationBoardBottom();

        int placementButtonCounter = 0;

        for (int column = 0; column < 10; column++) {
            for (int row = 0; row < 10; row++) {
                //text on buttons only for debug
                Button button = new Button();
                if (Game.debug) {
                    button.setText("" + placementButtonCounter);
                    placementButtonCounter++;
                }

                //removing blue border from next button
                button.setStyle("-fx-focus-traversable: false;");


                button.setMinSize(30, 30);
                button.setPrefSize(40, 40);
                playerLocationBoard.add(button, column, row);
                locationButtonList.add(button);

                //reading X and Y coordinates
                button.setOnAction(event -> {
                    if (Game.getCurrentShip() != null) {


                        Ship current = Game.getCurrentShip();
                        if (current.getShipMaxSize() > current.getShipFieldCount()) {
                            Integer xParam = GridPane.getColumnIndex(button);
                            Integer yParam = GridPane.getRowIndex(button);
                            if (Game.debug) {
                                System.out.println("X " + xParam + " Y " + yParam + " button " + event.getSource());
                            }

                            current.setCoordinates(xParam, yParam);
                            button.setStyle("-fx-background-color: yellow");
                            button.setDisable(true);
                        } else {

                            Game.setCurrentShip(null);
                        }

                    }
                });
            }
        }
        return locationButtonList;
    }

}
    
