import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProfileService, User } from '../../core/services/profile.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {
  id: number = parseInt(localStorage.getItem('id') || '0');
  login: string = localStorage.getItem('login') || '';
  password: string = '';
  confirmPassword: string = '';

  constructor(private profileService: ProfileService, private router: Router) {}

  updateProfile() {
    const trimmedLogin = this.login.trim();
    const trimmedPassword = this.password.trim();
    const trimmedConfirm = this.confirmPassword.trim();

    if (trimmedLogin.length < 5 || trimmedPassword.length < 5) {
      alert('Логин и пароль должны быть не менее 5 символов');
      return;
    }

    if (trimmedPassword !== trimmedConfirm) {
      alert('Пароли не совпадают');
      return;
    }

    const updatedUser: User = {
      id: this.id,
      login: trimmedLogin,
      password: trimmedPassword,
      role: 'user'
    };

    this.profileService.updateUser(updatedUser).subscribe({
      next: () => {
        localStorage.setItem('login', trimmedLogin);
        alert('Данные успешно обновлены');
        this.router.navigate(['/main']);
      },
      error: err => {
        let errorMessage = 'Ошибка при обновлении профиля';

        if (typeof err.error === 'string') {
          errorMessage = err.error;
        } else if (err.error?.message) {
          errorMessage = err.error.message;
        }

        alert(errorMessage);
      }
    });
  }

  goToMain() {
    this.router.navigate(['/main']);
  }
}
