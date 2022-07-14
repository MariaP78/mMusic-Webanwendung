import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadSongFormComponent } from './upload-song-form.component';

describe('UploadSongFormComponent', () => {
  let component: UploadSongFormComponent;
  let fixture: ComponentFixture<UploadSongFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UploadSongFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadSongFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
