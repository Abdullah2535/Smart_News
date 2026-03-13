// auth.guard.ts
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { UserService } from '../services/user.service';

export const authGuard: CanActivateFn = (route, state) => {
  const userService = inject(UserService);
  const router = inject(Router);

  // Check the computed signal value
  if (userService.isAuthenticated()) {
    return true;
  }

  // If not authenticated, redirect to the login page safely
  return router.createUrlTree(['/login']);
};
