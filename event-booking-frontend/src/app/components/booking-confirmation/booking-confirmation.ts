import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { BookingService } from '../../services/booking.service';
import { BookingResponse } from '../../models/booking.model';
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-booking-confirmation',
  standalone: true,
  imports: [CommonModule,RouterModule],
  templateUrl: './booking-confirmation.html',
  styleUrls: ['./booking-confirmation.css']
})
export class BookingConfirmationComponent implements OnInit {
  bookingId!: number;
  booking!: BookingResponse;
  price!: number;
  loading = true;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private bookingService: BookingService,
    private cdr: ChangeDetectorRef,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
  this.route.queryParams.subscribe(params => {
    this.bookingId = +params['bookingId'];
    this.price = +params['amount'];

    if (!this.bookingId) {
      alert('Booking ID missing.');
      return;
    }

    if (isNaN(this.price) || this.price <= 0) {
      alert('Booking amount is missing. Please try again.');
      return;
    }

    this.fetchBookingDetails();
  });
}

  fetchBookingDetails(): void {
    this.bookingService.getBookingById(this.bookingId).subscribe({
      next: (data) => {
        this.booking = data;
        this.loading = false;
        this.cdr.detectChanges(); 
      },
      error: (err) => {
        this.error = 'Unable to load booking details';
        this.loading = false;
      }
    });
  }

  downloadInvoice(): void {

    if (!this.price || isNaN(this.price)) {
      alert('Cannot download invoice: invalid amount.');
      return;
    }
    const amount = this.price;
    this.http.get(`http://localhost:8080/api/payments/ticket/${this.bookingId}?amount=${amount}`, { responseType: 'blob' })
      .subscribe((blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `ticket-${this.bookingId}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
    });
  }

}
