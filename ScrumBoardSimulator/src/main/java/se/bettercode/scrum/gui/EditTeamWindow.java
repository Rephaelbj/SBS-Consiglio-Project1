package se.bettercode.scrum.gui;


import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    
    boolean error;
    
    public EditTeamWindow(Team team)
    {
    	error = false;
    	
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
        
        Label maturityLabel = new Label("Maturity Level:");
        ChoiceBox<String> maturityChoiceBox = new ChoiceBox();
        maturityChoiceBox.getItems().addAll("Beginner", "Established", "Expert");
        HBox maturityPane = new HBox(10.0);
        maturityPane.getChildren().addAll(maturityLabel, maturityChoiceBox);
        add(maturityPane, 0, 2, 3, 1);
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction((event) -> currentStage.close());

        Button acceptButton = new Button("Accept");
        acceptButton.setOnAction((event) -> {
            if (!velocityField.getText().equals(""))
            	try {
            		team.setVelocity(Integer.parseInt(velocityField.getText()));
            		error = false;
                } catch (NumberFormatException e) {
                    errorLabel.setVisible(true);
                    error = true;
                }
            else error = false;
            if (maturityChoiceBox.getValue() != null)
            	team.setMaturity(maturityChoiceBox.getValue());
            if (!error) currentStage.close();
        });

        HBox buttonPane = new HBox(10);

        buttonPane.getChildren().addAll(cancelButton, acceptButton);
        add(buttonPane, 0, 3, 3, 1);

        add(errorLabel, 0, 4,3,1);
    }

    public void setStage(Stage stage){this.currentStage = stage;};
}
