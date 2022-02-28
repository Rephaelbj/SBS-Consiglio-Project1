package se.bettercode.scrum.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import se.bettercode.scrum.Story;
import se.bettercode.scrum.TaigaIntegration;
import se.bettercode.scrum.backlog.Backlog;
import se.bettercode.scrum.backlog.SelectableBacklogs;
import se.bettercode.scrum.team.SelectableTeams;
import se.bettercode.scrum.team.Team;
import se.bettercode.scrum.team.TeamImpl;

import java.io.IOException;

public class TaigaWindow extends  VBox{

    private String username;
    private String password;
    private String projectSlug;
    private JSONObject taigaResponse;
    private int project_id;
    private JSONArray userStoriesResponse;
    private String sprintName;
    private java.util.List<Story> taigaUserStories;
    Stage currentStage;
    Backlog backlog;
    public TaigaWindow(SelectableBacklogs backlogs, SelectableTeams teams)
    {

        Label promptLabel = new Label("Please enter your Taiga authentication credentials:");

        Text usernamePrompt = new Text("Username:");
        usernamePrompt.setFont(Font.font("Lucida Sans", FontWeight.BOLD, 14));
        TextField usernameField = new TextField();
        Label usernameErrorLabel = new Label("Please enter username!");
        usernameErrorLabel.setTextFill(Color.RED);
        usernameErrorLabel.setVisible(false);
        FlowPane usernamePane = new FlowPane(10.0, 10.0);
        usernamePane.getChildren().addAll(usernamePrompt, usernameField, usernameErrorLabel);

        Text passwordPrompt = new Text("Password:");
        passwordPrompt.setFont(Font.font("Lucida Sans", FontWeight.BOLD, 14));
        PasswordField passwordField = new PasswordField();
        Label passwordErrorLabel = new Label("Please enter password!");
        passwordErrorLabel.setTextFill(Color.RED);
        passwordErrorLabel.setVisible(false);
        FlowPane passwordPane = new FlowPane(10.0, 10.0);
        passwordPane.getChildren().addAll(passwordPrompt, passwordField, passwordErrorLabel);

        Text projectSlugPrompt = new Text("Project slug:");
        projectSlugPrompt.setFont(Font.font("Lucida Sans", FontWeight.BOLD, 14));
        TextField projectSlugField = new TextField();
        projectSlugField.setPrefWidth(250.0);
        Label projectSlugErrorLabel = new Label("Please enter project slug!");
        projectSlugErrorLabel.setTextFill(Color.RED);
        projectSlugErrorLabel.setVisible(false);
        FlowPane projectSlugPane = new FlowPane(10.0, 10.0);
        projectSlugPane.getChildren().addAll(projectSlugPrompt, projectSlugField, projectSlugErrorLabel);

        Text sprintNamePrompt = new Text("Sprint name:");
        sprintNamePrompt.setFont(Font.font("Lucida Sans", FontWeight.BOLD, 14));
        TextField sprintNameField = new TextField();
        sprintNameField.setPrefWidth(250.0);
        FlowPane sprintNamePane = new FlowPane(10.0, 10.0);
        sprintNamePane.getChildren().addAll(sprintNamePrompt, sprintNameField);

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(100.0);
        cancelButton.setOnAction((event) -> currentStage.close());

        Button acceptButton = new Button("Accept");
        acceptButton.setPrefWidth(100.0);
        acceptButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (usernameField.getText().equals("") || passwordField.getText().equals("")
                        || projectSlugField.getText().equals("")) {
                    if (usernameField.getText().equals("")) usernameErrorLabel.setVisible(true);
                    else usernameErrorLabel.setVisible(false);
                    if (passwordField.getText().equals("")) passwordErrorLabel.setVisible(true);
                    else passwordErrorLabel.setVisible(false);
                    if (projectSlugField.getText().equals("")) projectSlugErrorLabel.setVisible(true);
                    else projectSlugErrorLabel.setVisible(false);
                } else {
                    username = usernameField.getText();
                    password = passwordField.getText();
                    projectSlug = projectSlugField.getText();
                    sprintName = sprintNameField.getText();
                    try {
                        taigaUserStories = new java.util.ArrayList<>();
                        taigaResponse = new JSONObject(TaigaIntegration.getTaigaInfo(username, password, projectSlug));
                        project_id = taigaResponse.getJSONArray("epic_statuses").getJSONObject(0).getInt("project_id");
                        userStoriesResponse = new JSONArray(TaigaIntegration.getTaigaUserStories(project_id));
                        System.out.println(userStoriesResponse);

                        backlog = new Backlog("Taiga");
                        int velocity = 0;
                        String teamName = "";
                        for (int i = 0; i < userStoriesResponse.length(); i++) {
                            JSONObject usJSON = userStoriesResponse.getJSONObject(i);
                            if (i == 0)
                                teamName = usJSON.getJSONObject("project_extra_info").getString("name"); // Only get team name once
                            if (!sprintName.equals("")) { // Only grab all the user stories in the specified Sprint
                                String sName = !usJSON.isNull("milestone_name") ? usJSON.getString("milestone_name") : null;
                                if (sName != null && sName.equals(sprintName)) {
                                    String userStoryName = usJSON.getString("subject");
                                    System.out.println(usJSON.get("total_points").equals("null"));
                                    int points = 0;
                                    if(!usJSON.isNull("total_points")) {
                                        points = (int) usJSON.getDouble("total_points");
                                    }                                    velocity += points;
                                    backlog.addStory(new Story(points, userStoryName));
                                }
                            } else { // Otherwise grab all the user stories in the backlog
                                String userStoryName = usJSON.getString("subject");
                                System.out.println(usJSON.get("total_points").equals("null"));
                                int points = 0;
                                if(!usJSON.isNull("total_points")) {
                                    points = (int) usJSON.getDouble("total_points");
                                }
                                velocity += points;
                                backlog.addStory(new Story(points, userStoryName));
                            }
                        }

                        Team team = new TeamImpl(teamName, velocity);
                        teams.addTeam(team);
                        backlogs.put("Taiga", backlog);
                        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));

                    } catch (org.json.JSONException e) {
                        System.out.println(e);
                        Stage errorStage = new Stage();
                        errorStage.setTitle("Uh oh...");

                        Text errorText = new Text("Connection unsuccessful! Please try again.");
                        errorText.setFont(Font.font("Lucida Sans", FontWeight.SEMI_BOLD, 12.0));
                        Button okButton = new Button("OK");
                        okButton.setPrefWidth(100.0);
                        okButton.setTranslateX(80.0);
                        okButton.setOnAction(new EventHandler<ActionEvent>() {
                                                 @Override
                                                 public void handle(ActionEvent event) {
                                                     errorStage.close();
                                                 }
                                             });
                                VBox errorPane = new VBox(10.0);
                        errorPane.setPadding(new Insets(10.0, 0.0, 0.0, 20.0));
                        errorPane.getChildren().addAll(errorText, okButton);

                        errorStage.setScene(new Scene(errorPane, 320.0, 100.0));
                        errorStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        HBox buttonPane = new HBox(10.0);
        buttonPane.getChildren().addAll(cancelButton, acceptButton);

        setPadding(new Insets(20.0));
        getChildren().addAll(promptLabel, usernamePane, passwordPane, projectSlugPane, sprintNamePane, buttonPane);
    }

    public void setStage(Stage stage)
    {
        this.currentStage = stage;
    }

    public Backlog getBacklog()
    {
        return backlog;
    }
}
