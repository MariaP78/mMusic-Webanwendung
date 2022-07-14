import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerifiedSongDialogComponent } from './verified-song-dialog.component';

describe('VerifiedSongDialogComponent', () => {
  let component: VerifiedSongDialogComponent;
  let fixture: ComponentFixture<VerifiedSongDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerifiedSongDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VerifiedSongDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
