import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-upload-song-form',
  templateUrl: './upload-song-form.component.html',
  styleUrls: ['./upload-song-form.component.scss'],
})
export class UploadSongFormComponent implements OnInit {
  url: string;
  file: any;
  genres: any[] = ["rock", "pop", "folk_pop", "jazz", "opera", "hip_hop", "rap", "classical", "electronic", "dance", "latino", "heavy_metal"];

  constructor(
    private router: Router,
    private http: HttpClient,
    private snackBar: MatSnackBar,
    private auth: AuthenticationService
  ) {
    this.url = 'http://localhost:8080/mMusic-api/songs';
  }

  uploadSongForm = new FormGroup({
    title: new FormControl('', [Validators.required]),
    author: new FormControl('', [Validators.required]),
    stageName: new FormControl('', [Validators.required]),
    authorsEmail: new FormControl('', [Validators.required]),
    album: new FormControl('', [Validators.required]),
    genre: new FormControl('', [Validators.required]),
    releaseYear: new FormControl('', [Validators.required]),
  });

  onSubmit() {
    console.log(this.uploadSongForm.value);
    console.log(this.uploadSongForm.value.genre)
  }

  handleFileChange(e: any): void {
    this.file = e.files[0];
    console.log(this.file);
    // let formData = new FormData();
    // formData.append('file', this.file);
    // fetch(this.url + '/addSongToIPFS', { method: 'POST', body: formData })
    //   .then((r) => r.json())
    //   .then((r) => console.log(r));
  }

  uploadSongOnClick() {
    var songToBeAdded = {
      title: this.uploadSongForm.value.title,
      author: this.auth.currentUser.firstname + ' ' + this.auth.currentUser.lastname,
      stageName: this.auth.currentUser.stageName,
      authorsEmail: this.auth.currentUser.email,
      album: this.uploadSongForm.value.album,
      genre: this.uploadSongForm.value.genre,
      releaseYear: this.uploadSongForm.value.releaseYear,
    };

    let formData = new FormData();
    formData.append('data',new Blob([JSON.stringify(songToBeAdded)], {type: 'application/json'}));
    formData.append('file', this.file);

    //add song using API endpoint
    this.http.post(this.url + '/addSong', formData).subscribe({
      next: (data) => {
        console.log(data);
        this.snackBar.open('Song is being uploaded! It may take a few minutes...', 'Close', {
          duration: 5000,
        });
      },
      error: (error) => {
        console.error(error);
        this.snackBar.open('An error occurred...Song could NOT be uploaded!', 'Close', {
          duration: 5000,
        });
      },
    });
  }

  ngOnInit(): void {}
}
