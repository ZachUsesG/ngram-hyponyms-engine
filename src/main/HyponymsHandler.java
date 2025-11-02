package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {

    private final WordNetHelper wn;
    private final NGramMap ngm;

    public HyponymsHandler(WordNetHelper wn, NGramMap ngm) {
        this.wn = wn;
        this.ngm = ngm;
    }

    public double countInRange(String word, int startYear, int endYear) {
        TimeSeries hist = ngm.countHistory(word, startYear, endYear);
        if (!hist.isEmpty()) {
            return hist.values().stream().mapToDouble(Number::doubleValue).sum();
        }

        hist = ngm.countHistory(word.toLowerCase(), startYear, endYear);
        return hist.values().stream().mapToDouble(Number::doubleValue).sum();
    }

    @Override
    public String handle(NgordnetQuery q) {
        int k = q.k();
        int startYear = q.startYear();
        int endYear = q.endYear();
        List<String> words = q.words();

        Set<String> resultSet = wn.hyponyms(words);
        System.out.println(">>> QUERY: " + words + ", k = " + k + ", years = " + startYear + "-" + endYear);
        System.out.println(">>> Hyponym resultSet (before filtering): " + resultSet);

        if (k == 0) {
            List<String> filtered = new ArrayList<>();
            for (String word : resultSet) {
                if (Character.isLowerCase(word.charAt(0))) {
                    filtered.add(word);
                }
            }
            Collections.sort(filtered);
            return filtered.toString();
        } else {
            Map<String, Double> freqs = new HashMap<>();
            for (String word : resultSet) {
                double freq = wn.countInRange(word, startYear, endYear);
                freqs.put(word, freq);
                System.out.printf(">>> FREQ: %s => %.2f\n", word, freq);
            }

            List<Map.Entry<String, Double>> filteredEntries = new ArrayList<>();
            for (Map.Entry<String, Double> entry : freqs.entrySet()) {
                if (entry.getValue() > 0) {
                    filteredEntries.add(entry);
                }
            }

            filteredEntries.sort((a, b) -> {
                int cmp = Double.compare(b.getValue(), a.getValue());
                return (cmp != 0) ? cmp : a.getKey().compareTo(b.getKey());
            });

            System.out.println(">>> Filtered freqs (nonzero only): " + freqs);
            System.out.println(">>> Sorted entries: " + filteredEntries);

            List<String> result = new ArrayList<>();
            for (int i = 0; i < Math.min(k, filteredEntries.size()); i++) {
                result.add(filteredEntries.get(i).getKey());
            }
            Collections.sort(result);

            System.out.println(">>> Final topK output: " + result);
            return result.toString();
        }
    }






    /* private Set<String> unionThenIntersectWords(List<String> words, int k) {
        List<Set<String>> hyponymSets = new ArrayList<>();

        for (String word : words) {
            Set<Integer> allIds = wn.getSynsetIds(word);
            Set<Integer> idsToUse;
            if (k > 0) {
                Set<Integer> singletonIds = allIds.stream().filter(id -> {
                            List<String> synsetWords = wn.getWordsFromId(id);
                            return synsetWords.size() == 1 && synsetWords.contains(word);
                        }).collect(Collectors.toSet());

                if (!singletonIds.isEmpty()) {
                    idsToUse = singletonIds;
                } else {
                    idsToUse = allIds;
                }
            } else {
                idsToUse = allIds;
            }

            Set<String> hyponymsForThisWord = new HashSet<>();
            for (int id : idsToUse) {
                Set<Integer> closure = wn.graphTraverse(id);
                closure.add(id);
                for (int cid : closure) {
                    hyponymsForThisWord.addAll(wn.getWordsFromId(cid));
                }
            }

            hyponymSets.add(hyponymsForThisWord);
        }

        if (hyponymSets.isEmpty()) {
            return Set.of();
        }

        Set<String> result = new HashSet<>(hyponymSets.get(0));
        for (int i = 1; i < hyponymSets.size(); i++) {
            result.retainAll(hyponymSets.get(i));
        }

        return result;
    }
    public Set<String> getHyponymsForDebug(String word) {
        return unionThenIntersectWords(List.of(word), 0);
    }
    public NGramMap getNGramMap() {
        return this.ngm;
    } */
}
