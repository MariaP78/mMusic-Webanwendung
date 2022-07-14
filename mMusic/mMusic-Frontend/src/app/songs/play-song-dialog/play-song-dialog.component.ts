import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-play-song-dialog',
  templateUrl: './play-song-dialog.component.html',
  styleUrls: ['./play-song-dialog.component.scss']
})
export class PlaySongDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<PlaySongDialogComponent>) { }

  ngOnInit(): void {
  }

}
