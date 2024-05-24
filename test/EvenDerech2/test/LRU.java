package test;

import java.util.LinkedHashSet;
import java.util.Set;

public class LRU implements CacheReplacementPolicy {
    private Set<String> cache;

    public LRU() {
        cache = new LinkedHashSet<>();
    }

    @Override
    public void add(String word) {
        cache.remove(word);
        cache.add(word);
    }

    @Override
    public String remove() {
        String removed = cache.iterator().next();
        cache.remove(removed);
        return removed;
    }
}