import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Product } from '../services/product';
import { ProductService } from '../services/product.service';
import { Location } from '@angular/common';
import { AccountService } from '../services/account.service';

@Component({
  selector: 'app-product-admin',
  templateUrl: './new-product.component.html',
  styleUrls: ['./new-product.component.css'],
})
export class NewProductComponent implements OnInit {
  product: Product | undefined;
  isAdmin: boolean | undefined;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private location: Location,
    private user: AccountService
  ) {}

  ngOnInit(): void {
    this.product = {
      name: 'New Product',
      quantity: 0,
      price: 0.0,
      id: -1,
      imageUrlList: [],
      ratingList: [],
      avgRating: 0.0,
      sections: [],
      colors: [],
      size: 0,
      material: '',
      expediency: 0,
      gift: false,
    };
    this.getUser();
    this.product.imageUrlList[1] = "./assets/media/stlfiles/"
  }

  getUser(): void {
    this.user
      .getUserName()
      .subscribe((username) => (this.isAdmin = username == 'ADMIN'));
    // this.isAdmin = true;
  }

  goBack(): void {
    this.location.back();
  }

  createProduct(): void {
    if (this.product) {
      this.productService.addProduct(this.product).subscribe(() => {
        this.goBack();
      });
    }
  }


  removeSection(index : number): void {
    this.product?.sections.splice(index)
    this.product?.colors.splice(index);
  }

  addSection(): void {
    this.product?.sections.push('');
    this.product?.colors.push('');
  }

  trackByFn(index: any, item: any) {
    return index;
 }
 
}
