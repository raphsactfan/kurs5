import { Address } from './address.model';

export interface Supplier {
  id: number;
  name: string;
  representative: string;
  phone: string;
  address: Address;
}
