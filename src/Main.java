import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("=== Sugerencias sobre listings ===");

        // Archivos proporcionados
        String fileQueries = "data/listings_filtrado.csv";
        String fileFrequencies = "data/listings_frecuencia.csv";

        // Analizador estándar
        Analyzer analyzer = new StandardAnalyzer();

        // --- Leer frecuencias ---
        Map<String, Long> frequencies = Utils.readFrequencies(fileFrequencies);

        if (frequencies.isEmpty()) {
            System.out.println("¡Atención! El archivo de frecuencias está vacío o no se pudo leer.");
            return;
        }

        System.out.println("Frecuencias cargadas:");
        frequencies.forEach((k, v) -> System.out.println(k + " -> " + v));
        System.out.println("Total de términos: " + frequencies.size());

        // --- Leer consultas ---
        List<String> queries = Suggestion.readFileQueries(fileQueries);
        if (!queries.isEmpty() && queries.get(0).startsWith("id,")) {
            queries.remove(0); // Ignorar cabecera
        }
        System.out.println("Total de consultas: " + queries.size());

        // --- Extraer solo los nombres de los listings ---
        List<String> listingNames = queries.stream()
                .map(line -> line.split(",")[0].replaceAll("\"", "").trim())
                .distinct()
                .collect(Collectors.toList());

        // --- Guardar nombres en archivo temporal ---
        Path tempFile = Files.createTempFile("listings_names", ".txt");
        Files.write(tempFile, listingNames);

        // --- Construir Prefix Suggester ---
        System.out.println("\n=== Prefix Suggester ===");
        Suggestion.PrefixSuggester(tempFile.toString(), analyzer, analyzer);

        // --- Construir Fuzzy Suggester ---
        System.out.println("\n=== Fuzzy Suggester ===");
        Suggestion.FuzzyPrefixSuggester(tempFile.toString(), analyzer, analyzer);

        // --- Construir Infix Suggester ---
        System.out.println("\n=== Infix Suggester ===");
        Suggestion.InfixSuggester(tempFile.toString(), analyzer, analyzer);

        // --- Construir FreeText Suggester ---
        System.out.println("\n=== FreeText Suggester ===");
        Suggestion.NextTermSuggester(fileFrequencies, analyzer, analyzer);

        System.out.println("\n=== Fin de la ejecución ===");
    }
}
