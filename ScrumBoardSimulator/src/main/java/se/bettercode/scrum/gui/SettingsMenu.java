package se.bettercode.scrum.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SettingsMenu extends GridPane {
    TextField audio = new TextField();
    TextField working = new TextField();
    TextField complete = new TextField();
    TextField taskColor = new TextField();
    TextField windowColor = new TextField();
    TextField borderColor = new TextField();
    Button ok = new Button("Confirm");
    private Stage stage;

    public SettingsMenu(Stage stage)
    {
        this.stage = stage;

        Label audioPrompt = new Label("Audio Setting(ex: mute/custom)");
        Label workingPrompt = new Label("Working noise(ex: custom.mp3)");
        Label completePrompt = new Label("Complete noise(ex: custom.mp3)");
        Label taskColorPrompt = new Label("Task Color(ex: hexNum/default)");
        Label windowColorPrompt = new Label("Window Color(ex: hexNum/default)");
        Label borderColorPrompt = new Label("Border Color(ex: hexNum/default)");

        this.add(audioPrompt, 0 ,0);
        this.add(audio, 1 ,0);
        this.add(workingPrompt, 0 ,1);
        this.add(working, 1 ,1);
        this.add(completePrompt, 0 ,2);
        this.add(complete, 1 ,2);
        this.add(taskColorPrompt, 0 ,3);
        this.add(taskColor, 1 ,3);
        this.add(windowColorPrompt, 0 ,4);
        this.add(windowColor, 1 ,4);
        this.add(borderColorPrompt, 0 ,5);
        this.add(borderColor, 1 ,5);
        this.add(ok, 0,6,2,1);

        ok.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                try {
                    stage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
