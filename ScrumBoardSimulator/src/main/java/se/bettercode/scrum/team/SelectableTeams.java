package se.bettercode.scrum.team;

import se.bettercode.utils.Selectable;

import java.io.*;

public class SelectableTeams extends Selectable<Team> {

    public SelectableTeams() throws FileNotFoundException {
        readTeams();
    }


    public void readTeams() throws FileNotFoundException {
        // Check if Folder exist
        String path = System.getProperty("user.home") + "/Desktop/SBS Program/Teams.txt";
        String folderPath = System.getProperty("user.home") + "/Desktop/SBS Program";
        File file = new File(path);
        File file1 = new File(folderPath);
        try {
            if (file.exists()) {
                String input = "";
                FileInputStream fi = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fi));
                while((input = reader.readLine()) != null)
                {
                    String[] values = input.split("\\|");
                    if (values.length >= 3) {
                    	Team newTeam = new TeamImpl(values[0], Integer.parseInt(values[1]), values[2]);
                        put(newTeam.getName(), newTeam);
                    } else {
                    	Team newTeam = new TeamImpl(values[0], Integer.parseInt(values[1]));
                        put(newTeam.getName(), newTeam);
                    }
                    
                }
            } else {
                if(!file1.exists())
                {
                    file1.mkdirs();
                }
                FileOutputStream fs = new FileOutputStream(file);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fs));
                writer.write("Cobras|23\n");
                writer.write("The Smurfs|15\n");

                Team cobras = new TeamImpl("Cobras", 23);
                Team smurfs = new TeamImpl("The Smurfs", 15);

                put(cobras.getName(), cobras);
                put(smurfs.getName(), smurfs);
                writer.close();
            }
        }
        catch(Exception e)
        {
                System.out.println(e.toString());
        }
    }



    public void addTeam(Team team) throws IOException
    {
        put(team.getName(), team);
        String path = System.getProperty("user.home") + "/Desktop/SBS Program/Teams.txt";
        String folderPath = System.getProperty("user.home") + "/Desktop/SBS Program";
        File file = new File(path);
        File file1 = new File(folderPath);
        try {
            if (file.exists()) {
                FileOutputStream fs = new FileOutputStream(file,true);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fs));
                writer.write(team.getName()+ "|" + team.velocityProperty().get() + "|" + team.maturityProperty().getValue() + "\n");
                writer.close();
            } else {
                if(!file1.exists())
                {
                    file1.mkdirs();
                }
                FileOutputStream fs = new FileOutputStream(file);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fs));
                writer.write(team.getName()+ "|" + team.velocityProperty().get()+"\n");
                writer.close();
            }
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }

    }

}
