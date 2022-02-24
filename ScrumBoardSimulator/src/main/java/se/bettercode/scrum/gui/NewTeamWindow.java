package se.bettercode.scrum.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import se.bettercode.scrum.team.SelectableTeams;

public class NewTeamWindow extends GridPane {

    public Stage currentStage;
    private SelectableTeams teams;

    public NewTeamWindow(SelectableTeams team)
    {
        teams = team;
        Label titleLabel = new Label("New Team");

        TextField nameField = new TextField();
        TextField velocityField = new TextField();

        Label nameLabel = new Label("Name:");
        Label velocityLabel = new Label("Velocity: ");

        Button createTeam = new Button("Create Team");
        Button cancel = new Button("Cancel");

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentStage.close();
            }
        });

        this.add(titleLabel, 0 ,0, 2,1);

        this.add(nameLabel, 0 , 1, 1, 1);
        this.add(nameField, 1, 1, 1, 1);

        this.add(velocityLabel, 0,2,1,1);
        this.add(velocityField, 1,2,1,1);

        this.add(createTeam,0, 3, 1,1);
        this.add(cancel, 1, 3,1,1);
    }

    public void setStage(Stage newStage)
    {
        this.currentStage = newStage;
    }
}
