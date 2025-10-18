import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Utils {
public static Map<String, Long> readFrequencies(String filePath) {
    Map<String, Long> frequencies = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String linea;
        while ((linea = reader.readLine()) != null) {
            String[] partes = linea.split(";");
            if (partes.length == 2) {
                String palabra = partes[0].trim();
                try {
                    long freq = Long.parseLong(partes[1].trim());
                    frequencies.put(palabra, freq);
                } catch (NumberFormatException e) {
                    System.out.println("Número inválido en línea: " + linea);
                }
            } else {
                System.out.println("Línea mal formada: " + linea);
            }
        }
    } catch (IOException e) {
        System.out.println("Error al leer fichero: " + e.getMessage());
    }
    return frequencies;
}


}
