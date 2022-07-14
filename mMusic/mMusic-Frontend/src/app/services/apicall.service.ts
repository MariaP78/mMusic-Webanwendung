import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { AuthenticationService } from './authentication.service';
@Injectable({
  providedIn: 'root'
})
export class ApicallService {
  url: string;

  constructor(private httpClient: HttpClient, private auth: AuthenticationService) {
    this.url = 'http://localhost:8080/mMusic-api/songs';
  }

  getAllSongs(){
    return this.httpClient.get(this.url + '/getAllFullSongs').pipe(
      map((data) => {
        return data;
      }), catchError( error => {
        return throwError( 'Something went wrong!' );
      })
    )
  }

  getAllSongsFromPlaylist(email:any){
    return this.httpClient.get(this.url + '/getSongsFromUserPlaylist?userEmail=' + email).pipe(
      map((data) => {
        return data;
      }), catchError( error => {
        return throwError( 'Something went wrong!' );
      })
    )
  }

  getAllSongsFromUploads(email: any){
    return this.httpClient.get(this.url + '/getAllSongsUploadedByUser?userEmail=' + email).pipe(
      map((data) => {
        return data;
      }), catchError( error => {
        return throwError( 'Something went wrong!' );
      })
    )
  }

  getRecommendations(email: any){
    return this.httpClient.get(this.url + '/getRecommendations?userEmail=' + email).pipe(
      map((data) => {
        return data;
      }), catchError( error => {
        return throwError( 'Something went wrong!' );
      })
    )
  }
}
