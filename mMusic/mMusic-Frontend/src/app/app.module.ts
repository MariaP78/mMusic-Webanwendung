import { AuthenticationService } from './services/authentication.service';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { LoginFormComponent } from './users/login-form/login-form.component';
import { LoginComponent } from './users/login/login.component';
import { HomepageComponent } from './home/homepage/homepage.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RegisterComponent } from './users/register/register.component';
import { RegisterFormComponent } from './users/register-form/register-form.component';
import { AppRoutingModule } from './app-routing.module';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { NavHeaderComponent } from './navigation/nav-header/nav-header.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatListModule } from '@angular/material/list';
import { SidenavListComponent } from './navigation/sidenav-list/sidenav-list.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { initializeApp } from 'firebase/app';
import { AngularFireModule } from '@angular/fire/compat';
import { AngularFireAuthModule } from '@angular/fire/compat/auth';
import { AccountProfileComponent } from './users/account-profile/account-profile.component';
import { MatTooltipModule } from '@angular/material/tooltip';
import { HttpClientModule } from '@angular/common/http';
import { PlaylistComponent } from './users/playlist/playlist.component';
import { UploadSongFormComponent } from './songs/upload-song-form/upload-song-form.component';
import { UploadSongComponent } from './songs/upload-song/upload-song.component';
import { MatCardModule} from '@angular/material/card';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { PlaySongDialogComponent } from './songs/play-song-dialog/play-song-dialog.component';
import { UpdateSongDialogComponent } from './songs/update-song-dialog/update-song-dialog.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { ArtistUploadsComponent } from './users/artist-uploads/artist-uploads.component';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTabsModule } from '@angular/material/tabs';
import { MatChipsModule } from '@angular/material/chips';
import { MatSelectModule } from '@angular/material/select';
import { VerifiedSongDialogComponent } from './songs/verified-song-dialog/verified-song-dialog.component';
import { HackedSongDialogComponent } from './songs/hacked-song-dialog/hacked-song-dialog.component';

// mMusic Web App's Firebase configuration
const firebaseConfig = {
  apiKey: 'AIzaSyDYvjEQXB4-wS1jpwayPVPnZHwug0vGq48',
  authDomain: 'mmusic-web-app.firebaseapp.com',
  projectId: 'mmusic-web-app',
  storageBucket: 'mmusic-web-app.appspot.com',
  messagingSenderId: '338858992243',
  appId: '1:338858992243:web:9645f0159eef6860934006',
  measurementId: 'G-GNL2J60XHR',
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

@NgModule({
  declarations: [
    AppComponent,
    LoginFormComponent,
    LoginComponent,
    HomepageComponent,
    RegisterComponent,
    RegisterFormComponent,
    NavHeaderComponent,
    SidenavListComponent,
    AccountProfileComponent,
    PlaylistComponent,
    UploadSongFormComponent,
    UploadSongComponent,
    PlaySongDialogComponent,
    UpdateSongDialogComponent,
    ArtistUploadsComponent,
    VerifiedSongDialogComponent,
    HackedSongDialogComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    AppRoutingModule,
    MatSidenavModule,
    MatToolbarModule,
    FlexLayoutModule,
    MatListModule,
    AngularFireModule.initializeApp(firebaseConfig),
    AngularFireAuthModule,
    MatSnackBarModule,
    MatTooltipModule,
    HttpClientModule,
    MatCardModule,
    MatDialogModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatCheckboxModule,
    MatTabsModule,
    MatChipsModule,
    MatSelectModule
  ],
  providers: [AuthenticationService],
  bootstrap: [AppComponent],
  entryComponents: [PlaySongDialogComponent, UpdateSongDialogComponent, VerifiedSongDialogComponent, HackedSongDialogComponent]
})
export class AppModule {}
