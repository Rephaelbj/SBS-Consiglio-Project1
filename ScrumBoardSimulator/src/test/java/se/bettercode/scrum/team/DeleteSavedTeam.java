package se.bettercode.scrum.team;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
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
		
		String path = System.getProperty("user.home") + "/Desktop/SBS Program/Test.txt";
        String folderPath = System.getProperty("user.home") + "/Desktop/SBS Program";
        File file = new File(path);
        File file1 = new File(folderPath);
        try {
            if (file.exists()) {
                FileOutputStream fs = new FileOutputStream(file,true);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fs));
                writer.write(tpb.getName()+ "|" + tpb.velocityProperty().get() + "|" + tpb.maturityProperty().getValue() + "\n");
                writer.write(tts.getName()+ "|" + tts.velocityProperty().get() + "|" + tts.maturityProperty().getValue() + "\n");
                writer.write(koth.getName()+ "|" + koth.velocityProperty().get() + "|" + koth.maturityProperty().getValue() + "\n");
                writer.close();
            } else {
                if(!file1.exists())
                {
                    file1.mkdirs();
                }
                FileOutputStream fs = new FileOutputStream(file);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fs));
                writer.write(tpb.getName()+ "|" + tpb.velocityProperty().get() + "|" + tpb.maturityProperty().getValue() + "\n");
                writer.write(tts.getName()+ "|" + tts.velocityProperty().get() + "|" + tts.maturityProperty().getValue() + "\n");
                writer.write(koth.getName()+ "|" + koth.velocityProperty().get() + "|" + koth.maturityProperty().getValue() + "\n");
                writer.close();
            }
        }
        catch(Exception e) {}
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
