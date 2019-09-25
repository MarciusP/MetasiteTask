import java.io.*;
import java.util.Iterator;
import java.util.TreeMap;

public class writingThread implements Runnable {

  String fileName;
  TreeMap<String, Integer> sortedWords;
  String fileContents = "";
  String chars;

  // Thread constructable. Gets file name character and sorted HashMap
  public writingThread(String setOfChars, TreeMap<String, Integer> map)
  {
    sortedWords = map;
    chars = setOfChars;
  }

  // Thread run function which iterates through sorted words until it finds
  // file name character as the first letter of the word and writes all such
  // words to the file named after said character.
  public void run()
  {
    boolean foundChar;
    Iterator iterator;
    String key, value;
    char charToCheck;
    char[] disposable;
    char[] charsToPrint= chars.toCharArray();

    try {
      // The following loops go through character sequence to print
      //it has double loop which is not as efficient as it could be but it
      //enables code reusability because it could find elements in unsorted map
      for (int i=0; i<charsToPrint.length; i++) {
        iterator = sortedWords.keySet().iterator();
        foundChar = false;
        charToCheck = charsToPrint[i];
        while (iterator.hasNext()){
          key = iterator.next().toString();
          disposable = key.toCharArray();
          // A word is converted to character array in order to find it's first character
          if (disposable[0] == charToCheck){
              foundChar = true; // This variable indicates that the section of words
          // in TreeMap with desired character at the beginning was reached
          value = sortedWords.get(key).toString();

          fileContents += key + " " + value + "\n";
          }
          else if (foundChar)
            break; // loop is broken for efficiency after
            // desired character is no longer found
        } //while
      }//for

    } //try
    catch(Exception e) {
      System.out.println("Exception in writing thread loops: " + e);
    }

    if (!fileContents.equals("")){
      try{
        fileName=charsToPrint[0] + "-" + charsToPrint[charsToPrint.length-1];
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".txt"));
        writer.write(fileContents);

        writer.close();
      }
      catch (Exception e) {
        System.out.println("Exception in writing thread: " + e);
      }
    }

    try{
      Thread.sleep(50);
    }
    catch (Exception e) {}
    // Thread sleeps for a moment and terminates itself after completing its task

  }//run

}
