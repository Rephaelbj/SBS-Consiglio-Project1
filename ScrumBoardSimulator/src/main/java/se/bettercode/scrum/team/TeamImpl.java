package se.bettercode.scrum.team;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TeamImpl implements Team {

    private StringProperty name;
    private IntegerProperty velocity;

    private IntegerProperty workInProgressLimit;
    
    private StringProperty maturity;
    
    public enum MaturityLevel { BEGINNER, ESTABLISHED, EXPERT }
    public MaturityLevel mLevel;

    public TeamImpl(String name, int velocity) {
        this(name, velocity, 1);
        maturity = new SimpleStringProperty();
        maturity.setValue("Established");
        mLevel = MaturityLevel.ESTABLISHED; 
    }
    
    public TeamImpl(String name, int velocity, String maturity) {
        this(name, velocity, 1);
        this.maturity = new SimpleStringProperty();
        switch (maturity) {
        case "Beginner":
        	this.maturity.setValue("Beginner");
            mLevel = MaturityLevel.BEGINNER;
            break;
        case "Established":
        	this.maturity.setValue("Established");
            mLevel = MaturityLevel.ESTABLISHED;
            break;
        case "Expert":
        	this.maturity.setValue("Expert");
            mLevel = MaturityLevel.EXPERT;
            break;
        }
         
    }

    public TeamImpl(String name, int velocity, int workInProgressLimit) {
        if (workInProgressLimit < 1) {
            throw new IllegalArgumentException("workInProgressLimit must be 1 or greater.");
        }
        this.name = new SimpleStringProperty(name);
        this.velocity = new SimpleIntegerProperty(velocity);
        this.workInProgressLimit = new SimpleIntegerProperty(workInProgressLimit);
        maturity = new SimpleStringProperty();
        maturity.setValue("Established");
        mLevel = MaturityLevel.ESTABLISHED; 
    }

    @Override
    public final StringProperty nameProperty() {
        return this.name;
    }

    @Override
    public final void setName(String name) {
        this.name.setValue(name);
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    @Override
    public final IntegerProperty velocityProperty() {
        return velocity;
    }

    @Override
    public final void setVelocity(int velocity) {
        this.velocity.set(velocity);
    }

    @Override
    public int getWorkInProgressLimit() {
        return workInProgressLimit.get();
    }

    @Override
    public IntegerProperty workInProgressLimitProperty() {
        return workInProgressLimit;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name=" + name.getValue() +
                ", velocity=" + velocity.get() +
                ", WIP limit=" + workInProgressLimit.get() +
                '}';
    }
    
    @Override
    public void setMaturity(String m) {
    	switch (m) {
	    	case "Beginner":
	    		maturity.set("Beginner");
	    		mLevel = MaturityLevel.BEGINNER;
	    		break;
	    	case "Established":
	    		maturity.set("Established");
	    		mLevel = MaturityLevel.ESTABLISHED;
	    		break;
	    	case "Expert":
	    		maturity.set("Expert");
	    		mLevel = MaturityLevel.EXPERT;
	    		break;
    	}
    	System.out.println("Team: " + name.getValue() + " maturity level set to " + m + "!");
    }
    
    @Override
    public final StringProperty maturityProperty() {
    	switch (mLevel) {
	    	case BEGINNER:
	    		maturity.set("Beginner");
	    		break;
	    	case ESTABLISHED:
	    		maturity.set("Established");
	    		break;
	    	case EXPERT:
	    		maturity.set("Expert");
	    		break; 	
    	}
    	return this.maturity;
    }
}
