import { Component, OnInit, Input, ViewChild, ElementRef } from '@angular/core';
import Swal from 'sweetalert2';
import { Product } from '../product';
import { AccountService } from '../services/account.service';
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-add-star-rating',
  templateUrl: './add-star-rating.component.html',
  styleUrls: ['./add-star-rating.component.css'],
})
export class AddStarRatingComponent implements OnInit {
  rating: number;
  frozen: boolean;
  userName: string;
  @Input() product: Product;
  @ViewChild('StarContainer') ul!: ElementRef<HTMLUListElement>;
  @ViewChild('StarDiv') starDiv!: ElementRef<HTMLDivElement>;
  @ViewChild('YourRating') yourRating!: ElementRef<HTMLDivElement>;
  maxStars: number = 5;
  constructor(
    private account: AccountService,
    private productService: ProductService
  ) {
    this.rating = 0;
    this.frozen = false;
    this.userName = '';
    this.product = {
      name: '',
      quantity: 0,
      price: 0.0,
      id: -1,
      imageUrlList: [],
      sections: [],
      colors: [],
      size: 100,
      material: '',
      expediency: 0,
      gift: false,
      ratingList: [],
      avgRating: 0.0,
    };
  }

  ngOnInit(): void {
    this.account.getUserName().subscribe((user) => (this.userName = user));
  }
  ngAfterViewInit(): void {
    let allStars = this.ul.nativeElement.childNodes;
    let i = 0;
    this.ul.nativeElement.childNodes.forEach((child) => {
      let thisStarRating = new Number(i);

      child.addEventListener('mouseenter', (e) => {
        if (!child.firstChild?.isEqualNode(this.fullStar())) {
          let u = 0;
          allStars.forEach((star) => {
            if (u <= thisStarRating) {
              star.childNodes.forEach((img) => {
                if (!this.frozen) img.replaceWith(this.fullStar());
              });
            }
            u++;
          });
        } else {
          let u = 0;
          allStars.forEach((star) => {
            if (u > thisStarRating) {
              star.childNodes.forEach((img) => {
                if (!this.frozen) img.replaceWith(this.emptyStar());
              });
            }
            u++;
          });
        }
        if (!this.frozen) this.rating = thisStarRating.valueOf() + 1;
      });

      i++;
    });
  }
  emptyStar(): HTMLImageElement {
    let emptyStar = document.createElement('img');
    emptyStar.src = 'assets/media/images/ratings/star_clear.png';
    emptyStar.style.width = '1.5vw';
    return emptyStar;
  }
  fullStar(): HTMLImageElement {
    let fullStar = document.createElement('img');
    fullStar.src = 'assets/media/images/ratings/star_full.png';
    fullStar.style.width = '1.5vw';
    return fullStar;
  }
  toggleFreezeButton() {
    if (this.frozen) this.frozen = false;
    else {
      this.onAddRating();
      this.frozen = true;
    }

    this.yourRating.nativeElement.classList.toggle('hidden');
    this.starDiv.nativeElement.classList.toggle('active');
  }
  onAddRating() {
    if (this.rating > 5 || this.rating < 1) {
      this.infoInvalidInput('value should be between 1 and 5');
    } else if (!this.userName) {
      this.infoLoginToRate();
    } else {
      this.product.ratingList.push(this.rating);
      this.productService.updateProduct(this.product).subscribe((product) => {
        Swal.fire({
          text: 'Thankyou for your feedback!',
          icon: 'success',
        });
        this.product = product;
        this.product.imageUrlList[1] = product.imageUrlList[1];
      });
      Swal.fire('rating added');
    }
  }
  infoLoginToRate() {
    Swal.fire({
      title: `Login to rate items`,
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: 'Login',
      denyButtonText: `Maybe Later`,
    }).then((result) => {
      if (result.isConfirmed) {
        window.location.href = 'http://localhost:4200/login';
      } else {
        this.toggleFreezeButton();
      }
    });
  }

  infoInvalidInput(action: String) {
    Swal.fire({ title: `Invalid input: ${action}` });
  }
}
