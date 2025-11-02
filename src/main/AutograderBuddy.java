package main;

import browser.NgordnetQueryHandler;
import ngrams.NGramMap;

import java.io.IOException;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymsHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {
        try {
            NGramMap ngm = new NGramMap(wordFile, countFile);
            WordNetHelper wn = new WordNetHelper(synsetFile, hyponymFile, wordFile, countFile);
            return new HyponymsHandler(wn, ngm);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    /*
    @source
    Utilized Alexander Jame's youtube skeleton
    Chat helped me understand printStackTrace
    */

}
