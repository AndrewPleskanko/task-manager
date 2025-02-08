import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient,
              private router: Router) {
  }

  loginWithOAuth2(provider: string) {
    window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`;
  }

  handleAuthentication(token: string) {
    localStorage.setItem('token', token);
    this.router.navigate(['/home']);
  }

  logout(): void {
    localStorage.removeItem('token');
    sessionStorage.clear();

    this.router.navigate(['/login']);
  }
}
