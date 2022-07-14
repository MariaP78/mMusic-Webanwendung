import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { getAuth, signInWithEmailAndPassword } from 'firebase/auth';
import { ApicallService } from 'src/app/services/apicall.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { DialogService } from 'src/app/services/dialog.service';
// const auth = getAuth();

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss'],
})
export class HomepageComponent implements OnInit {
  playSongUrl: string;
  songs: any;
  audio: any;
  songUrl: any;
  songStatus: any;
  url: string;
  playing: any = null;
  recommendedSongs: any;

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
      // get all songs from API
      this.apiSongsService.getAllSongs().subscribe((data: any) => {
        this.songs = data;
      });
    }, 500);
    
    setTimeout(() => {
      console.log(this.auth.currentUser);
      // get all recommended songs from API
      this.apiSongsService.getRecommendations(this.auth.currentUser?.email).subscribe((data: any) => {
        this.recommendedSongs = data;
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

  addSongToPlaylist(userEmail: any, songHash: any) {
    // console.log('Add Song to Playlist:',hash);
    let addSongToPlaylist = new FormData();
    addSongToPlaylist.append('userEmail', userEmail);
    addSongToPlaylist.append('songHash', songHash);
    this.http
      .post(this.url + '/addSongToPlaylist', addSongToPlaylist)
      .subscribe({
        next: (data: any) => {
          if(data.code == 0){
            // song was added to playlist - frontend
            this.auth.currentUser.playlist?.push(songHash);
            this.snackBar.open(
              'Song was successfully added to your playlist!',
              'Close',
              {
                duration: 5000,
              }
            );
          }
          else{
            this.snackBar.open(
              'An error occurred...Song could not be added to your playlist!',
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
            'An error occurred...Song could not be added to your playlist!',
            'Close',
            {
              duration: 5000,
            }
          );
        },
      });
  }
}
