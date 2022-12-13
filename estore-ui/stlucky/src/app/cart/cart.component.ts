import { Component, OnInit } from '@angular/core';
import { AccountService } from '../services/account.service';
import { CartService } from '../services/cart.service';
import { Product } from '../interfaces/product';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit {
  public cart: Product[] = [];
  public userName: string = '';
  public price: number = 0;
  public checkoutSuccessful = false;
  public isUser: boolean = false;

  constructor(
    private accountService: AccountService,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    console.log('Initing Cart');

    this.accountService.getUserName().subscribe((username) => {
      console.log('In Cart Account Service');
      this.userName = username;
      this.cartService.getCart(username).subscribe((cart) => {
        this.cart = cart;
        let hasRemovedProducts = false;
        for (let i in this.cart) {
          if (this.cart[i].name == 'Unavailbale') hasRemovedProducts = true;
        }
        if (hasRemovedProducts) this.infoRemovedProducts();
      });
      this.cartService
        .getPrice(username)
        .subscribe((price) => (this.price = price));
      this.isUser = this.userName != 'ADMIN';
      if (
        this.userName == '' &&
        window.location.href == 'http://localhost:4200/cart'
      )
        this.infoLoginToUseCart();
    });
  }

  checkout() {
    this.cartService.checkout(this.userName).subscribe({
      next: (response) => {
        if (response.status > 199 && response.status < 300)
          this.checkoutSuccessful = true;
        this.infoCheckoutSuccess();
      },

      error: (e) => {
        if (e.status == 400) this.infoCheckoutFailure();
      },
    });
  }

  infoRemovedProducts() {
    this.cartService.cleanCart(this.userName).subscribe((cleaned) => {
      if (cleaned)
        this.cartService
          .getCart(this.userName)
          .subscribe((cart) => (this.cart = cart));
    });
    Swal.fire({
      title: '<strong><u>Items Removed</u></strong>',
      icon: 'info',
      html:
        'Items have been <b>removed</b>, from your cart by the store owners. <br><br>' +
        'Sorry for the inconveinience.',
      showCloseButton: true,
      showCancelButton: false,
      focusConfirm: false,
      confirmButtonText: '<i class="fa fa-thumbs-up"></i> I Understand',
      confirmButtonAriaLabel: 'Thumbs up, great!',
    });
  }

  infoLoginToUseCart() {
    Swal.fire({
      title: 'Login to use shopping cart.',
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: 'Login',
      denyButtonText: `Maybe Later`,
    }).then((result) => {
      if (result.isConfirmed) {
        window.location.href = 'http://localhost:4200/login';
      } else if (result.isDenied) {
        Swal.fire('You will only be able to browse products.', '', 'info').then(
          (result) => {
            window.location.href = 'http://localhost:4200';
          }
        );
      } else {
        window.location.href = 'http://localhost:4200';
      }
    });
  }

  infoCheckoutFailure() {
    Swal.fire({
      title: 'Checkout Error',
      text: 'Items in your cart are unavailable. Would you like to clean these unavailable items?',
      icon: 'error',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Clean Cart',
    }).then((result) => {
      if (result.isConfirmed) {
        this.cartService.cleanCart(this.userName).subscribe((cleaned) => {
          if (cleaned) {
            this.accountService.getUserName().subscribe((user) => {
              this.cartService
                .getCart(user)
                .subscribe((cart) => (this.cart = cart));
              this.cartService
                .getPrice(user)
                .subscribe((price) => (this.price = price));
            });
            Swal.fire('Cleaned!', 'Your cart has been cleaned', 'success');
          } else {
            Swal.fire('Error', 'Your cart has not been cleaned', 'error');
          }
        });
      }
    });
  }

  infoCheckoutSuccess() {
    let itemsCheckedOut = 0;
    for (let i in this.cart) {
      let product = this.cart[i];
      itemsCheckedOut += product.quantity;
    }
    Swal.fire(
      'Success!',
      `You have successfully checked out ${itemsCheckedOut} item(s) for $${this.price.toFixed(
        2
      )}
       Have A Lucky Day!`,
      'success'
    );
  }
}
