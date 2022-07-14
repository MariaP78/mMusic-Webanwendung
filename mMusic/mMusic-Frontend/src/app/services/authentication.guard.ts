import { MatSnackBar } from '@angular/material/snack-bar';
import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanLoad,
  Route,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { getAuth, onAuthStateChanged } from 'firebase/auth';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationGuard implements CanActivate, CanLoad {
  constructor(private router: Router, private snackBar: MatSnackBar) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<boolean> {
    return new Promise((resolve) => {
      const auth = getAuth();
      onAuthStateChanged(auth, (user) => {
        if (user) {
          // User is authenticated
          resolve(true);
        } else {
          // User is not authenticated
          this.snackBar.open(
            "You need to be authenticated to access this page! Please login or register if you don't have an account yet.",
            'Close',
            {
              duration: 5000,
            }
          );
          this.router.navigate(['/login']);
          resolve(false);
        }
      });
    });
  }

  canLoad(route: Route): Observable<boolean> | Promise<boolean> | boolean {
    return true;
  }
}
