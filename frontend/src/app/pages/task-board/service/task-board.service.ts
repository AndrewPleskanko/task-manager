import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import { User } from '../../../entities/User';
import {HttpClient} from "@angular/common/http";
import {HeaderService} from "../../../global-services/header.service";
import {environment} from "../../../environment/environment";

@Injectable({
  providedIn: 'root'
})
export class TaskBoardService {

  constructor(private http: HttpClient,
              private headerService: HeaderService) {
  }

  getUserInfo(): Observable<User> {
    const headers = this.headerService.getHeaders();

    return this.http.get<User>(`${environment.apiUrl}/tasks`, {headers});
  }
}
