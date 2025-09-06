export interface BookingRequest {
  userName: string;
  mobile: string;
  email: string;
  seatTypeId: number; 
  numberOfSeats: number;
  eventId: number; 
  status: string;
}


export interface BookingResponse {
  bookingId: number;
  userName: string;
  mobile: string;
  email: string;
  seatType: string; 
  numberOfSeats: number;
  eventId: number;
  status: string;

  price: number; 

  eventName: string; 
  eventDate: string; 
  location: string; 
  
}


export interface UpdateBookingDTO {
  userName?: string;
  mobile?: string;
  email?: string;
  seatType?: string;
  numberOfSeats?: number;
  eventId?: string;
  status?: string;
}

