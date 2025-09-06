// SeatType model
export interface SeatType {
  id: number;
  seatType: string;
  price: number;
  totalSeats: number;
  availableSeats: number;
  eventName: string ;
  bookedSeats:number;
}

// Event model
export interface SeatEvent {
  id: number;
  name: string;
  location: string;
  eventDate: string;
  eventTime: string;
  seatTypes: SeatType[];
}
