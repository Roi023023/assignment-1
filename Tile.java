package test;


public class Tile {
    public final char letter;
    public final int score;

    private Tile(char letter, int score){
        this.letter = letter;
        this.score = score;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + letter;
        result = prime * result + score;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tile other = (Tile) obj;
        if (letter != other.letter)
            return false;
        if (score != other.score)
            return false;
        return true;
    }

    public static class Bag{
        private static Bag bag = null; // Singleton

        private int[] quantities;
        private Tile[] tiles;

        private Bag(){
            quantities = new int[26];
            tiles = new Tile[26];

            for (int i = 0; i < 26; i++){
                char letter = (char) ('A' + i);
                int score = getScore(letter);
                int quantity = getQuantity(letter);
                
                quantities[i] = quantity;
                tiles[i] = new Tile(letter, score);
            }
        }

        public static Bag getBag(){
            if (bag == null){
                bag = new Bag();
            }
            return bag;
        }

        public Tile getRand(){
            if (size() == 0) return null;

            int index;
            do{
                index = (int) (Math.random() * 26);
            } while (quantities[index] == 0);

            quantities[index]--;
            return tiles[index];
        }

        public Tile getTile(char letter){
            int index = letter - 'A';
            if (index < 0 || index >= 26 || quantities[index] == 0) return null;

            quantities[index]--;
            return tiles[index];
        }
    }
	
}
