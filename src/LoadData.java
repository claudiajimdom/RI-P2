import java.io.*;
import java.util.*;

public class LoadData {

    public static List<Map<String, String>> loadData(String filePath) throws Exception {
        List<Map<String, String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) throw new IOException("CSV vacío");

String[] j = parseCSVLine(headerLine);
System.out.println("Headers:");
for (int i = 0; i < j.length; i++) {
    System.out.println(i + ": '" + j[i] + "'");
}
            

            // Obtener encabezados
            String[] headers = parseCSVLine(headerLine);
            int neighborhoodIndex = -1;
            int amenitiesIndex = -1;

            for (int i = 0; i < headers.length; i++) {
                if (headers[i].equalsIgnoreCase("host_neighbourhood")) neighborhoodIndex = i;
                if (headers[i].equalsIgnoreCase("amenities")) amenitiesIndex = i;
            }
            if (neighborhoodIndex == -1 || amenitiesIndex == -1)
                throw new IOException("No se encontraron las columnas host_neighbourhood o amenities");

          String line;
            int lineNumber = 2;
            while ((line = br.readLine()) != null) {
                try {
                    String[] values = parseCSVLine(line);
                    Map<String, String> record = new HashMap<>();
                    record.put("host_neighbourhood", neighborhoodIndex < values.length ? values[neighborhoodIndex] : "");
                    record.put("amenities", amenitiesIndex < values.length ? values[amenitiesIndex] : "");
                    records.add(record);
                } catch (Exception e) {
                    System.err.println("⚠ Línea " + lineNumber + " ignorada: " + e.getMessage());
                }
                lineNumber++;
            }
        }
        return records;
    }

    // Método simple para parsear líneas CSV respetando comillas
    private static String[] parseCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes; // cambiar estado
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString().trim());
        return tokens.toArray(new String[0]);
    }
}
