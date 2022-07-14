import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-verified-song-dialog',
  templateUrl: './verified-song-dialog.component.html',
  styleUrls: ['./verified-song-dialog.component.scss']
})
export class VerifiedSongDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<VerifiedSongDialogComponent>) { }

  ngOnInit(): void {
  }

}
