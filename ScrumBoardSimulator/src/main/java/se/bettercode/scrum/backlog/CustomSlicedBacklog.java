package se.bettercode.scrum.backlog;

import se.bettercode.scrum.RandomStoryTitleGenerator;
import se.bettercode.scrum.Story;

import java.util.ArrayList;

public class CustomSlicedBacklog extends Backlog {

    private int storyCount = 10;
    private int pointCount = 0;
    private int pointsPerStory = 0;
    public CustomSlicedBacklog() {
        super("palceholder");
         storyCount = 0;
         pointCount = 0;
         pointsPerStory = 0;

    }
    public void setStoryCount(int num){
        storyCount = num;
    }

    public void setPointCount(int parseInt) {
        pointCount = parseInt;
    }
    public int getPointCount(){
        return pointCount;
    }
    public void init(){
        ArrayList<String> storyTitles = (new RandomStoryTitleGenerator()).generate(storyCount);
        for(int i = 0; i < storyCount; i++){
            addStory(new Story(3, storyTitles.get(i)));
        }
    }

    public void setPointsPerStory(int parseInt) {
        pointsPerStory = parseInt;
    }
    public int getPointsPerStory() {
        return pointsPerStory;
    }
}
