package se.bettercode.scrum.team;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.junit.BeforeClass;
import org.junit.Test;

import se.bettercode.utils.Selectable;

public class DeleteSavedTeam {
	static Selectable<Team> teams;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		teams = new Selectable<Team>();
		TeamImpl tpb = new TeamImpl("Trailor Park Boys", 23, "Expert");
		TeamImpl tts = new TeamImpl("The Three Stooges", 51, "Established");
		TeamImpl koth = new TeamImpl("King of the Hill", 35, "Beginner");
		
		String path = System.getProperty("user.home") + "/Desktop/SBS Program/Teams.txt";
        String folderPath = System.getProperty("user.home") + "/Desktop/SBS Program";
        File file = new File(path);
        try {
            if (file.exists()) file.delete();
            FileOutputStream fs = new FileOutputStream(file,true);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fs));
            writer.write(tpb.getName()+ "|" + tpb.velocityProperty().get() + "|" + tpb.maturityProperty().getValue() + "\n");
            writer.write(tts.getName()+ "|" + tts.velocityProperty().get() + "|" + tts.maturityProperty().getValue() + "\n");
            writer.write(koth.getName()+ "|" + koth.velocityProperty().get() + "|" + koth.maturityProperty().getValue() + "\n");
            writer.close();
        }
        catch(Exception e) {}
	}

	@Test
	public void test() {
		teams.deleteTeam("The Three Stooges");
		
		String expectedFileContents = "Trailor Park Boys|23|ExpertKing of the Hill|35|Beginner";
		
		String actualFileContents = "";
		String path = System.getProperty("user.home") + "/Desktop/SBS Program/Teams.txt";
        File file = new File(path);
        try {
            if (file.exists()) {
                String input = "";
                FileInputStream fi = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fi));
                while((input = reader.readLine()) != null)
                	actualFileContents += input;
            } else fail("File not found!");
        }
        catch(Exception e) {}
        actualFileContents = actualFileContents.trim();
		
        System.out.println(actualFileContents);
		assertEquals(expectedFileContents, actualFileContents);
	}

}
