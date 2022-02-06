import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Message} from './message';
import {AuthService} from '../../auth/auth-service/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private CHAT_URL = environment.backendUrl + '/api/chat';
  private HEADERS = this.authService.getAuthHeaders();

  constructor(private httpClient: HttpClient,
              private authService: AuthService) { }

  getHistory(projectId: string): Observable<Message[]> {
    return this.httpClient.get<Message[]>(this.CHAT_URL + '/history/' + projectId, {headers: this.HEADERS});
  }
}
