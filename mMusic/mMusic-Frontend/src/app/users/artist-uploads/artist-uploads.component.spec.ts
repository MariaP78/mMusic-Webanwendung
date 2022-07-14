import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArtistUploadsComponent } from './artist-uploads.component';

describe('ArtistUploadsComponent', () => {
  let component: ArtistUploadsComponent;
  let fixture: ComponentFixture<ArtistUploadsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArtistUploadsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArtistUploadsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
