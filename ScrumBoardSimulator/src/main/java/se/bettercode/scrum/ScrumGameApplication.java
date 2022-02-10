package se.bettercode.scrum;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import se.bettercode.Main;
import se.bettercode.scrum.backlog.Backlog;
import se.bettercode.scrum.backlog.SelectableBacklogs;
import se.bettercode.scrum.gui.Board;
import se.bettercode.scrum.gui.BurnupChart;
import se.bettercode.scrum.gui.StatusBar;
import se.bettercode.scrum.gui.ToolBar;
import se.bettercode.scrum.prefs.StageUserPrefs;
import se.bettercode.scrum.team.SelectableTeams;
import se.bettercode.scrum.team.Team;
import se.bettercode.taiga.TaigaContainer;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;


public class ScrumGameApplication extends Application {

    private static final int SPRINT_LENGTH_IN_DAYS = 10;

    private Board board;
    private Sprint sprint;
    private Team team;
    private Backlog backlog;
    private StatusBar statusBar = new StatusBar();
    private SelectableBacklogs backlogs = new SelectableBacklogs();
    private SelectableTeams teams = new SelectableTeams();
    private ToolBar toolBar = new ToolBar(teams.getKeys(), backlogs.getKeys());
    private BurnupChart burnupChart = getNewBurnupChart();
    private Stage primaryStage;
    private StageUserPrefs prefs;
    
    public static void main(String[] args) {
        System.out.println("Launching JavaFX application.");
        launch(args);
    }

    @Override
    public void init() {
        System.out.println("Inside init()");
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
        try {
            taiga.login("rbjacks3@asu.edu", "BootyButtCheeks69");
            taiga.setProject("rbjacks3-ser515-groupproject-7");
        }
        catch(IOException e)
        {

        }
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
}
