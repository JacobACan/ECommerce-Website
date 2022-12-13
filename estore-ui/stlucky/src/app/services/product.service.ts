import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, of, tap } from 'rxjs';
import { Product } from './product';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  // private productsUrl = 'api/products';  // URL to web api
  private productsUrl = 'http://localhost:8080/products';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) {}

  /** GET product by id. Will 404 if id not found */
  getProduct(id: number): Observable<Product> {
    console.log(`got id : ${id}`);
    const url = `${this.productsUrl}/${id}`;
    return this.http.get<Product>(url).pipe(
      tap((_) => console.log(`fetched product id=${id}`)),
      catchError(this.handleError<Product>(`getProduct id=${id}`))
    );
  }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.productsUrl).pipe(
      tap((_) => console.log(`fetched products`)),
      catchError(this.handleError<Product[]>('getProducts', []))
    );
  }

  /* GET products whose name contains search term */
  searchProducts(term: string): Observable<Product[]> {
    if (!term.trim()) {
      // if not search term, return empty product array.
      return of([]);
    }
    console.log(`${this.productsUrl}/?container=${term}`);
    return this.http
      .get<Product[]>(`${this.productsUrl}/?container=${term}`)
      .pipe(
        tap((x) =>
          x.length
            ? console.log(`found products matching "${term}"`)
            : console.log(`no products matching "${term}"`)
        ),
        catchError(this.handleError<Product[]>('searchProducts', []))
      );
  }

  //////// Save methods //////////

  /** POST: add a new product to the server */
  addProduct(product: Product): Observable<Product> {
    return this.http
      .post<Product>(this.productsUrl, product, this.httpOptions)
      .pipe(
        tap((newProduct: Product) => this.infoProductAdded()),
        catchError(this.handleError<Product>('addProduct'))
      );
  }

  /** DELETE: delete the product from the server */
  deleteProduct(id: number): Observable<Product> {
    const url = `${this.productsUrl}/${id}`;

    return this.http.delete<Product>(url, this.httpOptions).pipe(
      tap((_) => this.infoProductDeleted()),
      catchError(this.handleError<Product>('deleteProduct'))
    );
  }

  /** PUT: update the product on the server */
  updateProduct(product: Product): Observable<any> {
    return this.http.put(this.productsUrl, product, this.httpOptions).pipe(
      tap((_) => console.log('product updated')),
      catchError(this.handleError<any>('updateProduct'))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      // TODO: send the error to remote logging infrastructure
      console.error(`HANDLE ERROR: ${error}`); // log to console instead
      console.log(`${error.message}`);

      // TODO: better job of transforming error for user consumption
      if (error.url == 'http://localhost:8080/products') {
        if (error.status == 400) {
          this.infoItemNotAdded();
        } else if (error.status == 409) {
          this.infoProductAlreadyCreated();
        } else if (error.status == 500) {
          this.infoInternalServerError();
        }
      }

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  private infoItemNotAdded() {
    Swal.fire({
      title: 'Product Not Created',
      text: 'Make sure proper data was entered into feilds',
      icon: 'error',
    });
  }
  private infoProductAdded() {
    Swal.fire({
      icon: 'success',
      title: 'Product Created',
    });
  }
  private infoProductAlreadyCreated() {
    Swal.fire({
      title: 'Product Not Created',
      text: 'Product already exists.',
      icon: 'error',
    });
  }
  private infoInternalServerError() {
    Swal.fire({
      title: 'Product Not Created',
      text: 'Internal Server Error',
      icon: 'error',
    });
  }

  private infoProductDeleted() {
    Swal.fire({
      icon: 'success',
      title: 'Product Deleted',
    });
  }
}
