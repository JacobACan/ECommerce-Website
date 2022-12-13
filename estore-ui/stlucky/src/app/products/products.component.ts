import { Component, OnInit, TestabilityRegistry } from '@angular/core';
import { Product } from '../product';
import { AccountService } from '../services/account.service';
import { ProductService } from '../services/product.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css'],
})
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  user: string = '';

  constructor(
    private productService: ProductService,
    private accountService: AccountService,
    private router: Router
  ) {}

  getHeroes(): void {
    this.productService
      .getProducts()
      .subscribe((products) => this.updateAndSortProducts(products));
  }

  updateAndSortProducts(products: Product[]) {
    if (this.user != 'ADMIN') {
      products.forEach((product) => {
        if (product.quantity > 0) {
          this.products.push(product);
        }
      });
    } else {
      this.products = products;
    }
    this.products.sort((a, b) =>
      a.avgRating > b.avgRating ? -1 : a.avgRating < b.avgRating ? 1 : 0
    );
  }

  ngOnInit(): void {
    this.getHeroes();
    this.accountService.getUserName().subscribe((username) => {
      this.user = username;
    });
  }

  selectedProduct?: Product;
  onSelect(product: Product): void {
    this.selectedProduct = product;
  }

  navigate(id: number) {
    this.accountService.getUserName().subscribe((user) => {
      if (user != 'ADMIN') this.router.navigate([`/product-page/${id}`]);
      else this.router.navigate([`/products/admin/${id}`]);
    });
  }
}
