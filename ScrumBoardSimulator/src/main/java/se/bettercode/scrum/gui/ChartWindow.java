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
        window.setTitle("Information Radiator");
        window.setMinWidth(500);

        Button closeButton = new Button("Close the Window");
        Button flipButton = new Button("Flip Chart");

        closeButton.setOnAction( e -> window.close());
        flipButton.setOnAction(e -> {

            burnupChart.flipData();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(burnupChart, closeButton, flipButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
