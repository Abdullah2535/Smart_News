import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { RegisterPayload } from '../../models/RegisterPayload';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './signup.html',
  styleUrls: ['./signup.css'],
})
export class SignupComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);
  private authService = inject(AuthService);

  // Define the form and its validation rules
  signupForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    userName: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(6)]],
  });

  onSubmit() {
    if (this.signupForm.valid) {
      // Use getRawValue() to get strongly typed data that matches the interface
      const userData: RegisterPayload = this.signupForm.getRawValue() as unknown as RegisterPayload;

      // Call the register method from the service
      this.authService.register(userData).subscribe({
        next: (response) => {
          console.log('User saved successfully!', response);
          // Route to the login page
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.error('Error saving user', err);
          // Here you could trigger a UI alert or message
        },
      });
    } else {
      this.signupForm.markAllAsTouched();
    }
  }
}
