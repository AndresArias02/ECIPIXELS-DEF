package edu.eci.arsw.controller;

import edu.eci.arsw.model.*;
import edu.eci.arsw.service.BoardServices;
import edu.eci.arsw.service.GameServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/eciPixelsGame")
@CrossOrigin(origins = "http://localhost:4200")
public class GameController {

    @Autowired
    private GameServices gameServices;

    @Autowired
    private BoardServices boardServices;

    @RequestMapping(value = "/leaderBoard",method = RequestMethod.GET)
    public ResponseEntity<?> getLeaderBoard(){
        try{
            List<Player> players = gameServices.getLeaderBoard();
            return new ResponseEntity<>(players,HttpStatus.ACCEPTED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/board",method = RequestMethod.GET)
    public ResponseEntity<?> getBoard(){
        try{
            Integer[][] board  = boardServices.getBoard();
            return new ResponseEntity<>(board,HttpStatus.ACCEPTED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/players",method = RequestMethod.GET)
    public ResponseEntity<?> getPlayers(){
        try{
            List<Player> players = gameServices.getPlayers() ;
            return new ResponseEntity<>(players,HttpStatus.ACCEPTED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/addPlayer",method = RequestMethod.POST)
    public ResponseEntity<?> addPlayer(@RequestBody String name) {
        if (name == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            gameServices.addNewPlayer(name);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/deletePlayer/{playerId}",method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePlayer(@PathVariable String playerId) {
        Player player = gameServices.getPlayer(playerId);
        if (player == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            gameServices.deletePlayer(player);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }
}
