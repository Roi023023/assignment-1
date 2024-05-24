package test;

import java.util.*;


public class Board {

    boolean isStarUsed = true;
    private static Board board = null;
    public final Tile[][] tiles;
    private static final int ROW = 15;
    private static final int COL = 15;
   
    enum BonusType {//new type of bonus
        STAR_MIDDLE,
        DOUBLE_LETTER,
        TRIPLE_LETTER,
        DOUBLE_WORD,
        TRIPLE_WORD
    }

    private Map<String, BonusType> specialSlot;

    private Board() {
        this.tiles = new Tile[15][15];
        specialSlot = new HashMap<>();
        initSpecialSlot();
    }

    private void initSpecialSlot() {
        specialSlot.put("1,1", BonusType.DOUBLE_WORD);
        specialSlot.put("1,13", BonusType.DOUBLE_WORD);
        specialSlot.put("2,2", BonusType.DOUBLE_WORD);
        specialSlot.put("2,12", BonusType.DOUBLE_WORD);
        specialSlot.put("3,3", BonusType.DOUBLE_WORD);
        specialSlot.put("3,11", BonusType.DOUBLE_WORD);
        specialSlot.put("4,4", BonusType.DOUBLE_WORD);
        specialSlot.put("4,10", BonusType.DOUBLE_WORD);
        specialSlot.put("10,4", BonusType.DOUBLE_WORD);
        specialSlot.put("10,10", BonusType.DOUBLE_WORD);
        specialSlot.put("11,3", BonusType.DOUBLE_WORD);
        specialSlot.put("11,11", BonusType.DOUBLE_WORD);
        specialSlot.put("12,2", BonusType.DOUBLE_WORD);
        specialSlot.put("12,12", BonusType.DOUBLE_WORD);
        specialSlot.put("13,1", BonusType.DOUBLE_WORD);
        specialSlot.put("13,13", BonusType.DOUBLE_WORD);

        specialSlot.put("7,7", BonusType.STAR_MIDDLE);//star is also worth rtiple word , but only once.
        specialSlot.put("0,0", BonusType.TRIPLE_WORD);
        specialSlot.put("0,7", BonusType.TRIPLE_WORD);
        specialSlot.put("0,14", BonusType.TRIPLE_WORD);
        specialSlot.put("7,0", BonusType.TRIPLE_WORD);
        specialSlot.put("7,14", BonusType.TRIPLE_WORD);
        specialSlot.put("14,0", BonusType.TRIPLE_WORD);
        specialSlot.put("14,7", BonusType.TRIPLE_WORD);
        specialSlot.put("14,14", BonusType.TRIPLE_WORD);

        specialSlot.put("0,3", BonusType.DOUBLE_LETTER);
        specialSlot.put("0,11", BonusType.DOUBLE_LETTER);
        specialSlot.put("2,6", BonusType.DOUBLE_LETTER);
        specialSlot.put("2,8", BonusType.DOUBLE_LETTER);
        specialSlot.put("3,0", BonusType.DOUBLE_LETTER);
        specialSlot.put("3,7", BonusType.DOUBLE_LETTER);
        specialSlot.put("3,14", BonusType.DOUBLE_LETTER);
        specialSlot.put("6,2", BonusType.DOUBLE_LETTER);
        specialSlot.put("6,6", BonusType.DOUBLE_LETTER);
        specialSlot.put("6,8", BonusType.DOUBLE_LETTER);
        specialSlot.put("6,12", BonusType.DOUBLE_LETTER);
        specialSlot.put("7,3", BonusType.DOUBLE_LETTER);
        specialSlot.put("7,11", BonusType.DOUBLE_LETTER);
        specialSlot.put("8,2", BonusType.DOUBLE_LETTER);
        specialSlot.put("8,6", BonusType.DOUBLE_LETTER);
        specialSlot.put("8,8", BonusType.DOUBLE_LETTER);
        specialSlot.put("8,12", BonusType.DOUBLE_LETTER);
        specialSlot.put("11,0", BonusType.DOUBLE_LETTER);
        specialSlot.put("11,7", BonusType.DOUBLE_LETTER);
        specialSlot.put("11,14", BonusType.DOUBLE_LETTER);
        specialSlot.put("12,6", BonusType.DOUBLE_LETTER);
        specialSlot.put("12,8", BonusType.DOUBLE_LETTER);
        specialSlot.put("14,3", BonusType.DOUBLE_LETTER);
        specialSlot.put("14,11", BonusType.DOUBLE_LETTER);

        specialSlot.put("1,5", BonusType.TRIPLE_LETTER);
        specialSlot.put("1,9", BonusType.TRIPLE_LETTER);
        specialSlot.put("5,1", BonusType.TRIPLE_LETTER);
        specialSlot.put("5,5", BonusType.TRIPLE_LETTER);
        specialSlot.put("5,9", BonusType.TRIPLE_LETTER);
        specialSlot.put("5,13", BonusType.TRIPLE_LETTER);
        specialSlot.put("9,1", BonusType.TRIPLE_LETTER);
        specialSlot.put("9,5", BonusType.TRIPLE_LETTER);
        specialSlot.put("9,9", BonusType.TRIPLE_LETTER);
        specialSlot.put("9,13", BonusType.TRIPLE_LETTER);
        specialSlot.put("13,5", BonusType.TRIPLE_LETTER);
        specialSlot.put("13,9", BonusType.TRIPLE_LETTER);
        

    }

    public static Board getBoard() {
        if (board == null) {//check if we already have a board..
            board = new Board();
        }
        return board;
    }

    public Tile[][] getTiles() {
        return Arrays.stream(tiles).map(Tile[]::clone).toArray(Tile[][]::new);
    }

    private boolean VerticalWord(Word w) {
        int row = w.getRow();
        int col = w.getCol();
        int length = w.getTiles().length;

        if (row + length >= 15) {//go out of the board.
            return false;
        }
        if (this.tiles[7][7] == null) {
            if (col == 7 && row <= 7 && row + length >= 7) {
                return true;
            }
        } else {
            for (int i = 0; i < length; i++) {
                if ((tiles[row + i][col - 1] != null && col - 1 >= 0) || (tiles[row + i][col + 1] != null && col + 1 < COL)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean NotVerticalWord(Word w) {
        int row = w.getRow();
        int col = w.getCol();
        int length = w.getTiles().length;

        if (col + length >= 15) {//go out of board.
            return false;
        }
        if (this.tiles[7][7] == null) {
            if (row == 7 && col <= 7 && col + length >= 7) {
                return true;
            }
        } else {
            for (int i = 0; i < length; i++) {
                if ((tiles[row - 1][col + i] != null && row - 1 >= 0) || (tiles[row + 1][col + i] != null && row + 1 < ROW)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean boardLegal(Word w0) {
        int row = w0.getRow();
        int col = w0.getCol();
        if (row < 0 || row >= ROW || col < 0 || col >= COL) {
            return false;
        }
        if (w0.isVertical()) {
            if (VerticalWord(w0)) return true;
        }
        else {
            if (NotVerticalWord(w0)) return true;
        }
        return false;
    }

   
    public boolean dictionaryLegal(Word word) {
        // TODO: For now its need to be true always.
        return true;
    }

    public ArrayList<Word> getWords(Word word) {
        ArrayList<Word> words = new ArrayList<>();
        words.add(word);
        
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.isVertical();
        Tile[] wordTiles = word.getTiles();
        
        // Check for new words formed vertically
        if (!vertical) {
            for (int i = 0; i < wordTiles.length; i++) {
                if (wordTiles[i] != null) {
                    Word newWord = getVerticalWord(row, col + i);
                    if (newWord != null) {
                        words.add(newWord);
                    }
                }
            }
        }
        
        // Check for new words formed horizontally
        if (vertical) {
            for (int i = 0; i < wordTiles.length; i++) {
                if (wordTiles[i] != null) {
                    Word newWord = getHorizontalWord(row + i, col);
                    if (newWord != null) {
                        words.add(newWord);
                    }
                }
            }
        }
        
        return words;
    }
    
    private Word getVerticalWord(int row, int col) {
        int startRow = row;
        while (startRow > 0 && tiles[startRow - 1][col] != null) {
            startRow--;
        }
        
        int endRow = row;
        while (endRow < 14 && tiles[endRow + 1][col] != null) {
            endRow++;
        }
        
        if (startRow == endRow) {
            return null;
        }
        
        Tile[] wordTiles = new Tile[endRow - startRow + 1];
        for (int i = startRow; i <= endRow; i++) {
            wordTiles[i - startRow] = tiles[i][col];
        }
        
        return new Word(wordTiles, startRow, col, true);
    }
    
    private Word getHorizontalWord(int row, int col) {
        int startCol = col;
        while (startCol > 0 && tiles[row][startCol - 1] != null) {
            startCol--;
        }
        
        int endCol = col;
        while (endCol < 14 && tiles[row][endCol + 1] != null) {
            endCol++;
        }
        
        if (startCol == endCol) {
            return null;
        }
        
        Tile[] wordTiles = new Tile[endCol - startCol + 1];
        for (int i = startCol; i <= endCol; i++) {
            wordTiles[i - startCol] = tiles[row][i];
        }
        
        return new Word(wordTiles, row, startCol, false);
    }

    private BonusType getBonus(int row, int col) {
        String key = row + "," + col;
        if (specialSlot.containsKey(key)) {
            return specialSlot.get(key);
        }
        return null;
    }

    public int getScore(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        int lenght = word.getTiles().length;
        int score = 0;
        int wordMultiplier = 1;
        int letterScore = 0;
        boolean isVertical = word.isVertical();
        if (!isVertical) {
            for (int i = 0; i < lenght; i++) {
                if (word.getTiles()[i] == null) {

                    letterScore = this.tiles[row][col + i].getScore();
                } else {
                    letterScore = word.getTiles()[i].getScore();
                }
                
                    BonusType bonus = getBonus(row, col + i);
                    if (bonus != null) {
                        switch (bonus) {
                            case DOUBLE_LETTER:
                                letterScore *= 2;
                                break;
                            case TRIPLE_LETTER:
                                letterScore *= 3;
                                break;
                            case STAR_MIDDLE:
                                wordMultiplier *= 2;
                                specialSlot.remove("7,7");//only the first one is get the special.
                                break;
                            case DOUBLE_WORD:
                                wordMultiplier *= 2;
                                break;
                            case TRIPLE_WORD:
                                wordMultiplier *= 3;
                                break;
                        }
                    }
                score += letterScore;
            }
            score *= wordMultiplier;

        } else {//if its vertical
            for (int i = 0; i < lenght; i++) {
                if (word.getTiles()[i] == null) {
                    letterScore = this.tiles[row + i][col].getScore();
                } else {
                    letterScore = word.getTiles()[i].getScore();
                }
                    BonusType bonus = getBonus(row + i, col);
                    if (bonus != null) {
                        switch (bonus) {
                            case DOUBLE_LETTER:
                                letterScore *= 2;
                                break;
                            case TRIPLE_LETTER:
                                letterScore *= 3;
                                break;
                            case STAR_MIDDLE:
                                specialSlot.remove("7,7");//only the first one is get the special.
                                wordMultiplier *= 2;
                                break;
                            case DOUBLE_WORD:
                                wordMultiplier *= 2;
                                break;
                            case TRIPLE_WORD:
                                wordMultiplier *= 3;
                                break;
                        }
                    }
                score += letterScore;
            }
            score *= wordMultiplier;
        }
        return score;
    }

    public int tryPlaceWord(Word word) {

        if (!boardLegal(word)) {
            return 0;
        }
        
        placeWord(word);
        int totalScore = 0;

        ArrayList<Word> newWords = getWords(word);
        for (Word newWord : newWords) {
            totalScore += getScore(newWord);
        }

        for (Word newWord : newWords) {

            if (!dictionaryLegal(newWord)) {
                return 0;
            }
           
        }
        return totalScore;
    }

    private void placeWord(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.isVertical();
        Tile[] wordTiles = word.getTiles();
        
        for (int i = 0; i < wordTiles.length; i++) {
            if (wordTiles[i] != null) {
                int r = vertical ? row + i : row;
                int c = vertical ? col : col + i;
                tiles[r][c] = wordTiles[i];
            }
        }
    }

}
