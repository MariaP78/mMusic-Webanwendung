import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss'],
})
export class LoginFormComponent implements OnInit {
  constructor(private router: Router, private auth: AuthenticationService) {}

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
    ]),
  });

  onSubmit() {
    //console.log(this.loginForm.value);
  }

  getErrorMessage() {
    if (this.loginForm.get('email')?.value.hasError('required')) {
      return 'You must enter a value';
    }

    return this.loginForm.get('email')?.value.hasError('email')
      ? 'Not a valid email'
      : '';
  }

  loginButtonOnClick() {
    console.log('User logged in successfully!');
    // sign in user
    this.auth.login(this.loginForm.value.email, this.loginForm.value.password);
  }

  ngOnInit(): void {}
}
