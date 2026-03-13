// app.routes.ts
import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { authGuard } from './guards/auth.guard';
import { SignupComponent } from './components/signup/signup';
import { PreferencesComponent } from './components/preferences/preferences';

// Import your standalone components
// import { DashboardComponent } from './dashboard/dashboard.component';
// import { ProfileComponent } from './profile/profile.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'signup',
    component: SignupComponent,
  },
  {
    path: 'preferences',
    component: PreferencesComponent,
    canActivate: [authGuard], // Protect this route
  },
  // {
  //   path: '',
  //   redirectTo: '/dashboard',
  //   pathMatch: 'full'
  // }
];
