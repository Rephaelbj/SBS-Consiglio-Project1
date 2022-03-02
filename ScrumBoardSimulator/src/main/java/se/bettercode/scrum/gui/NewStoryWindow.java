package se.bettercode.scrum.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import se.bettercode.scrum.RandomStoryTitleGenerator;
import se.bettercode.scrum.ScrumGameApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class NewStoryWindow extends GridPane {

    Application app;

    //TextAreas
    TextArea verbsF = new TextArea();
    TextArea articlesF = new TextArea();
    TextArea subjectF = new TextArea();

    //Labels
    Label verbsL = new Label("Add verbs: ");
    Label articlesL = new Label("Add articles: ");
    Label subjectL = new Label("Add subject: ");

    //Button
    Button createStory = new Button("Update Word File");

    private Stage currentStage = null;

    public NewStoryWindow(){
        loadWords();
        verbsF.setWrapText(true);
        articlesF.setWrapText(true);
        subjectF.setWrapText(true);
        this.add(verbsL, 0,0);
        this.add(verbsF, 1,0);
        this.add(articlesL,0,1);
        this.add(articlesF, 1, 1);
        this.add(subjectL,0,2);
        this.add(subjectF,1,2);
        this.add(createStory,0,4,2,1);
        createStory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    saveFile();
                    ((ScrumGameApplication) app).resetApp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveFile() throws IOException {
        String[] subjects = subjectF.getText().split(",");
        String[] articles = articlesF.getText().split(",");
        String[] verbs = verbsF.getText().split(",");
        RandomStoryTitleGenerator.updateFile(subjects,articles,verbs);
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    public void loadWords(){
        try{
            File file = new File(System.getProperty("user.home") + "/Desktop/SBS Program/words.txt");
            if(file.exists()){
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                int count = 0;
                while((st= br.readLine())!=null){
                    if (count == 0){
                        verbsF.setText(st);
                    }else if(count == 1){
                        articlesF.setText(st);
                    }else{
                        subjectF.setText(st);
                    }
                    count++;
                }
            }
        }catch (Exception e){

        }
    }

    public void setApp(Application appRef){
        this.app = appRef;
    }
}
