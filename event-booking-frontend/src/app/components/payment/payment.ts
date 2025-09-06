import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PaymentService } from '../../services/payment.service';
import { PaymentData, PaymentResponse } from '../../models/payment.model';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './payment.html',
  styleUrls: ['./payment.scss']
})
export class Payment implements OnInit {
  selectedMethod = '';
  paymentSuccessful = false;
  currentPayment: PaymentResponse | null = null;
  loading = false;

  
  bookingId!: number;
  userId!: number;
  amount!: number;
  
  // Card details
  cardNumber = '';
  cardExpiry = '';
  cvv = '';
  
  // UPI details
  upiId = '';
  
  // Net Banking details
  bankName = '';

  constructor(
    private paymentService: PaymentService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    
    this.userId = this.authService.getUserId() ?? 0;

    this.route.queryParams.subscribe(params => {
      this.bookingId = +params['bookingId'] || 0;
      this.amount = +params['amount'] || 0;
    });
  }

  payWithCard() {
    const paymentData: PaymentData = {
      bookingId: this.bookingId,
      userId: this.userId,
      paymentMethod: 'Card',
      amount: this.amount,
      cardNumberLast4: this.cardNumber.slice(-4),
      cardExpiry: this.cardExpiry,
      cvv: this.cvv
    };
    this.sendPayment(paymentData);
  }

  payWithUpi() {
    const paymentData: PaymentData = {
      bookingId: this.bookingId,
      userId: this.userId,
      paymentMethod: 'UPI',
      amount: this.amount,
      upiId: this.upiId
    };
    this.sendPayment(paymentData);
  }

  payWithNetBanking() {
    const paymentData: PaymentData = {
      bookingId: this.bookingId,
      userId: this.userId,
      paymentMethod: 'NetBanking',
      amount: this.amount,
      bankName: this.bankName
    };
    this.sendPayment(paymentData);
  }

  private sendPayment(data: PaymentData) {
    if (!this.bookingId || !this.amount) {
      alert('Booking information is missing. Please try again.');
      return;
    }

    this.loading = true;
    this.paymentService.processPayment(data).subscribe({
      next: (res: PaymentResponse) => {
        this.loading = false;
        this.paymentSuccessful = true;
        this.currentPayment = res;
        this.cdr.detectChanges();
        alert('Payment Successful! Transaction ID: ' + res.transactionId);

        
      },
      error: (err: any) => {
        this.loading = false;
        alert('Payment Failed: ' + (err.error?.message || err.message));
      }
    });
  }

  goToBookingConfirmation() {
    this.router.navigate(['/booking-confirmation'], { 
      queryParams: { bookingId: this.bookingId, amount: this.amount }
    });
  }

  downloadInvoice() {
    if (this.currentPayment) {
      this.paymentService.downloadInvoice(this.currentPayment.paymentId).subscribe({
        next: (blob: Blob) => {
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.download = `invoice-${this.currentPayment?.paymentId}.pdf`;
          link.click();
          window.URL.revokeObjectURL(url);
        },
        error: (err: any) => {
          alert('Failed to download invoice: ' + (err.error?.message || err.message));
        }
      });
    }
  }

  resetForm() {
    this.paymentSuccessful = false;
    this.currentPayment = null;
    this.selectedMethod = '';
    this.cardNumber = '';
    this.cardExpiry = '';
    this.cvv = '';
    this.upiId = '';
    this.bankName = '';
  }
}
