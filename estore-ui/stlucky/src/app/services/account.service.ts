import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, Subject } from 'rxjs';
import { User } from '../user';
import { Router } from '@angular/router';

import { HttpClient } from '@angular/common/http';
import { __param } from 'tslib';
import Swal from 'sweetalert2';

@Injectable({ providedIn: 'root' })
export class AccountService {
  private usersUrl = 'http://localhost:8080/users';
  $LoginStatus: Subject<boolean>;
  $UserName: Subject<string>;
  $UserRole: Subject<string>;
  newObs: Observable<User> | undefined;

  constructor(private http: HttpClient, private router: Router) {
    this.$LoginStatus = new Subject();
    this.$UserName = new BehaviorSubject('');
    this.$UserRole = new Subject();
  }

  login(name: string, password: string) {
    const url = this.usersUrl + '/user/' + name + '/';
    this.http.get<User>(url, { params: { password } }).subscribe({
      next: (data: User) => {
        console.log('subscribe runs again');
        console.log('data.name=' + data.name);

        if (data.name != '') {
          this.$LoginStatus.next(true);
          this.$UserName.next(data.name);
          this.infoLoggedIn();
          if (data.name.toLowerCase() == 'admin') {
            this.$UserRole.next('admin');
          } else {
            this.$UserRole.next('customer');
          }
        }
        this.router.navigateByUrl('');
      },

      error: (err) => {
        this.infoUnsuccessfulLogin();
      },
    });
  }

  signup(name: string, pw: string) {
    const url = this.usersUrl + '/?name=' + name + '&password=' + pw;
    this.http.put<User>(url, '').subscribe({
      next: (User) => {
        this.login(name, pw);
      },
      error: (e) => {
        if (e.status == 406) {
          if (name == '' || pw == '') {
            this.infoRegistrationQualifications();
          } else {
            this.infoUserExists();
          }
        }
      },
    });
  }

  logout() {
    this.$LoginStatus.next(false);
    this.$UserName.next('');
    this.$UserRole.next('');

    console.log('Logout done:  username=' + this.$UserName);
  }
  getLoginStatus() {
    return this.$LoginStatus.asObservable();
  }
  getUserName() {
    return this.$UserName;
  }
  getUserRole() {
    return this.$UserRole.asObservable();
  }

  infoLoggedIn() {
    this.$UserName.subscribe((username) => {
      if (username != '') {
        Swal.fire({
          title: `Welcome To STLUCKY ${username}!`,
          showConfirmButton: false,
          timer: 2000,
        });
      }
    });
  }
  infoUnsuccessfulLogin() {
    Swal.fire({
      title: 'Login Error',
      text: 'Your Username or Password are Incorrect',
      icon: 'error',
      showCancelButton: false,
      confirmButtonColor: '#3085d6',
      confirmButtonText: 'OK',
    });
  }
  infoUserExists() {
    Swal.fire({
      title: 'Registration Error',
      text: 'User already exists.',
      icon: 'error',
      showCancelButton: false,
      confirmButtonColor: '#3085d6',
      confirmButtonText: 'OK',
    });
  }
  infoRegistrationQualifications() {
    Swal.fire({
      text: 'Username and Password must be longer than 0 characters long.',
      icon: 'info',
    });
  }
}
