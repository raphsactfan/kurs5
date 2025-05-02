import { Supplier } from './supplier.model';
import { Category } from './category.model';

export interface Product {
  id: number;
  name: string;
  quantity: number;
  price: number;
  category: Category;
  supplier: Supplier;
}
