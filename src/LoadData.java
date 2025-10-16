
import java.io.*;
import java.util.*;
import com.opencsv.CSVReader;
import java.util.List;
import java.util.Map;


//Clase para parsear CSV 
public class LoadData {
    public static List<Map<String, String>> loadData(String filePath) throws Exception{
        List<Map<String, String>> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] headers = csvReader.readNext(); // Leer la primera línea como encabezados
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                Map<String, String> record = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    record.put(headers[i], values[i]);
                }
                records.add(record);
            }
        }
        return records;
    }
}


