import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Message} from './message';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private CHAT_URL = environment.backendUrl + '/chat';

  constructor(private httpClient: HttpClient) { }

  getHistory(projectId: number): Observable<Message[]> {
    return this.httpClient.get<Message[]>(this.CHAT_URL + '/history/' + projectId);
  }
}
