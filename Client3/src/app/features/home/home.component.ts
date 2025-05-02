import { Component } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  activeForm: 'login' | 'register' = 'login';
  login = '';
  password = '';
  loginError = '';

  constructor(private authService: AuthService) {}

  onLogin() {
    this.authService.login({ login: this.login, password: this.password }).subscribe({
      next: () => {
        this.loginError = '';
        window.location.href = '/main';
      },
      error: () => {
        this.loginError = 'Неверный логин или пароль';
      }
    });
  }

  registerPasswordConfirm = '';
  registerError = '';

  onRegister() {
    this.registerError = '';

    if (this.login.length < 5 || this.password.length < 5) {
      this.registerError = 'Логин и пароль должны содержать не менее 5 символов';
      return;
    }

    if (this.login.includes(' ') || this.password.includes(' ')) {
      this.registerError = 'Логин и пароль не должны содержать пробелов';
      return;
    }

    if (this.password !== this.registerPasswordConfirm) {
      this.registerError = 'Пароли не совпадают';
      return;
    }

    this.authService.register({ login: this.login, password: this.password }).subscribe({
      next: (response: string) => {
        if (response === 'ok') {
          this.registerError = '';
          alert('Регистрация прошла успешно');

          // Очищаем поля
          this.login = '';
          this.password = '';
          this.registerPasswordConfirm = '';

          this.activeForm = 'login';
        } else if (response === 'exists') {
          this.registerError = 'Пользователь уже существует';
        } else {
          this.registerError = 'Ошибка регистрации';
        }
      },
      error: () => {
        this.registerError = 'Ошибка сервера';
      }
    });
  }

}
