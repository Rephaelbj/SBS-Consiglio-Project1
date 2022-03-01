package se.bettercode.scrum.team;

import org.junit.Before;
import org.junit.Test;
import se.bettercode.scrum.backlog.CustomSlicedBacklog;
import se.bettercode.scrum.backlog.SelectableBacklogs;

import static org.junit.Assert.assertTrue;

public class TestStrategy {

    SelectableBacklogs backlogs;

    @Before
    public void setup() {
        backlogs = new SelectableBacklogs();
        backlogs.clear();
    }


    @Test
    public void testStrategy() {

        CustomSlicedBacklog custom = new CustomSlicedBacklog();
        custom.setName("Test");
        custom.setStoryCount(6);
        custom.setPointCount(40);
        custom.setPointsPerStory(1);

        backlogs.add(custom);
        assertTrue(backlogs.size(), 4);
        assertTrue(backlogs.get(custom.getName()) != null);
        backlogs.delete(custom);
        assertTrue(backlogs.size(), 3);
        assertTrue(backlogs.get(custom.getName()) == null);
    }
}
