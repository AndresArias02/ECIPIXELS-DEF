// play-game.component.ts
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { GameService } from '../../services/game.service';
import { Board } from '../../classes/board';
import { Player } from '../../classes/player';
import { WebSocketServiceService } from '../../services/web-socket-service.service';
import { GameState } from '../../classes/game-state';

@Component({
  selector: 'app-play-game',
  templateUrl: './play-game.component.html',
  styleUrls: ['./play-game.component.css']
})
export class PlayGameComponent implements OnInit {

  @ViewChild('canvas', { static: true }) canvas: ElementRef<HTMLCanvasElement>;

  gameState : GameState;

  constructor(private gameService: GameService,private webSocketService:WebSocketServiceService) { }

  ngOnInit(): void {
    this.webSocketService.getGameStateObservable().subscribe(
      (data: GameState) => {
        console.log("Recibido nuevo estado del juego:", data);
        this.gameState = data
        this.resizeCanvas();
        this.drawBoard();
      },
      (error) => {
        console.error("Error al recibir el estado del juego:", error);
      },
    );
  }

  /*getBoard() {
    console.log("Obteniendo el tablero...");
    this.gameService.getBoard().subscribe(
      (boardData: number[][]) => {
        console.log("Tablero recibido", boardData);
        this.board = { grid: boardData };
        this.resizeCanvas();
        this.drawBoard();
      },
      (error) => {
        console.error("Error al obtener el tablero:", error);
      }
    );
  }*/

  /*getPlayers() {
    console.log("Obteniendo jugadores...");
    this.gameService.getPlayers().subscribe(
      (players: Player[]) => {
        console.log("Jugadores recibidos:", players);
        this.players = players;
      },
      (error) => {
        console.error("Error al obtener los jugadores:", error);
      }
    );
  }*/
  

  /*getLeaderBoard() {
    console.log("Obteniendo leaderboard...");
    this.gameService.getLeaderBoard().subscribe(
      (leaderBoard: Player[]) => {
        console.log("Leaderboard recibido:", leaderBoard);
        this.leaderBoard = leaderBoard;
      },
      (error) => {
        console.error("Error al obtener el leaderboard:", error);
      }
    );
  }*/

  resizeCanvas(): void {
    if (!this.gameState.board) return;

    // Calcula el tamaño del canvas en función del tamaño del tablero y el tamaño de las celdas
    const canvasWidth = this.gameState.board[0].length * 20;
    const canvasHeight = this.gameState.board.length * 20;

    // Asigna el tamaño calculado al canvas
    this.canvas.nativeElement.width = canvasWidth;
    this.canvas.nativeElement.height = canvasHeight;
  }

  drawBoard(): void {
    const ctx = this.canvas.nativeElement.getContext('2d');
  
    // Verifica si el contexto es nulo
    if (!ctx) {
      console.error('No se pudo obtener el contexto del canvas');
      return;
    }
  
    // Limpia el canvas
    ctx.clearRect(0, 0, this.canvas.nativeElement.width, this.canvas.nativeElement.height);
  
    if (!this.gameState.board) return;
  
    // Itera sobre cada celda del tablero
    for (let y = 0; y < this.gameState.board.length; y++) {
      for (let x = 0; x < this.gameState.board[y].length; x++) {
        const cellValue = this.gameState.board[y][x];
        const color = this.getColor(cellValue);
  
        // Dibuja un borde negro en la posición de la celda
        if(cellValue != null){
          ctx.strokeStyle = 'black';
          ctx.strokeRect(x * 20, y * 20, 20, 20);
        }
        
  
        // Rellena la celda con el color correspondiente
        ctx.fillStyle = color;
        ctx.fillRect(x * 20, y * 20, 20, 20);
  
        // Dibuja la cabeza del jugador si la celda corresponde a la posición de su cabeza
        const playerWithHead = this.gameState.players.find(player => player.head.row === y && player.head.col === x);
        if (playerWithHead) {
            // Calcula el color más claro para la cabeza según el color del jugador
            let headColor = '';
            switch (playerWithHead.color) {
                case "blue": headColor = 'lightblue'; break;
                case "red": headColor = 'lightcoral'; break;
                case "yellow": headColor = 'darkgoldenrod'; break;
                case "green": headColor = 'lightGreen'; break;
                case "purple": headColor = 'darkorchid'; break;
                default: headColor = 'white'; break; // Color predeterminado en caso de no coincidir
            }
            // Dibuja el círculo con el color más claro
            ctx.fillStyle = headColor;
            ctx.beginPath();
            ctx.arc((x * 20) + 10, (y * 20) + 10, 10, 0, 2 * Math.PI);
            ctx.fill();
        }
      }
    }
  }
    
  // Método para obtener el color basado en el valor de la celda
  getColor(cellValue: number): string {
    // Si el valor de la celda es 0, devolvemos blanco
    if (cellValue === 0) {
      return 'gray';
    }

    if (cellValue === null) {
      return 'white';
    }

    // Buscamos el jugador correspondiente al id de la casilla
    const playerId = cellValue; 
    const foundPlayer = this.gameState.players.find(player => player.playerId === playerId);
    
    // Si no se encuentra el jugador, devolvemos un color predeterminado
    if (!foundPlayer) {
      return 'blue'; // Puedes ajustar este color según tu preferencia
    }
    
    // Devolvemos el color del jugador
    switch (foundPlayer.color) {
      case "blue": return 'blue';
      case "red": return 'red';
      case "yellow": return 'yellow';
      case "green": return 'green';
      case "purple": return 'purple';
      default: return 'gray'; // Por si acaso el jugador tiene un color no reconocido
    }
  }

}