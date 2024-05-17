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
            if (tiles[r][c] != null && wordTiles[i] != null && tiles[r][c] != wordTiles[i]) {
                return false;
            }
            if (tiles[r][c] != null && wordTiles[i] == null){//add this to make insted of '_' the word that need to be there.
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
        // TODO: For now its need to be true.
        return true;
    }
    
    public ArrayList<Word> getWords(Word word) {
        ArrayList<Word> newWords = new ArrayList<>();
        newWords.add(word);
        int down = 0;
        int up = 0;
        int right = 0;
        int left = 0;
        int r = 0;
        int j = 0;
        int count = 0;
        int index;
        int c;
        int newR = 0;
        int newC = 0;
        boolean next = false;

        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.isVertical();
        
        if (vertical) {
            for (Word w : newWords) {

                for (index = 0, r = row; index < w.getTiles().length && r < row + w.getTiles().length; index++, r++) {
                    if (w.getTiles()[index] == null && this.tiles[r][col] != null) {
                        next = true;
                        j = r + 1;
                        break;
                        
                    }
                }
            }
            index = 0;
            if (!next) {
                j = row;
            }
            while (this.tiles[j][col] != null && j < 15) {

                if (this.tiles[j][col + 1] != null && col + 1 < 15) {
                    right = col;
                    while (this.tiles[j][right] != null && right < 15) {
                        
                        if (tiles[j][right + 1] == null) {
                            count++;
                            break;
                        }
                        right++;
                        count++;

                    }
                }
                if (this.tiles[j][col - 1] != null && col - 1 >= 0) {
                    if (right != 0) {
                        left = col - 1;
                    } else {
                        left = col;
                    }
                    while (this.tiles[j][left] != null && left >= 0) {

                        if (tiles[j][left - 1] == null) {
                            count++;
                            break;
                        }
                        count++;
                        left--;
                    }
                }

                if (count > 1) {
                    if (left != 0) {
                        newC = left;
                    } else {
                        newC = col;
                    }
                    c = newC;
                    Tile[] t = new Tile[count];
                    for (int i = 0; i < count && newC < c + count; i++) {
                        t[i] = this.tiles[j][newC];
                        newC++;
                    }
                    Word newWord = new Word(t, j, c, false);
                    if (newWord != null) {
                        newWords.add(newWord);
                    }

                }

                j++;
                count = 0;
                left = 0;
                right = 0;
            }

        }
        if (!vertical) {
           

            for (Word w : newWords) {

                for (index = 0, c = col; index < w.getTiles().length && c < col + w.getTiles().length; index++, c++) {
                    if (w.getTiles()[index] == null && this.tiles[row][c] != null) {
                        next = true;
                        j = c + 1;
                        break;
                       
                    }
                }

            }

            index = 0;
            if (!next) {
                j = col;
            }

            while (this.tiles[row][j] != null && j < 15) {

                if (this.tiles[row + 1][j] != null && row + 1 < 15) {
                    down = row;
                    while (this.tiles[down][j] != null && down < 15) {
                        // count++;
                        if (tiles[down + 1][j] == null) {
                            count++;
                            break;
                        }
                        down++;
                        count++;

                    }
                }
                if (this.tiles[row - 1][j] != null && row - 1 >= 0) {
                    if (down != 0) {
                        up = row - 1;
                    } else {
                        up = row;
                    }
                    while (this.tiles[up][j] != null && up >= 0) {

                        if (tiles[up - 1][j] == null) {
                            count++;
                            break;
                        }
                        count++;
                        up--;
                    }
                }

                if (count > 1) {
                    if (up != 0) {
                        newR = up;
                    } else {
                        newR = row;
                    }
                    r = newR;
                    Tile[] t = new Tile[count];
                    for (int i = 0; i < count && newR < r + count; i++) {
                        t[i] = this.tiles[newR][j];
                        newR++;
                    }
                    Word newWord = new Word(t, r, j, true);
                    if (newWord != null) {
                        newWords.add(newWord);
                    }

                }

                j++;
                count = 0;
                up = 0;
                down = 0;
            }

        }

        return newWords;

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
            if(wordTiles[i] != null)//can delete this if statment , for now its work so i keep it.
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
                || (row == 3 || row == 11) && (col == 0 || col == 14 || col == 7)
                || (row == 6 || row == 8) && (col == 2 || col == 12 || col == 6 || col == 8)
                || (row == 7) && (col == 3 || col == 11);
    }
    
    private boolean isTripleLetterScore(int row, int col) {
        return (row == 1 || row == 13) && (col == 5 || col == 9)
                || (row == 5 || row == 9) && (col == 1 || col == 13 || col == 5 || col == 9);
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
                || (row == 7) && (col == 0 || col == 14);
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
        newWords.clear();
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