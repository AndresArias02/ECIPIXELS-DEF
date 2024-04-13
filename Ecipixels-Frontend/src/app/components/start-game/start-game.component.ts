import { Component, ViewChild } from '@angular/core';
import { GameService } from '../../services/game.service';
import { Router } from '@angular/router';
import { PlayGameComponent } from '../play-game/play-game.component';

@Component({
  selector: 'app-start-game',
  templateUrl: './start-game.component.html',
  styleUrl: './start-game.component.css'
})
export class StartGameComponent {
  name:string;
  
  constructor(private gameService:GameService, private router:Router){

  }

  addNewPlayer() {
    this.gameService.addPlayer(this.name).subscribe(
      (response) => {
        console.log("Jugador agregado correctamente", response);
        this.startGame();
      },
      (error) => {
        console.error("Error al agregar jugador:", error);
      }
    );
  }

  startGame(){
    this.router.navigate(['/Game']);
  }

}
