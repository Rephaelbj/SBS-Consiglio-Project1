package se.bettercode.scrum.gui;

import com.sun.glass.ui.Window;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import se.bettercode.scrum.backlog.Backlog;
import se.bettercode.scrum.backlog.CustomSlicedBacklog;
import se.bettercode.scrum.backlog.SelectableBacklogs;

import java.io.*;
import java.nio.file.Path;

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
    private Stage currentStage = null;

    public NewStrategyWindow(SelectableBacklogs backlogs)
    {
        createB.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                CustomSlicedBacklog strategy = new CustomSlicedBacklog();
                strategy.setName(nameF.getText());
                System.out.println("read in "+storiesF.getText()+ " "+ pointsF.getText());
                try{
                    strategy.setStoryCount(Integer.parseInt(storiesF.getText()));
                    strategy.setPointCount(Integer.parseInt(pointsF.getText()));
                    if(pointsF.getText().equals("")){
                        strategy.setPointsPerStory(0);
                    }
                    try{
                        if(Integer.parseInt(pointsF.getText()) >= 0){
                            strategy.setPointsPerStory(Integer.parseInt(pointsF.getText()));
                        }
                    }catch(Exception e){
                        strategy.setPointsPerStory(0);
                        System.out.println("error addingwith points per story");
                    }
                    strategy.init();
                    appendToFile(strategy);
                    backlogs.put(strategy.getName(),strategy);
                    System.out.println("added a new strategy " + strategy.getName());
                    currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
                }catch(Exception e){
                    strategy.setStoryCount(1);
                    strategy.setPointCount(1);
                    strategy.setPointsPerStory(0);
                    System.out.println("need to put numbers in for last three inputs");
                }
            }

        });
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
    private void appendToFile(CustomSlicedBacklog backlog)
    {
        System.out.println("add to file "+backlog.getName()+" "+ backlog.getPointCount()+" "+backlog.getStories().size()+" "+backlog.getPointsPerStory());
        try{
            System.out.println(getClass().getResource("/StrategySave.txt").getFile().toString());
            File file = new File(getClass().getResource("/StrategySave.txt").getFile().toString());
            FileWriter write = new FileWriter(file, true);
            BufferedWriter addText = new BufferedWriter(write);
            addText.write(backlog.getName());
            addText.newLine();
            addText.write(backlog.getPointCount()+"");
            addText.newLine();
            addText.write(backlog.getStories().size()+"");
            addText.newLine();
            addText.write(backlog.getPointsPerStory()+"");
            addText.newLine();
            addText.close();
            write.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public void setStage(Stage stage){
        this.currentStage = stage;
    }

}
