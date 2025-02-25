import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { API_CONFIG } from '../config/api.config';
import { TokenService } from './token.service';
import {User} from "../entities/User";

interface OAuthResponse {
  accessToken: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = API_CONFIG.baseUrl;

  constructor(private http: HttpClient, private tokenService: TokenService) {}

  register(user: User): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/add`, user);
  }

  login(user: User): Observable<OAuthResponse> {
    return this.http.post<OAuthResponse>(`${this.apiUrl}/login`, user);
  }

  getList() {
    const token = this.tokenService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get(`${this.apiUrl}/all`, {headers});
  }
}
