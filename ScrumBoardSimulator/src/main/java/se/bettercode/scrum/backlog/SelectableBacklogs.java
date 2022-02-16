package se.bettercode.scrum.backlog;

import se.bettercode.utils.Selectable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SelectableBacklogs extends Selectable<Backlog> {

    public SelectableBacklogs() {
        ArrayList<Backlog> logs = loadBacklogs();
        Backlog smallBacklog = new SmallBacklog();
        Backlog wellSlicedBacklog = new WellSlicedBacklog();
        Backlog poorlySlicedBacklog = new PoorlySlicedBacklog();
        logs.forEach((strat)->{put(strat.getName(), strat);});
        put(smallBacklog.getName(), smallBacklog);
        put(wellSlicedBacklog.getName(), wellSlicedBacklog);
        put(poorlySlicedBacklog.getName(), poorlySlicedBacklog);
    }

    private ArrayList<Backlog> loadBacklogs() {
        ArrayList<Backlog> create = new ArrayList<Backlog>();
        try {
            BufferedReader properties = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/StrategySave.txt")));
            CustomSlicedBacklog item = new CustomSlicedBacklog();
            int count = 0;
            for (String option = ""; option != null; option = properties.readLine()) {
                if(option.length()>=1){
                    System.out.println(option);
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
            e.printStackTrace();
        }
        return create;
    }
}
