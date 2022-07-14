import { HttpClient } from '@angular/common/http';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { getAuth, onAuthStateChanged } from 'firebase/auth';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-nav-header',
  templateUrl: './nav-header.component.html',
  styleUrls: ['./nav-header.component.scss'],
})
export class NavHeaderComponent implements OnInit {
  public currentUser: any;
  @Output() public sidenavHeader = new EventEmitter();

  constructor(
    public auth: AuthenticationService,
    private http: HttpClient,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {}

  public onToggleSidenav = () => {
    this.sidenavHeader.emit();
  };

  goToPlaylist() {
    //console.log('playlist button');
  }

  goToYourUploads() {
    //console.log('your uploads button');
  }
}
