import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Eventdata } from '../../models/eventsdata';
import { EventService } from '../../services/event-service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-event-component',
  imports: [CommonModule, RouterModule],
  templateUrl: './event-component.html',
  styleUrl: './event-component.css'
})
export class EventComponent implements OnInit{

  events: Eventdata[] = [];
  isLoading: boolean=true;
  errorMessage: String="";

  constructor(
              private eventService:EventService,
              private cdr: ChangeDetectorRef,
              public authservice:AuthService
            ) {}

  ngOnInit(): void {
    this.eventService.getAllEvents().subscribe({
      next: (data) => {
        this.events = data;
        this.isLoading = false;
        console.log(this.events);
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = 'Failed to load events';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

}