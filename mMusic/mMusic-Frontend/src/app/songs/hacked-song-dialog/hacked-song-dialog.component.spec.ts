import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HackedSongDialogComponent } from './hacked-song-dialog.component';

describe('HackedSongDialogComponent', () => {
  let component: HackedSongDialogComponent;
  let fixture: ComponentFixture<HackedSongDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HackedSongDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HackedSongDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
