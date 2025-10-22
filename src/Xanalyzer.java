import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Pattern;



public class Xanalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        // Patrón que captura: emojis completos (con variantes), palabras, @menciones, #hashtags
        Pattern tokenPattern = Pattern.compile(
            "[\\p{So}\\p{Sk}\\p{Sm}][\\p{M}\\uFE0F\\u200D]*" + // Emojis con modificadores y variantes
            "|[@#]?[\\p{L}\\p{N}_]+"  // Palabras, @menciones, #hashtags
        );
        Tokenizer tokenizer = new PatternTokenizer(tokenPattern, 0);

        // Filtro lowercase (los emojis no se afectan)
        TokenStream tokenStream = new LowerCaseFilter(tokenizer);

        // Filtro de sinónimos para emoticonos
        try {
            SynonymMap synonymMap = buildEmojiSynonyms();
            tokenStream = new SynonymGraphFilter(tokenStream, synonymMap, true);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return new TokenStreamComponents(tokenizer, tokenStream);
    }

    //Diccionario de sinónimos para emoticonos (cargado desde archivo CSV)
    private SynonymMap buildEmojiSynonyms() throws IOException, ParseException {
        SynonymMap.Builder builder = new SynonymMap.Builder(true);
        
        // Leer diccionario desde archivo CSV
        String filePath = "data/emoticonos.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                // Ignorar la cabecera del CSV
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                // Ignorar líneas vacías
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Busca el sinónimo al lado del emoticono
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String emoji = parts[0].trim();
                    String synonym = parts[1].trim();
                    builder.add(new CharsRef(emoji), new CharsRef(synonym), true);
                }
            }
        }
        
        return builder.build();
    }
}
