package se.bettercode.scrum;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Border;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import se.bettercode.scrum.backlog.Backlog;
import se.bettercode.scrum.backlog.SelectableBacklogs;
import se.bettercode.scrum.gui.Board;
import se.bettercode.scrum.gui.BurnupChart;
import se.bettercode.scrum.gui.StatusBar;
import se.bettercode.scrum.gui.ToolBar;
import se.bettercode.scrum.prefs.StageUserPrefs;
import se.bettercode.scrum.team.SelectableTeams;
import se.bettercode.scrum.team.Team;


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
        TaigaIntegration.getTaigaInfo();
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

}
