package edu.eci.arsw.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.eci.arsw.service.GameServices;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;
import java.util.*;

@RedisHash
public class Player implements Serializable{
    private Integer id;
    private String name;
    private Head head;
    private String color;
    private List<String> pixelsOwned;
    private List<String> pixelsRoute;
    private Integer gainedArea;
    private boolean isAlive = true;
    @Transient
    @JsonIgnore
    private GameServices gameServices;


    public Player(String name){
        setId();
        this.name = name;
        this.head = null;
        this.color = "";
        this.pixelsOwned = new ArrayList<>();
        this.pixelsRoute = new ArrayList<>();
        this.pixelsRoute.add("0,0");
        this.gainedArea = 0;
    }

    public Player() {

    }

    public void updatePixelsRoute() {
        Game game = gameServices.getGame();
        Integer pixel = game.getPixel(head.getRow(), head.getCol());
        String positionPixel = head.getRow() + "," + head.getCol();
        if (this.isAlive && pixel != null && head.getCol() != 0 && head.getRow() != 0 && head.getCol() != 49 && head.getRow() != 49) {
            checkIfPlayerKilledAnother(positionPixel);
            if (!pixelsOwned.contains(positionPixel)) {
                pixelsRoute.add(positionPixel);
            } else {
                if (!pixelsRoute.isEmpty()) {
                    List<String> routeCopy = new ArrayList<>(pixelsRoute);
                    for (String positionsPixels : routeCopy) {
                        String[] values = positionsPixels.split(",");
                        int x = Integer.parseInt(values[0]);
                        int y = Integer.parseInt(values[1]);
                        Integer p = game.getPixel(x, y);
                        if (p != null && p != 0) {
                            Player player = gameServices.getPlayer(String.valueOf(p));
                            gameServices.updatePixelBoardGrid(positionPixel, 0);
                            player.removePixel(positionsPixels);
                            player.setGainedArea(player.getPixelsOwned().size());
                        }
                        gameServices.updatePixelBoardGrid(positionPixel, this.id);
                    }

                    routeCopy.remove("0,0");
                    pixelsOwned.addAll(routeCopy);
                    setGainedArea(pixelsOwned.size());
                    pixelsRoute.clear();
                    pixelsRoute.add("0,0");
                    game.updateLeaderBoard();
                    gameServices.updateGame(game);
                }
            }
        } else {
            this.isAlive = false;
            game.deletePlayer(this);
        }
    }


    public void checkIfPlayerKilledAnother(String pixel) {
        Game game = gameServices.getGame();
        List<Player> playersToRemove = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            if (player != this && player.getPixelsRoute().contains(pixel)) {
                playersToRemove.add(player);
            }
        }
        for(Player p: playersToRemove){
            game.deletePlayer(p);
        }
    }

    public void addPixelOwned(int x, int y) {
        pixelsOwned.add(x + "," + y);
    }

    public void addPixelRoute(int x, int y) {
        pixelsRoute.add(x + "," + y);
    }

    public void removePixel(String p) {
        pixelsOwned.remove(p);
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Head getHead() {
        return this.head;
    }

    public String getPlayerName(){
        return this.name;
    }

    public String getColor() {
        return color;
    }

    public Integer getPlayerId(){
        return this.id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getPixelsOwned() {
        return pixelsOwned;
    }

    public List<String> getPixelsRoute() {
        return pixelsRoute;
    }

    public void setGameServices(GameServices gameServices) {
        this.gameServices = gameServices;
    }

    public int getGainedArea() {
        return gainedArea;
    }

    public void setGainedArea(int gainedArea) {
        this.gainedArea = gainedArea;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    private void setId(){
        UUID uuid = UUID.randomUUID();
        this.id = uuid.hashCode();
    }

    private void fillArea(List<String> path, Game game, Player player) {
        List<String> allPixels = new ArrayList<>();

        int minX = 50;
        int maxX = 0;
        int minY = 50;
        int maxY = 0;

        //Hallamos los minimos y maximos del area a capturar
        for(String pixel : path){
            String values [] = pixel.split(",");
            int x = Integer.parseInt(values[0]);
            int y = Integer.parseInt(values[1]);
            if (x <= minX) {
                minX=x;
            } else if (x >= maxX){
                maxX=x;
            }else if (y <= minY){
                minY=x;
            }else if (y >= maxY) {
                maxY=x;
            }
            if (x < maxX && x > minX && y < maxY && y > minY ){

            }
        }

        //buscamos los pixeles que estan dentro del area
        for (String pixel : path){

            String values [] = pixel.split(",");
            int x = Integer.parseInt(values[0]);
            int y = Integer.parseInt(values[1]);
            Integer p = game.getPixel(x,y);

            if (x < maxX && x > minX && y < maxY && y > minY ){

                allPixels.add(x+","+y);

                if (p != 0) {
                    player.removePixel(pixel);
                    player.setGainedArea(player.getPixelsOwned().size());
                }
                game.setPixelProperties(x,y, this.id);
            }
        }

        this.pixelsOwned.addAll(allPixels);
    }
}