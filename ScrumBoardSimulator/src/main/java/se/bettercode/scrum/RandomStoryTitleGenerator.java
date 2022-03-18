package se.bettercode.scrum;



import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RandomStoryTitleGenerator {

    static String[] verbs;
    static String[] articles;
    static String[] subject;
    String filePath = System.getProperty("user.home") + "/Desktop/SBS Program/words.txt";
    String folderPath =System.getProperty("user.home") + "/Desktop/SBS Program";
    File file = new File(filePath);
    File createdFile= new File(folderPath);

    public RandomStoryTitleGenerator() {
        try{
            if (file.exists()){
                readSetArrays(file);
            }else{
                if(!createdFile.exists()){
                    createdFile.mkdirs();
                }
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write("Create,Update,Remove,Refactor,Implement,Rewrite,Make,Redesign,Reduce,Add,Merge,Save,Get,Integrate,Provide,Reset,Fix,Change,Modify,Import,Authenticate\n");
                bw.write("a,the\n");
                bw.write("GUI,Merchant Service,Sendout,Customer Callback,Retention Service,Checkout,German Store,French Store,Nordic Suffering,User Tracking,Out-out Service,Self Service,Facebook Integration,G+ Integration,PSP Integration,Bank Integration,User ID Integration,Deposit Service,Statistics Overview,Spike,Random Idea,API,API Features and Specifications,Interface for User,Login for User,Map Viewer Integration\n");
                bw.close();
                readSetArrays(file);
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readSetArrays(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        int count =0;
        while((st= br.readLine())!=null){
            count++;
            String[] array = st.split(",");
            if (count==1){
                verbs = array;
            }else if(count==2){
                articles =array;
            }
            else{
                subject=array;
            }
        }
    }

    private Random random = new Random();


    static public void updateFile(String[] subjects, String[] articles, String[] verbs) throws IOException {
        // Delete the file
        String filePath = System.getProperty("user.home") + "/Desktop/SBS Program/words.txt";
        String folderPath =System.getProperty("user.home") + "/Desktop/SBS Program";
        File folder = new File(folderPath);
        File file = new File(filePath);
        if(folder.exists())
        {
            if(file.exists())
                file.delete();

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            int counter = 0;
            for(String verb : verbs)
            {
                counter++;
                if(counter < verbs.length)
                    bw.write(verb+",");
                else
                    bw.write(verb);

            }
            bw.write("\n");
            counter = 0;
            for(String article : articles)
            {
                counter++;
                if(counter < articles.length)
                    bw.write(article+",");
                else
                    bw.write(article);
            }
            bw.write("\n");
            counter = 0;
            for(String subject : subjects)
            {
                counter++;
                if(counter < subjects.length)
                    bw.write(subject+",");
                else
                    bw.write(subject);
            }
            bw.write("\n");
            bw.close();
            readSetArrays(file);
        }
    }

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
