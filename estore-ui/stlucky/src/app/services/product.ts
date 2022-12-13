export interface Product {
  id: number;
  price: number;
  quantity: number;
  name: string;
  imageUrlList: string[];
  sections: string[];
  colors: string[];
  material: string;
  size: number;
  expediency: number;
  gift: boolean;
  ratingList: number[];
  avgRating: number;
}
