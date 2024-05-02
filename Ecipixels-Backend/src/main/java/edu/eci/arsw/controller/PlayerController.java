package edu.eci.arsw.controller;

import edu.eci.arsw.service.GameServices;
import edu.eci.arsw.service.PlayerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/eciPixelsPlayer")
@CrossOrigin(origins = "http://localhost:4200")
public class PlayerController {

    @Autowired
    private PlayerServices playerServices;

    @Autowired
    private GameServices gameServices;


    @RequestMapping(value = "/pOwned/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> getPixelsOwned(@PathVariable String id){
        try{
            List<String> pixels= playerServices.getPixelsOwned(id);
            return new ResponseEntity<>(pixels,HttpStatus.ACCEPTED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    /*@RequestMapping(value = "/moveUp",method = RequestMethod.POST)
    public ResponseEntity<?> moveUp(@RequestBody Integer playerId) {
        System.out.println("-------- mover arriba -----------");
        if (playerId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            playerServices.move(playerId,gameServices);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }*/

    @RequestMapping(value = "/moveDown",method = RequestMethod.POST)
    public ResponseEntity<?> moveDown(@RequestBody Integer idPlayer) {
        System.out.println("-------- mover abajo -----------");
        if (idPlayer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            playerServices.moveDown(idPlayer,gameServices);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/moveRight",method = RequestMethod.POST)
    public ResponseEntity<?> moveRight(@RequestBody Integer idPlayer) {
        System.out.println("-------- mover derecha -----------");
        if (idPlayer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            playerServices.moveRight(idPlayer,gameServices);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/moveLeft",method = RequestMethod.POST)
    public ResponseEntity<?> moveLeft(@RequestBody Integer idPlayer) {
        System.out.println("-------- mover izquierda -----------");
        if (idPlayer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            playerServices.moveLeft(idPlayer,gameServices);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/stop",method = RequestMethod.POST)
    public ResponseEntity<?> stop(@RequestBody Integer playerId) {
        System.out.println("-------- parar jugadorr -----------");
        if (playerId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            playerServices.stop(playerId,gameServices);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }
}
