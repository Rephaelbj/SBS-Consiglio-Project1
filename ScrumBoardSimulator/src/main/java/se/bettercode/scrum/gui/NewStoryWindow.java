package se.bettercode.scrum.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class NewStoryWindow extends GridPane {

    //Textfields
    TextArea verbsF = new TextArea();
    TextArea articlesF = new TextArea();
    TextArea subjectF = new TextArea();

    //Labels
    Label verbsL = new Label("Add verbs: ");
    Label articlesL = new Label("Add articles: ");
    Label subjectL = new Label("Add subject: ");

    //Button
    Button createStory = new Button("Add to Auto-generated List of User Stories");
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
    }

    private void appendToFile(){

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

//    public NewStoryWindow(SelectableBacklogs backlogs)


}
