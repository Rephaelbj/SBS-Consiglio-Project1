package se.bettercode.scrum.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import se.bettercode.scrum.team.SelectableTeams;
import se.bettercode.scrum.team.Team;
import se.bettercode.scrum.team.TeamImpl;

public class NewTeamWindow extends GridPane {

    public Stage currentStage;
    private SelectableTeams teams;

    public NewTeamWindow(SelectableTeams team) {
        teams = team;
        Label titleLabel = new Label("New Team");
        Label errorLabel = new Label("Invalid input");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);

        TextField nameField = new TextField();
        TextField velocityField = new TextField();

        Label nameLabel = new Label("Name:");
        Label velocityLabel = new Label("Velocity: ");

        Button createTeam = new Button("Create Team");
        createTeam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    int velocity = Integer.parseInt(velocityField.getText());

                    if (nameField.getText() != "") {
                        Team team = new TeamImpl(nameField.getText(), velocity);
                        teams.addTeam(team);
                        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
                    } else {
                        errorLabel.setVisible(true);
                    }

                } catch (Exception e) {
                    errorLabel.setVisible(true);
                }
            }
        });
        Button cancel = new Button("Cancel");

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentStage.close();
            }
        });

        this.add(titleLabel, 0, 0, 2, 1);
        this.add(errorLabel, 1, 0, 2, 1);


        this.add(nameLabel, 0, 1, 1, 1);
        this.add(nameField, 1, 1, 1, 1);

        this.add(velocityLabel, 0, 2, 1, 1);
        this.add(velocityField, 1, 2, 1, 1);

        this.add(createTeam, 0, 3, 1, 1);
        this.add(cancel, 1, 3, 1, 1);
    }

    public void setStage(Stage newStage) {
        this.currentStage = newStage;
    }
}
