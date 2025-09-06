import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EventService } from '../../services/event-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-event-bookings',
  templateUrl: './event-bookings.html',
  styleUrls: ['./event-bookings.css'],
  standalone: true,
  imports: [CommonModule]
})
export class EventBookingsComponent implements OnInit {
  eventId!: number;
  bookings: any[] = [];
  isLoading = true;
  eventName: string | null = null;

  constructor(private route: ActivatedRoute, private eventService: EventService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.eventId = Number(this.route.snapshot.paramMap.get('eventId'));
    this.loadEventName();
    this.loadBookings();
  }

  loadEventName() {
    this.eventService.getEventById(this.eventId).subscribe({
      next: (event) => {
        this.eventName = event.eventName; 
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to fetch event name', err);
        this.eventName = null;
        this.cdr.detectChanges();
      }
    });
  }

  loadBookings() {
    this.eventService.getBookingsByEvent(this.eventId).subscribe({
      next: (data) => {
        this.bookings = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }
}
