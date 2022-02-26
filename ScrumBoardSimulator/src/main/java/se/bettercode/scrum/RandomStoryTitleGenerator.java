package se.bettercode.scrum;

import org.mockito.internal.matchers.Null;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class RandomStoryTitleGenerator {

    static String[] verbs;
    static String[] articles;
    static String[] subject;
    String filePath;

    public RandomStoryTitleGenerator() {


        filePath = System.getProperty("user.home") + "/Desktop/SBS Program/words.txt";
        System.out.println(filePath);
        File file = new File(filePath);


        try{
            BufferedReader br
                    = new BufferedReader(new FileReader(file));
            String st;
            int count = 0;
            while ((st = br.readLine()) != null){
                count++;
                // Print the string
                String[] array = st.split(",");
                if(count == 1){
                    verbs = array;
                }else if (count == 2){
                    articles = array;
                }else{
                    subject = array;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Random random = new Random();

    public String generateOne() {
        int randomVerbIndex = random.nextInt(verbs.length);
        int randomSubjectIndex = random.nextInt(subject.length);
        int randomArticleIndex = random.nextInt(articles.length);
        return verbs[randomVerbIndex] + " " + articles[randomArticleIndex] + " "+ subject[randomSubjectIndex];
    }

    public ArrayList<String> generate(int n) {
        final int MAX_ALLOWED_N = verbs.length * subject.length* articles.length;

        if (n > MAX_ALLOWED_N) {
            throw new IllegalArgumentException("Max value of n is " + MAX_ALLOWED_N);
        }

        String title;
        ArrayList<String> nRandomTitles = new ArrayList<String>();
        while (nRandomTitles.size() < n) {
            title = this.generateOne();
            if (!isStringInList(title, nRandomTitles)) {
                nRandomTitles.add(title);
            }
        }
        return nRandomTitles;
    }

    private boolean isStringInList(String search, List<String> list) {
        return list.stream().anyMatch(p -> p.equals(search));
    }
}
