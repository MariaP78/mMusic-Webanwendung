import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateSongDialogComponent } from './update-song-dialog.component';

describe('UpdateSongDialogComponent', () => {
  let component: UpdateSongDialogComponent;
  let fixture: ComponentFixture<UpdateSongDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateSongDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateSongDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
