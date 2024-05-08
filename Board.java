package test;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private static Board board = null;
    private Tile[][] tiles;

    private Board(){
        tiles = new Tile[15][15];
    }

    public static Board getBoard(){
        if(board == null){
            board = new Board();
        }
        return board;
    }
}
