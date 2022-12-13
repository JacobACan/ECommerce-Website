import {
  Component,
  OnInit,
  Input,
  ViewChild,
  ElementRef,
  AfterViewInit,
} from '@angular/core';

@Component({
  selector: 'app-star-rating',
  templateUrl: './star-rating.component.html',
  styleUrls: ['./star-rating.component.css'],
})
export class StarRatingComponent implements OnInit, AfterViewInit {
  @Input() rating: number;
  @ViewChild('StarContainer') ul!: ElementRef<HTMLUListElement>;
  maxStars: number = 5;
  constructor() {
    this.rating = 0;
  }

  ngOnInit(): void {}
  ngAfterViewInit(): void {
    let i = 0;
    while (i < this.maxStars) {
      if (Math.round(this.rating) > 0) {
        let li = document.createElement('li');
        li.appendChild(this.fullStar());
        this.ul.nativeElement.appendChild(li);
        this.rating -= 1;
      } else {
        let li = document.createElement('li');
        li.appendChild(this.emptyStar());
        this.ul.nativeElement.appendChild(li);
      }
      i++;
    }
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
}
