import main.HyponymsHandler;
import main.WordNetHelper;
import ngrams.NGramMap;
import browser.NgordnetQuery;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class AutograderTestNumberTwo {
    private HyponymsHandler handler;
    private WordNetHelper wn;

    private static final String CITY = "city";

    private static final int YEAR_START = 1470;
    private static final int YEAR_END = 2019;

    private static final int K_3 = 3;
    private static final int K_0 = 0;

    @Before
    public void setup() throws IOException {
        String synsetFile = "data/wordnet/synsets.txt";
        String hyponymFile = "data/wordnet/hyponyms.txt";
        String wordsFile = "data/ngrams/top_14377_words.csv";
        String countsFile = "data/ngrams/total_counts.csv";

        wn = new WordNetHelper(synsetFile, hyponymFile, wordsFile, countsFile);
        NGramMap ngm = new NGramMap(wordsFile, countsFile);
        handler = new HyponymsHandler(wn, ngm);
    }

    private List<String> getHyponyms(String word, int startYear, int endYear, int k) {
        NgordnetQuery q = new NgordnetQuery(List.of(word), k, startYear, endYear);
        String result = handler.handle(q);
        return result.replace("[", "").replace("]", "").split(",\\s*").length == 1 && result.equals("[]")
                ? List.of()
                : List.of(result.replace("[", "").replace("]", "").split(",\\s*"));
    }

    @Test
    public void onePointOne() {
        List<String> expected = List.of(
                "Fannie_Farmer", "Fannie_Merritt_Farmer", "Farmer",
                "baster", "chef", "cook"
        );
        List<String> actual = getHyponyms("cook", YEAR_START, YEAR_END, K_0);

        for (String word : expected) {
            assertTrue("Expected word missing: " + word, actual.contains(word));
        }

        List<String> forbidden = List.of(
                "Captain_Cook", "Captain_James_Cook", "James_Cook"
        );
        for (String word : forbidden) {
            assertFalse("Unexpected word included: " + word, actual.contains(word));
        }


    }

    @Test
    public void onePointThreePart1() {
        NgordnetQuery q = new NgordnetQuery(List.of("someWord"), 0, YEAR_START, YEAR_END);
        String result = handler.handle(q);
        assertFalse("Should exclude 'Almighty' when k = 0", result.contains("Almighty"));
    }

    @Test
    public void onePointThreePart2() {
        NgordnetQuery q = new NgordnetQuery(
                List.of("Alois_Senefelder", "Alvar_Aalto", "Alton_Glenn_Miller"),
                0,
                YEAR_START,
                YEAR_END

        );
        String result = handler.handle(q);

        assertFalse("Should exclude 'Almighty' from results", result.contains("Almighty"));
        assertTrue("Should return valid result or empty", result.equals("[]")
                || result.contains("Alois_Senefelder")
                || result.contains("Alvar_Aalto"));
    }

    @Test
    public void fivePointTwo() {
        NgordnetQuery q = new NgordnetQuery(
                List.of(CITY),
                K_3,
                YEAR_START,
                YEAR_END
        );
        String result = handler.handle(q);
        assertEquals("Expected only 'city' in result", "[city]", result);
    }

    @Test
    public void fivePointEight() {
        NgordnetQuery q = new NgordnetQuery(
                List.of("constituent"),
                1,
                YEAR_START,
                YEAR_END
        );
        String result = handler.handle(q);

        assertEquals("Expected most frequent descendant of 'constituent' to be 'so'", "[so]", result);
    }


}

