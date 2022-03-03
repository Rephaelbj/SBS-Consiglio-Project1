package se.bettercode.scrum.team;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.BeforeClass;
import org.junit.Test;

public class DeleteLoadedTeamTest {
	static SelectableTeams teams;

	@BeforeClass	
	public static void setUpBeforeClass() throws Exception {
		try {
			teams = new SelectableTeams();
		} catch(FileNotFoundException e) {
			fail("File not found");
		}
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
