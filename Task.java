import java.io.*;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

public class Task
{
  static final String[] fileNames = {"ABCDEFG", "HIJKLMN", "OPQRSTU", "VWXYZ"};
  // fileNames is declared as final because it should never be changed

  // Checks arguments from command line and if there aren't any files provided
  // thows an exception and exits the program
  private static void checkInput(String[] args){
      try{
        if (args.length==0)
          throw new Exception("No file given to read");
      }
      catch (Exception e){
        System.out.println(e);
        System.exit(0);
      }
  }

  // Reads words from the files provided and puts them to HashMap
  private static HashMap<String, Integer> readWords(String[] args){
    int argumentToUse=1;
    // argumentToUse is hard coded here because there will always be at least 1
    // argument provided (exception is thrown in checkInput otherwise).
    HashMap<String, Integer> words = new HashMap<>();
    try{
      String st;
      BufferedReader br = new BufferedReader(new FileReader(args[0]));
      String[] arrayOfWords;

      while ((st = br.readLine()) != null || argumentToUse<=args.length){
        //The following if is activated once the first file reading ends and
        // if there are other files provided it will set the reading to be done
        // in the following file.
        if (st == null){
          br.close();
          br = new BufferedReader(new FileReader(args[argumentToUse-1]));
          argumentToUse++;
          continue;
        }
        st = st.replace(".","").replace(",","").toUpperCase(); // Removes dots
        // and commas from the string and puts it all to capitals to make the
        // the reading case insensitive. If text has more punctuation symbols
        // this line should be extended to include them.
        arrayOfWords = st.split(" ");
        // The string of words without punctuation symbols is converted into
        // array of words before putting them into HashMap.
        for (int i = 0; i<arrayOfWords.length; i++)
        {
          if (words.containsKey(arrayOfWords[i]))
              words.put(arrayOfWords[i], words.get(arrayOfWords[i]) + 1);
          else
              words.put(arrayOfWords[i], 1);
          // If the word is already in the HashMap, its occurrences value is
          // updated and otherwise it is added to the HashMap.
        }
      }//while
      br.close();
    }//try
    catch (Exception e){
      System.out.println("Exception was thown in word reading: " + e);
      System.exit(0);
    }
    // Returns HashMap with all the words encountered and their occurrences count
    return words;
  }

  // Creates multiple threads to write to multiple files based on fileNames
  // one thread per one character sequence in fileNames
  private static void threadWrite(TreeMap<String, Integer> sorted){
    for (int i = 0; i<fileNames.length; i++){

      Runnable writing = new writingThread(fileNames[i], sorted);

      new Thread(writing).start();

    }

  }

  public static void main(String[] args) {

    HashMap<String, Integer> words;

    checkInput(args);

    try{

      words = readWords(args);

      // TreeMap automatically sorts values based on natural order (A-Z)
      TreeMap<String, Integer> sorted = new TreeMap<>(words);

      if(sorted.firstKey().isEmpty())
        sorted.remove(sorted.firstKey());
      // In sorted TreeMap first key sometimes appears to be empty and therefore
      // is removed.

      threadWrite(sorted);

    }
    catch (Exception e){
      System.out.println("Exception from main: " + e);
    }

  }// main
}
