import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SupplierService, Supplier, Address } from '../../core/services/supplier.service';

@Component({
  selector: 'app-suppliers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './suppliers.component.html',
  styleUrls: ['./suppliers.component.css']
})
export class SuppliersComponent implements OnInit {
  suppliers: Supplier[] = [];
  search = '';
  sortBy = '';
  newSupplier: Partial<Supplier> = {
    name: '',
    representative: '',
    phone: '',
    address: { country: '', city: '', street: '' }
  };
  editingSupplier: Supplier | null = null;

  constructor(private supplierService: SupplierService, private router: Router) {}

  ngOnInit(): void {
    this.loadSuppliers();
  }

  loadSuppliers() {
    this.supplierService.getAll(this.search, this.sortBy).subscribe(data => {
      this.suppliers = data;
    });
  }

  addSupplier() {
    const s = this.newSupplier;
    const a = s.address!;

    if (!s.name?.trim() || !s.representative?.trim() || !s.phone?.trim()
      || !a.country?.trim() || !a.city?.trim() || !a.street?.trim()) {
      alert('Все поля должны быть заполнены');
      return;
    }

    this.supplierService.add(s).subscribe({
      next: () => {
        this.newSupplier = {
          name: '',
          representative: '',
          phone: '',
          address: { country: '', city: '', street: '' }
        };
        this.loadSuppliers();
      },
      error: err => {
        alert(err.error?.message || 'Ошибка при создании поставщика');
      }
    });
  }

  editSupplier(supplier: Supplier) {
    this.editingSupplier = JSON.parse(JSON.stringify(supplier));
  }

  saveSupplier() {
    if (!this.editingSupplier) return;

    const s = this.editingSupplier;
    const a = s.address;

    if (!s.name?.trim() || !s.representative?.trim() || !s.phone?.trim()
      || !a.country?.trim() || !a.city?.trim() || !a.street?.trim()) {
      alert('Все поля должны быть заполнены');
      return;
    }

    this.supplierService.update(s).subscribe({
      next: () => {
        this.editingSupplier = null;
        this.loadSuppliers();
      },
      error: err => {
        alert(err.error?.message || 'Ошибка при обновлении поставщика');
      }
    });
  }

  deleteSupplier(id: number) {
    if (confirm('Удалить поставщика?')) {
      this.supplierService.delete(id).subscribe(() => {
        this.loadSuppliers();
      });
    }
  }

  cancelEdit() {
    this.editingSupplier = null;
  }

  goToMain() {
    this.router.navigate(['/main']);
  }
}
