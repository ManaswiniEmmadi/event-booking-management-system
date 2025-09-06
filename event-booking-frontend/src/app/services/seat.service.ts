// src/app/services/event.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SeatEvent, SeatType } from '../models/seats.model';



@Injectable({
  providedIn: 'root'
})
export class SeatService {
  private apiUrl = 'http://localhost:8080/seats'; // Spring Boot API

  constructor(private http: HttpClient) {}

  getAllEvents(): Observable<SeatEvent[]> {
    return this.http.get<SeatEvent[]>(this.apiUrl);
  }

  addSeatType(eventId: number, payload: any): Observable<SeatType> {
    return this.http.post<SeatType>(`${this.apiUrl}/add/${eventId}`, payload);
  }
  

  getSeatsByEventId(eventId: number): Observable<SeatType[]> {
  return this.http.get<SeatType[]>(`http://localhost:8080/seats/event/${eventId}`);
}


  bookSeats(eventId: number, seatTypeId: number): Observable<any> {
  const bookedSeats = 2; // hardcoded for now
  return this.http.put(`${this.apiUrl}/${eventId}/seat-types/${seatTypeId}/book?bookedSeats=${bookedSeats}`, {});
}

}
