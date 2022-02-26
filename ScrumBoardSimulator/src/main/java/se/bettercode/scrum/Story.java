package se.bettercode.scrum;


import java.util.Random;

public class Story {

    private StoryDays storyDays = new StoryDays();

    public enum StoryState {TODO, STARTED, FINISHED;}
    public int happyValue = 0;
    public int buisnessValue = 0;
    private StoryPointSet storyPointSet;

    private StoryStateProperty status = new StoryStateProperty();
    private String title = "";

    public Story(int points) {
        this(points, "");
        Random r = new Random();
        happyValue = r.nextInt(4);
    }

    public Story(int points, String title) {
        if (points < 0) {
            throw new IllegalArgumentException("Points must not be negative.");
        }
        Random r = new Random();
        happyValue = r.nextInt(4);
        buisnessValue = r.nextInt(6);
        this.title = title;
        storyPointSet = new StoryPointSet(points);
    }

    public StoryStateProperty statusProperty() {
        return status;
    }

    public StoryPoint getTotalPoints() {
        return storyPointSet.getTotal();
    }

    public int getTotalPointsAsInt() {
        return getTotalPoints().getPoints();
    }

    public StoryPoint getPointsDone() {
        return storyPointSet.getDone();
    }

    public int getPointsDoneAsInt() {
        return getPointsDone().getPoints();
    }

    public StoryState getStatus() {
        return status.getState();
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLeadTime() {
        return storyDays.getLeadTime();
    }

    /**
     *
     * @param points
     * @return any leftover points
     */
    public int workOnStory(int points, int day) {
        if (status.getState() == StoryState.TODO) {
            status.setState(StoryState.STARTED);
            storyDays.setStartedDay(day);
        }

        int leftover = 0;
        int pointsToApply;

        if (points >= getRemainingPoints()) {
            pointsToApply = getRemainingPoints();
            storyPointSet.apply(pointsToApply);
            leftover = points - pointsToApply;
            status.setState(StoryState.FINISHED);
            storyDays.setDoneDay(day);
        } else { // points < getRemainingPoints()
            storyPointSet.apply(points);
        }

        return leftover;
    }

    public int getRemainingPoints() {
        return storyPointSet.getRemaining().getPoints();
    }

    public void setBuisnessValue(int buisnessValue){this.buisnessValue = buisnessValue;}
    public String getBuisnessValue(){
        String value = "";
        switch (buisnessValue){
            case 0:
                value = "x-small";
                break;
            case 1:
                value = "small";
                break;
            case 2:
                value = "medium";
                break;
            case 3:
                value = "large";
                break;
            case 4:
                value = "x-large";
                break;
            case 5:
                value = "xx-large";
                break;
        }
        return value;
    }
    /**
     * This method sets the happy value of the story
     * @param newHappyValue
     */
    public void setHappyValue(int newHappyValue)
    {
        this.happyValue = newHappyValue;
    }

    /**
     * This method gets the happy value for the story
     * @return
     */
    public String getHappyValue()
    {
        String returnValue = "";
        switch(happyValue)
        {
            case 0:
                returnValue = "Sad";
                break;
            case 1:
                returnValue = "Mad";
                break;
            case 2:
                returnValue = "Happy";
                break;
            case 3:
                returnValue = "Confused";
                break;
        }
        return returnValue;
    }

    @Override
    public String toString() {
        return "Story{" +
                "points=" + getTotalPoints().getPoints() +
                ", pointsDone=" + getPointsDone().getPoints() +
                ", status=" + status.getValue() +
                ", happiness=" + getHappyValue() +
                ", buisnessValue=" + getBuisnessValue() +
                '}';
    }
}
