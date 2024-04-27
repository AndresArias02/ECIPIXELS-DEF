package edu.eci.arsw.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.eci.arsw.service.GameServices;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RedisHash
public class Player implements Runnable, Serializable {
    private Integer id;
    private String name;
    private Head head;
    private String color;
    private List<String> pixelsOwned;
    private List<String> pixelsRoute;
    private Integer gainedArea;
    private boolean isAlive = true;
    private boolean movingUp = false;
    private boolean movingRight = false;
    private boolean movingLeft = false;
    private boolean movingDown = false;
    private boolean move = false;
    @Transient
    @JsonIgnore
    private GameServices gameServices;


    public Player(String name, GameServices gameServices){
        setId();
        this.name = name;
        this.head = null;
        this.color = "";
        this.pixelsOwned = new ArrayList<>();
        this.pixelsRoute = new ArrayList<>();
        this.pixelsRoute.add("0,0");
        this.gainedArea = 0;
        this.gameServices = gameServices;
    }

    public Player() {

    }

    @Override
    public void run() {
        while (move) {
            try {
                System.out.println("-----------------------------------------");
                moveUp();
                moveDown();
                moveLeft();
                moveRight();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        Thread player = new Thread(this);
        player.start();
    }

    private void moveUp(){
        if(movingUp){
            System.out.println("se esta moooviiieeendddooo arrribbbaa");
            this.head.moveUp();
            gameServices.updatePlayer(this);
            updatePixelsRoute();
        }
    }

    private void moveDown(){
        if(movingDown){
            System.out.println("se esta moooviiieeendddooo abaaaaajjjjooo");
            this.head.moveDown();
            gameServices.updatePlayer(this);
            updatePixelsRoute();
        }
    }

    private void moveLeft(){
        if(movingLeft){
            System.out.println("se esta moooviiieeendddooo iiizzzqqqquuuiiieeerrrdaaa");
            this.head.moveLeft();
            gameServices.updatePlayer(this);
            updatePixelsRoute();
        }
    }

    private void moveRight(){
        if(movingRight){
            System.out.println("se esta moooviiieeendddooo dddeeerrreeeccchhhaa");
            this.head.moveRight();
            gameServices.updatePlayer(this);
            updatePixelsRoute();
        }
    }

    public void updatePixelsRoute() {
        Game game = gameServices.getGame();
        Integer pixel = game.getPixel(head.getRow(), head.getCol());
        String positionPixel = head.getRow()+","+ head.getCol();
        if (pixel != null) {
            checkIfPlayerKilledAnother(positionPixel);
            if (!pixelsOwned.contains(positionPixel)) {
                pixelsRoute.add(positionPixel);
            } else {
                if (!pixelsRoute.isEmpty()) {
                    for (String positionsPixels : pixelsRoute) {
                        String[] values = positionsPixels.split(",");
                        int x = Integer.parseInt(values[0]);
                        int y = Integer.parseInt(values[1]);
                        Integer p = game.getPixel(x,y);
                        if (p != null && p!=0) {
                            Player player = gameServices.getPlayer(String.valueOf(p));
                            gameServices.updatePixelBoardGrid(positionPixel,0);
                            player.removePixel(positionsPixels);
                            player.setGainedArea(player.getPixelsOwned().size());
                        }
                        gameServices.updatePixelBoardGrid(positionPixel,this.getPlayerId());
                    }

                    //fillArea(pixelsRoute, game,this);
                    pixelsOwned.addAll(pixelsRoute);
                    setGainedArea(pixelsOwned.size());
                    pixelsRoute.clear();
                    pixelsRoute.add("0,0");
                    game.updateLeaderBoard();
                    gameServices.updateGame(game);
                }
            }
        } else {
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

    public void movePlayerUp() {
        synchronized (this){
            this.move = true;
            this.movingUp = true;
            this.movingDown = false;
            this.movingLeft = false;
            this.movingRight = false;
            this.run();
        }

    }

    public void movePlayerRight() {
        synchronized (this){
            this.move = true;
            this.movingRight = true;
            this.movingUp = false;
            this.movingDown = false;
            this.movingLeft = false;
            this.run();
        }

    }

    public void movePlayerLeft() {
        this.move = true;
        this.movingLeft = true;
        this.movingRight = false;
        this.movingUp = false;
        this.movingDown = false;
        this.run();
    }

    public void movePlayerDown() {
        synchronized (this){
            this.move = true;
            this.movingDown = true;
            this.movingRight = false;
            this.movingUp = false;
            this.movingLeft = false;
            this.run();
        }
    }

    public void stopPlayer() {
        synchronized (this){
            this.move = false;
            this.movingDown = false;
            this.movingRight = false;
            this.movingUp = false;
            this.movingLeft = false;
            this.run();
        }
    }

    public synchronized void addPixelOwned(int x, int y) {
        pixelsOwned.add(x + "," + y);
    }

    public synchronized void addPixelRoute(int x, int y) {
        pixelsRoute.add(x + "," + y);
    }

    public synchronized void removePixel(String p) {
        pixelsOwned.remove(p);
    }

    public Integer getPlayerId() {
        return id;
    }

    public String getPlayerName() {
        return name;
    }


    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public String getColor() {
        return color;
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

    public boolean playerIsAlive() {
        return isAlive;
    }

    public synchronized void setAlive(boolean alive) {
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
                game.setPixelProperties(x,y, this.getPlayerId());
            }
        }

        this.pixelsOwned.addAll(allPixels);

    }

}