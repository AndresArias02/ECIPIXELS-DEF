package edu.eci.arsw.model;

import java.io.Serializable;
import java.util.*;

public class LeaderBoard implements Serializable {
    private List<Player> players;

    public LeaderBoard() {
        this.players = new ArrayList<>();
    }
    public void addNewPlayer(Player player){
        players.add(player);
        setInLeaderBoard();
    }

    public void setInLeaderBoard(){
        players.sort(Comparator.comparingInt(Player::getGainedArea).reversed());
    }



    public List<Player> getLeaderBoard() {
        return players;
    }

    public void setLeaderBoard(ArrayList<Player> players) {
        this.players = players;
    }
}
