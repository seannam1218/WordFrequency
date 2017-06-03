package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    private static Scanner scanner = null;
    private static Map wordsMap;
    private static Iterator<Map.Entry<String, Integer>> wmIter;
    //Create a list of undesirable words for purposes of avoiding these words when
    private static Set<String> xSet = new HashSet<>(Arrays.asList
            ("A", "THE", "THIS", "THAT", "THESE", "THOSE", "THERE", "HERE",                  //articles and pronouns
            "AND", "THEN", "BUT", "HOWEVER", "SO",                                           //conjunctions
            "BE", "BEEN", "IS", "S", "WAS", "ARE", "WERE",                                   //be verbs (and possession)
            "HAVE", "HAD", "HAS",                                                            //have verbs
            "TO", "OF", "AS", "IN", "FROM", "ON", "IT", "AT", "FOR", "WITH", "BY", "INTO",   //conjunctions
            "HE", "SHE", "HIS", "HIM", "HER", "HERS", "THEY", "THEM", "THEIR", "WE", "US", "OUR",
            "I", "MY", "YOU", "YOUR",                                                       //pronouns and possessions
            "�", "U", "T", "NOT", "NO"));                                                  //miscellaneous

    private static int TOP_ENTRIES = 5;

    public static void main(String[] args) {
        //String dir = System.getProperty("user.dir");
        //System.out.print(dir);  --> C:\Users\Sean\IntelliJIDEAProjects\WordFrequency

        loadFile("input.txt");
        scanFile();

        try {

            printTop(TOP_ENTRIES);
        } catch (Exception e) {
            System.out.println("Exception caught");
        }
    }

    private static void loadFile(String input) {
        File file = new File (input);

        try {
            // create new scanner instance using delimiter at (" "), (","), ("!"), ("?"), ("."), ("'"), ("""), ("\")
            // line breaks ("\r\n"), and etc. or any combination of these in succession.
            // the "\" before ", \, and \r\n escapes the function of these letters in the code,
            // thus registering them as strings.
            scanner = new Scanner(file).useDelimiter("[\\\\| -,!@#$^&*?.'\"\\r\n+)(]+");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.print("file not found");
        }

    }

    private static void scanFile() {
        //Create a map and store the words and their frequencies in it.
        wordsMap = new HashMap<String, Integer>();

        while (scanner.hasNext()) {
            String key = scanner.next().toUpperCase();
            // if the scanned String is contained in the xSet, then do nothing and move on.
            if (!xSet.contains(key)) {
                if (!wordsMap.containsKey(key)) {
                    wordsMap.put(key, 1);
                } else {
                    Integer freq = (Integer) wordsMap.get(key);
                    freq ++;
                    wordsMap.put(key, freq);
                }
            }
        }
        scanner.close();
    }


    private static void printTop(int i) {
        wmIter = wordsMap.entrySet().iterator();
        while(i > 0) {
            //Print the top 10 most frequently used words
            int mostFreqV = (int) Collections.max(wordsMap.values());
            getKeyFromValue(wordsMap, mostFreqV);
        }
    }

    private static void getKeyFromValue(Map hm, int v) {
        //ConcurrentModificationException happens here since I am iterating through the map and deleting entries at the same time.
        /*for (Object k : hm.keySet()) {
            if (hm.get(k).equals(v)) {
                System.out.println(k + " - " + hm.get(k));
                hm.remove(k);
                TOP_ENTRIES --;
                printTop(TOP_ENTRIES);
            }
        }*/

        //To bypass ConcurrentModificationException, I need to use an iterator, and use the iterator's remove method.
        while (wmIter.hasNext()) {
            Map.Entry<String, Integer> pair = wmIter.next();
            String k = pair.getKey();
            if (hm.get(k).equals(v)) {
                System.out.println(k + " - " + hm.get(k));
                wmIter.remove();
                TOP_ENTRIES --;
                printTop(TOP_ENTRIES);
            }
        }
    }
}
