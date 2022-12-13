import { Component, OnInit } from '@angular/core';
import { AccountService } from '../services/account.service';
import { Observable, Subject } from 'rxjs';

import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';

import { Product } from '../product';
import { ProductService } from '../services/product.service';

import { Router } from '@angular/router';

@Component({
  selector: 'app-product-search',
  templateUrl: './product-search.component.html',
  styleUrls: ['./product-search.component.css'],
})
export class ProductSearchComponent implements OnInit {
  products$!: Observable<Product[]>;
  public searchTerms = new Subject<string>();
  constructor(
    private productService: ProductService,
    private accountService: AccountService,
    private router: Router
  ) {}
  public user = '';

  // Push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }

  ngOnInit(): void {
    this.accountService.getUserName().subscribe((user) => (this.user = user));
    this.products$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: string) => this.productService.searchProducts(term))
    );
  }
  navigate(id: number) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.accountService.getUserName().subscribe((user) => {
      if (user != 'ADMIN') this.router.navigate([`/product-page/${id}`]);
      else this.router.navigate([`/products/admin/${id}`]);
    });
  }
}
