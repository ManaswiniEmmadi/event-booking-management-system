import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { EventService } from '../../services/event-service';
import { Eventdata } from '../../models/eventsdata';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-user-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './user-dashboard.html',
  styleUrls: ['./user-dashboard.css']
})
export class UserDashboardComponent implements OnInit {
  username: string = '';
  events: Eventdata[] = [];
  loading = true;
  error = '';

  constructor(
    private authService: AuthService,
    private eventService: EventService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    this.username = this.authService.getUsername() || 'User';
    this.loadEvents();
  }

  loadEvents() {
    this.eventService.getAllEvents().subscribe({
      next: (data) => {
        console.log('Events Data:', data);
        this.events = data;
        this.loading = false;
        this.cdr.detectChanges(); 
      },
      error: () => {
        this.error = 'Failed to load events.';
        this.loading = false;
        this.cdr.detectChanges(); 
      }
    });
  }

  viewSeats(eventId: number) {
    this.router.navigate(['/seats', eventId]);
  }

  logout() {
    this.authService.logout();
  }
}
