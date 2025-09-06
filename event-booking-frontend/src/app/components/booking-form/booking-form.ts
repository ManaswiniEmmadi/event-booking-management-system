import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, ReactiveFormsModule } from '@angular/forms';
import { BookingRequest } from '../../models/booking.model';
import { BookingService } from '../../services/booking.service';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-booking-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './booking-form.html',
  styleUrls: ['./booking-form.css']
})
export class BookingFormComponent {
  bookingForm: FormGroup;
  successMessage = '';

  availableSeats = 0;
  price = 0;

  seatTypeName: string = ''; 

  bookingId?: number;

  bookedSeats: number = 0; 


  constructor(
    private fb: FormBuilder,
    private bookingService: BookingService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    this.bookingForm = this.fb.group({
      userName: ['', Validators.required],
      mobile: ['', [Validators.required, Validators.pattern('^[6-9][0-9]{9}$')]],
      email: ['', [Validators.required, Validators.email]],
      seatTypeId: ['', Validators.required],
      numberOfSeats: [1, [Validators.required, Validators.min(1)]],
      eventId: ['', Validators.required],
      eventName: [''],
      status: ['PENDING', Validators.required]
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const seatTypeId = +params['seatTypeId'] || 0; 
      this.seatTypeName = params['seatTypeName'] || ''; 
      this.availableSeats = +params['availableSeats'] || 0;
      this.price = +params['price'] || 0;
      const eventName = params['eventName'] || '';
      const eventId = +params['eventId'] || 0;
      

      this.bookingForm.patchValue({
        seatTypeId: seatTypeId,
        numberOfSeats: 1,
        eventName: eventName,
        eventId: eventId
      });

      //max validator for number of seats
      this.bookingForm.get('numberOfSeats')?.setValidators([
        Validators.required,
        Validators.min(1),
        Validators.max(this.availableSeats)
      ]);
      this.bookingForm.get('numberOfSeats')?.updateValueAndValidity();
    });
  }

  

  onSubmit() {
    if (this.bookingForm.valid) {
      const booking: BookingRequest = {
        userName: this.bookingForm.value.userName,
        mobile: this.bookingForm.value.mobile,
        email: this.bookingForm.value.email,
        seatTypeId: Number(this.bookingForm.value.seatTypeId),
        numberOfSeats: Number(this.bookingForm.value.numberOfSeats),
        eventId: Number(this.bookingForm.value.eventId),
        status: this.bookingForm.value.status
      };

    

      if (booking.numberOfSeats > this.availableSeats) {
        this.successMessage = `Only ${this.availableSeats} seats are available.`;
        return;
      }

      this.bookingService.createBooking(booking).subscribe({
        next: (res) => {
          this.successMessage = 'Booking successful!';
          
          this.bookingId = res.bookingId;  

          this.bookedSeats = booking.numberOfSeats; 

          this.availableSeats -= booking.numberOfSeats;

          this.cdr.detectChanges();

          console.log('Available seats after booking:', this.availableSeats);

          this.bookingForm.get('numberOfSeats')?.setValidators([
            Validators.required,
            Validators.min(1),
            Validators.max(this.availableSeats)
          ]);
          this.bookingForm.get('numberOfSeats')?.updateValueAndValidity();
          
          this.bookingForm.patchValue({ numberOfSeats: 1 });

        },
        error: (err) => {
          console.error('Booking error:', err);
          this.successMessage = 'Booking failed. Try again.';
        }
      });
    }
  }

  goToPayment() {
    this.router.navigate(['/payment'], { 
      queryParams: { bookingId: this.bookingId, amount: this.price * this.bookedSeats }
    });
  }


}
