import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {CryptoJsService} from '../crypto-js-service/crypto-js.service';
import {JwtResponse} from '../utils/jwt-response.model';
import {JwtRequest} from '../utils/jwt-request.model';
import {map} from 'rxjs/operators';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private JWT_TOKEN = 'jwt';
  private USER_EMAIL = 'user-email';

  private AUTH_URL = environment.backendUrl + '/api/authenticate';
  private CAN_ACTIVATE_URL = environment.backendUrl + '/api/activate';

  constructor(private http: HttpClient,
              private cryptoJsService: CryptoJsService) {
  }

  public authenticate(email: string, password: string): Observable<void> {
    const jwtRequest = new JwtRequest(email, password);
    return this.http.post<JwtResponse>(this.AUTH_URL, jwtRequest)
      .pipe(map(jwtResponse => {
        this.setItemsFromResponse(jwtResponse);
      }));
  }

  private setItemsFromResponse(jwtResponse: JwtResponse): void {
    const jwtToken = jwtResponse.jwtToken;
    const userEmail = this.cryptoJsService.encrypt(jwtResponse.username);
    this.setItems(jwtToken, userEmail);
  }

  public setItems(jwtToken: string, userEmail: string): void {
    sessionStorage.setItem(this.JWT_TOKEN, jwtToken);
    sessionStorage.setItem(this.USER_EMAIL, userEmail);
  }

  public clearItems(): void {
    sessionStorage.clear();
  }

  public canActivate(): Observable<object> {
    const headers = this.getAuthHeaders();
    return this.http.get(this.CAN_ACTIVATE_URL, {headers});
  }

  public isAuthenticated(): boolean {
    const jwtToken = sessionStorage.getItem(this.JWT_TOKEN);
    const userEmail = sessionStorage.getItem(this.USER_EMAIL);
    return jwtToken != null && userEmail != null;
  }

  public getUserEmail(): string {
    const userEmail = sessionStorage.getItem(this.USER_EMAIL);
    return userEmail != null ? this.cryptoJsService.decrypt(userEmail) : '';
  }

  public getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: this.getAuthToken(),
    });
  }

  private getAuthToken(): string {
    const jwtToken = sessionStorage.getItem(this.JWT_TOKEN);
    return 'Bearer ' + jwtToken;
  }

}
