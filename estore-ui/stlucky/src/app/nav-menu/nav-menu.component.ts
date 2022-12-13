import { Component, HostListener, OnInit } from '@angular/core';
import { AccountService } from '../services/account.service';
import { CartService } from '../services/cart.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nav-menu',
  templateUrl: './nav-menu.component.html',
  styleUrls: ['./nav-menu.component.css'],
})
export class NavMenuComponent implements OnInit {
  LoginStatus: boolean;
  UserName: string;
  UserRole: string;
  amountInCart: number;

  constructor(
    private account: AccountService,
    private router: Router,
    private cart: CartService
  ) {
    this.LoginStatus = false;
    this.UserName = '';
    this.UserRole = '';
    this.amountInCart = 0;
  }

  ngOnInit(): void {
    this.account
      .getLoginStatus()
      .subscribe((LoginStatus) => (this.LoginStatus = LoginStatus));
    this.account.getUserName().subscribe((UserName) => {
      this.UserName = UserName;
    });
    this.account
      .getUserRole()
      .subscribe((UserRole) => (this.UserRole = UserRole));
    this.updateCartIcon();
  }

  onLogout() {
    this.router.navigateByUrl('/').then(() => this.account.logout());
    this.amountInCart = 0;
  }

  updateCartIcon() {
    this.cart.onCartChanged().subscribe((a) => {
      console.log('CHANGE DETECTED');
      this.account.getUserName().subscribe((UserName) => {
        new Promise((f) => setTimeout(f, 200)).then((f) => {
          this.cart.getCart(UserName).subscribe((cart) => {
            let amount = 0;
            cart.forEach((product) => {
              amount += product.quantity;
            });
            this.amountInCart = amount;
          });
        });
      });
    });
  }

  @HostListener('window:scroll', ['$event'])
  onWindowScroll() {
    let element = document.querySelector('.nav-backing') as HTMLElement;
    let nav_icons = document.querySelectorAll('.nav-icon');
    let nav_buttons = document.querySelectorAll('.nav-button');
    let logo = document.querySelector('.logo') as HTMLElement;
    if (
      window.pageYOffset >
      element.clientHeight - element.clientHeight * (70 / 100)
    ) {
      element.classList.add('navbar-inverse');
      logo.classList.add('logo-inverse');
      nav_icons.forEach((icon) => {
        icon.classList.add('nav-icon-inverse');
      });
      nav_buttons.forEach((icon) => {
        icon.classList.add('nav-button-inverse');
      });
    } else {
      element.classList.remove('navbar-inverse');
      logo.classList.remove('logo-inverse');
      nav_icons.forEach((icon) => {
        icon.classList.remove('nav-icon-inverse');
      });
      nav_buttons.forEach((icon) => {
        icon.classList.remove('nav-button-inverse');
      });
    }
  }
}
