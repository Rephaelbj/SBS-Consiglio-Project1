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

    public static void displayBurnUpChart(BurnupChart burnupChart) {
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

    public static void displayBurnDownChart(BurnDownChart burnDownChart) {
        Stage window = new Stage();
        window.setTitle("Burn Down Chart");
        window.setMinWidth(500);

        Button closeButton = new Button("Close the Window");
        closeButton.setOnAction( e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(burnDownChart, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    private static void display(VBox layout, String windowTitle) {
        Stage window = new Stage();
        window.setTitle(windowTitle);
        window.setMinWidth(500);
        Button closeButton = new Button("Close the Window");
        closeButton.setOnAction( e -> window.close());

        layout.getChildren().add(closeButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
