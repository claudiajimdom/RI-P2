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

    //Diccionario de sinónimos para emoticonos
    private SynonymMap buildEmojiSynonyms() throws IOException, ParseException {
        SynonymMap.Builder builder = new SynonymMap.Builder(true);

        // Emojis básicos sin variantes
        builder.add(new CharsRef("😊"), new CharsRef("feliz"), true);
        builder.add(new CharsRef("😡"), new CharsRef("enfadado"), true);
        builder.add(new CharsRef("😢"), new CharsRef("triste"), true);
        builder.add(new CharsRef("😂"), new CharsRef("risa"), true);
        builder.add(new CharsRef("😍"), new CharsRef("enamorado"), true);
        builder.add(new CharsRef("😎"), new CharsRef("guay"), true);
        builder.add(new CharsRef("😴"), new CharsRef("cansado"), true);
        builder.add(new CharsRef("🤔"), new CharsRef("pensativo"), true);
        builder.add(new CharsRef("😱"), new CharsRef("asombrado"), true);
        builder.add(new CharsRef("👌"), new CharsRef("perfecto"), true);
        builder.add(new CharsRef("👍"), new CharsRef("bien"), true);
        builder.add(new CharsRef("👎"), new CharsRef("mal"), true);
        builder.add(new CharsRef("🙏"), new CharsRef("gracias"), true);
        builder.add(new CharsRef("💪"), new CharsRef("fuerza"), true);
        builder.add(new CharsRef("🔥"), new CharsRef("genial"), true);
        builder.add(new CharsRef("❤"), new CharsRef("amor"), true);
        
        // Variantes con presentation selector (U+FE0F)
        builder.add(new CharsRef("❤\uFE0F"), new CharsRef("amor"), true);
        
        return builder.build();
    }
}
