import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SeatService } from '../../services/seat.service';
import { CommonModule } from '@angular/common';
import { SeatType } from '../../models/seats.model';

@Component({
  selector: 'app-seats',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './seats.html',
  styleUrls: ['./seats.css']
})
export class SeatComponent implements OnInit {
  eventId!: number;
  seats: SeatType[] = [];
  eventName: string = '';

  loading: boolean = false;
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private seatService: SeatService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.eventId = Number(params.get('eventId'));
      if (this.eventId) {
        this.loadSeats();
      }
    });
  }

  loadSeats(): void {
    this.loading = true;
    this.errorMessage = '';

    this.seatService.getSeatsByEventId(this.eventId).subscribe({
      next: (eventData: SeatType[]) => {
        this.seats = eventData;
        this.eventName = eventData[0]?.eventName || '';
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error fetching seat info:', err);
        this.loading = false;
        this.errorMessage = 'Failed to fetch seat information. Please try again.';
        this.cdr.detectChanges();
      },
    });
  }

  bookSeat(seat: SeatType) {
    this.router.navigate(['/book'], {
      queryParams: {
        seatTypeId: seat.id,
        seatTypeName: seat.seatType,
        price: seat.price,
        availableSeats: seat.availableSeats,
        eventName: this.eventName,
        eventId: this.eventId
      }
    });
  }
}
