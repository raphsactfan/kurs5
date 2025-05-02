import { Address } from './address.model';
import { User } from './user.model';
import { OrderProduct } from './order-product.model';
import { OrderDetails } from './order-details.model';

export interface OrderTable {
    orderId: number;
    quantity: number;
    totalAmount: number;
    address: Address;
    user: User;
    orderProducts: OrderProduct[];
    orderDetails: OrderDetails;
}
