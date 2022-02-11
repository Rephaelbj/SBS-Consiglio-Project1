package se.bettercode.scrum.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChartWindow {

    public static void display(BurnupChart burnupChart) {
        Stage window = new Stage();
        window.setTitle("Burn Up Chart");
        window.setMinWidth(500);

        Button closeButton = new Button("Close the Window");
        closeButton.setOnAction( e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(burnupChart, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
