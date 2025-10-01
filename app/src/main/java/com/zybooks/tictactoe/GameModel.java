package com.zybooks.peerproject2;

public class GameModel {
    private char[] board;
    private char currentPlayer;

    public GameModel() {
        board = new char[9];
        reset();
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(char player) {
        currentPlayer = player;
    }

    public boolean makeMove(int index) {
        if (board[index] == ' ') {
            board[index] = currentPlayer;
            return true;
        }
        return false;
    }

    public boolean checkWin() {
        int[][] wins = {
                {0,1,2},{3,4,5},{6,7,8}, // rows
                {0,3,6},{1,4,7},{2,5,8}, // cols
                {0,4,8},{2,4,6}           // diagonals
        };
        for (int[] pos : wins) {
            if (board[pos[0]] != ' ' &&
                    board[pos[0]] == board[pos[1]] &&
                    board[pos[1]] == board[pos[2]]) {
                return true;
            }
        }
        return false;
    }

    public boolean isBoardFull() {
        for (char c : board) {
            if (c == ' ') return false;
        }
        return true;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    public void reset() {
        for (int i = 0; i < 9; i++) board[i] = ' ';
        currentPlayer = 'X';
    }
}
