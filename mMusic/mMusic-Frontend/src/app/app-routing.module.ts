import { ArtistUploadsComponent } from './users/artist-uploads/artist-uploads.component';
import { UploadSongComponent } from './songs/upload-song/upload-song.component';
import { PlaylistComponent } from './users/playlist/playlist.component';
import { AccountProfileComponent } from './users/account-profile/account-profile.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from './home/homepage/homepage.component';
import { LoginComponent } from './users/login/login.component';
import { RegisterComponent } from './users/register/register.component';
import { AuthenticationGuard } from './services/authentication.guard';

const routes: Routes = [
  {path: '', component: HomepageComponent, canActivate: [AuthenticationGuard]},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'profile', component: AccountProfileComponent, canActivate: [AuthenticationGuard]},
  {path: 'newSong', component: UploadSongComponent, canActivate: [AuthenticationGuard]},
  {path: 'playlist', component: PlaylistComponent, canActivate: [AuthenticationGuard]},
  {path: 'your-uploads', component: ArtistUploadsComponent, canActivate: [AuthenticationGuard]},
  {path: '**', redirectTo:''},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
