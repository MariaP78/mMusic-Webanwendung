import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlaySongDialogComponent } from './play-song-dialog.component';

describe('PlaySongDialogComponent', () => {
  let component: PlaySongDialogComponent;
  let fixture: ComponentFixture<PlaySongDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlaySongDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PlaySongDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
