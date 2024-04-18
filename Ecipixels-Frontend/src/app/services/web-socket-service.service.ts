import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';
import { GameState } from '../classes/game-state';

@Injectable({
  providedIn: 'root'
})
export class WebSocketServiceService {

  private stompClient: any;
  private gameStateSubject: Subject<GameState> = new Subject<GameState>();

  constructor() { }

  initconnectionSocket(){
    const URL = "//localhost:8080/game-socket";
    const socket = new SockJS(URL);
    this.stompClient = Stomp.over(socket); 
  }

  getGameStateFromServer(): void {
    this.stompClient.connect({}, () => {
        console.log('Connected to WebSocket server');
        this.stompClient.subscribe('/topic/GameState', (data: any) => {
            const gameState = JSON.parse(data.body) as GameState;
            console.log("Recived data: " , gameState);
            this.gameStateSubject.next(gameState);
        });
    }, (error: any) => {
        console.error('Error connecting to WebSocket server:', error);
    });
  }
  
  getGameStateObservable(): Observable<GameState> {
    return this.gameStateSubject.asObservable();
  }

  sendMessage(message:string){
    this.stompClient.send(`/app/gameState`,{}, message);
  }

}
