package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter {
    private BitSet bitSet;
    private MessageDigest[] hashFunctions;

    public BloomFilter(int size, String... algs) {
        bitSet = new BitSet(size);
        hashFunctions = new MessageDigest[algs.length];
        for (int i = 0; i < algs.length; i++) {
            try {
                hashFunctions[i] = MessageDigest.getInstance(algs[i]);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    public void add(String word) {
        for (MessageDigest md : hashFunctions) {
            byte[] digest = md.digest(word.getBytes());
            BigInteger bi = new BigInteger(1, digest);
            int index = bi.intValue() % bitSet.size();
            bitSet.set(Math.abs(index));
        }
    }

    public boolean contains(String word) {
        for (MessageDigest md : hashFunctions) {
            byte[] digest = md.digest(word.getBytes());
            BigInteger bi = new BigInteger(1, digest);
            int index = bi.intValue() % bitSet.size();
            if (!bitSet.get(Math.abs(index))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitSet.length(); i++) {
            sb.append(bitSet.get(i) ? "1" : "0");
        }
        return sb.toString();
    }
}