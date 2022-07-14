import { UpdateSongDialogComponent } from './../../songs/update-song-dialog/update-song-dialog.component';
import { DialogService } from './../../services/dialog.service';
import { ApicallService } from './../../services/apicall.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-playlist',
  templateUrl: './playlist.component.html',
  styleUrls: ['./playlist.component.scss'],
})
export class PlaylistComponent implements OnInit {
  playSongUrl: string;
  songs: any;
  audio: any;
  songUrl: any;
  songStatus: any;
  url: string;
  playing:any=null;

  constructor(
    private router: Router,
    private apiSongsService: ApicallService,
    private http: HttpClient,
    private snackBar: MatSnackBar,
    private dialogService: DialogService,
    public auth: AuthenticationService
  ) {
    this.playSongUrl =
      'http://localhost:8080/mMusic-api/songs/playSong?ipfsHash=';
    this.url = 'http://localhost:8080/mMusic-api/songs';
  }

  ngOnInit(): void {
    //show all songs on init
    setTimeout(() => {
      console.log(this.auth.currentUser);
      this.apiSongsService
        .getAllSongsFromPlaylist(this.auth.currentUser.email)
        .subscribe((data: any) => {
          this.songs = data;
        });
    }, 500);
  }

  onSubmit() {}

  playSong(ipfsHash: string) {
    this.audio?.pause();
    //play song using API endpoint
    const url = `${this.playSongUrl}${ipfsHash}`;
    this.http.get(url).subscribe({
      next: (data: any) => {
        this.songStatus = Object.values(data)[0];
        this.songUrl = Object.values(data)[1];

        // verify if song is on blockchain(original) or not
        // HAPPY PATH --> 0 = original song, song was not hacked
        if (this.songStatus == '0') {
          this.snackBar.open(
            'This song was verified through Blockchain and it is original!',
            'Close',
            {
              duration: 5000,
            }
          );
          this.audio = new Audio(this.songUrl);
          this.audio.play();
          this.playing=ipfsHash;
        } else {
          // SAD PATH --> 1 = hacked song, song was hacked
          console.log('song is hacked');
          // show modal to ask user if he wants to play hacked song or not
          this.dialogService
            .openPlaySongDialog()
            .afterClosed()
            .subscribe((result) => {
              if (result) {
                this.audio = new Audio(this.songUrl);
                this.audio.play();
                this.playing=ipfsHash;
              } else {
                this.snackBar.open(
                  'The playback of this song was cancelled!',
                  'Close',
                  {
                    duration: 5000,
                  }
                );
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

  pauseSong() {
    this.audio.pause();
    this.playing=null;
  }

  deleteSong(userEmail: any, songHash: any){
    console.log(songHash);
    let deleteSongFromPlaylist = new FormData();
    deleteSongFromPlaylist.append('userEmail', userEmail);
    deleteSongFromPlaylist.append('songHash', songHash);
    this.http
      .post(this.url + '/deleteSongFromPlaylist', deleteSongFromPlaylist)
      .subscribe({
        next: (data: any) => {
          if(data.code == 0){
            // song was deleted from playlist - frontend
            setTimeout(() => {
              console.log(this.auth.currentUser);
              this.apiSongsService
                .getAllSongsFromPlaylist(this.auth.currentUser.email)
                .subscribe((data: any) => {
                  this.songs = data;
                });
            }, 500);
            this.snackBar.open(
              'Song was successfully deleted from your playlist!',
              'Close',
              {
                duration: 5000,
              }
            );
          }
          else{
            this.snackBar.open(
              'An error occurred...Song could not be deleted from your playlist!',
              'Close',
              {
                duration: 5000,
              }
            );
          }
        },
        error: (error) => {
          console.error(error);
          this.snackBar.open(
            'An error occurred...Song could not be deleted from your playlist!',
            'Close',
            {
              duration: 5000,
            }
          );
        },
      });
  }
}
