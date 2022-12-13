export interface Product {
  name: string;
  id: number;
  quantity: number;
  imageUrlList: string[];
  price: number;
  sections: string[];
  colors: string[];
  material: string;
  size: number;
  expediency: number;
  gift: boolean;
  ratingList: number[];
  avgRating: number;
}
