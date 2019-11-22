package GameUI;

import GameMechanic.Player;
import GameMechanic.Ship;
import GameMechanic.ShipName;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridHelperMethods {
  
    private static double minButtonSize = 39;
    
    public static List<Button> create100ButtonList(GridPane toPopulate, String cssId, boolean disableGridButtons, EventHandler<ActionEvent> eventHandlerGridButtons) {
        List<Button> resultButtonList = new ArrayList<Button>();

        int buttonCounter = 0;

        for (int column = 0; column < 10; column++) {
            for (int row = 0; row < 10; row++) {
                Button button = new Button();

                //TODO
                //only for debug
                button.setText("" + buttonCounter);
                buttonCounter++;


                button.setId(cssId);
                button.setDisable(disableGridButtons);
                button.setMinSize(minButtonSize, minButtonSize);

                Pane pane = new Pane(button);
                toPopulate.add(pane, column, row);
                resultButtonList.add(button);

                button.setOnAction(eventHandlerGridButtons);
            }
        }
        return resultButtonList;
    }

    public static boolean hitCheck(Player player, Integer coordinate) {
        return player.getShipsList().stream()
                .flatMap(ship -> ship.getCoordinates().stream())
                .anyMatch(s -> s.equals(coordinate));
    }

    public static Ship shipFromGridButton(List<Button> gridButtons, List<Ship> shipList, Button buttonEntered) {
        int index = gridButtons.indexOf(buttonEntered);
        return shipList.stream()
                .filter(ship -> ship.getCoordinates().contains(index))
                .findAny().get();
    }

    public static void gridMarkers(GridPane gridRoot) {
        List<Label> markerLabels = new ArrayList<Label>();
        for (int i = 1; i <= 10; i++) {
            Label markerLetters = new Label(numberToLetter(i - 1));
            Label markerNumbers = new Label("" + i);

            markerLabels.add(markerLetters);
            markerLabels.add(markerNumbers);

            gridRoot.add(markerLetters, i, 0);
            gridRoot.add(markerNumbers, 0, i);

        }

        markerLabels.forEach(label -> {
            label.setMinHeight(minButtonSize);
            label.setMinWidth(minButtonSize);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setAlignment(Pos.CENTER);
            label.setId("boardMarkers");
        });
    }

    public static String numberToLetter(int number) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return Character.toString(alphabet.charAt(number));
    }

   
}