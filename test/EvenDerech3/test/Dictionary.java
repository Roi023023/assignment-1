package test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {
    private CacheManager existCM;
    private CacheManager notExistCM;
    private BloomFilter bloomFilter;
    private String[] fileNames;

    public Dictionary(String... fileNames) {
        existCM = new CacheManager(400, new LRU());
        notExistCM = new CacheManager(100, new LFU());
        bloomFilter = new BloomFilter(256, "MD5", "SHA1");
        this.fileNames = fileNames;

        for (String fileName : fileNames) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] words = line.split("\\W+");
                    for (String word : words) {
                        bloomFilter.add(word);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean query(String word) {
        if (existCM.query(word)) {
            return true;
        }
        if (notExistCM.query(word)) {
            return false;
        }
        boolean exist = bloomFilter.contains(word);
        if (exist) {
            existCM.add(word);
        } else {
            notExistCM.add(word);
        }
        return exist;
    }

    public boolean challenge(String word) {
        try {
            boolean exist = IOSearcher.search(word, fileNames);
            if (exist) {
                existCM.add(word);
            } else {
                notExistCM.add(word);
            }
            return exist;
        } catch (IOException e) {
            return false;
        }
    }
}