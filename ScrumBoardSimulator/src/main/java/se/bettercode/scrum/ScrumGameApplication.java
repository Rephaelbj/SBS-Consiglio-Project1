package se.bettercode.scrum;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import se.bettercode.scrum.backlog.Backlog;
import se.bettercode.scrum.backlog.SelectableBacklogs;
import se.bettercode.scrum.gui.*;
import se.bettercode.scrum.prefs.StageUserPrefs;
import se.bettercode.scrum.team.SelectableTeams;
import se.bettercode.scrum.team.Team;
import se.bettercode.taiga.TaigaContainer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ScrumGameApplication extends Application {

    private static final int SPRINT_LENGTH_IN_DAYS = 10;

    private Board board;
    private Sprint sprint;
    private Team team;
    private Backlog backlog;
    private String chart;
    private StatusBar statusBar = new StatusBar();
    private SelectableBacklogs backlogs = new SelectableBacklogs();
    private SelectableTeams teams = new SelectableTeams();
    private ToolBar toolBar = new ToolBar(teams.getKeys(), backlogs.getKeys());
    private BurnupChart burnupChart = getNewBurnupChart();
    private BurnDownChart burnDownChart = getBurnDownChart();
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
        MenuItem fItem6 = new MenuItem("Exit");
        fileMenu.getItems().addAll(fItem1,fItem2,fItem3,fItem4,fItem5,fItem6);

        // Team menu
        //Strategy menu
        Menu teamMenu = new Menu("Team");
        MenuItem tItem1 = new MenuItem("New");
        MenuItem tItem2 = new MenuItem("Edit");
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
            burnDownChart.removeAllData();
            burnupChart.bindBurnupDaysProperty(backlog.getBurnup().burnupDaysProperty());
            burnDownChart.bindBurnDownDaysProperty(backlog.getBurnDown().burnDownDaysProperty());
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
        toolBar.setBurnUpButtonAction((event) -> ChartWindow.displayBurnUpChart(burnupChart));
        toolBar.setBurnDownChatButtonButtonAction((event) -> ChartWindow.displayBurnDownChart(burnDownChart));
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
        return new BurnupChart(SPRINT_LENGTH_IN_DAYS, false);
    }

    private BurnDownChart getBurnDownChart() {
        return new BurnDownChart();
    }
}
