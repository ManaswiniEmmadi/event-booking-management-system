import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentData, PaymentResponse } from '../models/payment.model';


@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private apiUrl = 'http://localhost:8080/api/payments';
  private invoiceUrl = 'http://localhost:8080/api/invoices';

  constructor(private http: HttpClient) {}

  processPayment(data: PaymentData): Observable<PaymentResponse> {
    return this.http.post<PaymentResponse>(this.apiUrl, data);
  }

  downloadInvoice(paymentId: number): Observable<Blob> {
    return this.http.get(`${this.invoiceUrl}/download/${paymentId}`, {
      responseType: 'blob'
    });
  }
}
