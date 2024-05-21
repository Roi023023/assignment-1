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

    public char getLetter() {
        return letter;
    }

    public int getScore() {
        return score;
    }

    public static class Bag{
        private static Bag bag = null; 

        private int[] quantities;
        private Tile[] tiles;

        private Bag(){
            quantities = new int[26];
            tiles = new Tile[26];

            for (int i = 0; i < 26; i++){
                char letter = (char) ('A' + i);//TODO:
                int score = getScore(letter);//use the getScore() method
                int quantity = getQuantity(letter);//use the getQuantity() method
                
                quantities[i] = quantity;
                tiles[i] = new Tile(letter, score);
            }
        }

        private static int getScore(char letter){
            switch (letter) {
                case 'A': case 'E': case 'I': case 'L': case 'N': case 'O': case 'R': case 'S': case 'T': case 'U':
                    return 1;
                case 'D': case 'G':
                    return 2;
                case 'B': case 'C': case 'M': case 'P':
                    return 3;
                case 'F': case 'H': case 'V': case 'W': case 'Y':
                    return 4;
                case 'K':
                    return 5;
                case 'J': case 'X':
                    return 8;
                case 'Q': case 'Z':
                    return 10;
                default:
                    return 0;
            }
        }

        private static int getQuantity(char letter) {
            switch (letter) {
                case 'A': case 'I':
                    return 9;
                case 'B': case 'C': case 'F': case 'H': case 'M': case 'P': case 'V': case 'W': case 'Y':
                    return 2;
                case 'D': case 'L': case 'S': case 'U':
                    return 4;
                case 'E':
                    return 12;
                case 'G': case 'O':
                    return 3;
                case 'J': case 'K': case 'Q': case 'X': case 'Z':
                    return 1;
                case 'N': case 'R': case 'T':
                    return 6;
                default:
                    return 0;
            }
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
            int index = letter - 'A';//return the letter number.
            if (index < 0 || index >= 26 || quantities[index] == 0) return null;//if its not a letter. or no quantities in beg left.
            quantities[index]--;
            return tiles[index];
        }

        public static Bag getBag(){
            if (bag == null){
                bag = new Bag();
            }
            return bag;
        }

        public int size(){
            int sum = 0;
            for(int i : quantities){
                sum += i;
            }
            return sum;
        }

        public void put(Tile tile){
            int index = tile.letter - 'A';
            if(index >= 0 && index < 26 && quantities[index] < getQuantity(tile.letter)){
            quantities[index]++;
            }
        }

        public int[] getQuantities(){
            return quantities.clone();
        }
    }
}