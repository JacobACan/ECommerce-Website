import { Component, OnInit } from '@angular/core';
import { AccountService } from '../services/account.service';
import { User } from '../user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  LoginStatus: boolean | undefined;

  user: User;

  constructor(private router: Router, private account: AccountService) {
    this.LoginStatus = false;
    this.user = { name: '', password: '' };
  }

  ngOnInit(): void {
    this.account.getLoginStatus().subscribe((LoginStatus) => {
      this.LoginStatus = LoginStatus;
    });
  }

  onLogin() {
    this.account.login(this.user.name, this.user.password);
  }

  onSignup() {
    this.account.signup(this.user.name, this.user.password);
  }
}
