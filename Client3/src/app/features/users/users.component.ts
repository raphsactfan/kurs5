import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { UserService, User } from '../../core/services/user.service';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  users: User[] = [];
  search = '';
  sortBy = '';
  newUser: Partial<User> = { login: '', password: '', role: 'user' };
  editingUser: User | null = null;

  currentLogin: string = localStorage.getItem('login') || '';

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getAll(this.search, this.sortBy).subscribe(data => {
      this.users = data;
    });
  }

  addUser() {
    const login = this.newUser.login?.trim() || '';
    const password = this.newUser.password?.trim() || '';

    if (login.length < 5 || password.length < 5) {
      alert('Логин и пароль должны содержать не менее 5 символов');
      return;
    }

    if (login.includes(' ') || password.includes(' ')) {
      alert('Логин и пароль не должны содержать пробелов');
      return;
    }

    this.userService.add(this.newUser).subscribe({
      next: () => {
        this.newUser = { login: '', password: '', role: 'user' };
        this.loadUsers();
      },
      error: err => {
        let errorMessage = 'Данный логин уже занят';

        if (typeof err.error === 'string') {
          errorMessage = err.error;
        } else if (err.error?.message) {
          errorMessage = err.error.message;
        }

        alert(errorMessage);
      }
    });
  }

  editUser(user: User) {
    this.editingUser = { ...user };
  }

  saveUser() {
    if (!this.editingUser) return;

    const login = this.editingUser.login?.trim() || '';
    const password = this.editingUser.password?.trim() || '';

    if (login.length < 5 || password.length < 5) {
      alert('Логин и пароль должны содержать не менее 5 символов');
      return;
    }

    if (login.includes(' ') || password.includes(' ')) {
      alert('Логин и пароль не должны содержать пробелов');
      return;
    }

    this.userService.update(this.editingUser).subscribe({
      next: () => {
        this.editingUser = null;
        this.loadUsers();
      },
      error: err => {
        let errorMessage = 'Данный логин занят';

        if (typeof err.error === 'string') {
          errorMessage = err.error;
        } else if (err.error?.message) {
          errorMessage = err.error.message;
        }

        alert(errorMessage);
      }
    });
  }

  deleteUser(id: number) {
    const user = this.users.find(u => u.id === id);
    if (!user) return;

    if (user.login === this.currentLogin) {
      alert('Вы не можете удалить самого себя');
      return;
    }

    if (confirm(`Удалить пользователя "${user.login}"?`)) {
      this.userService.delete(id).subscribe(() => {
        this.loadUsers();
      });
    }
  }

  cancelEdit() {
    this.editingUser = null;
  }

  goToMain() {
    this.router.navigate(['/main']);
  }
}
