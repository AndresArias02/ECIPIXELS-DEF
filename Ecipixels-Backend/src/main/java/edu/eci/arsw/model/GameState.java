package edu.eci.arsw.model;

import java.util.List;

public class GameState {

    private Integer[][] board;
    private List<Player> players;
    private List<Player> leaderBoard;

    public GameState(Integer[][] board, List<Player> players, List<Player> leaderBoard) {
        this.board = board;
        this.players = players;
        this.leaderBoard = leaderBoard;
    }

    public GameState() {
    }

    public Integer[][] getBoard() {
        return board;
    }

    public void setBoard(Integer[][] board) {
        this.board = board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getLeaderBoard() {
        return leaderBoard;
    }

    public void setLeaderBoard(List<Player> leaderBoard) {
        this.leaderBoard = leaderBoard;
    }
}
