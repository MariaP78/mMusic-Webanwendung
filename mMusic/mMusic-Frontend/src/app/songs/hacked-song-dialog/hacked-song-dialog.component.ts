import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-hacked-song-dialog',
  templateUrl: './hacked-song-dialog.component.html',
  styleUrls: ['./hacked-song-dialog.component.scss']
})
export class HackedSongDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<HackedSongDialogComponent>) { }

  ngOnInit(): void {
  }

}
