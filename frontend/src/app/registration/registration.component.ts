import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../services/user.service";
import {AbstractControl, FormControl, FormGroup, Validators, ReactiveFormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent implements OnInit {
  errorMessage = '';
  registrationForm!: FormGroup;

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit() {
    this.registrationForm = new FormGroup({
      username: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]),
      password: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(40), this.passwordComplexityValidator]),
      email: new FormControl('', [Validators.required, Validators.email, Validators.maxLength(40)])
    });
  }

  register() {
    const {username, password, email} = this.registrationForm.value;
    this.userService.register({username, password, email}).subscribe(
      () => {
        this.router.navigate(['/home']);
      },
      () => {
        this.errorMessage = 'Registration failed';
      }
    );
  }

  passwordComplexityValidator(control: AbstractControl): Record<string, boolean> | null {
    const value = control.value;
    const hasUpperCase = /[A-Z]/.test(value);
    const hasLowerCase = /[a-z]/.test(value);
    const hasNumeric = /[0-9]/.test(value);

    if (!hasUpperCase) {
      return {missingUpperCase: true};
    }
    if (!hasLowerCase) {
      return {missingLowerCase: true};
    }
    if (!hasNumeric) {
      return {missingNumeric: true};
    }
    return null;
  }
}
