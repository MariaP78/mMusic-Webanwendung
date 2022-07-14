import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ApicallService } from 'src/app/services/apicall.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { DialogService } from 'src/app/services/dialog.service';

@Component({
  selector: 'app-artist-uploads',
  templateUrl: './artist-uploads.component.html',
  styleUrls: ['./artist-uploads.component.scss']
})
export class ArtistUploadsComponent implements OnInit {
  playSongUrl: string;
  songs: any;
  audio: any;
  songUrl: any;
  songStatus: any;
  url: string;
  playing:any=null;

  constructor(private router: Router,
    private apiSongsService: ApicallService,
    private http: HttpClient,
    private snackBar: MatSnackBar,
    private dialogService: DialogService,
    public auth: AuthenticationService) {
      this.playSongUrl =
      'http://localhost:8080/mMusic-api/songs/playSong?ipfsHash=';
    this.url = 'http://localhost:8080/mMusic-api/songs';
    }

  ngOnInit(): void {
    //show all songs on init
    setTimeout(() => {
      console.log(this.auth.currentUser);
      this.apiSongsService
        .getAllSongsFromUploads(this.auth.currentUser?.email)
        .subscribe((data: any) => {
          this.songs = data;
        });
    }, 500);
  }

  onSubmit() {}

  updateSong(hashCode: any,title:any,stageName:any,genre:any){
    console.log(hashCode);
    this.dialogService
      .openUpdateSongDialog(title,stageName,genre)
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          let updatedFormData = new FormData();
          updatedFormData.append('ipfsHash', hashCode);
          updatedFormData.append('title', this.dialogService.formData.title);
          updatedFormData.append('stageName', this.dialogService.formData.stageName);
          updatedFormData.append('genre', this.dialogService.formData.genre);
          this.http
            .post(this.url + '/updateSongDetails', updatedFormData)
            .subscribe({
              next: (data) => {
                this.snackBar.open(
                  'Song details were successfully updated!',
                  'Close',
                  {
                    duration: 5000,
                  }
                );
                // reload songs
                setTimeout(() => {
                  console.log(this.auth.currentUser);
                  this.apiSongsService
                    .getAllSongsFromUploads(this.auth.currentUser.email)
                    .subscribe((data: any) => {
                      this.songs = data;
                    });
                }, 500);
              },
              error: (error) => {
                console.error(error);
                this.snackBar.open(
                  'An error occurred...Song details could not be updated!',
                  'Close',
                  {
                    duration: 5000,
                  }
                );
              },
            });
        } else {
          this.snackBar.open('No updates were registered!', 'Close', {
            duration: 5000,
          });
        }
      });
  }

  verifySong(hashCode: any){
    console.log(hashCode);
    //play song using API endpoint
    const url = `${this.playSongUrl}${hashCode}`;
    this.http.get(url).subscribe({
      next: (data: any) => {
        this.songStatus = Object.values(data)[0];
        this.songUrl = Object.values(data)[1];

        // verify if song is on blockchain(original) or not
        // HAPPY PATH --> 0 = original song, song was not hacked
        if (this.songStatus == '0') {
          this.dialogService.openVerifiedSongDialog().afterClosed().subscribe((result) => {
            if (result) {
              console.log("verified dialog appeared");
            }
          });
        } else {
          // SAD PATH --> 1 = hacked song, song was hacked
          console.log('song is hacked');
          this.dialogService.openHackedSongDialog().afterClosed().subscribe((result) => {
            if (result) {
              console.log("hacked dialog appeared");
            }
          });
        }
      },
      error: (error) => {
        console.log(error.message);
        this.snackBar.open('An error occurred...', 'Close', {
          duration: 5000,
        });
      },
    });
  }

}
