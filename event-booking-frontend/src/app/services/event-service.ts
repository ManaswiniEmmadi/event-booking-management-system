import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateEvent, Eventdata } from '../models/eventsdata';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  url = 'http://localhost:8080/event';

  constructor(private http: HttpClient) {}

  getAllEvents(): Observable<Eventdata[]> {
    return this.http.get<Eventdata[]>(`${this.url}/all`);
  }

  getEventById(eventId: number): Observable<any> {
    return this.http.get<any>(`${this.url}/get/${eventId}`);
  }

  createEvent(payload: CreateEvent): Observable<string> {
    return this.http.post(`${this.url}/create`, payload, { responseType: 'text' });
  }

  getMyEvents(): Observable<Eventdata[]> {
    return this.http.get<Eventdata[]>(`${this.url}/my`);
  }

  getBookingsByEvent(eventId: number) {
    return this.http.get<any[]>(`${this.url}/${eventId}/bookings`);
  }


  updateEvent(eventId: number, updatedData: Partial<Eventdata>): Observable<Eventdata> {
    return this.http.put<Eventdata>(`${this.url}/update/${eventId}`, updatedData);
  }

  deleteEvent(eventId: number): Observable<string> {
    return this.http.delete(`${this.url}/delete/${eventId}`, { responseType: 'text' });
  }
}

