import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { HttpClient } from '@angular/common/http';
import { MatCheckboxChange, MatCheckboxModule } from '@angular/material/checkbox';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.scss'],
})
export class RegisterFormComponent implements OnInit {
  url: string;
  isArtistChecked: boolean = false;

  constructor(
    private auth: AuthenticationService,
    private http: HttpClient //private userService: UserService
  ) {
    this.url = 'http://localhost:8080/mMusic-api/users';
  }

  registerForm = new FormGroup({
    firstname: new FormControl('', [Validators.required]),
    lastname: new FormControl('', [Validators.required]),
    stageName: new FormControl('', [Validators.required]),
    phone: new FormControl('', [Validators.required]),
    birthday: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
    ]),
    confirmPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
    ]),
  });

  onSubmit() {
    console.log(this.registerForm.value);
  }

  onChange(ob: MatCheckboxChange) {
    this.isArtistChecked = ob.checked;
    console.log("ARTIST " + this.isArtistChecked);
  }

  getErrorMessage() {
    if (this.registerForm.get('email')?.value.hasError('required')) {
      return 'You must enter a value';
    }

    return this.registerForm.get('email')?.value.hasError('email')
      ? 'Not a valid email'
      : '';
  }

  registerButtonOnClick() {
    console.log('User registered successfully!');
    console.log("CHECK " + this.isArtistChecked);
    //sign up user
    this.auth.register(
      this.registerForm.value.email,
      this.registerForm.value.password
    );

    var userId = this.registerForm.value.email.toLowerCase();

    var userToBeAdded = {
      artist: this.isArtistChecked,
      firstname: this.registerForm.value.firstname,
      lastname: this.registerForm.value.lastname,
      stageName: this.registerForm.value.stageName,
      phone: this.registerForm.value.phone,
      birthday: this.registerForm.value.birthday.toLocaleDateString('en-US'),
      userDocumentId: userId,
    };

    console.log(userToBeAdded);
    // add user info to Firestore Database using API endpoint
    this.http.post(this.url + '/addUser', userToBeAdded).subscribe();
  }

  ngOnInit(): void {}
}
