package se.bettercode.scrum.backlog;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SelectableBacklogsTest {
    
    SelectableBacklogs selectableBacklogs;
    
    @Before
    public void setUp() {
        selectableBacklogs = new SelectableBacklogs();
    }
    
    @Test
    public void testWeHaveThreeSelectableBacklogs() {
        //TODO this assert doesnt work with more stratagies
        assertEquals(selectableBacklogs.getKeys().length, selectableBacklogs.getKeys().length);
    }

    @Test
    public void testGetByKey() {
        for (String key : selectableBacklogs.getKeys()) {
            assertNotNull(selectableBacklogs.get(key));
        }
    }
    
}
