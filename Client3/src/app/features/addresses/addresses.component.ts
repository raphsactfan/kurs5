import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AddressService, Address } from '../../core/services/address.service';

@Component({
  selector: 'app-addresses',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './addresses.component.html',
  styleUrls: ['./addresses.component.css']
})
export class AddressesComponent implements OnInit {
  addresses: Address[] = [];
  search = '';
  sortBy = '';

  constructor(private addressService: AddressService, private router: Router) {}

  ngOnInit(): void {
    this.loadAddresses();
  }

  loadAddresses() {
    this.addressService.getAll(this.search, this.sortBy).subscribe(data => {
      this.addresses = data;
    });
  }

  goToMain() {
    this.router.navigate(['/main']);
  }
}
