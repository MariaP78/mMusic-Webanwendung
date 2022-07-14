import { HttpClient } from '@angular/common/http';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { getAuth, onAuthStateChanged } from 'firebase/auth';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-sidenav-list',
  templateUrl: './sidenav-list.component.html',
  styleUrls: ['./sidenav-list.component.scss'],
})
export class SidenavListComponent implements OnInit {
  authenticated: boolean = false;
  // getUserUrl: string;
  user: any;
  artistFlag: any;
  public currentUser: any;
  public userEmail: any;

  @Output() sidenavList = new EventEmitter();
  constructor(public auth: AuthenticationService, private http: HttpClient, private snackBar: MatSnackBar) {
    // const authenticate = getAuth();
    // // Get the currently signed-in user
    // onAuthStateChanged(authenticate, (user) => {
    //   if (user) {
    //     // User is signed in, see docs for a list of available properties
    //     // https://firebase.google.com/docs/reference/js/firebase.User
    //     const uid = user.uid;
    //     this.currentUser = user;
    //     this.userEmail = user.email;
    //     localStorage.setItem('email', this.userEmail);
    //     this.authenticated = true;
    //   } else {
    //     // User is signed out
    //     localStorage.setItem('email', '');
    //     this.authenticated = false;
    //   }
    // });
    // this.getUserUrl = 'http://localhost:8080/mMusic-api/users/getUser?userDocumentId=';

    // // get artist flag from db
    // var email = localStorage.getItem('email');
    // const url = `${this.getUserUrl}${email}`;

    // // get user data from firestore
    // this.http.get(url).subscribe({
    //   next: (data: any) => {
    //     this.user = data;
    //     this.artistFlag = data.artist;
    //     console.log("ARTIST " + data.artist)
    //   },
    //   error: (error) => {
    //     console.error(error);
    //         this.snackBar.open('No user signed in!', 'Close', {
    //           duration: 5000,
    //         });
    //   },
    // });
  }

  ngOnInit(): void {

  }

  public onSidenavClose = () => {
    this.sidenavList.emit();
  };

  public onSidenavCloseSignout() {
    this.sidenavList.emit();
    this.auth.logout();
  }
}
