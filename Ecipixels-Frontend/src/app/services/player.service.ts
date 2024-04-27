import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Player } from '../classes/player';
import { Observable } from 'rxjs';
import { Board } from '../classes/board';

@Injectable({
  providedIn: 'root'
})
export class PlayerService {

  private URL = "http://localhost:8080/api/eciPixelsPlayer";

  constructor(private httpClient:HttpClient) { }

  moveUp(idPlayer: number): Observable<HttpResponse<any>> {
    return this.httpClient.post<any>(`${this.URL}/moveUp`,idPlayer, { observe: 'response' });
  }
  
  moveDown(idPlayer: number): Observable<HttpResponse<any>> {
    return this.httpClient.post<any>(`${this.URL}/moveDown`,idPlayer, { observe: 'response' });
  }
 
  moveLeft(idPlayer: number): Observable<HttpResponse<any>> {
    return this.httpClient.post<any>(`${this.URL}/moveLeft`,idPlayer, { observe: 'response' });
  }

  moveRight(idPlayer: number): Observable<HttpResponse<any>> {
    return this.httpClient.post<any>(`${this.URL}/moveRight`,idPlayer, { observe: 'response' });
  }
  
  stop(idPlayer: number): Observable<HttpResponse<any>> {
    return this.httpClient.post<any>(`${this.URL}/stop`, idPlayer, { observe: 'response' } );
  }
  
}
