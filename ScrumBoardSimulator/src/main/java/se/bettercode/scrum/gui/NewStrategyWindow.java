package se.bettercode.scrum.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class NewStrategyWindow extends GridPane {

    // Textfields
    TextField nameF = new TextField();
    TextField pointsF = new TextField();
    TextField storiesF = new TextField();
    TextField pointLimitF = new TextField();

    // Labels
    Label nameL = new Label("Strategy Name:");
    Label pointsL = new Label("Number of Points in Backlog:");
    Label storiesL = new Label("Number of Stories:");
    Label pointLimitL = new Label("Points per Story(optional):");

    //Button
    Button createB = new Button("Create Strategy");

    public NewStrategyWindow()
    {
        this.add(nameL, 0 ,0);
        this.add(nameF, 1 ,0);
        this.add(pointsL, 0 ,1);
        this.add(pointsF, 1 ,1);
        this.add(storiesL, 0 ,2);
        this.add(storiesF, 1 ,2);
        this.add(pointLimitL, 0 ,3);
        this.add(pointLimitF, 1 ,3);
        this.add(createB, 0,4,2,1);
    }
}
