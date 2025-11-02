import browser.NgordnetQuery;
import main.HyponymsHandler;
import main.WordNetHelper;
import ngrams.NGramMap;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import static org.junit.Assert.*;

public class HyponymsHandlerEdgeTest {

    private HyponymsHandler handler;
    private static final int START_YEAR = 1470;
    private static final int END_YEAR = 2019;
    private static final int ALT_START_YEAR = 2000;
    private static final int ALT_END_YEAR = 2020;
    private static final int OLD_START_YEAR = 1920;
    private static final int OLD_END_YEAR = 1980;
    private static final int TRANSITION_START_YEAR = 1800;
    private static final int K_1 = 1;
    private static final int K_2 = 2;
    private static final int K_3 = 3;
    private static final int K_4 = 4;
    private static final int K_10 = 10;
    private static final double TOLERANCE = 0.0001;

    private NgordnetQuery newQuery(List<String> words, int k, int startYear, int endYear) {
        return new NgordnetQuery(words, startYear, endYear, k);
    }

    @Before
    public void setUp() throws IOException {
        String wordFile = "data/ngrams/top_14377_words.csv";
        String countFile = "data/ngrams/total_counts.csv";

        NGramMap ngm = new NGramMap(wordFile, countFile);
        WordNetHelper wn = new WordNetHelper(
                "data/wordnet/synsets.txt",
                "data/wordnet/hyponyms.txt",
                wordFile,
                countFile
        );
        handler = new HyponymsHandler(wn, ngm);
    }

    @Test
    public void testHyponymsFilteredByCountHistory() {
        String result = handler.handle(newQuery(List.of("apology"), K_3, START_YEAR, END_YEAR));
        assertEquals("[apology, excuse]", result);
    }



    @Test
    public void testIncludeInputWordIfHyponym() {
        String result = handler.handle(newQuery(List.of("computer"), K_4, ALT_START_YEAR, ALT_END_YEAR));
        assertTrue(result.contains("computer"));
    }



    @Test
    public void testMultipleSynsets() {
        List<String> inputs = List.of("coupling", "mating");
        String result = handler.handle(newQuery(inputs, K_10, TRANSITION_START_YEAR, ALT_END_YEAR));
        assertTrue(result.contains("union"));
        assertTrue(result.contains("cross"));
        assertTrue(result.contains("pairing"));
        assertTrue(result.contains("hybridization"));
    }



    @Test
    public void testTiebreakAlphabetically() {
        String result = handler.handle(newQuery(List.of("conviction"), K_2, START_YEAR, END_YEAR));
        assertEquals("[conviction, sentence]", result);
    }

    @Test
    public void testLessThanKAvailable() {
        NgordnetQuery q = new NgordnetQuery(List.of("fruit"), K_1, ALT_START_YEAR, ALT_END_YEAR);
        String result = handler.handle(q);
        assertTrue(result.contains("banana"));
    }

    @Test
    public void testExcludeNonCorpusHyponyms() {
        String result = handler.handle(newQuery(List.of("finch"), K_10, OLD_START_YEAR, OLD_END_YEAR));
        assertFalse(result.contains("Spizella_arborea"));
    }

    @Test
    public void testFrequencyCaseInsensitive() {
        double count1 = handler.countInRange("CS61A", ALT_START_YEAR, ALT_END_YEAR);
        double count2 = handler.countInRange("cs61a", ALT_START_YEAR, ALT_END_YEAR);
        assertEquals(count1, count2, TOLERANCE);
    }
    /*
    @source
    Used ChatGPT to help figure out which hyponyms from synsets also show up
    in the top_14377 word list so as to build test cases that actually pass
    */
}
