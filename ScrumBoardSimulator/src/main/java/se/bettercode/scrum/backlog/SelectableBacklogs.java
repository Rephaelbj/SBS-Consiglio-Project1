package se.bettercode.scrum.backlog;

import se.bettercode.utils.Selectable;

import java.io.*;
import java.util.ArrayList;

public class SelectableBacklogs extends Selectable<Backlog> {
    ArrayList<Backlog> logs;
    Backlog smallBacklog = new SmallBacklog();
    Backlog wellSlicedBacklog = new WellSlicedBacklog();
    Backlog poorlySlicedBacklog = new PoorlySlicedBacklog();

    public SelectableBacklogs() {
        logs = loadBacklogs();
        if(logs != null){
            logs.forEach((strat)->{put(strat.getName(), strat);});
        }
        put(smallBacklog.getName(), smallBacklog);
        put(wellSlicedBacklog.getName(), wellSlicedBacklog);
        put(poorlySlicedBacklog.getName(), poorlySlicedBacklog);
    }
    private ArrayList<Backlog> loadBacklogs() {
        ArrayList<Backlog> create = new ArrayList<Backlog>();
        try {
            BufferedReader properties = new BufferedReader(new InputStreamReader(new FileInputStream(new File(System.getProperty("user.home") + "/Desktop/SBS Program//StrategySave.txt"))));
            CustomSlicedBacklog item = new CustomSlicedBacklog();
            int count = 0;
            for (String option = ""; option != null; option = properties.readLine()) {
                if(option.length()>=1){
                    if(count < 4){
                        if(count == 0){
                            item.setName(option);
                        }else if(count == 1){
                            item.setPointCount(Integer.parseInt(option.replace("\n", "")));
                        }else if(count == 2){
                            item.setStoryCount(Integer.parseInt(option.replace("\n", "")));
                        }else if(count == 3){
                            item.setPointsPerStory(Integer.parseInt(option.replace("\n", "")));
                            item.init();
                            create.add(item);
                        }
                    }else{
                        count = 0;
                        item = new CustomSlicedBacklog();
                        item.setName(option);
                    }
                    count++;
                }else if(count == 3){
                    item.init();
                    create.add(item);
                    count++;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return create;
    }

    public void clear() {
        logs = new ArrayList<Backlog>();
        empty();
        put(smallBacklog.getName(), smallBacklog);
        put(wellSlicedBacklog.getName(), wellSlicedBacklog);
        put(poorlySlicedBacklog.getName(), poorlySlicedBacklog);
        try{
            File file = new File(System.getProperty("user.home") + "/Desktop/SBS Program/StrategySave.txt");
            file.delete();
            file = new File(System.getProperty("user.home") + "/Desktop/SBS Program/StrategySave.txt");
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void add(CustomSlicedBacklog custom) {
        put(custom.getName(), custom);
        try{
            File file = new File(System.getProperty("user.home") + "/Desktop/SBS Program/StrategySave.txt");
            FileWriter write = new FileWriter(file, true);
            BufferedWriter addText = new BufferedWriter(write);
            addText.write(custom.getName());
            addText.newLine();
            addText.write(custom.getPointCount()+"");
            addText.newLine();
            addText.write(custom.getStories().size()+"");
            addText.newLine();
            addText.write(custom.getPointsPerStory()+"");
            addText.newLine();
            addText.close();
            write.close();
            file.createNewFile();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public int size() {
        return getSize();
    }

    public void delete(CustomSlicedBacklog custom) {
        remove(custom.getName());
        clear();
        put(smallBacklog.getName(), smallBacklog);
        put(wellSlicedBacklog.getName(), wellSlicedBacklog);
        put(poorlySlicedBacklog.getName(), poorlySlicedBacklog);
        for (Backlog log : logs) {
            put(log.getName(), log);
        }
    }
}
