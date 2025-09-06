import { ChangeDetectorRef, Component } from '@angular/core';

import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Eventdata } from '../../models/eventsdata';
import { EventService } from '../../services/event-service';
import { Router } from '@angular/router'; 

@Component({
  selector: 'app-my-events',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './my-events.html',
  styleUrl: './my-events.css'
})
export class MyEvents {
  myEvents: Eventdata[] = [];
  isLoading = true;
  errorMessage: string | null = null;

  selectedEvent: Eventdata | null = null;
  updateForm!: FormGroup;

  bookings: any[] = []; 
  selectedEventId: number | null = null;

  constructor(private eventService: EventService, private fb: FormBuilder,private cdr: ChangeDetectorRef, private router: Router) {}

  ngOnInit(): void {
    this.loadMyEvents();
  }

  loadMyEvents() {
    this.eventService.getMyEvents().subscribe({
      next: (data) => {
        this.myEvents = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = 'Failed to load events.';
        this.isLoading = false;
        console.error(err);
        this.cdr.detectChanges();
      }
    });
  }

  startUpdate(event: Eventdata) {
    this.selectedEvent = event;
    this.updateForm = this.fb.group({
      eventName: [event.eventName, Validators.required],
      eventDate: [event.eventDate, Validators.required],
      location: [event.location, Validators.required],
      capacity: [event.capacity, [Validators.required, Validators.min(1)]],
      description: [event.description],
      status: [event.status, Validators.required]
    });
  }

  cancelUpdate() {
    this.selectedEvent = null; 
  }

  submitUpdate() {
    if (!this.selectedEvent) return;
    this.eventService.updateEvent(this.selectedEvent.eventId, this.updateForm.value).subscribe({
      next: () => {
        alert('Event updated successfully!');
        this.selectedEvent = null;
        this.loadMyEvents();
      },
      error: (err) => {
        console.error('Update failed', err);
        alert('Failed to update event.');
      }
    });
  }

  deleteEvent() {
    if (!this.selectedEvent) return;
    if (confirm('Do you want to delete this event?')) {
      this.eventService.deleteEvent(this.selectedEvent.eventId).subscribe({
        next: (msg) => {
          alert(msg);
          this.selectedEvent = null;
          this.loadMyEvents();
        },
        error: (err) => {
          console.error('Delete failed', err);
          alert('Failed to delete event.');
        }
      });
    }
  }

  goToAddSeats(eventId: number) {
    this.router.navigate(['/add-seats', eventId]);
  }

  viewBookings(eventId: number) {
    this.selectedEventId = eventId;
    this.eventService.getBookingsByEvent(eventId).subscribe({
      next: (data) => {
        this.bookings = data;
        this.router.navigate(['/event-bookings', eventId]); 
      },
      error: (err) => {
        console.error('Failed to load bookings', err);
        alert('Failed to load bookings');
      }
    });
  }
}
