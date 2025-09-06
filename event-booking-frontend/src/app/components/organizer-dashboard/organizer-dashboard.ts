import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { EventComponent } from '../event-component/event-component';
import { RouterLink, RouterOutlet } from '@angular/router';
import { Header } from '../org-header/header';

@Component({
  selector: 'app-organizer-dashboard',
  imports: [ Header, RouterOutlet],
  templateUrl: './organizer-dashboard.html',
  styleUrl: './organizer-dashboard.css'
})
export class OrganizerDashboardComponent {
  username: string = '';
  constructor(private authService: AuthService) {}
  ngOnInit() {
    this.username = this.authService.getUsername() || 'User';
  }
  logout() { this.authService.logout(); }
}



