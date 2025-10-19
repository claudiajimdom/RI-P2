import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;

import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Pattern;

public class Xanalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        // tokens validos (@cadena , #cadena)
        Pattern cadena = Pattern.compile("\\s+"); // separa por espacio
        Tokenizer tokenizer = new PatternTokenizer(cadena, -1);

        // Filtro lowercase 
        TokenStream tokenStream = new LowerCaseFilter(tokenizer);

        // Filtro de sinónimos para emojis
        try {
            SynonymMap synonymMap = buildEmojiSynonyms();
            tokenStream = new SynonymGraphFilter(tokenStream, synonymMap, true);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return new TokenStreamComponents(tokenizer, tokenStream);
    }

    //Diccionario de sinónimos para emojis
    private SynonymMap buildEmojiSynonyms() throws IOException, ParseException {
        SynonymMap.Builder builder = new SynonymMap.Builder(true);

        builder.add(new CharsRef(":-)"), new CharsRef("feliz"), true);
        builder.add(new CharsRef(":)"), new CharsRef("feliz"), true);
        builder.add(new CharsRef("😊"), new CharsRef("feliz"), true);

        builder.add(new CharsRef(":-("), new CharsRef("triste"), true);
        builder.add(new CharsRef(":("), new CharsRef("triste"), true);
        builder.add(new CharsRef("😢"), new CharsRef("triste"), true);

        builder.add(new CharsRef("😂"), new CharsRef("risa"), true);

        return builder.build();
    }
}
