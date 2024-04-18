import { Component, OnInit, ViewChild } from '@angular/core';
import { GameService } from '../../services/game.service';
import { Router } from '@angular/router';
import { WebSocketServiceService } from '../../services/web-socket-service.service';

@Component({
  selector: 'app-start-game',
  templateUrl: './start-game.component.html',
  styleUrl: './start-game.component.css'
})
export class StartGameComponent implements OnInit{

  name:string;
  
  constructor(private gameService:GameService, private router:Router,private webSocketService: WebSocketServiceService){

  }

  ngOnInit(): void {
    this.webSocketService.initconnectionSocket();
    this.webSocketService.getGameStateFromServer();
  }

  addNewPlayer() {
    this.gameService.addPlayer(this.name).subscribe(
      (response) => {
        console.log("Jugador agregado correctamente", response);
        this.webSocketService.sendMessage("Nuevo jugador:"+this.name);
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
