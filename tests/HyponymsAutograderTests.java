import browser.NgordnetQuery;
import main.HyponymsHandler;
import main.WordNetHelper;
import ngrams.NGramMap;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class HyponymsAutograderTests {

    private static final int K_0 = 0;
    private static final int K_2 = 2;
    private static final int K_3 = 3;
    private static final int K_4 = 4;
    private static final int K_5 = 5;

    private static final int YEAR_1500 = 1500;
    private static final int YEAR_1800 = 1800;
    private static final int YEAR_1900 = 1900;
    private static final int YEAR_1950 = 1950;
    private static final int YEAR_2000 = 2000;
    private static final int YEAR_2020 = 2020;

    private static final int YEAR_START = 1470;
    private static final int YEAR_END = 2019;
    private static final int ALT_YEAR_START = 1920;
    private static final int ALT_YEAR_END = 1980;
    private static final int SMALL_K = 3;
    private static final int INTERSECT_K = 4;

    private HyponymsHandler handler;

    private NgordnetQuery newQuery(List<String> words, int k, int startYear, int endYear) {
        return new NgordnetQuery(words, startYear, endYear, k);
    }
    private List<String> getHyponyms(String word, int startYear, int endYear, int k) {
        NgordnetQuery q = new NgordnetQuery(List.of(word), startYear, endYear, k);
        String result = handler.handle(q);

        result = result.substring(1, result.length() - 1).trim();
        if (result.isEmpty()) {
            return List.of();
        }

        String[] parts = result.split(",\\s*");
        return Arrays.stream(parts).map(String::trim).collect(Collectors.toList());
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

    @Test public void testBasicSingleWordMultipleContextsK0() {
        System.out.println(handler.handle(newQuery(List.of("table"), K_0, YEAR_1800, YEAR_2020)));
    }

    @Test public void testRandomSingleWordLargeGraphK0() {
        System.out.println(handler.handle(newQuery(List.of("energy"), K_0, YEAR_1500, YEAR_2020)));
    }

    @Test public void testConsecutiveQueriesDiffFilesK0() {
        System.out.println(handler.handle(newQuery(List.of("cell"), K_0, YEAR_1500, YEAR_2020)));
        System.out.println(handler.handle(newQuery(List.of("network"), K_0, YEAR_1500, YEAR_2020)));
    }

    @Test public void testMultiWordQueryConsistencyK0() {
        String a = handler.handle(newQuery(List.of("device", "machine"), K_0, YEAR_1500, YEAR_2020));
        String b = handler.handle(newQuery(List.of("machine", "device"), K_0, YEAR_1500, YEAR_2020));
        assertEquals(a, b);
    }

    @Test public void testNonEmptyIntersectionEecsK0() {
        System.out.println(handler.handle(newQuery(List.of("CS", "course"), K_0, YEAR_2000, YEAR_2020)));
    }

    @Test public void testEmptyIntersectionEecsK0() {
        assertEquals("[]", handler.handle(newQuery(List.of("CS", "banana"), K_0, YEAR_2000, YEAR_2020)));
    }

    @Test public void testNonEmptyIntersectionLargeK0() {
        System.out.println(
                handler.handle(newQuery(List.of("CS", "class", "lecture", "lab"), K_0, YEAR_2000, YEAR_2020)));
    }

    @Test
    public void testEmptyIntersectionLargeK0() {
        assertEquals(
                "[]",
                handler.handle(newQuery(
                        List.of("banana", "robot", "pencil", "dog"),
                        K_0, YEAR_2000, YEAR_2020)));
    }


    @Test public void testNonExistentWordIntersectionK0() {
        assertEquals("[]", handler.handle(newQuery(List.of("zzznotaword", "CS"), K_0, YEAR_2000, YEAR_2020)));
    }

    @Test public void testRandomMultiWordLargeGraphK0() {
        System.out.println(handler.handle(newQuery(List.of("sound", "music"), K_0, YEAR_1500, YEAR_2020)));
    }

    @Test public void testConsecutiveMultiQueriesK0() {
        System.out.println(handler.handle(newQuery(List.of("music", "sound"), K_0, YEAR_1500, YEAR_2020)));
        System.out.println(handler.handle(newQuery(List.of("structure", "building"), K_0, YEAR_1500, YEAR_2020)));
    }

    @Test public void testRandomTwoWordsSmallGraphK0() {
        System.out.println(handler.handle(newQuery(List.of("plant", "seed"), K_0, YEAR_1800, YEAR_2020)));
    }

    @Test public void testRandomTwoWordsSmallGraphK0Alt() {
        System.out.println(handler.handle(newQuery(List.of("bird", "feather"), K_0, YEAR_1800, YEAR_2020)));
    }

    @Test public void testBasicRandomGraphMultiWordK0() {
        System.out.println(handler.handle(newQuery(List.of("circle", "geometry"), K_0, YEAR_1500, YEAR_2020)));
    }

    @Test public void testRandomSingleWordSmallKNonZero() {
        System.out.println(handler.handle(newQuery(List.of("cake"), K_2, YEAR_1800, YEAR_2020)));
    }

    @Test public void testSmallRandomSingleWordKNonZero() {
        System.out.println(handler.handle(newQuery(List.of("bottle"), K_3, YEAR_1900, YEAR_2020)));
    }

    @Test public void testLargeRandomSingleWordKNonZero() {
        System.out.println(handler.handle(newQuery(List.of("theory"), K_5, YEAR_1500, YEAR_2020)));
    }

    @Test public void testConsecutiveQueriesSingleK0() {
        System.out.println(handler.handle(newQuery(List.of("team"), K_0, YEAR_1950, YEAR_2020)));
        System.out.println(handler.handle(newQuery(List.of("game"), K_0, YEAR_1950, YEAR_2020)));
    }

    @Test public void testMultiContextSingleWordKNonZero() {
        System.out.println(handler.handle(newQuery(List.of("plant"), K_3, YEAR_1800, YEAR_2020)));
    }

    @Test public void testSmallRandomSingleWordKNonZero2() {
        System.out.println(handler.handle(newQuery(List.of("box"), K_2, YEAR_1800, YEAR_2020)));
    }

    @Test public void testMultiContextMultiWordKNonZero() {
        System.out.println(handler.handle(newQuery(List.of("plant", "factory"), K_2, YEAR_1800, YEAR_2020)));
    }

    @Test public void testSmallRandomMultiWordKNonZero() {
        System.out.println(handler.handle(newQuery(List.of("cat", "lion"), K_2, YEAR_1800, YEAR_2020)));
    }

    @Test public void testEecsFileMultiWordKNonZero() {
        System.out.println(handler.handle(newQuery(List.of("CS", "programming"), K_2, YEAR_2000, YEAR_2020)));
    }

    @Test public void testLargeRandomMultiWordKNonZero() {
        System.out.println(handler.handle(newQuery(List.of("car", "truck", "bus"), K_3, YEAR_1800, YEAR_2020)));
    }

    @Test public void testHardcodedMultiKNonZero() {
        System.out.println(handler.handle(newQuery(List.of("student", "learner"), K_2, YEAR_2000, YEAR_2020)));
    }

    @Test public void testHardcodedMultiK0() {
        System.out.println(handler.handle(newQuery(List.of("student", "learner"), K_0, YEAR_2000, YEAR_2020)));
    }

    @Test public void testWordWithNoHyponymsKNonZero() {
        System.out.println(handler.handle(newQuery(List.of("everything"), K_3, YEAR_1800, YEAR_2020)));
    }

    @Test public void testLessThanKResultsKNonZero() {
        System.out.println(handler.handle(newQuery(List.of("duck", "goose"), K_5, YEAR_1900, YEAR_2020)));
    }

    @Test public void testSmallRandomMultiWordKNonZero2() {
        System.out.println(handler.handle(newQuery(List.of("cat", "kitten"), K_3, YEAR_1800, YEAR_2020)));
    }

    @Test public void testLargeRandomMultiWordKNonZero2() {
        System.out.println(
                handler.handle(newQuery(List.of("instrument", "tool", "device"), K_4, YEAR_1800, YEAR_2020)));
    }

    @Test public void testLargeRandomMultiWordAltKNonZero() {
        System.out.println(
                handler.handle(newQuery(List.of("emotion", "feeling", "sensation"), K_4, YEAR_1800, YEAR_2020)));
    }

    @Test
    public void testEleven() {
        String result = handler.handle(newQuery(List.of("light"), K_0, YEAR_1800, YEAR_2020));
        System.out.println("T1.1: " + result);
    }

    @Test
    public void testTirteen() {
        String result = handler.handle(newQuery(List.of("change"), K_0, YEAR_1500, YEAR_2020));
        System.out.println("T1.3: " + result);
    }

    @Test
    public void testFiftyTwo() {
        String result = handler.handle(newQuery(List.of("knife"), K_3, YEAR_1900, YEAR_2020));
        System.out.println("T5.2: " + result);
    }

    @Test
    public void testFiftyEight() {
        String result = handler.handle(newQuery(List.of("spoon"), K_2, YEAR_1800, YEAR_2020));
        System.out.println("T5.8: " + result);
    }

    @Test
    public void testFiftyNine() {
        String result = handler.handle(newQuery(List.of("concept"), K_4, YEAR_1500, YEAR_2020));
        System.out.println("T5.9: " + result);
    }

    @Test
    public void testFiveOneOne() {
        String res1 = handler.handle(newQuery(List.of("emotion"), K_0, YEAR_1950, YEAR_2020));
        String res2 = handler.handle(newQuery(List.of("logic"), K_0, YEAR_1950, YEAR_2020));
        System.out.println("T5.11.1: " + res1);
        System.out.println("T5.11.2: " + res2);
    }

    @Test
    public void testSixtyOne() {
        String result = handler.handle(newQuery(List.of("scale"), K_3, YEAR_1800, YEAR_2020));
        System.out.println("T6.1: " + result);
    }

    @Test
    public void testEightySix() {
        String result = handler.handle(newQuery(List.of("emotion", "response", "behavior"), K_4, YEAR_1800, YEAR_2020));
        System.out.println("T8.6: " + result);
    }

    @Test
    public void testEightAndSeven() {
        String result = handler.handle(
                newQuery(List.of("temperature", "climate", "weather"), K_4, YEAR_1800, YEAR_2020)
        );
        System.out.println("T8.7: " + result);
    }




}
