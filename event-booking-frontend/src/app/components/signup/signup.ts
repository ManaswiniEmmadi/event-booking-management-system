import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ChangeDetectorRef } from '@angular/core';
import { throws } from 'assert';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './signup.html',
  styleUrls: ['./signup.css']
})
export class SignupComponent implements OnInit {
  signupForm!: FormGroup;
  errorMessage = '';
  successMessage = '';

  roles = ['user', 'organizer'];
  genders = ['Male', 'Female', 'Other'];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.signupForm = this.fb.group(
      {
        fullName: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        password: ['', Validators.required],
        confirmPassword: ['', Validators.required],
        role: ['user', Validators.required],
        phoneNumber: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
        gender: ['', Validators.required],
        dateOfBirth: ['', Validators.required],
        address: ['', Validators.required]
      },
      { validators: this.passwordsMatchValidator }
    );
  }

  private passwordsMatchValidator(control: AbstractControl) {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    return password && confirmPassword && password !== confirmPassword
      ? { passwordsMismatch: true }
      : null;
  }

  onSubmit(): void {
    if (this.signupForm.invalid) {
      this.signupForm.markAllAsTouched();
      return;
    }

    const formData = { ...this.signupForm.value };
    delete formData.confirmPassword; 

    this.authService.register(formData).subscribe({
      next: (res) => {
        this.successMessage = 'Account created successfully! Redirecting to login...';
        console.log('Signup successful:', formData);
        console.log('Response:', res.message);
        this.cdr.detectChanges(); 
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Registration failed. Please try again.';
        this.cdr.detectChanges(); 
        console.log('Signup error:', err);

      }
    });
  }
}
