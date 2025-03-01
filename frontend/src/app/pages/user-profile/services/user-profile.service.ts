import {Injectable} from '@angular/core';
import {environment} from '../../../environment/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TokenService} from "../../../global-services/token.service";
import {User} from "../../../entities/User";

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {
  constructor(private http: HttpClient,
              private tokenService: TokenService) {
  }

  getUserInfo(): Observable<User> {
    const token = this.tokenService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get<User>(`${environment.apiUrl}/auth/v1/user/me`, {headers});
  }
}
