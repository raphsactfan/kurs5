import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';  // Импортируем компоненты
import { MainComponent } from './features/main/main.component';
import { AuthGuard } from '@app/core/guards/auth.guard';
import { CategoriesComponent } from './features/categories/categories.component';
import { AdminGuard } from '@app/core/guards/admin.guard';
import { UsersComponent } from './features/users/users.component';
import { AddressesComponent } from './features/addresses/addresses.component';
import { SuppliersComponent } from './features/suppliers/suppliers.component';
import { ProductsComponent } from './features/products/products.component';
import { ProfileComponent } from './features/profile/profile.component';
import { CatalogComponent } from './features/catalog/catalog.component';
import { CartComponent } from './features/cart/cart.component';
import { OrderComponent } from './features/order/order.component';
import { AdminOrdersComponent } from './features/admin-orders/admin-orders.component';
import { UserOrdersComponent } from './features/user-orders/user-orders.component';
import { StatisticsComponent } from './features/statistics/statistics.component';
import { ReportsComponent } from './features/reports/reports.component';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },  // Перенаправление на home
  { path: 'home', component: HomeComponent },  // Маршрут для HomeComponent
  { path: 'main', component: MainComponent, canActivate: [AuthGuard] },
  { path: 'categories', component: CategoriesComponent, canActivate: [AdminGuard] },
  { path: 'users', component: UsersComponent, canActivate: [AdminGuard] },
  { path: 'addresses', component: AddressesComponent, canActivate: [AdminGuard] },
  { path: 'suppliers', component: SuppliersComponent, canActivate: [AdminGuard] },
  { path: 'products', component: ProductsComponent, canActivate: [AdminGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'catalog', component: CatalogComponent, canActivate: [AuthGuard] },
  { path: 'cart', component: CartComponent, canActivate: [AuthGuard] },
  { path: 'order', component: OrderComponent, canActivate: [AuthGuard] },
  { path: 'orders', component: AdminOrdersComponent, canActivate: [AdminGuard] },
  { path: 'user-orders', component: UserOrdersComponent, canActivate: [AuthGuard] },
  { path: 'statistics', component: StatisticsComponent, canActivate: [AdminGuard] },
  { path: 'reports', component: ReportsComponent, canActivate: [AdminGuard] }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],  // Подключаем маршруты
  exports: [RouterModule]  // Экспортируем маршруты для использования в других модулях
})
export class AppRoutingModule {}
