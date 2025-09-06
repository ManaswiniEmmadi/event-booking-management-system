import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './user-profile.html',
  styleUrls: ['./user-profile.css']
})
export class UserProfileComponent {
  username: string = '';
  email: string = '';
  role: string = '';
  userId: number = 0;
  userRole: string = '';

  constructor(private authService: AuthService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.username = this.authService.getUsername() || 'User';
    this.email = localStorage.getItem('email') || 'user@example.com';
    this.userRole = this.authService.getRole() || 'User';
    this.cdr.detectChanges(); 
  }
}
