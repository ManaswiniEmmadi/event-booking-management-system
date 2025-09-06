export interface Eventdata {
  eventId: number;
  eventName: string;
  eventDate: string; // LocalDate from backend will be string
  location: string;
  capacity: number;
  description: string;
  status: 'UPCOMING' | 'ONGOING' | 'COMPLETED' | 'CANCELLED';
  organizerName: string
  organizerEmail: string;
  organizerPhone: string;
}

export interface CreateEvent {
  eventName: string;
  eventDate: string; // 'YYYY-MM-DD'
  location: string;
  capacity: number;
  description?: string;
  // organizerId: number;
}