package test;

import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {
    private Map<String, Dictionary> dictionaries;
    private static DictionaryManager instance;

    private DictionaryManager() {
        dictionaries = new HashMap<>();
    }

    public static DictionaryManager get() {
        if (instance == null) {
            instance = new DictionaryManager();
        }
        return instance;
    }

    public boolean query(String... args) {
        boolean result = false;
        String word = args[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            String book = args[i];
            if (!dictionaries.containsKey(book)) {
                dictionaries.put(book, new Dictionary(book));
            }
            result |= dictionaries.get(book).query(word);
        }
        return result;
    }

    public boolean challenge(String... args) {
        boolean result = false;
        String word = args[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            String book = args[i];
            if (!dictionaries.containsKey(book)) {
                dictionaries.put(book, new Dictionary(book));
            }
            result |= dictionaries.get(book).challenge(word);
        }
        return result;
    }

    public int getSize() {
        return dictionaries.size();
    }
}