package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import ngrams.NGramMap;

public class WordNetHelper {

    private GraphsHelper graph;
    private Map<String, Set<Integer>> synsetFinder;
    private Map<Integer, List<String>> nounRecover;
    private NGramMap ngm;
    public Set<Integer> graphTraverse(Set<Integer> synsetIds) {
        return graph.traverse(synsetIds);
    }
    public void debugInspectWord(String word) {
        Set<Integer> ids = getSynsetIds(word);

        for (int id : ids) {
            List<String> words = getWordsFromId(id);
            Set<Integer> children = graphTraverse(id);
        }
    }

    public WordNetHelper(String synsetsF, String hyponymsF, String wordFile, String countFile) throws IOException {
        graph = new GraphsHelper();
        synsetFinder = new HashMap<>();
        nounRecover = new HashMap<>();
        ngm = new NGramMap(wordFile, countFile);

        List<String> hypoLines = Files.readAllLines(Paths.get(hyponymsF));
        for (String line : hypoLines) {
            String[] parts = line.split(",");
            int from = Integer.parseInt(parts[0]);
            graph.addNode(from);
            for (int i = 1; i < parts.length; i++) {
                int to = Integer.parseInt(parts[i]);
                graph.addNode(to);
                graph.addConnect(from, to);
            }
        }

        List<String> synsetLines = Files.readAllLines(Paths.get(synsetsF));
        for (String line : synsetLines) {
            String[] parts = line.split(",", 3);
            int synsetId = Integer.parseInt(parts[0]);
            String[] words = parts[1].split(" ");
            for (String word : words) {
                word = word.toLowerCase();
                synsetFinder.putIfAbsent(word, new HashSet<>());
                synsetFinder.get(word).add(synsetId);
            }
            nounRecover.put(synsetId, Arrays.asList(words));
        }
    }
    public Set<Integer> graphTraverse(int synsetId) {
        return graph.traverse(Set.of(synsetId));
    }

    public List<String> getWordsFromId(int id) {
        return nounRecover.getOrDefault(id, List.of());
    }


    public Set<String> hyponymsQ(int id) {
        Set<Integer> reachable = graph.traverse(Set.of(id));
        Set<String> hyponymList = new HashSet<>();
        for (int synsetId : reachable) {
            List<String> words = nounRecover.get(synsetId);
            if (words != null) {
                hyponymList.addAll(words);
            }
        }
        return hyponymList;
    }

    /* public Set<String> hyponymsFreq(int from, int to) {
        Set<String> fromSet = hyponymsQ(from);
        Set<String> toSet = hyponymsQ(to);
        Set<String> shared = new HashSet<>();
        for (String wordShare : fromSet) {
            if (toSet.contains(wordShare)) {
                shared.add(wordShare);
            }
        }
        return shared;
    } */

    public Map<String, Set<Integer>> getSynsetFinder() {
        return synsetFinder;
    }

    public Set<Integer> getSynsetIds(String word) {
        return synsetFinder.getOrDefault(word.toLowerCase(), Set.of());
    }

    public double countInRange(String word, int startYear, int endYear) {
        double total = 0;
        for (Double count : ngm.countHistory(word, startYear, endYear).values()) {
            if (count != null) {
                total += count;
            }
        }

        return total;
    }

    /* public Set<Integer> getCourseRelatedSynsetIds(String word) {
        Set<Integer> matching = new HashSet<>();

        for (Map.Entry<Integer, List<String>> entry : nounRecover.entrySet()) {
            int id = entry.getKey();
            for (String w : entry.getValue()) {
                if (w.matches("CS\\d+[A-Z]*")) {
                    matching.add(id);
                    break;
                }
            }
        }

        return matching;
    } */

    public Set<String> hyponyms(List<String> words) {
        List<Set<String>> allHypos = new ArrayList<>();

        for (String word : words) {
            Set<Integer> ids = getSynsetIds(word);
            Set<String> hyposForWord = new HashSet<>();
            for (int id : ids) {
                hyposForWord.addAll(hyponymsQ(id));
            }
            allHypos.add(hyposForWord);
        }

        if (allHypos.isEmpty()) {
            return Set.of();
        }

        Set<String> intersection = new HashSet<>(allHypos.get(0));
        for (int i = 1; i < allHypos.size(); i++) {
            intersection.retainAll(allHypos.get(i));
        }

        return intersection;
    }


    /*
    @source
    Utilized Alexander Jame's youtube skeleton
    Chat clarified my understanding for the
    functions parseInt, set.Of, IOEexception
    */
}
