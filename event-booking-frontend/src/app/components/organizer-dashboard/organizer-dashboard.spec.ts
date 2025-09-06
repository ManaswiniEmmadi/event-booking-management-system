import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganizerDashboardComponent } from './organizer-dashboard';

describe('OrganizerDashboard', () => {
  let component: OrganizerDashboardComponent;
  let fixture: ComponentFixture<OrganizerDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrganizerDashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrganizerDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
