import { Component, OnInit } from '@angular/core';
import { Product } from '../interfaces/product';
import { CartService } from '../services/cart.service';
import { ProductService } from '../services/product.service';
import { AccountService } from '../services/account.service';
import { ActivatedRoute } from '@angular/router';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import * as THREE from 'three';
import Swal from 'sweetalert2';
import { MaterialTypes } from '../interfaces/materials.enum';
import { ColorSelect } from '../interfaces/colors.enum';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-page',
  templateUrl: './product-page.component.html',
  styleUrls: ['./product-page.component.css'],
})
export class ProductPageComponent implements OnInit {
  private userName: string = '';
  id: number = -1;

  public materialTypes = Object.values(MaterialTypes);
  public colorSelect = Object.values(ColorSelect);

  product: Product;
  errorStatus: boolean = false;
  errorResponse: string = '';
  numberAdded: number = 0;
  stlModel: string = '';
  color: THREE.Light = new THREE.PointLight();
  textures: THREE.Material = new THREE.MeshPhongMaterial()
  temporary_string: string = '';
  scale: THREE.Vector3 = new THREE.Vector3( 0.03, 0.03, 0.03);

  

  renderer = new THREE.WebGLRenderer({ antialias: true });
  camera = new THREE.PerspectiveCamera(50, window.innerWidth / window.innerHeight, .3, 1000);

  controls = new OrbitControls(this.camera, this.renderer.domElement);
  scene = new THREE.Scene();
  light = new THREE.PointLight();
  material = new THREE.MeshPhongMaterial()



  

  isUser: boolean = false;
  isProduct: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    private accountService: AccountService,
    private router: Router
    
  ) {
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
      
      this.renderer.setPixelRatio(1)
      this.renderer.shadowMap.enabled = true
      this.scale = new THREE.Vector3( 1, 1, 1 )
  
      // default camera position
      this.camera.position.set(3, 3, 3)
  
      // default light position
      this.light.position.set(1, 1, 2)
  
      this.controls.enableZoom = false
      this.controls.minDistance = 100
      this.controls.maxDistance = 100
  
      this.controls.update()
  }

  ngOnInit(): void {
    console.log('INITTING PRODUCT PAGE');
    this.getProduct();
    this.accountService.getUserName().subscribe((userName) => {
      this.userName = userName;
      console.log(this.userName != 'ADMIN');
      this.isUser = this.userName != 'ADMIN';
      this.setNumberAdded();
    });
  }

  addToCart() {
    this.cartService.addToCart(this.product, this.userName).subscribe({
      next: (numberAdded) => (this.numberAdded = numberAdded),
      error: (e) => {
        if (e.status == 405) this.infoLoginToUseCart('add');
        if (e.status == 409) this.infoItemsUnavailable();
      },
    });
  }

  removeFromCart() {
    this.cartService.removeFromCart(this.product, this.userName).subscribe({
      next: (numberAdded) => (this.numberAdded = numberAdded),
      error: (e) => {
        this.infoLoginToUseCart('remove');
        console.log(`Remove From Cart Status: ${e.status}`);
      },
    });
  }

  getProduct() {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.id = id;
    this.productService.getProduct(id).subscribe((product) => {
      this.product = product;
      this.stlModel = product.imageUrlList[1];
      this.isProduct = this.product.name != '';
      this.color = new THREE.PointLight(this.product.colors[0])
      this.temporary_string = this.product.material
      this.scene.background = new THREE.Color(0xedf2f4)
      this.product.size = 25
      
      switch(this.temporary_string) {
        case 'Hard ABS':
          this.textures = new THREE.MeshPhongMaterial({ color: 0xFFFFFF, shininess: 100, specular: 0xFFFFFF});
            break;
        case 'Flexible TPE':
          this.textures = new THREE.MeshPhongMaterial({ color: 0xFFFFFF, shininess: 0, specular: 0x0});
            break;
        case 'Biodegradable PLA':
          this.textures = new THREE.MeshPhongMaterial({ color: 0xFFFFFF, shininess: 0xFFFFFF, specular: 0x0});
            break;
        case 'Heat Resistant PLA':
          this.textures = new THREE.MeshPhongMaterial({ color: 0xFFFFFF, shininess: 0, specular: 0xFFFFFF});
            break;
        case 'Wood Fibre':
          this.textures = new THREE.MeshPhongMaterial({ color: 0x5f5035, shininess: 0, specular: 0x0});
            break;
        case 'Glow in the Dark ABS':
          this.textures = new THREE.MeshPhongMaterial({ color: 0xecdf89, shininess: 100, specular: 0xFFFFFF});
          this.scene.background = new THREE.Color("black")
            break;
      }

      this.scale = new THREE.Vector3( 1, 1, 1 )
    });
  }


  save(): void {
    if (this.product) {
      this.productService.updateProduct(this.product).subscribe(() => {
        window.location.reload();
      });
    }
  }


  setNumberAdded() {
    this.cartService.getCart(this.userName).subscribe((cart) => {
      console.log('ADDING NUM ADDED');
      for (let i = 0; i < cart.length; i++) {
        let product: Product = cart[i];
        if (product.id == this.id) this.numberAdded = product.quantity;
      }
      console.log(`NUMBER ADDED SET TO ${this.numberAdded}`);
    });
  }
  goHome() {
    this.router.navigateByUrl('/');
  }

  /**
   * Update the ratingList, then call updateProduct on this.product and subscribe to it
   */

  infoLoginToUseCart(action: string) {
    Swal.fire({
      title: `Login to ${action} items to your shopping cart.`,
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: 'Login',
      denyButtonText: `Maybe Later`,
    }).then((result) => {
      if (result.isConfirmed) {
        window.location.href = 'http://localhost:4200/login';
      } else if (result.isDenied) {
        Swal.fire('You will only be able to browse products.', '', 'info');
      } else {
        Swal.fire('You will only be able to browse products.', '', 'info');
      }
    });
  }
  infoItemsUnavailable() {
    Swal.fire(`Sorry, we are out of stock.`, '', 'info');
  }
}
