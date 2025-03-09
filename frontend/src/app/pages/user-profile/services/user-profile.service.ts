import {Injectable} from '@angular/core';
import {environment} from '../../../environment/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from "../../../entities/User";
import {HeaderService} from "../../../global-services/header.service";

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {
  constructor(private http: HttpClient,
              private headerService: HeaderService) {
  }

  getUserInfo(): Observable<User> {
    const headers = this.headerService.getHeaders();

    return this.http.get<User>(`${environment.apiUrl}/auth/user/me`, {headers});
  }
}
