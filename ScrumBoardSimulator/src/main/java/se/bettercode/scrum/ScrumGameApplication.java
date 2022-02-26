package se.bettercode.scrum;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.json.JSONArray;
import org.json.JSONObject;

import se.bettercode.scrum.backlog.Backlog;
import se.bettercode.scrum.backlog.SelectableBacklogs;
import se.bettercode.scrum.gui.Board;
import se.bettercode.scrum.gui.BurnupChart;
import se.bettercode.scrum.gui.StatusBar;
import se.bettercode.scrum.gui.ToolBar;
import se.bettercode.scrum.prefs.StageUserPrefs;
import se.bettercode.scrum.team.SelectableTeams;
import se.bettercode.scrum.team.Team;
import se.bettercode.scrum.team.TeamImpl;


public class ScrumGameApplication extends Application {

	public static final Color SCRUM_BOARD_BLUE = Color.web("#336699");
    
	private static final int SPRINT_LENGTH_IN_DAYS = 10;

    private Board board = new Board();
    private Sprint sprint;
    private Team team;
    private Backlog backlog;
    private StatusBar statusBar;
    private SelectableBacklogs backlogs;
    private SelectableTeams teams;
    private ToolBar toolBar;
    private BurnupChart burnupChart;
    private Stage primaryStage;
    private StageUserPrefs prefs;
    private String username;
    private String password;
    private String projectSlug;
    private JSONObject taigaResponse;
    private int project_id;
    private JSONArray userStoriesResponse;
    private String sprintName;
    private java.util.List<Story> taigaUserStories;
    
    public static void main(String[] args) {
        System.out.println("Launching JavaFX application.");
        launch(args);
    }

    @Override
    public void init() {
        System.out.println("Inside init()");
        
        board = new Board();
        statusBar = new StatusBar();
        backlogs = new SelectableBacklogs();
        teams = new SelectableTeams();
        toolBar = new ToolBar(teams.getKeys(), backlogs.getKeys());
        burnupChart = getNewBurnupChart();
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Inside start()");
        this.primaryStage = primaryStage;
        prefs = new StageUserPrefs(primaryStage);
        prefs.load();
        setStage();
        bindActionsToToolBar();
        primaryStage.show();
    }

    private void setStage() {
        primaryStage.setTitle("Scrum Game");
        BorderPane borderPane = new BorderPane();
        board.prefWidthProperty().bind(primaryStage.widthProperty());
        borderPane.setCenter(board);
        borderPane.setRight(burnupChart);
        borderPane.setTop(toolBar);
        borderPane.setBottom(statusBar);
        primaryStage.setScene(new Scene(borderPane, 800, 600));
    }

    private boolean initSprint() {
        if (team != null && backlog != null) {
            sprint = new Sprint("First sprint", SPRINT_LENGTH_IN_DAYS, team, backlog);
            board.bindBacklog(backlog);
            burnupChart.removeAllData();
            //burnupChart = getNewBurnupChart();
            burnupChart.bindBurnupDaysProperty(backlog.getBurnup().burnupDaysProperty());
            toolBar.bindRunningProperty(sprint.runningProperty());
            return true;
        }
        return false;
    }

    private void bindSprintDataToStatusBar() {
        statusBar.bindTeamName(team.nameProperty());
        statusBar.bindTeamVelocity(team.velocityProperty());
        statusBar.bindStoryPointsDone(backlog.donePointsProperty());
        statusBar.bindDaysInSprint(sprint.lengthInDaysProperty());
        statusBar.bindCurrentDay(sprint.currentDayProperty());
        statusBar.bindLeadTime(backlog.averageLeadTimeProperty());
    }

    private void bindActionsToToolBar() {
        ChangeListener backlogChoiceBoxListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                backlog = backlogs.get(newValue.toString());
                loadData();
            }
        };

        ChangeListener teamChoiceBoxListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                team = teams.get(newValue.toString());
                loadData();
            }
        };

        toolBar.setTeamChoiceBoxListener(teamChoiceBoxListener);
        toolBar.setBacklogChoiceBoxListener(backlogChoiceBoxListener);
        toolBar.setStartButtonAction((event) -> sprint.runSprint());
        toolBar.setEditButtonAction((event) -> { if (team != null) buildEditTeamStage(); });
        toolBar.setTaigaButtonAction((event) -> buildTaigaIntegrationStage());

        toolBar.setResetGameButtonAction((event) -> {
        	primaryStage.close();
        	team = null;
        	backlog = null;
        	this.init();
        	this.start(new Stage());
        });
    }

    private void loadData() {
        if (initSprint()) {
            bindSprintDataToStatusBar();
        }
    }

    public void stop() {
        System.out.println("Inside stop()");
        prefs.save();
    }

    private BurnupChart getNewBurnupChart() {
        return new BurnupChart(SPRINT_LENGTH_IN_DAYS);
    }
    
    /* This method is used to build the Stage for editing the team attributes. */
    private void buildEditTeamStage() {
    	Stage editTeamStage = new Stage();
    	editTeamStage.setTitle("Edit Team");
    	
    	Label teamLabel = new Label();
    	teamLabel.textProperty().bind(team.nameProperty());
    	teamLabel.setFont(Font.font("Lucida Sans", FontWeight.BOLD, 20.0));
    	teamLabel.setPadding(new Insets(0.0, 0.0, 0.0, 20.0));
    	teamLabel.setTextFill(SCRUM_BOARD_BLUE);
    	
    	Label velocityLabel = new Label("Velocity:");
    	TextField velocityField = new TextField();
    	velocityField.setPrefWidth(60.0);
    	Label errorLabel = new Label("Input must be an integer!");
    	errorLabel.setTextFill(Color.RED);
    	errorLabel.setVisible(false);
    	HBox velocityPane = new HBox(10.0);
    	velocityPane.getChildren().addAll(velocityLabel, velocityField, errorLabel);
    	
    	Button cancelButton = new Button("Cancel");
    	cancelButton.setPrefWidth(100.0);
    	cancelButton.setOnAction((event) -> editTeamStage.close());
    	
    	Button acceptButton = new Button("Accept");
    	acceptButton.setPrefWidth(100.0);
    	acceptButton.setOnAction((event) -> {
    		try {
    			team.setVelocity(Integer.parseInt(velocityField.getText()));
    			editTeamStage.close();
    		} catch (NumberFormatException e) {
    			errorLabel.setVisible(true);
    		}
    	});
    	
    	HBox buttonPane = new HBox(10.0);
    	buttonPane.getChildren().addAll(cancelButton, acceptButton);
    	
    	VBox editTeamPane = new VBox(10.0);
    	editTeamPane.setPadding(new Insets(20.0));
    	editTeamPane.getChildren().addAll(teamLabel, velocityPane, buttonPane);
    	
    	editTeamStage.setScene(new Scene(editTeamPane, 350.0, 300.0));
    	editTeamStage.show();
    }

    public void buildBurndownChartStage() {

        Stage burndownChartStage = new Stage();
        burndownChartStage.setTitle("Burndown Chart");

        Label burndownLabel = new Label("Burndown Chart");
        VBox burndownChartPane = new VBox(10.0);
        burndownChartPane.setPadding(new Insets(20.0));
        burndownChartPane.getChildren().addAll(burndownLabel);

        final NumberAxis xAxis = new NumberAxis(0, 10, 1);
        final NumberAxis yAxis = new NumberAxis(0,26,1);


        AreaChart burnDownChart = new AreaChart(xAxis, yAxis);


        BorderPane borderpane = new BorderPane(burndownLabel);
        borderpane.setAlignment(burndownLabel, Pos.TOP_CENTER);

        borderpane.setCenter(burnDownChart);


        //burndownChartStage.setScene(scene);
        burndownChartStage.show();

    }
    
    /* This method is used to build the Stage for editing the team attributes. */
    private void buildTaigaIntegrationStage() {
    	Stage taigaIntegrationStage = new Stage();
    	taigaIntegrationStage.setTitle("Taiga Integration");
    	
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
    	cancelButton.setOnAction((event) -> taigaIntegrationStage.close());
    	
    	Button acceptButton = new Button("Accept");
    	acceptButton.setPrefWidth(100.0);
    	acceptButton.setOnAction((event) -> {
    		if (usernameField.getText().equals("") || passwordField.getText().equals("")
    				|| projectSlugField.getText().equals("")) {
    			if (usernameField.getText().equals("")) usernameErrorLabel.setVisible(true);
    			else usernameErrorLabel.setVisible(false);
    			if (passwordField.getText().equals("")) passwordErrorLabel.setVisible(true);
    			else passwordErrorLabel.setVisible(false);
    			if (projectSlugField.getText().equals("")) projectSlugErrorLabel.setVisible(true);
    			else projectSlugErrorLabel.setVisible(false);
    		} else {
    			taigaIntegrationStage.close();
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
	    				if (i == 0) teamName = usJSON.getJSONObject("project_extra_info").getString("name"); // Only get team name once
	    				if (!sprintName.equals("")) { // Only grab all the user stories in the specified Sprint
		    				String sName = !usJSON.isNull("milestone_name") ? usJSON.getString("milestone_name") : null;
		    				if (sName != null && sName.equals(sprintName)) {
			    				String userStoryName = usJSON.getString("subject");
			    				int points = usJSON.getInt("total_points");
			    				velocity += points;
			    				backlog.addStory(new Story(points, userStoryName));
		    				}
	    				} else { // Otherwise grab all the user stories in the backlog
	    					String userStoryName = usJSON.getString("subject");
		    				int points = usJSON.getInt("total_points");
		    				velocity += points;
		    				backlog.addStory(new Story(points, userStoryName));
	    				}
	    			}
	    			team = new TeamImpl(teamName, velocity);
	    			loadData();
	    			
	    		} catch (org.json.JSONException e) {
	    			System.out.println(e);
	    			Stage errorStage = new Stage();
	    			errorStage.setTitle("Uh oh...");
	    			
	    			Text errorText = new Text("Connection unsuccessful! Please try again.");
	    			errorText.setFont(Font.font("Lucida Sans", FontWeight.SEMI_BOLD, 12.0));
	    			Button okButton = new Button("OK");
	    			okButton.setPrefWidth(100.0);
	    			okButton.setTranslateX(80.0);
	    			okButton.setOnAction(eh -> errorStage.close());
	    			VBox errorPane = new VBox(10.0);
	    			errorPane.setPadding(new Insets(10.0, 0.0, 0.0, 20.0));
	    			errorPane.getChildren().addAll(errorText, okButton);
	    			
	    			errorStage.setScene(new Scene(errorPane, 320.0, 100.0));
	    			errorStage.show();
	    		}
    		}
    	});
    	
    	HBox buttonPane = new HBox(10.0);
    	buttonPane.getChildren().addAll(cancelButton, acceptButton);
    	
    	VBox taigaIntegrationPane = new VBox(10.0);
    	taigaIntegrationPane.setPadding(new Insets(20.0));
    	taigaIntegrationPane.getChildren().addAll(promptLabel, usernamePane, passwordPane, projectSlugPane, sprintNamePane, buttonPane);
    	
    	taigaIntegrationStage.setScene(new Scene(taigaIntegrationPane, 450.0, 250.0));
    	taigaIntegrationStage.show();
    }

}
