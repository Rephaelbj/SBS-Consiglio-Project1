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


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;


public class ScrumGameApplication extends Application {


    private static final int SPRINT_LENGTH_IN_DAYS = 10;

    private Board board;
    private Sprint sprint;
    private Team team;
    private Backlog backlog;
    private StatusBar statusBar = new StatusBar();
    private SelectableBacklogs backlogs;
    private SelectableTeams teams;
    private ToolBar toolBar;
    private BurnupChart burnupChart;
    private Stage primaryStage;
    private StageUserPrefs prefs;

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
        } catch (FileNotFoundException e) {
        }
        toolBar = new ToolBar(teams.getKeys(), backlogs.getKeys());
        burnupChart = getNewBurnupChart();
        //TODO: set up the reading of a property file to change existing settings of the application
        try {
            String audioSetting = "";
            String taskSetting = "";
            String windowSetting = "";
            String borderSetting = "";
            BufferedReader properties = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/properties.txt")));
            for (String option = ""; option != null; option = properties.readLine()) {
                if (!option.contains("//") && !option.equals("")) {
                    if (option.contains("Audio")) {
                        audioSetting = option.substring(7);
                        System.out.println(audioSetting);
                    } else if (option.contains("TaskColor")) {
                        taskSetting = option.substring(11);
                        System.out.println(taskSetting);
                    } else if (option.contains("WindowColor")) {
                        windowSetting = option.substring(13);
                        System.out.println(windowSetting);
                    } else if (option.contains("BorderColor")) {
                        borderSetting = option.substring(13);
                        System.out.println(borderSetting);
                    }
                }
            }
            board = new Board(audioSetting, taskSetting);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Inside start()");
        this.primaryStage = primaryStage;
        prefs = new StageUserPrefs(primaryStage);
        prefs.load();
        bindActionsToToolBar();
        setStage();
        primaryStage.show();
        System.out.println(primaryStage.isShowing());
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


    private MenuBar makeMenus() {
        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem fItem1 = new MenuItem("Save");
        MenuItem fItem2 = new MenuItem("Load");
        MenuItem fItem3 = new MenuItem("Import from Taiga");
        fItem3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();

                TaigaWindow taigaWindow = new TaigaWindow(backlogs, teams);
                taigaWindow.setStage(stage);
                Scene scene = new Scene(taigaWindow, 400, 300);
                stage.setScene(scene);
                stage.setTitle("Taiga Import");
                stage.show();
                stage.setOnCloseRequest(e -> {
                    System.out.println("HERE");
                    if (taigaWindow.getBacklog() != null && taigaWindow.getBacklog().getTotalPoints() > 0) {
                        System.out.println("BRO");
                        backlog = taigaWindow.getBacklog();
                        loadData();
                    }
                });
            }
        });
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
        fileMenu.getItems().addAll(fItem1, fItem2, fItem3, fItem4, fItem5, fItem6);

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
                if (team != null) {
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
                    stage.show();
                }

            }
        });
        MenuItem tItem3 = new MenuItem("Delete");
        teamMenu.getItems().addAll(tItem1, tItem2, tItem3);

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
        strategyMenu.getItems().addAll(sItem1, sItem2, sItem3);


        //Story menu
        Menu storyMenu = new Menu("User Story");
        MenuItem stItem1 = new MenuItem("Add");

        stItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                NewStoryWindow newStoryWindow = new NewStoryWindow();
                newStoryWindow.setStage(stage);
                newStoryWindow.setAlignment(Pos.CENTER);
                newStoryWindow.setHgap(10);
                newStoryWindow.setVgap(10);
                Scene scene = new Scene(newStoryWindow, 700, 500);
                stage.setScene(scene);
                stage.setTitle("Add User Story");
                stage.setResizable(false);
                stage.show();
            }
        });
        storyMenu.getItems().addAll(stItem1);

        MenuBar menuBar = new MenuBar();

        // Add menus
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(teamMenu);
        menuBar.getMenus().add(strategyMenu);
        menuBar.getMenus().add(storyMenu);
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
                if (newValue != null) {
                    backlog = backlogs.get(newValue.toString());
                    loadData();
                }
            }
        };

        ChangeListener teamChoiceBoxListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    team = teams.get(newValue.toString());
                    loadData();
                }
            }
        };

        toolBar.setTeamChoiceBoxListener(teamChoiceBoxListener);
        toolBar.setBacklogChoiceBoxListener(backlogChoiceBoxListener);
        toolBar.setBurnUpButtonAction((event) -> ChartWindow.display(burnupChart));
        toolBar.setStartButtonAction((event) -> sprint.runSprint());

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

}
