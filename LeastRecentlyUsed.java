import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;


class LeastRecentlyUsed{

    //Generates a testing data file
    //textFile: the name of the data file to be outputted
    static private void generateReferenceStrings(String textFile) throws IOException
    {
        File dataFile = new File(textFile);
        BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile));
        Random random = new Random();
        List<String> randomList = new ArrayList<String>();
        int pageAmount = 8;
        
        //For the amount of unique page create a unique string of size 30 seven times and put in list
        for(int x = 0; x < pageAmount; x++)
        {
            StringBuilder sb = new StringBuilder("");
            for(int y = 0; y < 30; y++)
            {
                sb.append(random.nextInt(10));
            }
            randomList.add(sb.toString());
        }
        
        //selectes random string from array that contains random strings
        int amountOfStrings = 30;
        for(int x = 0; x < amountOfStrings; x++)
        {
            int selectedPosition = random.nextInt(randomList.size());
            bw.write(randomList.get(selectedPosition));
            bw.newLine();
        }

        bw.close();
    }


    //Executes optimal algorithm for page faults. It looks ahead of incoming pages and takes the farthest
    //of the current frame's pages for replacement
    //String textfile: use for testing data
    //Int frameSize: Size of frame 
    static private int LRU(String textFile, int frameSize) throws IOException
    {
        if (frameSize <= 0) throw new IllegalArgumentException("frameSize must be > 0");
        int pageFaults = 0; 
        File readFile = new File(textFile);
        List<String> pageList = new ArrayList<String>();
        List<String> frameList = new ArrayList<String>(Arrays.asList(new String[frameSize]));
        BufferedReader rd = new BufferedReader(new FileReader(readFile));
        String readLine;

        int page = 0;
        while((readLine = rd.readLine()) != null) {
           int nullIndex;
           String pageReference = readLine;
           pageList.add(pageReference);
           if(!frameList.contains(pageReference) && ((nullIndex = frameList.indexOf(null)) != -1)) 
           {
                System.out.println("Page fault for: " + pageReference);
                pageFaults +=1;
                frameList.set(nullIndex, pageReference);
                IntStream.range(0, frameList.size())
                    .forEach(i -> System.out.println("frame:" + (i+1) + ": " + frameList.get(i)));

           }
           else if(!frameList.contains(pageReference))
           {
                System.out.println("Page fault for: " + pageReference);
                pageFaults +=1;
                Map<Integer, String> distanceMap = new HashMap<>(); 
                List<String> reversedList = pageList.reversed();
                for(String framePage : frameList)
                {
                    int distance = 0;
                    distance += reversedList.indexOf(framePage);
                    distanceMap.put(distance, framePage);
                }
                int maximumDistance = Collections.max(distanceMap.keySet()); 
                if(distanceMap.containsKey(-1)) //case where an item is not used forever
                {
                    String unusedPage = distanceMap.get(-1);
                    frameList.set(frameList.indexOf(unusedPage), pageList.get(page));
                }
                else{
                    String pageRepalcement = pageList.get(pageList.size() - maximumDistance-1); //derevrese maximuim distance
                    frameList.set(frameList.indexOf(pageRepalcement), pageList.get(page));
                }
                /*-------------------------------------------------------------------- Print pages*/
                      IntStream.range(0, frameList.size())
                    .forEach(i -> System.out.println("frame:" + (i+1) + ": " + frameList.get(i)));
           }
           page +=1; 
        }
        rd.close();

        return pageFaults;
       
    }

    public static void main(String[] args) throws IOException {
        //generateReferenceStrings("data.txt");
        System.out.println("LRU Number of page faults in data.txt: " + LRU("data.txt", 1));
    }
}