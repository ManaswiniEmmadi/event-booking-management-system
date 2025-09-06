import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddSeatsComponent } from './add-seats';

describe('AddSeats', () => {
  let component: AddSeatsComponent;
  let fixture: ComponentFixture<AddSeatsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddSeatsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddSeatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
