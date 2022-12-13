export interface Product {
  name: string;
  quantity: number;
  imageUrlList: Array<string>;
  price: number;
  id: number;
  sections: string[];
  colors: string[];
  material: string;
  size: number;
  expediency: number;
  gift: boolean;
  ratingList: Array<number>;
  avgRating: number;
}
