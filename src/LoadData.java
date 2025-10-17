import java.io.*;
import java.util.*;

public class LoadData {

    public static List<Map<String, String>> loadData(String filePath) throws Exception {
        List<Map<String, String>> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) throw new IOException("CSV vacío");

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
            StringBuilder multiline = new StringBuilder(); // Para líneas que se rompen por comillas
            while ((line = br.readLine()) != null) {
                multiline.append(line);
                String[] values = parseCSVLine(multiline.toString());

                // Detecta si hay comillas desbalanceadas
                if (countQuotes(multiline.toString()) % 2 != 0) {
                    multiline.append("\n"); // continuar con la siguiente línea
                    continue;
                }

                multiline.setLength(0); // reset para la próxima línea

                try {
                    String neighborhood = neighborhoodIndex < values.length ? values[neighborhoodIndex].trim() : "";
                    String amenities = amenitiesIndex < values.length ? values[amenitiesIndex].trim() : "";

                    // Filtrar registros vacíos
                    if (neighborhood.isEmpty() && amenities.isEmpty()) continue;

                    Map<String, String> record = new HashMap<>();
                    record.put("host_neighborhood", neighborhood);
                    record.put("amenities", amenities);
                    records.add(record);
                } catch (Exception e) {
                    System.err.println("⚠ Línea " + lineNumber + " ignorada: " + e.getMessage());
                }
                lineNumber++;
            }
        }

        return records;
    }

    // Método para parsear líneas CSV tolerante a comillas mal cerradas
    private static String[] parseCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes; // alterna estado de comillas
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

    // Cuenta comillas en una línea
    private static int countQuotes(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == '"') count++;
        }
        return count;
    }

}
