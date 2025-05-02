import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';


@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  imports: [CommonModule],
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  login: string = '';
  role: string = '';

  constructor(private router: Router) {}

  ngOnInit() {
    this.login = localStorage.getItem('login') || '';
    this.role = localStorage.getItem('role') || '';
  }

  logout() {
    localStorage.removeItem('login');
    localStorage.removeItem('role');
    localStorage.removeItem('id');
    window.location.href = '/'; // редирект на стартовую
  }

  goToCategories() {
    this.router.navigate(['/categories']);
  }

  goToUsers() {
    this.router.navigate(['/users']);
  }

  goToAddresses() {
    this.router.navigate(['/addresses']);
  }

  goToSuppliers() {
    this.router.navigate(['/suppliers']);
  }

  goToProducts() {
    this.router.navigate(['/products']);
  }

  goToUserOrders() {
    this.router.navigate(['/user-orders']);
  }

  goToProfile() {
    this.router.navigate(['/profile']);
  }

  goToCatalog() {
    this.router.navigate(['/catalog']);
  }

  goToOrders() {
    this.router.navigate(['/orders']);
  }

  goToStatistics() {
    this.router.navigate(['/statistics']);
  }

  goToReports() {
    this.router.navigate(['/reports']);
  }

}
