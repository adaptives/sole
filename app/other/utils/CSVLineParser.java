package other.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//credit: This code was taken from a StackOverflow question 
//http://stackoverflow.com/questions/1441556/parsing-csv-input-with-a-regex-in-java
public class CSVLineParser {

    /*
     * This Pattern will match on either quoted text or text between commas, including
     * whitespace, and accounting for beginning and end of line.
     */
    private final Pattern csvPattern = Pattern.compile("\"([^\"]*)\"|(?<=,|^)([^,]*)(?:,|$)");  
    private ArrayList<String> allMatches = null;        
    private Matcher matcher = null;
    private String match = null;
    private int size;

    public CSVLineParser() {                
        allMatches = new ArrayList<String>();
        matcher = null;
        match = null;
    }

    public String[] parse(String csvLine) {
        matcher = csvPattern.matcher(csvLine);
        allMatches.clear();
        String match;
        while (matcher.find()) {
                match = matcher.group(1);
                if (match!=null) {
                        allMatches.add(match);
                }
                else {
                        allMatches.add(matcher.group(2));
                }
        }

        size = allMatches.size();               
        if (size > 0) {
                return allMatches.toArray(new String[size]);
        }
        else {
                return new String[0];
        }                       
    }   

    public static void main(String[] args) {            
        String lineinput = "the quick,\"brown, fox jumps\",over,\"the\",,\"lazy dog\"";

        CSVLineParser myCSV = new CSVLineParser();
        System.out.println("Testing CSVParser with: \n " + lineinput);
        for (String s : myCSV.parse(lineinput)) {
                System.out.println(s);
        }
    }

}

