package se.bettercode.scrum.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.awt.event.ActionEvent;
import java.beans.EventHandler;

public class NewStoryWindow extends GridPane {

    //Textfields
    TextField verbsF = new TextField();
    TextField articlesF = new TextField();
    TextField subjectF = new TextField();

    //Labels
    Label verbsL = new Label("Add verbs: ");
    Label articlesL = new Label("Add articles: ");
    Label subjectL = new Label("Add subject: ");

    //Button
    Button createStory = new Button("Add to Auto-generated List of User Stories");
    private Stage currentStage = null;

    public NewStoryWindow(){

        this.add(verbsL, 0,0);
        this.add(verbsF, 1,0);
        this.add(articlesL,0,1);
        this.add(articlesF, 1, 1);
        this.add(subjectL,0,2);
        this.add(subjectF,1,2);
        this.add(createStory,0,4,2,1);
    }

    private void appendToFile(){

    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }


//    public NewStoryWindow(SelectableBacklogs backlogs)


}
