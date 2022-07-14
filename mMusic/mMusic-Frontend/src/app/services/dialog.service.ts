import { HackedSongDialogComponent } from './../songs/hacked-song-dialog/hacked-song-dialog.component';
import { VerifiedSongDialogComponent } from './../songs/verified-song-dialog/verified-song-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { Injectable } from '@angular/core';
import { PlaySongDialogComponent } from '../songs/play-song-dialog/play-song-dialog.component';
import { UpdateSongDialogComponent } from '../songs/update-song-dialog/update-song-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class DialogService {
  formData: any;

  constructor(private dialog: MatDialog) { }

  openPlaySongDialog(){
    return this.dialog.open(PlaySongDialogComponent, {
      disableClose: true
    });
  }

  openUpdateSongDialog(title:any,stageName:any,genre:any){
    return this.dialog.open(UpdateSongDialogComponent, {data:{title, stageName, genre},
      disableClose: true,
    });
  }

  openVerifiedSongDialog(){
    return this.dialog.open(VerifiedSongDialogComponent, {
      disableClose: true,
    });
  }

  openHackedSongDialog(){
    return this.dialog.open(HackedSongDialogComponent, {
      disableClose: true,
    });
  }
}
