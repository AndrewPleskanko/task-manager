import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import {User} from "../entities/User";
import {HeaderService} from "./header.service";
import {environment} from "../environment/environment";

interface OAuthResponse {
  accessToken: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private headerService: HeaderService) {}

  register(user: User): Observable<User> {
    return this.http.post<User>(`${environment.apiUrl}/add`, user);
  }

  login(user: User): Observable<OAuthResponse> {
    return this.http.post<OAuthResponse>(`${environment.apiUrl}/auth/login`, user);
  }

  getList() {
    const headers = this.headerService.getHeaders();
    return this.http.get(`${environment.apiUrl}/all`, {headers});
  }
}
