import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.email.UAX29URLEmailAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class Main {

    /*public static void main(String[] args) throws IOException {

        System.out.println("=== Sugerencias sobre listings ===");


        // Archivos proporcionados
        String fileQueries = "data/listings_filtrado.csv";
        String fileFrequencies = "data/listings_frecuencia.csv";

        // Analizador estándar
        Analyzer analyzer = new StandardAnalyzer();

        // --- Leer frecuencias ---
        Map<String, Long> frequencies = Utils.readFrequencies(fileFrequencies);

        if (frequencies.isEmpty()) {
            System.out.println("El archivo de frecuencias está vacío o no se pudo leer.");
            return;
        }

        //System.out.println("Frecuencias cargadas:");
        //frequencies.forEach((k, v) -> System.out.println(k + " -> " + v));
        //System.out.println("Total de términos: " + frequencies.size());

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

        // --- Mostrar tokens generados por distintos analizadores ---
        String texto1 = "En principio, el almacenamiento y recuperación de información es simple. " +
                "Supongamos que existe un almacén de documentos y una persona (usuario del almacén) " +
                "formula una pregunta (petición o consulta) que tiene por respuesta un conjunto de " +
                "documentos que satisfacen la necesidad de información expresada por esa pregunta";

        // 1. Standard
        Tokenfilters.mostrarTokens(Tokenfilters.stardardAnalyzer(), texto1, "StandardAnalyzer");

        // 2. LowerCase
        Tokenfilters.mostrarTokens(Tokenfilters.lowerCaseAnalyzer(), texto1, "LowerCaseAnalyzer");

        // 3. StopFilter
        Tokenfilters.mostrarTokens(Tokenfilters.stopAnalyzer(), texto1, "StopAnalyzer");

        // 4. EdgeNGram
        Tokenfilters.mostrarTokens(Tokenfilters.edgengramAnalyzer(), texto1, "EdgeNGramAnalyzer");

        // 5. Snowball (Stemming)
        Tokenfilters.mostrarTokens(Tokenfilters.snowballAnalyzer(), texto1, "SnowballAnalyzer");

        // 6. Shingle (n-gramas de palabras)
        Tokenfilters.mostrarTokens(Tokenfilters.shingleAnalyzer(), texto1, "ShingleAnalyzer");

        // 7. NGram (n-gramas de caracteres)
        Tokenfilters.mostrarTokens(Tokenfilters.ngramAnalyzer(), texto1, "NGramAnalyzer");

        // 8. CommonGramsFilter
        Tokenfilters.mostrarTokens(Tokenfilters.commonGramsFilter(), texto1, "CommonGramsFilter");

        // 9. Sinónimo (vacío por ahora)
        Tokenfilters.mostrarTokens(Tokenfilters.synomAnalyzer(), texto1, "SynonymAnalyzer (sin filtros)");



        // --- Prueba Xanalyzer con ficheros (primeros N listings) ---
        String texto2 = "@Carlossainz55 sets the fastest first sector before @alexalbonarg snatches it from him - Albon's time is then deleted 👌 #F1 #USGP";
        System.out.println("\n=== Prueba Xanalyzer ===");
        try (Xanalyzer x = new Xanalyzer();
             TokenStream tokenStream = x.tokenStream("campo", new StringReader(texto2))) {
            CharTermAttribute termAttr = tokenStream.addAttribute(CharTermAttribute.class);

            tokenStream.reset();
            System.out.println("Tokens generados:");
            while (tokenStream.incrementToken()) {
                System.out.println("- " + termAttr.toString());
            }
            tokenStream.end();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private static void imprimirTokens(Analyzer analyzer, String text, String nombre) throws IOException {
        System.out.println("Analizador: " + nombre + " | Texto: " + text);
        TokenStream ts = analyzer.tokenStream("field", text);
        CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
        ts.reset();
        while (ts.incrementToken()) {
            System.out.print("[" + term.toString() + "] ");
        }
        ts.end();
        ts.close();
        System.out.println();
    }*/


    public static void main(String[] args) throws IOException {

        System.out.println("=== Sugerencias sobre listings ===");

        // Archivos proporcionados
        String fileQueries = "data/listings_filtrado.csv";
        String fileFrequencies = "data/listings_frecuencia.csv";




        //pruebaANALYZER



    

        //Analyzer keywAnalyzer = new KeywordAnalyzer();
        Analyzer whitAnalyzer = new WhitespaceAnalyzer();
        Analyzer simpAnalyzer = new SimpleAnalyzer();
        //Analyzer stopAnalyzer = new StopAnalyzer();
        Analyzer stanalyzer = new StandardAnalyzer();
        Analyzer UAXAnalyzer = new UAX29URLEmailAnalyzer();
        Analyzer engAnalyzer = new EnglishAnalyzer();
        Analyzer spaAnalyzer = new SpanishAnalyzer();
        Analyzer Xanalyzer = new Xanalyzer();

        // --- Leer frecuencias ---
        Map<String, Long> frequencies = Utils.readFrequencies(fileFrequencies);

        if (frequencies.isEmpty()) {
            System.out.println("El archivo de frecuencias está vacío o no se pudo leer.");
            return;
        }

        /*System.out.println("Frecuencias cargadas:");
        frequencies.forEach((k, v) -> System.out.println(k + " -> " + v));
        System.out.println("Total de términos: " + frequencies.size());*/

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
        Suggestion.PrefixSuggester(tempFile.toString(), stanalyzer, stanalyzer);

        // --- Construir Fuzzy Suggester ---
        System.out.println("\n=== Fuzzy Suggester ===");
        Suggestion.FuzzyPrefixSuggester(tempFile.toString(), stanalyzer, stanalyzer);

        // --- Construir Infix Suggester ---
        System.out.println("\n=== Infix Suggester ===");
        Suggestion.InfixSuggester(tempFile.toString(), stanalyzer, stanalyzer);

        // --- Construir FreeText Suggester ---
        System.out.println("\n=== FreeText Suggester ===");
        Suggestion.NextTermSuggester(fileFrequencies, stanalyzer, stanalyzer);

        System.out.println("\n=== Fin de la ejecución ===");
    }
}


