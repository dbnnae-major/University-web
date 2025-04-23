import { Component, inject } from '@angular/core';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTooltipModule } from '@angular/material/tooltip';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserService } from '../user.service';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [CommonModule, FormsModule, MatTooltipModule],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css'],
})
export class WelcomeComponent {
  private http: HttpClient;
  private router: Router;
  private userService: UserService;

  notifications: { message: string; fadeOut: boolean }[] = [];

  constructor() {
    this.http = inject(HttpClient);
    this.router = inject(Router);
    this.userService = inject(UserService);
  }

  addNotification(message: string): void {
    this.notifications.push({ message, fadeOut: false });
    setTimeout(() => {
      this.notifications[this.notifications.length - 1].fadeOut = true;
    }, 5000);
    setTimeout(() => {
      this.notifications.shift();
    }, 6000);
  }

  // Переменные для данных формы логина
  login_log_form: string = '';
  password_log_form: string = '';

  // Переменные для данных формы регистрации
  login_reg_form: string = '';
  password_reg_form: string = '';
  password_confirm_reg_form: string = '';
  passwordMismatchError: boolean = false;

  activeForm: 'login' | 'register' | null = null;

  showForm(type: 'login' | 'register'): void {
    this.activeForm = type;
    this.login_log_form = '';
    this.password_log_form = '';

    this.login_reg_form = '';
    this.password_reg_form = '';
    this.password_confirm_reg_form = '';

    this.passwordMismatchError = false;
  }

  goToHome(username: string): void {
    this.userService.setUsername(username);
    this.router.navigate(['/home']);
  }

  login(): void {
    const loginData = {
      username: this.login_log_form,
      password: this.password_log_form,
    };

    const url = `http://127.0.0.1:25664/controller?username=${this.login_log_form}&password=${this.password_log_form}`;

    this.http
      .get(url, {
        headers: {
          Authorization: 'Bearer token',
          'X-Request-Type': 'Authorization',
        },
        withCredentials: true,
      })
      .subscribe({
        next: (response) => {
          console.log('Login successful', response);
          this.goToHome(this.login_log_form);
        },
        error: (error) => {
          if (error.status === 400) {
            console.error('Bad request: Missing username or password', error);
            this.addNotification('Ошибка: Пропущен логин или пароль');
          } else if (error.status === 401) {
            console.error('Unauthorized: Invalid username or password', error);
            this.addNotification('Ошибка: Неверный логин или пароль');
          } else {
            console.error('Other error occurred', error);
            this.addNotification('Произошла ошибка');
          }
        },
        complete: () => {
          console.log('Login request completed');
        },
      });
  }

  register(): void {
    if (this.password_reg_form !== this.password_confirm_reg_form) {
      this.passwordMismatchError = true;
      this.addNotification('Пароли не совпадают');
      return;
    }

    this.passwordMismatchError = false;

    const registrationData = {
      username: this.login_reg_form,
      password: this.password_reg_form,
    };

    this.http
      .post('http://127.0.0.1:25664/controller', registrationData, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: 'Bearer token',
          'X-Request-Type': 'Authorization',
        },
        withCredentials: true,
      })
      .subscribe({
        next: (response) => {
          console.log('Registration successful', response);
          this.addNotification(
            'Регистрация успешна, войдите под своим аккаунтом.'
          );
        },
        error: (error) => {
          if (error.status === 400) {
            console.error('Bad request: Missing username or password', error);
            this.addNotification('Ошибка: Пропущен логин или пароль');
          } else if (error.status === 409) {
            console.error('Conflict: Username is occupied', error);
            this.addNotification('Ошибка: Имя пользователя занято');
          } else {
            console.error('Other error occurred', error);
            this.addNotification('Произошла ошибка');
          }
        },
        complete: () => {
          console.log('Registration request completed');
        },
      });
  }
}
