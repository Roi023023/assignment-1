package test;

import java.util.HashSet;
import java.util.Set;

public class CacheManager {
    private int size;
    private CacheReplacementPolicy policy;
    private Set<String> cache;

    public CacheManager(int size, CacheReplacementPolicy policy) {
        this.size = size;
        this.policy = policy;
        cache = new HashSet<>();
    }

    public boolean query(String word) {
        return cache.contains(word);
    }

    public void add(String word) {
        policy.add(word);
        cache.add(word);
        if (cache.size() > size) {
            String removed = policy.remove();
            cache.remove(removed);
        }
    }
}