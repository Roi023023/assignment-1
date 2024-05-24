package test;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class LFU implements CacheReplacementPolicy {
    private Map<String, Integer> frequencyMap;
    private Map<Integer, Set<String>> frequencySet;
    private int minFrequency;

    public LFU() {
        frequencyMap = new HashMap<>();
        frequencySet = new HashMap<>();
        minFrequency = 0;
    }

    @Override
    public void add(String word) {
        int frequency = frequencyMap.getOrDefault(word, 0);
        frequencyMap.put(word, frequency + 1);
        frequencySet.computeIfAbsent(frequency, k -> new LinkedHashSet<>()).remove(word);
        frequencySet.computeIfAbsent(frequency + 1, k -> new LinkedHashSet<>()).add(word);
        if (frequencySet.get(minFrequency).isEmpty()) {
            minFrequency++;
        }
    }

    @Override
    public String remove() {
        String removed = frequencySet.get(minFrequency).iterator().next();
        frequencySet.get(minFrequency).remove(removed);
        frequencyMap.remove(removed);
        return removed;
    }
}