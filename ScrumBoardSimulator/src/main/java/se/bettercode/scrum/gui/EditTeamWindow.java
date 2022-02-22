package se.bettercode.scrum.gui;


import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import se.bettercode.scrum.team.Team;

public class EditTeamWindow extends GridPane {
    public static final Color SCRUM_BOARD_BLUE = Color.web("#336699");
    public Stage currentStage;
    public EditTeamWindow(Team team)
    {

        Label teamLabel = new Label();
        teamLabel.textProperty().bind(team.nameProperty());
        teamLabel.setFont(Font.font("Lucida Sans", FontWeight.BOLD, 20.0));
        teamLabel.setTextFill(SCRUM_BOARD_BLUE);
        teamLabel.setTextAlignment(TextAlignment.CENTER);
        add(teamLabel,0,0,3,1);

        Label velocityLabel = new Label("Velocity:");
        TextField velocityField = new TextField();
        velocityField.setMaxWidth(70);
        Label errorLabel = new Label("Input must be an integer!");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        HBox velocityPane = new HBox(10);
        velocityField.setPromptText(String.valueOf(team.velocityProperty().get()));
        velocityPane.getChildren().addAll(velocityLabel,velocityField);
        add(velocityPane, 0, 1,3,1);
        setAlignment(Pos.CENTER);
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction((event) -> currentStage.close());

        Button acceptButton = new Button("Accept");
        acceptButton.setOnAction((event) -> {
            try {
                team.setVelocity(Integer.parseInt(velocityField.getText()));
                currentStage.close();
            } catch (NumberFormatException e) {
                errorLabel.setVisible(true);
            }
        });

        HBox buttonPane = new HBox(10);

        buttonPane.getChildren().addAll(cancelButton, acceptButton);
        add(buttonPane, 0, 2, 3, 1);

        add(errorLabel, 0, 3,3,1);
    }

    public void setStage(Stage stage){this.currentStage = stage;};
}
