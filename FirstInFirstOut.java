import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
        
        for(int x = 0; x <= pageAmount; x++)
        {
            StringBuilder sb = new StringBuilder("");
            for(int y = 0; y < 30; y++)
            {
                sb.append(random.nextInt(10));
            }
            randomList.add(sb.toString());
        }
        
        //selectes random string from array
        int amountOfStrings = 30;
        for(int x = 0; x < amountOfStrings; x++)
        {
            int selectedPosition = random.nextInt(randomList.size());
            bw.write(randomList.get(selectedPosition));
            bw.newLine();
        }

        bw.close();
    }


    static private int FirstInFirstOut(String textFile)
    {
        return 1;
    }

    public static void main(String[] args) throws IOException {
        generateReferenceStrings("data.txt");
    }
}