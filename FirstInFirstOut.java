import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;


class FirstInFirstOut{

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

    //Generates first in frist out paging algorithm
    //Input: input of testing data
    //framesize max frame size to test pagefault
    static private float FIFO(String textFile, int frameSize) throws IOException
    {
        if (frameSize <= 0) throw new IllegalArgumentException("frameSize must be > 0");
        File readFile = new File(textFile); 
        BufferedReader rd = new BufferedReader(new FileReader(readFile));
        int totalPageFaults = 0;
        int totalJobs = 0;
        //circular pointer, points to 

        String referenceString;
        while((referenceString = rd.readLine()) != null) 
        {
            int nullIndex;
            System.out.println("Now calculating page fault for reference string " 
                                + referenceString + "\n----------\n----------");
            int queuePointer = 0;
            Character[] characterArray = new Character[frameSize];
            List<Character> frameList = new ArrayList<Character>(Arrays.asList(characterArray));
            int pageFaults = 0; 
            for(int currentPageIndex = 0; currentPageIndex < referenceString.length(); currentPageIndex++)
            {
                Character currentCharacter = referenceString.charAt(currentPageIndex);
                if(!frameList.contains(currentCharacter) 
                    && ((nullIndex = frameList.indexOf(null)) != -1))
                {
                    pageFaults += 1;
                    frameList.set(nullIndex, currentCharacter);
                    System.out.println("Page fault for: " + currentCharacter); 
                }
                else if(!frameList.contains(currentCharacter))
                {
                    pageFaults +=1;
                    System.out.println("Page fault for: " + currentCharacter); 
                    frameList.set(queuePointer, currentCharacter);
                    queuePointer +=1;
                }
                else
                {
                    System.out.println("No page fault for: " + referenceString.charAt(currentPageIndex)); 
                }

                if(queuePointer >= frameList.size())
                {
                    queuePointer = 0;
                }
           
                IntStream.range(0, frameList.size())
                    .forEach(i -> System.out.println("frame" + (i+1) + ": " + frameList.get(i)));

                System.out.println("-------------------------------------");
            }
                System.out.println("Total page fault for reference string: " + referenceString + " -> "
                                 + pageFaults);
                totalPageFaults += pageFaults;
                totalJobs += 1;

            /* 
            {
            if(!frameList.contains(readLine) && !((readIndex = frameList.indexOf(null)) ==-1)){ //replace null with page 
                pageFaults +=1;
                frameList.set(readIndex, readLine);
                System.out.println("Page fault for: " + readLine); 
                queuePointer +=1;
            }
            else if(!frameList.contains(readLine))
            {
                pageFaults +=1;
                frameList.set(queuePointer, readLine);
                queuePointer+=1;
                System.out.println("Page fault for: " + readLine); 
            }
            
            //Go back to beginning to ensure queuing 
            if(queuePointer > frameSize - 1)
                queuePointer = 0;
           
            IntStream.range(0, frameList.size())
                .forEach(i -> System.out.println("frame:" + (i+1) + ": " + frameList.get(i)));

            System.out.println("-------------------------------------");
            */
        }
        int averagePageFault = totalPageFaults/totalJobs;
        rd.close();
        System.out.println("Average amount of page faults FIFO: " + averagePageFault);
        return averagePageFault;
    }

    public static void main(String[] args) throws IOException {
        //generateReferenceStrings("data.txt", 50);
        System.out.println("Total page faults for FIFO in data.txt: " + FIFO("data.txt", 3));
    }
}