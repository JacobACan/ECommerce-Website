/*****************************************************
 * 
 *  File:       ProductListTest.java
 *  Author:     Jacob Canedy
 *              SWEN-261-05, TEAM 1A AAAAAAAA
 *  Purpose:    Define a Product object.
 * 
 *****************************************************
 * EDIT HISTORY
 *****************************************************
 * 10/10/2022   <JAC>   created tests for all public methods in ProductList.java
 * 10/13/2022   <JAC>   code coverage added for addProduct
 * 10/13/2022   <JAC>   code coverage added for equals method
 * 10/13/2022   <SZ>    added testDeleteProduct, updated testAddproduct to test adding the same product twice
 * 10/13/2022   <JAC><SZ>   100% code coverage
 *****************************************************/

package com.estore.api.estoreapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.util.TreeMap;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.Objects.Product;
import com.estore.api.estoreapi.Objects.ProductList;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("Model-Teir")
public class ProductListTest {
    @Test
    void testConstructor() throws IOException{
        ProductList productList = new ProductList("src\\test\\java\\com\\estore\\api\\estoreapi\\testdata\\testproducts.json", new ObjectMapper());
        for(Product iii : productList.getProducts()){
            productList.deleteProduct(iii.getId());
        }
        ProductList compareProducts = new ProductList("data\\products.json", new ObjectMapper());
        assertEquals(new ProductList("src\\test\\java\\com\\estore\\api\\estoreapi\\testdata\\testproducts.json", new ObjectMapper()), productList);
        assertEquals(false, productList.equals("NO IT WON'T"));
        assertEquals(false, productList.equals(compareProducts));
        Product product = new Product("Test", 2, null, 10, 
                                      null, null, "Carbon", 1, 1, true);
        assertEquals(false, productList.containsValue(product));
        assertEquals(product, productList.addProduct(product));
        assertEquals(null, productList.addProduct(product));
        assertEquals(true, productList.containsValue(product));
        assertEquals(product, productList.getProduct(product.getId()));
        assertEquals(null, productList.getProduct(-999));
        assertEquals(product, productList.getProduct(product.getName()));
        assertEquals(null, productList.getProduct("No product with this name exists"));

        product.setName("New product name!");
        assertEquals(product, productList.updateProduct(product));
        Product secondProduct = new Product("Product 2", 2, null, 10, 
                                        null, null, "Carbon", 1, 1, true);
        secondProduct.setId(-999);
        assertEquals(null, productList.updateProduct(secondProduct));

        productList.addProduct(secondProduct);
        assertEquals(0, productList.getProductContains("NO PRODUCTS CONTAIN THIS NAME").length);
        assertEquals(product, productList.getProductContains("New product name!")[0]);
        assertEquals(secondProduct, productList.getProducts()[1]);

        Product thirdProduct = new Product("Product 3", 0, null, 10, 
                                        null, null, "Carbon", 1, 1, true);
        productList.addProduct(thirdProduct);

        TreeMap<Integer, Integer> shoppingCart = new TreeMap<>();
        assertEquals(0, productList.getCartPrice(shoppingCart));
        assertEquals(false, productList.cleanInvalidCartProducts(shoppingCart));
        assertEquals(false, productList.cleanInvalidCartProducts(null));
        assertEquals(0, productList.getCartProducts(shoppingCart).length);
        assertEquals(false, productList.checkoutUser(shoppingCart));
        assertEquals(false, productList.checkoutUser(null));
        shoppingCart.put(1,1);
        shoppingCart.put(-999, 1);
        assertEquals(10.0, productList.getCartPrice(shoppingCart));
        shoppingCart.put(thirdProduct.getId(), 1);
        shoppingCart.put(2, 1000);
        assertEquals(new Product("Unavailbale", 0, null, 0, 
            null, null, "", 100, 0, false), 
                     productList.getCartProducts(shoppingCart)[0]);
        assertEquals(0, productList.getCartProducts(null).length);
        assertEquals(0, productList.getCartPrice(null));
        assertEquals(false, productList.checkoutUser(shoppingCart));
        assertEquals(true, productList.cleanInvalidCartProducts(shoppingCart));
        assertEquals(false, productList.cleanInvalidCartProducts(shoppingCart));
        assertEquals(true, productList.checkoutUser(shoppingCart));





        // clear list and do things that don't actually rely on list contents.
        assertEquals(true, productList.deleteProduct(product.getId()));
        assertEquals(false, productList.deleteProduct(product.getId()));
        for(Product iii : productList.getProducts()){
            productList.deleteProduct(iii.getId());
        }
        assertEquals("src\\test\\java\\com\\estore\\api\\estoreapi\\testdata\\testproducts.json", productList.getRoute());
        assertEquals(productList.hashCode(), productList.hashCode());
        assertEquals(productList.toString(), productList.toString());
    }

}
