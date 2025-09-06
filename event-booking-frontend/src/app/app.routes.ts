import { Routes } from '@angular/router';
import { BookingFormComponent } from './components/booking-form/booking-form';
import { LoginComponent } from './components/login/login';
import { SignupComponent } from './components/signup/signup';
import { UserDashboardComponent } from './components/user-dashboard/user-dashboard';
import { OrganizerDashboardComponent } from './components/organizer-dashboard/organizer-dashboard';
import { UserProfileComponent } from './components/user-profile/user-profile';
import { authGuard  } from './guards/auth-guard';
import { roleGuard } from './guards/role-guard';
import { Addevent } from './components/addevent/addevent';
import { EventComponent } from './components/event-component/event-component';
import { MyEvents } from './components/my-events/my-events';
import { AddSeatsComponent } from './components/add-seats/add-seats';
import { Payment } from './components/payment/payment';
import { BookingConfirmationComponent } from './components/booking-confirmation/booking-confirmation';
import { EventBookingsComponent } from './components/event-bookings/event-bookings';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Public routes
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },

  // Protected routes
  { path: 'user-dashboard', component: UserDashboardComponent, canActivate: [authGuard, roleGuard('user')] },
  { path: 'organizer-dashboard', component: OrganizerDashboardComponent, canActivate: [authGuard, roleGuard('organizer')] },
  { path: 'book', component: BookingFormComponent, canActivate: [authGuard] },
  {
    path: 'bookings',
    loadComponent: () =>
      import('./components/booking-list/booking-list').then(m => m.BookingListComponent),
    canActivate: [authGuard]
  },
  { path: 'profile', component: UserProfileComponent, canActivate: [authGuard]  },
 

  { path: 'viewEvent', component: EventComponent, canActivate: [authGuard] },

  {path:"myevents",component:MyEvents, canActivate: [authGuard] },

  {
    path: 'organizer-dashboard',
    component: OrganizerDashboardComponent,
    canActivate: [authGuard, roleGuard('organizer')],
    children: [
      { path: 'viewevent', component: EventComponent },
      {path:"myevents",component:MyEvents},
      { path: 'addevent', component: Addevent },
      { path: 'profile', component: UserProfileComponent }
    ]
  },

  { 
    path: 'seats/:eventId', 
    loadComponent: () => import('./components/seats/seats').then(m => m.SeatComponent),
    canActivate: [authGuard]
  },

  { path: 'add-seats/:eventId', component: AddSeatsComponent, canActivate: [authGuard]  },
    
  {path: 'payment', component: Payment, canActivate: [authGuard]   },

  { path: 'booking-confirmation', component: BookingConfirmationComponent, canActivate: [authGuard]  },
    
  {
    path: 'event-bookings/:eventId',
    component: EventBookingsComponent,
    canActivate: [authGuard] 
  },


  // Wildcard route
  { path: '**', redirectTo: 'login' }
];
