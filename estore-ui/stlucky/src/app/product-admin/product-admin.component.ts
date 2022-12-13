import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Product } from '../services/product';
import { ProductService } from '../services/product.service';
import { Location } from '@angular/common';
import { AccountService } from '../services/account.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-product-admin',
  templateUrl: './product-admin.component.html',
  styleUrls: ['./product-admin.component.css'],
})
export class ProductAdminComponent implements OnInit {
  product: Product | undefined;
  isAdmin: boolean | undefined;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private location: Location,
    private user: AccountService
  ) {}

  ngOnInit(): void {
    this.getProduct();
    this.getUser();
  }

  getProduct(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.productService
      .getProduct(id)
      .subscribe((product) => (this.product = product));
  }

  getUser(): void {
    this.user
      .getUserName()
      .subscribe((username) => (this.isAdmin = username == 'ADMIN'));
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.product) {
      this.productService.updateProduct(this.product).subscribe(() => {
        this.infoProductUpdated();
        this.goBack();
      });
    }
  }

  deleteProduct(): void {
    if (this.product) {
      this.productService
        .deleteProduct(this.product.id)
        .subscribe(() => this.goBack());
    }
  }

  removeSection(index: number): void {
    this.product?.sections.splice(index);
    this.product?.colors.splice(index);
  }

  addSection(): void {
    this.product?.sections.push('');
    this.product?.colors.push('');
  }

  trackByFn(index: any, item: any) {
    return index;
  }
  private infoProductUpdated() {
    Swal.fire({
      icon: 'success',
      title: 'Product Updated',
    });
  }
}
