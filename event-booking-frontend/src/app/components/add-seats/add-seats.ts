import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors, ValidatorFn, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SeatService } from '../../services/seat.service';
import { EventService } from '../../services/event-service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-add-seats',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './add-seats.html',
  styleUrls: ['./add-seats.css']
})
export class AddSeatsComponent implements OnInit {
  form!: FormGroup;
  eventId!: number;
  successMessage = '';
  errorMessage = '';
  seatTypes = ['VIP', 'General'];
  eventCapacity = 0;  

  constructor(
    private fb: FormBuilder,
    private seatService: SeatService,
    private eventService: EventService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.eventId = Number(this.route.snapshot.paramMap.get('eventId'));

    this.form = this.fb.group({
      seatType: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(100)]],
      totalSeats: [{ value: '', disabled: true }, [Validators.required]],
      availableseats: [1, [Validators.required, Validators.min(0)]],
      eventName: [{ value: '', disabled: true }]
    });

    
    this.eventService.getEventById(this.eventId).subscribe({
      next: (event) => {
        this.eventCapacity = event.capacity; 
        this.form.patchValue({
          eventName: event.eventName,
          totalSeats: this.eventCapacity
        });

        this.form.get('availableseats')?.addValidators([
          this.maxAvailableValidator(this.eventCapacity)
        ]);
        this.form.get('availableseats')?.updateValueAndValidity();
      },
      error: () => {
        this.errorMessage = 'Failed to load event details';
      }
    });
  }

  maxAvailableValidator = (capacity: number): ValidatorFn => {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (value > capacity) {
        return { maxAvailable: { max: capacity } };
      }
      return null;
    };
  };

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const seatData = { ...this.form.getRawValue() };

    this.seatService.addSeatType(this.eventId, seatData).subscribe({
      next: () => {
        this.successMessage = 'Seat info added successfully!';
        console.log('Seat info added:', seatData);
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Failed to add seats';
        this.cdr.detectChanges();
      }
    });
  }
}
