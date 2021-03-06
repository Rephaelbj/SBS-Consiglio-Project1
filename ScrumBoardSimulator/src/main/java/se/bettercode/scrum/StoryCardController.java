package se.bettercode.scrum;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;

public class StoryCardController extends BorderPane {

    public static final int MEDIUM_STORY_POINTS = 5;

    @FXML
    private Text storyPoints;

    @FXML
    private Text storyTitle;

    @FXML
    private BorderPane storyCard;

    private Story story;

    public StoryCardController(Story story, String taskColor) {
        this.story = story;
        URL location = getClass().getResource("StoryCard.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        if(!taskColor.contains("default")){
            if(taskColor.contains("red")) {
                storyCard.setStyle("-fx-background-color: #ff0000;");
            }else if(taskColor.contains("blue")){
                storyCard.setStyle("-fx-background-color: #0000ff;");
            }else if(taskColor.contains("green")){
                storyCard.setStyle("-fx-background-color: #00ff00;");
            }else{
                storyCard.setStyle("-fx-background-color: #"+taskColor+";");
            }
        }
        storyTitle.setText(story.getTitle());
        storyPoints.setText(Integer.toString(story.getPointsDone().getPoints()) +
                "/" + Integer.toString(story.getTotalPoints().getPoints()));
        setPrefHeight(getHeightBasedOnStoryPoints());
    }

    public void bindStoryTitle(StringProperty title) {
        storyTitle.textProperty().bind(title);
    }

    public void bindStoryPoints(IntegerProperty points) {
        storyPoints.textProperty().bind(Bindings.convert(points));
    }

    private double getHeightBasedOnStoryPoints() {
        if (story.getTotalPoints().getPoints() > MEDIUM_STORY_POINTS) {
            return 110;
        } else if (story.getTotalPoints().getPoints() < MEDIUM_STORY_POINTS) {
            return 45;
        } else {
            return 80;
        }
    }
}
