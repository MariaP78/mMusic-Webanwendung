import { AuthenticationService } from 'src/app/services/authentication.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ApicallService } from 'src/app/services/apicall.service';
@Component({
  selector: 'app-account-profile',
  templateUrl: './account-profile.component.html',
  styleUrls: ['./account-profile.component.scss'],
})
export class AccountProfileComponent implements OnInit {
  public currentUser: any;
  birthday: any;
  firstname: any;
  lastname: any;
  phone: any;
  url: string;

  constructor(
    private router: Router,
    private snackBar: MatSnackBar,
    private http: HttpClient,
    public auth: AuthenticationService,
    public apiService: ApicallService
  ) {
    this.url = 'http://localhost:8080/mMusic-api/users';

  }

  updateUserDetailsForm: any;

  onSubmit() {}

  ngOnInit(): void {
    console.log(this.auth.currentUser)
    this.updateUserDetailsForm = new FormGroup({
      birthday: new FormControl(new Date(this.auth.currentUser?.birthday), [Validators.required]),
      firstname: new FormControl(this.auth.currentUser?.firstname, [Validators.required]),
      lastname: new FormControl(this.auth.currentUser?.lastname, [Validators.required]),
      phone: new FormControl(this.auth.currentUser?.phone, [Validators.required]),
      stageName: new FormControl(this.auth.currentUser?.stageName, [Validators.required]),
    });
  }

  updateUserDetailsOnClick() {
    let updatedUserFormData = new FormData();
    updatedUserFormData.append('email', this.auth.currentUser.email);
    updatedUserFormData.append(
      'birthday',
      new Date(this.updateUserDetailsForm.value.birthday).toLocaleDateString('en-US')

    );
    updatedUserFormData.append(
      'firstname',
      this.updateUserDetailsForm.value.firstname
    );
    updatedUserFormData.append(
      'lastname',
      this.updateUserDetailsForm.value.lastname
    );
    updatedUserFormData.append('phone', this.updateUserDetailsForm.value.phone);
    updatedUserFormData.append('stageName', this.updateUserDetailsForm.value.stageName);
    this.http.post(this.url + '/updateUser', updatedUserFormData).subscribe({
      next: (data) => {
        this.snackBar.open(
          'Your account details were successfully updated!',
          'Close',
          {
            duration: 5000,
          }
        );
        this.auth.currentUser = {...this.auth.currentUser, ...data};
      },
      error: (error) => {
        console.error(error);
        this.snackBar.open(
          'An error occurred...Your account details could not be updated!',
          'Close',
          {
            duration: 5000,
          }
        );
      },
    });
  }
}
