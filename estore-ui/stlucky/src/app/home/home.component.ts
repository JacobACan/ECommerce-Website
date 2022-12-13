import { Component, HostListener } from '@angular/core';
import { OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
  @HostListener('window:scroll', ['$event'])
  onWindowScroll() {
    let element = document.querySelector('.info-backing') as HTMLElement;
    let logo = document.querySelector('.home-logo') as HTMLElement;
    let description = document.querySelector('.description') as HTMLElement;
    if (
      window.pageYOffset >
      element.clientHeight - element.clientHeight * (95 / 100)
    ) {
      logo.classList.add('disappear');
      description.classList.add('disappear');
    } else {
      logo.classList.remove('disappear');
      description.classList.remove('disappear');
    }
    if (
      window.pageYOffset >
      element.clientHeight - element.clientHeight * (70 / 100)
    ) {
      logo.classList.add('remove');
      description.classList.add('remove');
    } else {
      logo.classList.remove('remove');
      description.classList.remove('remove');
    }
  }
}
