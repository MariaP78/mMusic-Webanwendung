import { DialogService } from './../../services/dialog.service';
import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-update-song-dialog',
  templateUrl: './update-song-dialog.component.html',
  styleUrls: ['./update-song-dialog.component.scss']
})
export class UpdateSongDialogComponent implements OnInit {
  genres: any[] = ["rock", "pop", "folk_pop", "jazz", "opera", "hip_hop", "rap", "classical", "electronic", "dance", "latino", "heavy_metal"];
  updateSongForm:any;
  constructor(public dialogRef: MatDialogRef<UpdateSongDialogComponent>, private dialog: DialogService,@Inject(MAT_DIALOG_DATA) public data: any ) { }

  ngOnInit(): void {
    console.log(this.data);
    this.updateSongForm = new FormGroup({
    title: new FormControl(this.data.title, [Validators.required]),
    stageName: new FormControl(this.data.stageName, [Validators.required]),
    genre: new FormControl(this.data.genre, [Validators.required])
  });
  }



  onSubmit() {
  }

  sendData(){
    this.dialog.formData = this.updateSongForm.value;
  }

}
