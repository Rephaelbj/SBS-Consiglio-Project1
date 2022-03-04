package se.bettercode.scrum.team;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import se.bettercode.utils.*;

public class DeleteLoadedTeamTest {
	static Selectable<Team> teams;

	@BeforeClass	
	public static void setUpBeforeClass() throws Exception {
			teams = new Selectable<Team>();
			TeamImpl tpb = new TeamImpl("Trailor Park Boys", 23, "Expert");
			TeamImpl tts = new TeamImpl("The Three Stooges", 51, "Established");
			TeamImpl koth = new TeamImpl("King of the Hill", 35, "Beginner");
			teams.put(tpb.getName(), tpb);
			teams.put(tts.getName(), tts);
			teams.put(koth.getName(), koth);
	}

	@Test
	public void test() {
		teams.deleteTeam("The Three Stooges");
		
		String expectedTeams = "Teams: Trailor Park Boys, King of the Hill";
		
		String actualTeams = "Teams: ";
		String[] teamNames = teams.getKeys();
		if (teamNames.length > 1) actualTeams += teamNames[0];
		for (int i = 1; i < teamNames.length; i++) {
			actualTeams += ", " + teamNames[i];
		}
		
		assertEquals(expectedTeams, actualTeams);
	}

}
