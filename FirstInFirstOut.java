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

    //Generates first in frist out paging algorithm
    //Input: input of testing data
    //framesize max frame size to test pagefault
    static private int FIFO(String textFile, int frameSize) throws IOException
    {
        if (frameSize <= 0) throw new IllegalArgumentException("frameSize must be > 0");
        int pageFaults = 0; 
        File readFile = new File(textFile);
        String[] stringArray = new String[frameSize];
        List<String> frameList = new ArrayList<String>(Arrays.asList(stringArray));
        BufferedReader rd = new BufferedReader(new FileReader(readFile));
        int queuePointer = 0; //circular pointer, points to 

        String readLine;
        int readIndex;
        while((readLine = rd.readLine()) != null) 
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
        }
        rd.close();
        return pageFaults;
    }

    public static void main(String[] args) throws IOException {
        //generateReferenceStrings("data.txt");
        System.out.println("Total page faults for FIFO in data.txt: " + FIFO("data.txt", 3));
    }
}