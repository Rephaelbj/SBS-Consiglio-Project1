package se.bettercode.scrum.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class ToolBar extends HBox {

    private final Button startButton = new Button("Start Sprint");
    private final Button editButton = new Button("Edit Team");
    private final Button burndownButton = new Button("Burndown Chart");
    private final Region buttonSpacer = new Region();
    private final Button resetGameButton = new Button("Reset Game");
    private ChoiceBox<String> teamChoiceBox = new ChoiceBox<>();
    private ChoiceBox<String> backlogChoiceBox = new ChoiceBox<>();
    private ChoiceBox<String> burnChartChoiceBox = new ChoiceBox<>();

    private Button burnUpChartButton = new Button("Burn Up Chart");

    public ToolBar(String[] teams, String[] backlogs) {
        setPadding(new Insets(15, 12, 15, 12));
        setSpacing(10);
        setStyle("-fx-background-color: #336699;");

        teamChoiceBox.setItems(FXCollections.observableArrayList(teams));
        teamChoiceBox.setTooltip(new Tooltip("Select team"));
        teamChoiceBox.setPrefWidth(100);

        backlogChoiceBox.setItems(FXCollections.observableArrayList(backlogs));
        backlogChoiceBox.setTooltip(new Tooltip("Select backlog"));
        backlogChoiceBox.setPrefWidth(100);

        burnUpChartButton.setPrefSize(150, 20);

        startButton.setPrefSize(100, 20);
        
        editButton.setPrefSize(100, 20);

        burndownButton.setPrefSize( 130, 20);

        buttonSpacer.setPrefWidth(860);
        resetGameButton.setPrefSize(100, 20);

        getChildren().addAll(teamChoiceBox, backlogChoiceBox, burnUpChartButton, startButton, buttonSpacer, resetGameButton);

    }

    public void setBurnUpButtonAction(EventHandler<ActionEvent> eventHandler) {
        burnUpChartButton.setOnAction(eventHandler);
    }

    public void setStartButtonAction(EventHandler<ActionEvent> eventHandler) {
        startButton.setOnAction(eventHandler);
    }

    public void bindRunningProperty(BooleanProperty booleanProperty) {
        teamChoiceBox.disableProperty().bind(booleanProperty);
        backlogChoiceBox.disableProperty().bind(booleanProperty);
        burnChartChoiceBox.disableProperty().bind(booleanProperty);
        startButton.disableProperty().bind(booleanProperty);
    }

    public void setTeamChoiceBoxListener(ChangeListener<String> changeListener) {
        teamChoiceBox.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    public void setBacklogChoiceBoxListener(ChangeListener<String> changeListener) {
        backlogChoiceBox.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    public void setChartChoiceBoxListener(ChangeListener<String> changeListener) {
        burnChartChoiceBox.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    public void setStrategies(String[] backlogs) {
        backlogChoiceBox.setItems(FXCollections.observableArrayList(backlogs));
    }

    public void setTeams(String[] teams)
    {
        teamChoiceBox.setItems(FXCollections.observableArrayList(teams));
    }
    
    public void setEditButtonAction(EventHandler<ActionEvent> eventHandler) {
    	editButton.setOnAction(eventHandler);
    }

    public void setBurndownButtonAction(EventHandler<ActionEvent> eventHandler) { burndownButton.setOnAction(eventHandler);}

    public void setResetGameButtonAction(EventHandler<ActionEvent> eventHandler) {
    	resetGameButton.setOnAction(eventHandler);
    }
}
