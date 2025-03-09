import {Injectable} from '@angular/core';
import {HttpHeaders} from '@angular/common/http';
import {TokenService} from './token.service';

@Injectable({
  providedIn: 'root',
})
export class HeaderService {
  constructor(private tokenService: TokenService) {
  }

  getHeaders(): HttpHeaders {
    const token = this.tokenService.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }
}
