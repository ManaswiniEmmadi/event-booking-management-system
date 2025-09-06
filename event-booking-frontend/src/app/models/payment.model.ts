// Updated to match backend PaymentRequestDTO
export interface PaymentData {
  bookingId: number;
  userId: number;
  paymentMethod: 'Card' | 'UPI' | 'NetBanking';
  amount: number;
  cardNumberLast4?: string;
  cardExpiry?: string;
  cvv?: string;
  upiId?: string;
  bankName?: string;
}

export interface PaymentResponse {
  paymentId: number;
  bookingId: number;
  userId: number;
  paymentMethod: string;
  transactionId: string;
  paymentStatus: string;
  amount: number;
  paymentDate: string;
}