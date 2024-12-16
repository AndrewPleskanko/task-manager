import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient, private router: Router) {}

  // Метод для ініціації аутентифікації через OAuth2
  loginWithOAuth2(provider: string) {
    window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`;
  }

  // Метод для збереження токена і перенаправлення користувача
  handleAuthentication(token: string) {
    localStorage.setItem('token', token);
    this.router.navigate(['/home']);
  }
}
