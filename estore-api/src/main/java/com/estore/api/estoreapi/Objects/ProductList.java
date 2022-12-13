/*****************************************************
 * 
 *  File:       ProductList.java
 *  Author:     Christopher Nokes
 *              Samson Zhang
 *              SWEN-261-05, TEAM 1A AAAAAAAA
 *  Purpose:    Define a list of all products stored on the site.
 * 
 *****************************************************
 * EDIT HISTORY
 *****************************************************
 * 09/21/2022   <CRN>   File created, added basic functionality.
 * 09/22/2022   <CRN>   Changed ID setting to be based on product number, not list position
 * 09/22/2022   <SZ>    Updated addProduct() and its documentation
 * 09/22/2022   <SZ>    Added save() method
 * 09/23/2022   <CRN>   Changed ArrayList methods to TreeMap methods.
 * 09/23/2022   <DCA>   Added updateProduct() method
 * 09/24/2022   <ZMB>   Added deleteProduct() method
 * 09/25/2022   <SZ>    Updated addProduct() so products with the same name don't get added
 * 09/28/2022   <CRN>   Added getPrice() and addProductCart() for use with the shopping cart.
 * 10/10/2022   <JAC>   Changed id from static to private variable that is instantiated
 * 10/10/2022   <JAC>   Id of products added to cart is tracked.
 * 10/10/2022   <JAC>   Adding product to cart returns a product with the correct quantity
 * 10/13/2022   <SZ>    Updated addProductCart to check if product already exists in list
 *                      update the product quantity if the product already exists but has a different quantity
 * 10/13/2022   <JAC>   Updated Equals Method to check size before testing every element in list.
 * 10/14/2022   <CRN>   Fixed default constructor; handles empty list.
 * 10/18/2022   <CRN>   Removed cart handling.
 *****************************************************/

package com.estore.api.estoreapi.Objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ProductList implements ProductListInterface {
    @JsonProperty("ProductMap") TreeMap<Integer, Product> productMap;         // list holding all products
    private ObjectMapper objectMapper;      // conversion map from Objects to JSON
    private String filename;                // file that holds the product list
    private int id;              // id number of the next product to be added.

    /***
     * construct a list of products, given input file string and object mapper
     * @param filenameIN        Directory of the JSON.
     * @param objectMapperIN    ObjectMapper of a product.
     */
    @Autowired
    public ProductList(@Value("${products.file}") String filenameIN, ObjectMapper objectMapperIN) {
        this.id = 0;
        this.filename = filenameIN; 
        this.objectMapper = objectMapperIN;

        productMap = new TreeMap<Integer, Product>();
        // read the file, loop through it, add everything to the list
        try {
            Product[] productArray = objectMapper.readValue(new File(filename),Product[].class);
            for(int i = 0; i < productArray.length; i++) {
                if(productArray[i] == null) continue;
                productArray[i].setId(id);
                productMap.put(id++, productArray[i]);
            }
        } catch(Exception e) {
            System.err.println("Internal error reading productlist from file: " + e);
        }
    }

    /**
     * Saves the {@linkplain Product product} from the arrayList into the file as an array of JSON objects
     * 
     * @return true if the {@link Product product} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        // convert arrayList to array
        if(filename.equals("NO FILE")) return false;
        Product[] list = new Product[productMap.size()];
        for(int i = 0; i < productMap.size(); i++) {
            list[i] = productMap.get(i);
        }
        // write to file
        objectMapper.writeValue(new File(filename), list);
        return true;
    }

    /**
    * {@inheritDoc}
    */
    @JsonIgnore
    public Product addProduct(Product product) throws IOException {
        for (int id: productMap.keySet()){
            if (productMap.get(id).getName().equals(product.getName()))
                return null;
        }
        product.setId(id);
        productMap.put(id++, product);
        save();
        return product;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    public Product getProduct(int id){
        for (Product product : this.productMap.values()) {
            if (id == product.getId()) return product;
        };
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    public Product getProduct(String name){
        for(Product product : productMap.values()){
            if(product.getName().equals(name)) return product;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    public Product[] getProductContains(String name){
        return getProductArray(name);
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    public Product[] getProducts() {
        synchronized(productMap) {
            return getProductArray();
        }
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    public Product[] getProductArray() {
        return getProductArray("");
    }

    /**
     * {@inheritDoc}
     */

    @JsonIgnore
    public Product[] getProductArray(String containsText){

        ArrayList<Product> productArray = new ArrayList<>();

      for(Product product : productMap.values()) {
            if(product.productNameContains(containsText)) productArray.add(product);
        }

        Product[] productReturn = new Product[productArray.size()];
        productArray.toArray(productReturn);
        return productReturn;

    }

    /**
     * Gets the path to .json file that this product list is referencing.
     * @return path to .json file that this product list is referencing.
     */
    public String getRoute() {
        return this.filename;
    }
    private TreeMap<Integer, Product> getProductTreeMap() {
        return this.productMap;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public Product updateProduct(Product product) throws IOException {
        synchronized(productMap) {
            if (!productMap.containsKey(product.getId()))
                return null;  // product does not exist

            productMap.put(product.getId(), product);
            save(); // may throw an IOException
            return product;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public boolean deleteProduct(int id) throws IOException {
        synchronized(productMap) {
            if (productMap.containsKey(id)) {
                productMap.remove(id);
                return save();
            }
            else
                return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    public boolean containsValue(Product product){
        return productMap.containsValue(product);
    }


    /**
     * {@InheritDoc}
     */
    @JsonIgnore
    public double getCartPrice(TreeMap<Integer, Integer> shoppingCart) {
        double currentVal = 0.0;
        if(shoppingCart != null && shoppingCart.size() > 0){
            for(Integer iii : shoppingCart.keySet()){
                if (productMap.get(iii) != null) {
                    currentVal += productMap.get(iii).getPrice() * shoppingCart.get(iii);
                } else {
                    currentVal += 0;
                }
            }
        } return currentVal;
    }

    public Product[] getCartProducts(TreeMap<Integer, Integer> shoppingCart) {
        if(shoppingCart == null || shoppingCart.size() == 0) return new Product[0];
        ArrayList<Product> productArrayList = new ArrayList<>();
        for(Integer iii : shoppingCart.keySet()){
            Product current = productMap.get(iii);
            if(current != null) {
                Product deepCopy = new Product(current.getName(), shoppingCart.get(iii), current.getImageUrlList(), current.getPrice(), current.getSections(), current.getColors(), current.getMaterial(), current.getSize(), current.getExpediency(), current.isGift());
                deepCopy.setId(current.getId());
                productArrayList.add(deepCopy);
            } else {
                Product productUnavailable = new Product("Unavailbale", 0, null, 0, null, null, "", 100, 0, false);
                productUnavailable.setId(iii);
                productArrayList.add(productUnavailable);
            }
        }
        Product[] productArray = productArrayList.toArray(new Product[0]);
        return productArray;
    }

    public boolean cleanInvalidCartProducts(TreeMap<Integer, Integer> shoppingCart) {
        if(shoppingCart == null || shoppingCart.size() == 0) return false;
        boolean itemsRemoved = false;
        Set<Integer> keySet = shoppingCart.keySet(); //.keyset() shows changes of the tree map when going through the for loop causing iterator errors.
        Set<Integer> deepCopyKeySet = new HashSet<Integer>(); //deep copy to solve issue.
        deepCopyKeySet.addAll(keySet);
        for(Integer iii : deepCopyKeySet) {
            if (productMap.get(iii) == null || productMap.get(iii).getQuantity() < shoppingCart.get(iii)){
                shoppingCart.remove(iii);
                itemsRemoved = true;
            }
        }
        return itemsRemoved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ProductList) {
            ProductList list = (ProductList) obj;
            if (list.getProductTreeMap().equals(productMap)) return true;
            return false;
        } 
        return false;
    }
    @Override
    public String toString() {
        return this.productMap.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public boolean checkoutUser(TreeMap<Integer, Integer> userCart){
        // first, check cart validity.
        if(userCart == null || userCart.size() < 1) return false;
        // second, check to make sure the cart CAN be purchased.
        for(Integer iii : userCart.keySet()){
            if(productMap.get(iii) == null || productMap.get(iii).getQuantity() < userCart.get(iii)) return false;
        }
        // if they all can be, loop through again and remove them from the cart.
        for(Integer iii : userCart.keySet()){
            productMap.get(iii).setQuantity(productMap.get(iii).getQuantity() - userCart.get(iii));
        } try { save(); } 
        catch(Exception e){ }
        return true;
    }
    
}
