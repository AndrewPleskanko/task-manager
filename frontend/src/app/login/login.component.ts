import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {UserService} from "../global-services/user.service";
import {AuthService} from "../global-services/auth.service";
import {User} from "../entities/User";
import {ReactiveFormsModule} from '@angular/forms';
import {NgIf} from "@angular/common";

interface OAuthResponse {
  accessToken: string;
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loginError = '';
  successMessage = '';

  constructor(private userService: UserService,
              private router: Router,
              private authService: AuthService,
              private route: ActivatedRoute) {
    this.loginForm = new FormGroup({
      password: new FormControl('', Validators.required),
      username: new FormControl('', Validators.required),
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      if (token) {
        localStorage.setItem('token', token);
        this.router.navigate(['/home']).then(() => {
          this.successMessage = 'You have successfully logged in!';
        }).catch(() => {
          this.loginError = 'Navigation failed after login';
        });
      }
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const user: User = this.loginForm.value;

      this.userService.login(user).subscribe({
        next: (data: OAuthResponse) => {
          localStorage.setItem('token', data.accessToken);
          this.router.navigate(['/home']).then(() => {
            this.successMessage = 'You have successfully logged in!';
          }).catch(() => {
            this.loginError = 'Navigation failed after login';
          });
        },
        error: (err) => {
          console.error(err);
          this.loginError = 'Invalid username or password';
        }
      });
    }
  }

  loginWithGoogle() {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  }

  loginWithGithub() {
    window.location.href = 'http://localhost:8080/oauth2/authorization/github';
  }
}
