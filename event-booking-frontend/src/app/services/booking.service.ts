import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BookingRequest, BookingResponse, UpdateBookingDTO } from '../models/booking.model';

export interface BookingStatusUpdateDTO {
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  private baseUrl = 'http://localhost:8080/api/bookings';

  constructor(private http: HttpClient) {}

  //Create a new booking
  createBooking(dto: BookingRequest): Observable<BookingResponse> {
    return this.http.post<BookingResponse>(`${this.baseUrl}/addBooking`, dto);
  }

  //Get all bookings
  getAllBookings(): Observable<BookingResponse[]> {
    return this.http.get<BookingResponse[]>(`${this.baseUrl}/viewAllBookings`);
  }

  //MyBookings
  getMyBookings(): Observable<BookingResponse[]> {
    return this.http.get<BookingResponse[]>(`${this.baseUrl}/myBookings`);
  }

  //Get booking by ID
  getBookingById(id: number): Observable<BookingResponse> {
    return this.http.get<BookingResponse>(`${this.baseUrl}/${id}`);
  }

  //Get bookings by event ID
  getBookingsByEventId(eventId: string): Observable<BookingResponse[]> {
    return this.http.get<BookingResponse[]>(`${this.baseUrl}/event/${eventId}`);
  }

  //Get bookings by status
  getBookingsByStatus(status: string): Observable<BookingResponse[]> {
    const params = new HttpParams().set('status', status);
    return this.http.get<BookingResponse[]>(`${this.baseUrl}/by-status`, { params });
  }

  //Full update of a booking
  updateBooking(id: number, dto: UpdateBookingDTO): Observable<BookingResponse> {
    return this.http.put<BookingResponse>(`${this.baseUrl}/updateBooking/${id}`, dto);
  }

  //Partial update (status only)
  updateBookingStatus(bookingId: number, status: string): Observable<BookingResponse> {
    return this.http.patch<BookingResponse>(
      `${this.baseUrl}/${bookingId}/status`,
      {}, //status is in query param
      { params: { status } }
    );
  }

  //Delete a booking
  deleteBooking(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/delete/${id}`, { responseType: 'text' });
  }
}
