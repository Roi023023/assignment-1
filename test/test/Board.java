package test;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private static Board board = null;
    private Tile[][] tiles;
    boolean isStarUsed = true;

    private Board() {
        tiles = new Tile[15][15];
    }
    
    public static Board getBoard() {
        if (board == null) {
            board = new Board();
        }
        return board;
    }
    
    public Tile[][] getTiles() {
        return Arrays.stream(tiles).map(Tile[]::clone).toArray(Tile[][]::new);
    }
    
    public boolean boardLegal(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.isVertical();
        Tile[] wordTiles = word.getTiles();
        
        // Check if the word is within the board boundaries
        if (row < 0 || row >= 15 || col < 0 || col >= 15) {
            return false;
        }
        
        // Check if the word fits on the board
        if (vertical) {
            if (row + wordTiles.length > 15) {
                return false;
            }
        } else {
            if (col + wordTiles.length > 15) {
                return false;
            }
        }
        
        // Check if the word overlaps with existing tiles
        for (int i = 0; i < wordTiles.length; i++) {
            int r = vertical ? row + i : row;
            int c = vertical ? col : col + i;
            if (tiles[r][c] != null && wordTiles[i] != null && tiles[r][c] != wordTiles[i]) {//TODO:i have a problem here with the null 
                return false;
            }
            if (tiles[r][c] != null && wordTiles[i] == null){//TODO:i add to change '_' for what is in this place.
                wordTiles[i] = tiles[r][c];
            }
        }
        
        // Check if the word is connected to an existing tile or the star in the center
        boolean connected = false;
        for (int i = 0; i < wordTiles.length; i++) {
            int r = vertical ? row + i : row;
            int c = vertical ? col : col + i;
            if (hasTileAdjacent(r, c) || (r == 7 && c == 7)) {
                connected = true;
                break;
            }
        }
        
        return connected;
    }
    
    private boolean hasTileAdjacent(int row, int col) {
        // Check if there is a tile in any adjacent position
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int r = row + i;
                int c = col + j;
                if (r >= 0 && r < 15 && c >= 0 && c < 15 && tiles[r][c] != null) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean dictionaryLegal(Word word) {
        // TODO: Implement dictionary check
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
    
    public int getScore(Word word) {
        int score = 0;
        int wordMultiplier = 1;
        int[] wordScores = new int[word.getTiles().length];
        
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.isVertical();
        Tile[] wordTiles = word.getTiles();
        
        for (int i = 0; i < wordTiles.length; i++) {
            int r = vertical ? row + i : row;
            int c = vertical ? col : col + i;
            
            int letterScore = 0;
            int letterMultiplier = 1;
            if(wordTiles[i] != null)//TODO:can delete this if statment
            {
                letterScore = wordTiles[i].score;
            }

            // Check for bonus squares
            if (tiles[r][c] == null) {
                if (isDoubleLetterScore(r, c)) {
                    letterMultiplier = 2;
                } else if (isTripleLetterScore(r, c)) {
                    letterMultiplier = 3;
                } else if (isDoubleWordScore(r, c)) {
                    wordMultiplier *= 2;
                } else if (isTripleWordScore(r, c)) {
                    wordMultiplier *= 3;
                }
            }
            
            wordScores[i] = letterScore * letterMultiplier;
        }
        
        for (int wordScore : wordScores) {
            score += wordScore;
        }
        
        score *= wordMultiplier;
        return score;
    }
    
    private boolean isDoubleLetterScore(int row, int col) {
        return (row == 0 || row == 14) && (col == 3 || col == 11)
                || (row == 2 || row == 12) && (col == 6 || col == 8)
                || (row == 3 || row == 11) && (col == 0 || col == 14 || col == 7)//TODO:add col 7.
                || (row == 6 || row == 8) && (col == 2 || col == 12 || col == 6 || col == 8)//TODO:add col 6 , 8.
                || (row == 7) && (col == 3 || col == 11);
    }
    
    private boolean isTripleLetterScore(int row, int col) {
        return (row == 1 || row == 13) && (col == 5 || col == 9)
                || (row == 5 || row == 9) && (col == 1 || col == 13 || col == 5 || col == 9);//TODO:add col 5,9
    }
    
    private boolean isDoubleWordScore(int row, int col) {
        
        if (isStarUsed && row == 7 && col == 7) {
            isStarUsed = false;
            return true;
        }
        
        return (row == 1 || row == 13) && (col == 1 || col == 13)
        || (row == 2 || row == 12) && (col == 2 || col == 12)
        || (row == 3 || row == 11) && (col == 3 || col == 11)
        || (row == 4 || row == 10) && (col == 4 || col == 10);
        
    }
    
    private boolean isTripleWordScore(int row, int col) {
        return (row == 0 || row == 14) && (col == 0 || col == 14)
                || (row == 7) && (col == 0 || col == 14);//TODO:need to be in row 7 , col 0 and 14
    }
    
    public int tryPlaceWord(Word word) {
        if (!boardLegal(word)) {
            return 0;
        }
        
        ArrayList<Word> newWords = getWords(word);

        for (Word newWord : newWords) {
            if (!dictionaryLegal(newWord)) {
                return 0;
            }
        }
        
        int score = 0;
        for (Word newWord : newWords) {
            score += getScore(newWord);
            placeWord(newWord);
        }
        return score;
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