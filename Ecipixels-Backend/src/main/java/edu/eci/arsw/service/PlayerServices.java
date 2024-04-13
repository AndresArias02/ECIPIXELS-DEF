package edu.eci.arsw.service;


import edu.eci.arsw.Configurations.RedisConfig;
import edu.eci.arsw.model.Player;
import edu.eci.arsw.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServices {


    @Autowired
    PlayerRepository playerRepository;

    @Cacheable(RedisConfig.cacheName)
    public void addPlayer(Player player) {
        playerRepository.save(player);
        player.start();
    }

    @Cacheable(RedisConfig.cacheName)
    public void updatePlayer(Player player){
        playerRepository.save(player);
    }

    @Cacheable(RedisConfig.cacheName)
    public void deletePlayer(Player player){
        playerRepository.deleteById(player.getPlayerId());
    }

    public void moveUp(Integer idPlayer){
        Player player;
        Optional<Player> optionalPlayer = playerRepository.findById(idPlayer);
        if (optionalPlayer.isPresent()) {
            player = optionalPlayer.get();
            player.movePlayerUp();
        }
    }

    public void moveDown(Integer idPlayer){
        Player player;
        Optional<Player> optionalPlayer = playerRepository.findById(idPlayer);
        if (optionalPlayer.isPresent()) {
            player = optionalPlayer.get();
            player.movePlayerDown();
        }
    }

    public void moveRight(Integer idPlayer){
        Player player;
        Optional<Player> optionalPlayer = playerRepository.findById(idPlayer);
        if (optionalPlayer.isPresent()) {
            player = optionalPlayer.get();
            player.movePlayerRight();
            playerRepository.save(player);
        }
    }

    public void moveLeft(Integer idPlayer){
        Player player;
        Optional<Player> optionalPlayer = playerRepository.findById(idPlayer);
        if (optionalPlayer.isPresent()) {
            player = optionalPlayer.get();
            player.movePlayerLeft();
            playerRepository.save(player);
        }
    }

    public void stop(Integer idPlayer){
        Player player;
        Optional<Player> optionalPlayer = playerRepository.findById(idPlayer);
        if (optionalPlayer.isPresent()) {
            player = optionalPlayer.get();
            player.stopPlayer();
            playerRepository.save(player);
        }
    }

    public List<String> getPixelsOwned(String id){
        Optional<Player> optionalPlayer = getPlayer(Integer.parseInt(id));
        List<String> pixels = null;
        if (optionalPlayer.isPresent()) {
            pixels = optionalPlayer.get().getPixelsOwned();
        }
        return pixels;
    }

    public Optional<Player> getPlayer(Integer idPlayer) {
        return playerRepository.findById(idPlayer);
    }

    public Iterable<Player> getPLayers(){
        return playerRepository.findAll();
    }
}