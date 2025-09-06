import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BookingService } from '../../services/booking.service';
import { BookingResponse } from '../../models/booking.model';
import { Eventdata } from '../../models/eventsdata'; 
import { EventService } from '../../services/event-service'; 
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-bookings-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './booking-list.html',
  styleUrls: ['./booking-list.css']
})
export class BookingListComponent implements OnInit {
  bookings: BookingResponse[] = [];
  events: Eventdata[] = [];
  loading = true;
  errorMessage = '';

  constructor(
    private bookingService: BookingService,
    private eventService: EventService,
    private cdr: ChangeDetectorRef, 
    private router: Router 
  ) {}

  ngOnInit(): void {
    this.eventService.getAllEvents().subscribe({
      next: (eventsData) => {
        this.events = eventsData;

        this.bookingService.getMyBookings().subscribe({
          next: (bookingsData) => {
            console.log('Bookings Data:', bookingsData);
            console.log('Events Data:', this.events);
            this.bookings = bookingsData.map(booking => {
            const event = this.events.find(e => +e.eventId === +booking.eventId);
            return {
              ...booking,
              eventName: event?.eventName || 'Unknown Event',
              eventDate: event?.eventDate || '',
              location: event?.location || ''
            };
          });

            this.loading = false;
            this.cdr.detectChanges(); 
          },
          error: () => {
            this.errorMessage = 'Failed to load bookings.';
            this.loading = false;
            this.cdr.detectChanges(); 
          }
        });
      },
      error: () => {
        this.errorMessage = 'Failed to load events.';
        this.loading = false;
        this.cdr.detectChanges(); 
      }
    });
  }

  goToPayment(booking: any) {
    this.router.navigate(['/payment'], { 
      queryParams: { bookingId: booking.bookingId, amount: booking.price }
    });
  }
  


  
}
