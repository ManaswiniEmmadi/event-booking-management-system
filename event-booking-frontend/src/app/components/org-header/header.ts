import { Component } from '@angular/core';
import { RouterLink, RouterModule,} from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterModule],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header {
  constructor(private authService: AuthService) {}
    logout() { this.authService.logout(); }

}
