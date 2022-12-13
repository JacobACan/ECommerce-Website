import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CartComponent } from './cart/cart.component';
import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';
import { LoginComponent } from './login/login.component';
import { NavMenuComponent } from './nav-menu/nav-menu.component';
import { ProductPageComponent } from './product-page/product-page.component';
import { ProductAdminComponent } from './product-admin/product-admin.component';
import { NewProductComponent } from './new-product/new-product.component';
import { StlModelViewerModule } from 'angular-stl-model-viewer';
import { ProductsComponent } from './products/products.component';
import { ProductSearchComponent } from './product-search/product-search.component';
import { StarRatingComponent } from './star-rating/star-rating.component';
import { AddStarRatingComponent } from './add-star-rating/add-star-rating.component';

@NgModule({
  declarations: [
    AppComponent,
    CartComponent,
    HomeComponent,
    AboutComponent,
    LoginComponent,
    NavMenuComponent,
    ProductPageComponent,
    ProductAdminComponent,
    NewProductComponent,
    ProductsComponent,
    ProductSearchComponent,
    StarRatingComponent,
    AddStarRatingComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    BrowserModule,
    StlModelViewerModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
