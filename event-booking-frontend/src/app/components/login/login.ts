import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  errorMessage = '';
  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
  if (this.loginForm.valid) {
    const { email, password } = this.loginForm.value;

    this.authService.login(email!, password!).subscribe({
      next: (user) => {
        if (user.role?.toLowerCase() === 'user') {
          this.router.navigate(['/user-dashboard']);
        } else if (user.role?.toLowerCase() === 'organizer') {
          this.router.navigate(['/organizer-dashboard']);
        }
      },
      error: () => {
        this.errorMessage = 'Invalid email or password';
      }
    });
  }
}



}
