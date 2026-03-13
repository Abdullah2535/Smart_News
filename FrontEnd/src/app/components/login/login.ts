import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})
export class LoginComponent {
  loginForm: FormGroup;
  isLoading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private userService: UserService,
  ) {
    this.loginForm = this.fb.group({
      userName: ['', [Validators.required]],
      password: ['', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';

      this.authService.login(this.loginForm.value).subscribe({
        next: (response) => {
          // Store your token (e.g., in localStorage or a state management solution)
          localStorage.setItem('token', response.token);
          this.isLoading = false;

          this.userService.setCurrentUser({ username: this.loginForm.value.userName });

          this.router.navigate(['/preferences']); // Redirect to the main news feed
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'اسم المستخدم او كلمة المرور غير صحيحة'; // Invalid email or password
          console.error('Login failed', err);
        },
      });
    }
  }
}
