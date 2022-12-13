import { Injectable } from '@angular/core';
import { Product } from '../interfaces/product'
import { HttpClient, HttpHeaders, HttpClientModule } from '@angular/common/http'
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  $cartChange : Subject<number>
  private amountInCart : number = 0;
  private apiUrl :string = 'http://localhost:8080/users/cart'
  private sessionId : string = "" //Used if we want to verify transaction.
  constructor(private http: HttpClient) {
    this.$cartChange = new BehaviorSubject(0)
  }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type' : 'application/json'}),
  };

  addToCart(product: Product, userName : string) {
    this.$cartChange.next(1)
    return this.http.put<number>(`${this.apiUrl}/add/${userName}`, product, this.httpOptions)
  }
  removeFromCart(product: Product, userName : string) {
    this.$cartChange.next(0)
    return this.http.put<number>(`${this.apiUrl}/remove/${userName}`, product, this.httpOptions)
  }

  getCart(userName : string) {
    return this.http.get<Product[]>(`${this.apiUrl}/${userName}`)
  }

  getPrice(userName : string) {
    return this.http.get<number>(`${this.apiUrl}/price/${userName}`);
  }

  checkout(userName : string) {
    this.$cartChange.next(2)
    return this.http.post<number>(`${this.apiUrl}/${userName}`,this.sessionId, {observe: 'response'})
  }

  cleanCart(userName : string) {
    this.$cartChange.next(3)
    return this.http.post<boolean>(`${this.apiUrl}/${userName}/clean`, this.sessionId, this.httpOptions)
  }
  onCartChanged() {
    return this.$cartChange
  }
}
