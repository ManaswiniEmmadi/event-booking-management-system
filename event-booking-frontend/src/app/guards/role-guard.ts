import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const roleGuard = (expectedRole: string): CanActivateFn => {
  return () => {
    const router = inject(Router);
    const token = localStorage.getItem('authToken');
    const role = localStorage.getItem('userRole')?.toLowerCase();

    if (token && role === expectedRole.toLowerCase()) {
      return true;
    } else {
      // Redirect them to their correct dashboard if logged in but wrong role
      if (token && role) {
        if (role === 'user') {
          router.navigate(['/user-dashboard']);
        } else if (role === 'organizer') {
          router.navigate(['/organizer-dashboard']);
        }
      } else {
        router.navigate(['/login']);
      }
      return false;
    }
  };
};
