// auth.service.ts
import { Injectable, signal, computed } from '@angular/core';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  // Private writable signal holding the user state
  private currentUserSignal = signal<User | null>(null);

  // Public readonly signals for components to consume
  readonly currentUser = this.currentUserSignal.asReadonly();

  // Computed signal that automatically updates when currentUserSignal changes
  readonly isAuthenticated = computed(() => this.currentUserSignal() !== null);

  // Method to set user data (call this after a successful backend login)
  setCurrentUser(user: User) {
    this.currentUserSignal.set(user);
  }

  // Method to clear user data
  // logout() {
  //   this.currentUserSignal.set(null);
  // }
}
