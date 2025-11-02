package main;

import java.util.*;


public class GraphsHelper {

    private Map<Integer, Set<Integer>> adjacencyList;

    public GraphsHelper() {
        adjacencyList = new HashMap<>();
    }

    public void addNode(int id) {
        adjacencyList.putIfAbsent(id, new HashSet<>());
    }

    public void addConnect(int from, int to) {
        if (adjacencyList.containsKey(from) && !adjacencyList.get(from).contains(to)) {
            adjacencyList.putIfAbsent(from, new HashSet<>());
            adjacencyList.get(from).add(to);
        }
    }

    public Set<Integer> traverse(Set<Integer> startNodes) {
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> stack = new ArrayDeque<>(startNodes);

        while (!stack.isEmpty()) {
            int node = stack.pop();
            if (visited.add(node)) {
                Set<Integer> neighbors = adjacencyList.getOrDefault(node, Set.of());
                stack.addAll(neighbors);
            }
        }

        return visited;
    }

    public int size() {
        return adjacencyList.size();
    }
    /*
    @source
    helper methods from getting started youtube videos in lab
    chatgpt on helping me understand the utility of the
    putIfAbsent function and addAll
    */
}
