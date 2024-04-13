package edu.eci.arsw.controller;

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

    @RequestMapping(value = "/moveUp",method = RequestMethod.POST)
    public ResponseEntity<?> moveUp(@RequestBody Integer idPlayer) {
        if (idPlayer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            playerServices.moveUp(idPlayer);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/moveDown",method = RequestMethod.POST)
    public ResponseEntity<?> moveDown(@RequestBody Integer idPlayer) {
        if (idPlayer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            playerServices.moveDown(idPlayer);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/moveRight",method = RequestMethod.POST)
    public ResponseEntity<?> moveRight(@RequestBody Integer idPlayer) {
        if (idPlayer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            playerServices.moveRight(idPlayer);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/moveLeft",method = RequestMethod.POST)
    public ResponseEntity<?> moveLeft(@RequestBody Integer idPlayer) {
        if (idPlayer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            playerServices.moveLeft(idPlayer);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/stop",method = RequestMethod.POST)
    public ResponseEntity<?> stop(@RequestBody Integer idPlayer) {
        if (idPlayer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            playerServices.stop(idPlayer);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception ex){
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE,null,ex);
            return new ResponseEntity<>("Error" + ex.getMessage(),HttpStatus.FORBIDDEN);
        }
    }
}
