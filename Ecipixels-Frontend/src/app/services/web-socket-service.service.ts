import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';
import { GameState } from '../classes/game-state';
import { Head } from '../classes/head';

@Injectable({
  providedIn: 'root'
})
export class WebSocketServiceService {

  private stompClient: any;
  private gameStateSubject: Subject<GameState> = new Subject<GameState>();
  private gameStateMovements: Subject<GameState> = new Subject<GameState>();
  constructor() { }

  initconnectionSocket(){
    const URL = "//localhost:8080/game-socket";
    const socket = new SockJS(URL);
    this.stompClient = Stomp.over(socket); 
  }

  getGameStateFromServer(): void {
    this.stompClient.connect({}, () => {
        console.log('Connected to WebSocket server to loggin');
        this.stompClient.subscribe('/topic/GameState', (data: any) => {
            const gameState = JSON.parse(data.body) as GameState;
            console.log("Recived data: " , gameState);
            this.gameStateSubject.next(gameState);
        });
    }, (error: any) => {
        console.error('Error connecting to WebSocket server:', error);
    });
  }

  movePlayerInServer(): void {
    this.stompClient.connect({}, () => {
        console.log('Connected to WebSocket server to move');
        this.stompClient.subscribe('/topic/movePlayer', (data: any) => {
            const gameState = JSON.parse(data.body) as GameState;
            console.log("Recived data: " , gameState);
            this.gameStateMovements.next(gameState);
        });
    }, (error: any) => {
        console.error('Error connecting to WebSocket server:', error);
    });
  }

  getGameStateObservableLogging(): Observable<GameState> {
    return this.gameStateSubject.asObservable();
  }

  getGameStateObservableMovements(): Observable<GameState> {
    return this.gameStateMovements.asObservable();
  }

  sendMessageLogin(message:string){
    this.stompClient.send(`/app/gameState`,{}, message);
  }

  sendMessageToMovePlayer(playerId:number, row:number, col:number){
    console.log("-------------- Data enviada para el movimiento -------------------")
    console.log(playerId,row,col);
    this.stompClient.send(`/app/movePlayer/${playerId}/${row}/${col}`, {});
  }
}
