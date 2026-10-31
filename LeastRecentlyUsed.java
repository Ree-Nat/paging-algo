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
    static private void generateReferenceStrings(String textFile, int referenceStringAmount) throws IOException
    {
        Random random = new Random(); 
        File dataFile = new File(textFile);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile)))
        {
        
        //For the amount of unique page create a unique string of size 30 seven times and put in list
        for(int x = 0; x < referenceStringAmount; x++)
        {
            StringBuilder sb = new StringBuilder("");
            for(int y = 0; y < 30; y++)
            {
                sb.append(random.nextInt(8));
            }
            bw.write(sb.toString());
            bw.newLine();
        }
        }

    }


    //Calculates average page fault for least recently used paging algorithm by calculating the distance
    //of the furthest, past page reference 
    //by using a queue pointer that wraps around everytime it goes over the frame size. 
    //Input: input of testing data
    //framesize max frame size to test pagefault
    //Returns-> the average page fault per input string file
    static private float LRU(String textFile, int frameSize) throws IOException
    {
        if (frameSize <= 0) throw new IllegalArgumentException("frameSize must be > 0");
        int totalReferenceStrings = 0;
        int totalPageFaults = 0;
        File readFile = new File(textFile);
        BufferedReader rd = new BufferedReader(new FileReader(readFile));


        String referenceString;
        while((referenceString = rd.readLine()) != null){ 
            int pageFaults = 0; 
             totalReferenceStrings +=1;
               System.out.println("Now calculating page fault for reference string " 
                                + referenceString + "\n----------\n----------");
             List<Character> frameList = new ArrayList<Character>(Arrays.asList(new Character[frameSize]));
             for(int currentPageIndex = 0; currentPageIndex < referenceString.length(); currentPageIndex++)
             {
                Character currentPage = referenceString.charAt(currentPageIndex);
                int nullIndex;
                if(!frameList.contains(currentPage) && (nullIndex = frameList.indexOf(currentPage)) != -1)
                {
                    System.out.println("Page fault for: " + currentPage);
                    pageFaults += 1;
                    frameList.set(nullIndex, currentPage);
                }
                else if(!frameList.contains(currentPage))
                {
                    System.out.println("Page fault for: " + currentPage);
                    pageFaults +=1;
                    Map<Integer, Character> distanceMap = new HashMap<>(); 
                    String futureReferences = referenceString.substring(0, currentPageIndex);
                    List<Character> pageList = futureReferences.chars()
                                                .mapToObj(c -> (char) c)
                                                .toList().reversed();
                    for(Character framePage : frameList)
                    {
                        int distance = 0;
                        distance += pageList.indexOf(framePage);
                        distanceMap.put(distance, framePage);
                    }
                    int maxDistance = Collections.max(distanceMap.keySet()); 
                    if(distanceMap.containsKey(-1)) //case where an item is not used forever
                    {
                        Character unusedPage = distanceMap.get(-1);
                        frameList.set(frameList.indexOf(unusedPage), currentPage);
                    }
                    else{
                        Character pageRepalcement = pageList.get(maxDistance);
                        frameList.set(frameList.indexOf(pageRepalcement), currentPage);
                    }
                }
                IntStream.range(0, frameList.size())
                    .forEach(i -> System.out.println("frame" + (i+1) + ": " + frameList.get(i)));
                System.out.println("-------------------------------------");
        }
                System.out.println("Total page faults for reference String: " + 
                                                referenceString + "-> " + pageFaults);
                totalPageFaults += pageFaults;
     }
        rd.close();
        float averagePageFault = totalPageFaults / totalReferenceStrings;
        return averagePageFault;
    }

    public static void main(String[] args) throws IOException {
        //generateReferenceStrings("data.txt");
        System.out.println("LRU Number of page faults in data.txt: " + LRU("data.txt", 3));
    }
}