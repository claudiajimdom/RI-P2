
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.commongrams.CommonGramsFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import java.io.IOException;
import java.io.StringReader;

public class Tokenfilters {
    public static Analyzer stardardAnalyzer() {
        return new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer source = new StandardTokenizer();
                return new TokenStreamComponents(source);
            }
        };
    }
    
    public static Analyzer lowerCaseAnalyzer() {
        return new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer source = new StandardTokenizer();
                TokenStream filter = new LowerCaseFilter(source);
                return new TokenStreamComponents(source, filter);
            }
        };
    }
    public static Analyzer stopAnalyzer() {
        return new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer source = new StandardTokenizer();
                CharArraySet stopWords = StopFilter.makeStopSet("es", "de", "en");
                TokenStream filter = new StopFilter(source, stopWords);
                return new TokenStreamComponents(source, filter);
            }
        };
    }
    public static Analyzer edgengramAnalyzer() {
        return new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer source = new StandardTokenizer();
                TokenStream filter = new EdgeNGramTokenFilter(source, 2, 4, true);
                return new TokenStreamComponents(source, filter);
            }
        };
    }
    public static Analyzer snowballAnalyzer() {
        return new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer source = new StandardTokenizer();
                TokenStream filter = new SnowballFilter(source, "Spanish");
                return new TokenStreamComponents(source, filter);
            }
        };
    }
    public static Analyzer shingleAnalyzer() {
        return new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer source = new StandardTokenizer();
                ShingleFilter filter = new ShingleFilter(source,2,3);
                
                return new TokenStreamComponents(source, filter);
            }
        };
    }
    public static Analyzer ngramAnalyzer() {
        return new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer source = new StandardTokenizer();
                NGramTokenFilter filter = new NGramTokenFilter(source, 2, 3, true);
                return new TokenStreamComponents(source, filter);
            }
        };
    }

    public static Analyzer commonGramsFilter() {
        return new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                Tokenizer source = new StandardTokenizer();
                CharArraySet commonWords = CharArraySet.copy(StopFilter.makeStopSet("es", "de", "en"));
                CommonGramsFilter filter = new CommonGramsFilter(source, commonWords);
                return new TokenStreamComponents(source, filter);
            }
        };
    }

    public static void mostrarTokens(Analyzer analyzer, String texto, String nombre) throws IOException {
        System.out.println("=== " + nombre + " ===");
        TokenStream ts = analyzer.tokenStream("field", new StringReader(texto));
        CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
        ts.reset();
        while (ts.incrementToken()) {
            System.out.print("[" + term.toString() + "] ");
        }
        ts.end();
        ts.close();
        System.out.println("\n");
    }
}
