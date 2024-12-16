import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { API_CONFIG } from '../config/api.config';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = API_CONFIG.baseUrl;

  constructor(private http: HttpClient, private tokenService: TokenService) {}

  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, user);
  }

  login(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/authenticate`, user);
  }

  getList() {
    const token = this.tokenService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get(`${this.apiUrl}/all`, {headers});
  }
}
