import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AboutComponent } from './about/about.component';
import { CartComponent } from './cart/cart.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { ProductPageComponent } from './product-page/product-page.component';
import { ProductAdminComponent } from './product-admin/product-admin.component';
import { NewProductComponent } from './new-product/new-product.component';
import { ProductSearchComponent } from './product-search/product-search.component';

const routes: Routes = [
  { path: 'cart', component: CartComponent },
  { path: 'about', component: AboutComponent },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'product-page/:id', component: ProductPageComponent},
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'products/admin/:id', component: ProductAdminComponent},
  { path: 'products/admin', component: NewProductComponent},
  { path: 'products/search', component: ProductSearchComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
