package ngrams;

import edu.princeton.cs.algs4.In;

import java.io.File;
import java.util.*;


/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private Map<String, TimeSeries> wordToCount = new HashMap<>();
    private TimeSeries totalCount = new TimeSeries();

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */

    public NGramMap(String wordsFilename, String countsFilename) {
        wordToCount = new HashMap<>();
        totalCount = new TimeSeries();

        In countIn = new In(new File(countsFilename).toURI().toString());
        while (countIn.hasNextLine()) {
            String line = countIn.readLine();
            try {

                String[] parts = line.split(",");
                int year = Integer.parseInt(parts[0].trim());
                double count = Double.parseDouble(parts[1].trim());

                totalCount.put(year, count);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("skipped bad count entry: " + line);
            }

        }

        In wordIn = new In(new File(wordsFilename).toURI().toString());
        while (wordIn.hasNextLine()) {
            String line = wordIn.readLine();
            String[] parts = line.split("\t");
            String word = parts[0];
            int year = Integer.parseInt(parts[1]);
            double count = Double.parseDouble(parts[2]);
            if (!wordToCount.containsKey(word)) {
                wordToCount.put(word, new TimeSeries());
            }
            wordToCount.get(word).put(year, count);
        }
    }


    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {

        if (wordToCount.containsKey(word)) {
            TimeSeries fullUseOfWord = wordToCount.get(word);
            return new TimeSeries(fullUseOfWord, startYear, endYear);
        } else if (wordToCount.containsKey(word.toLowerCase())) {
            TimeSeries fullUseOfWord = wordToCount.get(word.toLowerCase());
            return new TimeSeries(fullUseOfWord, startYear, endYear);
        } else {
            return new TimeSeries();
        }
    }



    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        if (wordToCount.containsKey(word)) {
            return new TimeSeries(wordToCount.get(word));
        } else {
            return new TimeSeries();
        }
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(totalCount);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries wordResult = countHistory(word, startYear, endYear);
        if (wordResult.isEmpty()) {
            return new TimeSeries();
        } else {
            TimeSeries allWords = new TimeSeries(totalCount, startYear, endYear);
            return wordResult.dividedBy(allWords);
        }
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        TimeSeries wordResult = countHistory(word);
        if (wordResult.isEmpty()) {
            return new TimeSeries();
        } else {
            TimeSeries allWords = new TimeSeries(totalCount, wordResult.firstKey(), wordResult.lastKey());
            return wordResult.dividedBy(allWords);
        }
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries accumulation = new TimeSeries();

        for (String wordCurrent : words) {
            TimeSeries wordAmount = weightHistory(wordCurrent, startYear, endYear);
            accumulation = accumulation.plus(wordAmount);
        }

        return accumulation;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries accumulation = new TimeSeries();

        for (String wordCurrent : words) {
            TimeSeries wordAmount = weightHistory(wordCurrent);
            accumulation = accumulation.plus(wordAmount);
        }

        return accumulation;
    }
    public Set<String> allWords() {
        return new HashSet<>(wordToCount.keySet());
    }
}
