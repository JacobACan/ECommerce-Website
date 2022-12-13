import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddStarRatingComponent } from './add-star-rating.component';

describe('AddStarRatingComponent', () => {
  let component: AddStarRatingComponent;
  let fixture: ComponentFixture<AddStarRatingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddStarRatingComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddStarRatingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
