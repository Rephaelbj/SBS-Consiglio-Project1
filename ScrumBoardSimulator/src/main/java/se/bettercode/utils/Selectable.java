package se.bettercode.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class Selectable<T> {

    private HashMap<String, T> map = new HashMap<>();

    public T get(String key) {
        return map.get(key);
    }

    public void put(String key, T value) {
        map.put(key, value);
    }

    public String[] getKeys() {
        return map.keySet().toArray(new String[map.size()]);
    }
    
    public void deleteTeam(String key) {
    	// Remove from collection
    	map.remove(key);
    	
    	// Remove from save file
    	String output = "";
        String path = System.getProperty("user.home") + "/Desktop/SBS Program/Teams.txt";
        File file = new File(path);
        try {
            if (file.exists()) {
                String input = "";
                FileInputStream fi = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fi));
                while((input = reader.readLine()) != null)
                {
                    String[] values = input.split("\\|");
                    if (values.length >= 2) {
                    	if (!values[0].equals(key)) {
                    		output += input + "\n";
                    	}
                    }                    
                }
                System.out.println("output: " + output);
                reader.close();
                fi.close();
                file.delete();
            }
            file = new File(path);
            FileOutputStream fs = new FileOutputStream(file,true);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fs));
            writer.write(output);
            writer.close();
        } catch (FileNotFoundException fnfe) {} catch (IOException ioe) {}
    }
}
