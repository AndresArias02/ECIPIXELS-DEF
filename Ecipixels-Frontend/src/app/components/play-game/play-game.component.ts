import { Component, OnInit, ViewChild, ElementRef, HostListener } from '@angular/core';
import { GameService } from '../../services/game.service';
import { Player } from '../../classes/player';
import { WebSocketServiceService } from '../../services/web-socket-service.service';
import { GameState } from '../../classes/game-state';
import { PlayerService } from '../../services/player.service';

@Component({
  selector: 'app-play-game',
  templateUrl: './play-game.component.html',
  styleUrls: ['./play-game.component.css']
})
export class PlayGameComponent implements OnInit {

  @ViewChild('canvas', { static: true }) canvas: ElementRef<HTMLCanvasElement>;
  private ctx: CanvasRenderingContext2D;

  gameState : GameState;
  player : Player;
  playerId: number; 
  currentDirection: string;
  moveTimeout: any;

  constructor(private playerService:PlayerService,private gameService: GameService,private webSocketService:WebSocketServiceService) { }

  ngOnInit(): void {
    const canvas = this.canvas.nativeElement;
    const context = canvas.getContext('2d');
  
    if (!context) {
      console.error('No se pudo obtener el contexto del canvas');
      return;
    }

    this.ctx = context;
  
    this.webSocketService.initconnectionSocket();
    this.webSocketService.movePlayerInServer();
    this.getGameStateLogging();
    this.getGameStateMovements();
    this.getPlayer();
  }
  
  

  getPlayer() {
    this.player = this.gameService.getCurrentPlayer();
    console.log('jugador en sesión :', this.player);
    if (this.player) {
      this.playerId = this.player.playerId;
    }
  }

  updatePlayer() {
    if (this.gameState && this.playerId) {
      const updatedPlayer = this.gameState.players.find(player => player.playerId === this.playerId);
      if (updatedPlayer) {
        this.player = updatedPlayer;
      }
    }
  }

  getGameStateLogging(){
    this.webSocketService.getGameStateObservableLogging().subscribe(
      (data: GameState) => {
        this.gameState = data
        this.resizeCanvas();
        this.drawBoard();
      },
      (error) => {
        console.error("Error al recibir el estado del juego:", error);
      },
    );
  }

  getGameStateMovements(){
    this.webSocketService.getGameStateObservableMovements().subscribe(
      (data: GameState) => {
        this.gameState = data
        this.resizeCanvas();
        this.updatePlayer();
        this.drawBoard();
      },
      (error) => {
        console.error("Error al recibir el estado del juego:", error);
      },
    );
  }

  // Método para manejar los eventos del teclado
  @HostListener('document:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (!this.playerId) return;

    let newDirection: string;

    switch (event.key) {
      case 'ArrowUp':
        newDirection = 'up';
        break;
      case 'ArrowDown':
        newDirection = 'down';
        break;
      case 'ArrowLeft':
        newDirection = 'left';
        break;
      case 'ArrowRight':
        newDirection = 'right';
        break;
      case 'p':
        console.log('paaaarrrooooo');
        this.stop();
        return;
      default:
        return;
    }

    // Verificar si la nueva dirección es diferente a la dirección actual
    if (this.currentDirection !== newDirection) {
      // Detener el movimiento actual antes de cambiar la dirección
      this.stop();
      
      // Actualizar la dirección actual
      this.currentDirection = newDirection;
      
      // Ejecutar el método correspondiente a la nueva dirección
      switch (newDirection) {
        case 'up':
          console.log('arrribbbaaaaa');
          this.moveUp();
          break;
        case 'down':
          console.log('abbbaaajooooo');
          this.moveDown();
          break;
        case 'left':
          console.log('izquierdaaaaaaaa');
          this.moveLeft();
          break;
        case 'right':
          console.log('dereccccchhhhhaaaa');
          this.moveRight();
          break;
      }
    }
  }

  moveUp() {
    this.movePlayer(0, -1);
  }

  moveDown(){
    this.movePlayer(0, 1);
  }

  moveLeft(){
    this.movePlayer(-1, 0);
  }

  moveRight(){
    this.movePlayer(1, 0);
  }

  movePlayer(deltaX: number, deltaY: number) {
    if (!this.player) return;

    const moveHead = () => {
      if (!this.player.isAlive || this.currentDirection === "stop") return;
      
      this.player.head.row += deltaY;
      this.player.head.col += deltaX;

      const row = this.player.head.row;
      const col = this.player.head.col;
      this.webSocketService.sendMessageToMovePlayer(this.player.playerId, row, col);

      this.moveTimeout = setTimeout(moveHead, 1000);
    };

    moveHead();
  }

  stop() {
    this.currentDirection = "stop";
    clearTimeout(this.moveTimeout);
  }

  resizeCanvas(): void {
    if (!this.gameState.board) return;

    const canvasWidth = this.gameState.board[0].length * 20;
    const canvasHeight = this.gameState.board.length * 20;

    this.canvas.nativeElement.width = canvasWidth;
    this.canvas.nativeElement.height = canvasHeight;
  }

  drawBoard(): void {
    if (!this.ctx || !this.gameState.board) return;

    this.ctx.clearRect(0, 0, this.canvas.nativeElement.width, this.canvas.nativeElement.height);
  
    for (let y = 0; y < this.gameState.board.length; y++) {
      for (let x = 0; x < this.gameState.board[y].length; x++) {
        const cellValue = this.gameState.board[y][x];
        const color = this.getColor(cellValue);
  
        if (cellValue != null) {
          this.ctx.strokeStyle = 'black';
          this.ctx.strokeRect(x * 20, y * 20, 20, 20);
        }
        
        this.ctx.fillStyle = color;
        this.ctx.fillRect(x * 20, y * 20, 20, 20);
  
        const playerWithHead = this.gameState.players.find(player => player.head.row === y && player.head.col === x);
        if (playerWithHead) {
            let headColor = this.getPlayerColor(playerWithHead.color);
            this.ctx.fillStyle = headColor;
            this.ctx.beginPath();
            this.ctx.arc((x * 20) + 10, (y * 20) + 10, 10, 0, 2 * Math.PI);
            this.ctx.fill();
        }
      }      
    }

    this.drawPlayersPixels();
  }

  drawPlayersPixels(): void {
    for (const player of this.gameState.players) {
      if (!player.pixelsRoute) continue;
  
      const playerColor = this.getPlayerColor(player.color);
  
      for (const pixel of player.pixelsRoute) {
        const [row, col] = pixel.split(',').map(Number);
  
        if ((row === player.head.row && col === player.head.col) || (row === 0 && col === 0)) continue;
  
        // Pintar el pixel
        this.ctx.fillStyle = playerColor;
        this.ctx.fillRect(col * 20, row * 20, 20, 20);
  
        // Dibujar los bordes en negro
        this.ctx.strokeStyle = 'black';
        this.ctx.strokeRect(col * 20, row * 20, 20, 20);
      }
    }
  }
  
  getPlayerColor(color: string): string {
    switch (color) {
      case "blue": return 'lightblue';
      case "red": return 'lightcoral';
      case "yellow": return 'darkgoldenrod';
      case "green": return 'lightGreen';
      case "purple": return 'darkorchid';
      default: return 'white';
    }
  }
  
    
  getColor(cellValue: number): string {
    if (cellValue === 0) {
      return 'gray';
    }

    if (cellValue === null) {
      return 'white';
    }

    const playerId = cellValue; 
    const foundPlayer = this.gameState.players.find(player => player.playerId === playerId);
    
    if (!foundPlayer) {
      return 'blue';
    }
    
    switch (foundPlayer.color) {
      case "blue": return 'blue';
      case "red": return 'red';
      case "yellow": return 'yellow';
      case "green": return 'green';
      case "purple": return 'purple';
      default: return 'gray';
    }
  }
}
