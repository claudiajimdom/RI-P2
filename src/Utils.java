import java.io.*;
import java.util.*;
import java.nio.file.*;

public class Utils {    
    
    public static Map<String, Long> readFrequencies(Path file) throws IOException {
        Map<String, Long> map = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(file)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String term = parts[0].trim();
                    long freq = Long.parseLong(parts[1].trim());
                    map.put(term, freq);
                }
            }
        }
        return map;
    }


    
    
    
    
    
    public static void calculateWeight(){

    }



}
