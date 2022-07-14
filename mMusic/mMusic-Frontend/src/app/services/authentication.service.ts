import { Injectable } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import {
  getAuth,
  onAuthStateChanged,
  signInWithEmailAndPassword,
  signOut,
  createUserWithEmailAndPassword,
} from 'firebase/auth';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  public currentUser: any;

  constructor(
    public afAuth: AngularFireAuth,
    private snackBar: MatSnackBar,
    private router: Router
  ) {
    const auth = getAuth();
    // Get the currently signed-in user
    onAuthStateChanged(auth, (user) => {
      if (user) {
        // create user object from firestore
        fetch(
          'http://localhost:8080/mMusic-api/users/getUser?userDocumentId=' +
            user.email?.toLowerCase()
        )
          .then((u: any) => u.json())
          .then((u: any) => {
            this.currentUser = u;
            this.currentUser.email = user.email?.toLowerCase();
            console.log('Signed in user: ' + user.email?.toLowerCase());
          });
      } else {
        // User is signed out
        this.currentUser = null;
        console.log('No user signed in!');
      }
    });
  }

  // Login Function Firebase
  login = (email: string, password: string) => {
    const auth = getAuth();
    signInWithEmailAndPassword(auth, email, password)
      .then((userCredential) => {
        // Signed in
        // create user object from firestore
        fetch(
          'http://localhost:8080/mMusic-api/users/getUser?userDocumentId=' +
            email
        )
          .then((u: any) => u.json())
          .then((u: any) => {
            this.currentUser = u;
            this.currentUser.email = email;
          })
          .catch((error) => {
            console.log(error);
            this.currentUser = null;
          });

        this.snackBar.open('Logged in successfully!', 'Close', {
          duration: 5000,
        });
        this.router.navigate(['/']);
      })
      .catch((error) => {
        this.currentUser = null;
        const errorCode = error.code;
        const errorMessage = error.message;
        console.log(errorMessage);
        this.snackBar.open('Login failed!', 'Close', {
          duration: 5000,
        });
      });
  };

  // Logout Function Firebase
  logout() {
    const auth = getAuth();
    signOut(auth)
      .then(() => {
        // Sign-out successful.
        this.currentUser = null;
        console.log('User successfully signed out');
        this.snackBar.open('Logged out successfully!', 'Close', {
          duration: 5000,
        });
        this.router.navigate(['']);
      })
      .catch((error) => {
        // An error happened.
        const errorCode = error.code;
        const errorMessage = error.message;
        console.log(errorMessage);
        this.snackBar.open('Logout failed!', 'Close', {
          duration: 5000,
        });
      });
  }

  // Register Function Firebase
  register = (email: string, password: string) => {
    const auth = getAuth();
    createUserWithEmailAndPassword(auth, email, password)
      .then((userCredential) => {
        // Signed in
        const user = userCredential.user;
        this.currentUser = user;
        console.log('The following user is registered: ' + user.email);
        this.snackBar.open('Registered successfully!', 'Close', {
          duration: 5000,
        });
        setTimeout(() => {
        this.router.navigate(['/profile']);
        }, 500);
      })
      .catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;
        console.log(errorMessage);
        this.snackBar.open('You could not be registered!', 'Close', {
          duration: 5000,
        });
      });
  };
}
