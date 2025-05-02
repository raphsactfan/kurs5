import { Product } from './product.model';

export interface OrderProduct {
    orderId: number;
    productId: number;
    product: Product;
}
