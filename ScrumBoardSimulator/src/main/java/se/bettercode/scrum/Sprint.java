package se.bettercode.scrum;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import se.bettercode.scrum.backlog.Backlog;
import se.bettercode.scrum.team.Team;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Sprint {

    public static final int SLEEP_MILLIS = 500;
    private String name;
    private IntegerProperty lengthInDays = new SimpleIntegerProperty(0);
    private Team team;
    private Backlog backlog;
    private IntegerProperty currentDay = new SimpleIntegerProperty(0);
    private BooleanProperty running = new SimpleBooleanProperty(false);

    public Sprint(String name, int lengthInDays, Team team, Backlog backlog) {
        this.name = name;
        this.lengthInDays.set(lengthInDays);
        this.team = team;
        this.backlog = backlog;
    }

    public Backlog getBacklog() {
        return backlog;
    }

    public int getLengthInDays() {
        return lengthInDays.get();
    }

    public IntegerProperty lengthInDaysProperty() {
        return lengthInDays;
    }

    public int getDailyBurnrate() {
        return team.velocityProperty().get() / lengthInDays.get();
    }

    public int getCurrentDay() {
        return currentDay.get();
    }

    public IntegerProperty currentDayProperty() {
        return currentDay;
    }

    public boolean getRunning() {
        return running.get();
    }

    public boolean isDone() {
        return getLengthInDays() == getCurrentDay();
    }

    public BooleanProperty runningProperty() {
        return running;
    }

    public void runSprint() {
        if (team == null || backlog == null) {
            throw new IllegalArgumentException("Team and Backlog must both be set before running Sprint");
        }

        int dailyBurn = getDailyBurnrate();

        System.out.println("Running Sprint simulation with team " + team + " for Sprint \"" + name + "\" for " + lengthInDays.get() + " days...");
        System.out.println(backlog);
        System.out.println("Total backlog size is " + backlog.getTotalPoints() + " points.");
        System.out.println("Burning through backlog at " + dailyBurn + " points per day.");

        DateFormat dateFormat = new SimpleDateFormat("MMM/dd/yyyy");
        Calendar cal = Calendar.getInstance();


        new Thread() {

            @Override
            public void run() {
                setRunning(true);
                for (int day=0; day<=lengthInDays.get(); day++) {
                    setCurrentDay(day);

                    // Happiness Value
                    String happiness_value;
                    Random random = new Random();
                    int rand = 0;
                    while (true){
                        rand = random.nextInt(5);
                        if(rand !=0) break;
                    }
                    switch (rand){
                        case 1:
                            happiness_value = "sad";
                            break;
                        case 2:
                            happiness_value = "mad";
                            break;
                        case 3:
                            happiness_value = "confused";
                            break;
                        case 4:
                            happiness_value = "happy";
                            break;
                        default:
                            happiness_value = "Neutral";
                            break;
                    }

                    System.out.println(dateFormat.format(cal.getTime()) + ": " + backlog.getFinishedStoriesCount() + " finished stories in total. Happiness Value: " + happiness_value);

                    cal.add(Calendar.DATE, 1);
                    dateFormat.format(cal.getTime());

                    backlog.runDay(dailyBurn, day);
                    sleepThread();
                    if (backlog.isFinished()) {
                        break;
                    }
                }
                setRunning(false);
                System.out.println(backlog);
                System.out.println("A total of " + backlog.getFinishedPoints() + " points have been finished!");
                System.out.println("Wasted " + backlog.getWorkInProgressPoints() + " points");
            }

        }.start();
    }

    private void sleepThread() {
        try {
            Thread.sleep(SLEEP_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentDay(int day) {
        Platform.runLater(() -> {currentDay.set(day);});
    }

    private void setRunning(boolean running) {
        Platform.runLater(() -> this.running.set(running));
    }

}
