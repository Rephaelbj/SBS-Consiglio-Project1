package se.bettercode.scrum;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import se.bettercode.scrum.backlog.Backlog;
import se.bettercode.scrum.backlog.SelectableBacklogs;
import se.bettercode.scrum.gui.*;
import se.bettercode.scrum.prefs.StageUserPrefs;
import se.bettercode.scrum.team.SelectableTeams;
import se.bettercode.scrum.team.Team;
import se.bettercode.scrum.team.TeamImpl;
import se.bettercode.taiga.TaigaContainer;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;


public class ScrumGameApplication extends Application {

	public static final Color SCRUM_BOARD_BLUE = Color.web("#336699");
	
    private static final int SPRINT_LENGTH_IN_DAYS = 10;

    private Board board;
    private Sprint sprint;
    private Team team;
    private Backlog backlog;
    private String chart;
    private StatusBar statusBar = new StatusBar();
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
   

    public ScrumGameApplication() throws FileNotFoundException {
    }
    
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
        try {
        	teams = new SelectableTeams();
        } catch (FileNotFoundException e) {}
        toolBar = new ToolBar(teams.getKeys(), backlogs.getKeys());
        burnupChart = getNewBurnupChart();
        //TODO: set up the reading of a property file to change existing settings of the application
        try{
            String audioSetting = "";
            String taskSetting = "";
            String windowSetting = "";
            String borderSetting = "";
            BufferedReader properties = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/properties.txt")));
            for(String option = ""; option != null; option = properties.readLine()){
                if(!option.contains("//") && !option.equals("")){
                    if(option.contains("Audio")){
                        audioSetting = option.substring(7);
                        System.out.println(audioSetting);
                    }else if(option.contains("TaskColor")){
                        taskSetting = option.substring(11);
                        System.out.println(taskSetting);
                    }else if(option.contains("WindowColor")){
                        windowSetting = option.substring(13);
                        System.out.println(windowSetting);
                    }else if(option.contains("BorderColor")){
                       borderSetting = option.substring(13);
                        System.out.println(borderSetting);
                    }
                }
            }
            board = new Board(audioSetting, taskSetting);
        }catch(Exception e){
            System.out.println(e.toString());
        }

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
        TaigaContainer taiga = new TaigaContainer();
        System.out.println(primaryStage.isShowing());
        try {
            taiga.login("rbjacks3@asu.edu", "BootyButtCheeks69");
           // taiga.setProject("rbjacks3-ser515-groupproject-7");
           // taiga.getData();
        }
        catch(IOException e)
        {
            System.out.println("OH NO");
        }
    }

    private void setStage() {
        primaryStage.setTitle("Scrum Game");
        //Adding all the menu items to the menu
        BorderPane borderPane = new BorderPane();

        BorderPane topPane = new BorderPane();
        topPane.setTop(makeMenus());
        topPane.setCenter(toolBar);


        borderPane.setTop(topPane);
        //borderPane.setTop(menuBar);
        board.prefWidthProperty().bind(primaryStage.widthProperty());

        borderPane.setCenter(board);
        borderPane.setBottom(statusBar);
        primaryStage.setScene(new Scene(borderPane, 800, 600));
    }


    private MenuBar makeMenus()
    {
        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem fItem1 = new MenuItem("Save");
        MenuItem fItem2 = new MenuItem("Load");
        MenuItem fItem3 = new MenuItem("Import from Taiga");
        MenuItem fItem4 = new MenuItem("Export to Taiga");
        MenuItem fItem5 = new MenuItem("Settings");
        fItem5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                SettingsMenu settings = new SettingsMenu(stage);
                settings.setAlignment(Pos.CENTER);
                settings.setHgap(10);
                settings.setVgap(10);
                Scene scene = new Scene(settings, 400, 300);
                stage.setScene(scene);
                stage.setTitle("Settings");
                stage.show();
            }
        });
        MenuItem fItem6 = new MenuItem("Exit");
        fileMenu.getItems().addAll(fItem1,fItem2,fItem3,fItem4,fItem5,fItem6);

        // Team menu
        //Strategy menu
        Menu teamMenu = new Menu("Team");
        MenuItem tItem1 = new MenuItem("New");
        tItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                NewTeamWindow newTeamWindow = new NewTeamWindow(teams);
                newTeamWindow.setStage(stage);
                newTeamWindow.setAlignment(Pos.CENTER);
                newTeamWindow.setHgap(10);
                newTeamWindow.setVgap(10);
                Scene scene = new Scene(newTeamWindow, 400, 200);
                stage.setScene(scene);
                stage.setTitle("New Team");
                stage.setResizable(false);
                stage.show();
                stage.setOnCloseRequest(e -> {
                    toolBar.setTeams(teams.getKeys());
                });
            }
        });
        MenuItem tItem2 = new MenuItem("Edit");

        tItem2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(team != null) {
                    Stage stage = new Stage();
                    EditTeamWindow editTeam = new EditTeamWindow(team);
                    editTeam.setStage(stage);
                    editTeam.setAlignment(Pos.CENTER);
                    editTeam.setHgap(10);
                    editTeam.setVgap(10);
                    Scene scene = new Scene(editTeam, 300, 200);
                    stage.setScene(scene);
                    stage.setTitle("Edit Team");
                    stage.setResizable(false);
                    stage.show();                }

            }
        });
        MenuItem tItem3 = new MenuItem("Delete");
        teamMenu.getItems().addAll(tItem1,tItem2,tItem3);

        //Strategy menu
        Menu strategyMenu = new Menu("Strategy");
        MenuItem sItem1 = new MenuItem("New");
        sItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                NewStrategyWindow newStrategyWindow = new NewStrategyWindow(backlogs);
                newStrategyWindow.setStage(stage);
                newStrategyWindow.setAlignment(Pos.CENTER);
                newStrategyWindow.setHgap(10);
                newStrategyWindow.setVgap(10);
                Scene scene = new Scene(newStrategyWindow, 400, 200);
                stage.setScene(scene);
                stage.setTitle("New Strategy");
                stage.setResizable(false);
                stage.show();
                stage.setOnCloseRequest(e -> {
                    toolBar.setStrategies(backlogs.getKeys());
                });

            }
        });
        MenuItem sItem2 = new MenuItem("Edit");
        MenuItem sItem3 = new MenuItem("Delete");
        strategyMenu.getItems().addAll(sItem1,sItem2,sItem3);


        MenuBar menuBar = new MenuBar();

        // Add menus
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(teamMenu);
        menuBar.getMenus().add(strategyMenu);
        return menuBar;

    }
    private boolean initSprint() {
        if (team != null && backlog != null) {
            sprint = new Sprint("First sprint", SPRINT_LENGTH_IN_DAYS, team, backlog);
            board.bindBacklog(backlog);
            burnupChart.removeAllData();
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
                if(newValue != null) {
                    backlog = backlogs.get(newValue.toString());
                    loadData();
                }
            }
        };

        ChangeListener teamChoiceBoxListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(newValue != null) {
                    team = teams.get(newValue.toString());
                    loadData();
                }
            }
        };

        toolBar.setTeamChoiceBoxListener(teamChoiceBoxListener);
        toolBar.setBacklogChoiceBoxListener(backlogChoiceBoxListener);
        toolBar.setBurnUpButtonAction((event) -> ChartWindow.display(burnupChart));
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
        return new BurnupChart(SPRINT_LENGTH_IN_DAYS, false);
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
