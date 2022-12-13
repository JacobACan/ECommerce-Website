/*****************************************************
 * 
 *  File:       Product.java
 *  Author:     Christopher Nokes
 *              SWEN-261-05, TEAM 1A AAAAAAAA
 *  Purpose:    Define a Product object.
 * 
 *****************************************************
 * EDIT HISTORY
 *****************************************************
 * 09/20/2022   <CRN>   File created, added basic functionality.
 * 09/23/2022   <CRN>   Added JsonProperties, changed arraylist to treemap.
 * 09/28/2022   <CRN>   Added incrementer and decrementer to quantity.
 * 10/04/2022   <CRN>   Made the constructor public, added a toString and .equals.
 * 10/04/2022   <CRN>   Added nullpointer checkers on imageUrlList accessors that were missing them, other checks.
 * 11/4/2022    <SZ>    Added rating attributes and related methods
 *****************************************************/

package com.estore.api.estoreapi.Objects;


import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
    
    @JsonProperty("name") private String name;
    @JsonProperty("id") private int productIdNum = -1;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("imageUrlList") private ArrayList<String> imageUrlList;
    @JsonProperty("price") private double price;
    @JsonProperty("sections") private ArrayList<String> sections;
    @JsonProperty("colors") private ArrayList<String> colors;
    @JsonProperty("material") private String material;
    @JsonProperty("size") private double size;
    @JsonProperty("expediency") private double expediency;
    @JsonProperty("gift") private boolean gift;
    @JsonProperty("ratingList") private ArrayList<Integer> ratingList;
    @JsonProperty("avgRating") private double avgRating;
    // TODO -- DESCRIPTIONS AND REVIEWS


    public static final String STRING_FORMAT = "Product: [name=%s, id=%d, quantity=%d, price=%f]";

    // ****************************************************
    // Constructor
    // ****************************************************
    /***
     * Constructs a product object.
     * @param name              Name to set the product to.
     * @param quantity          Quantity to set the product to.
     * @param imageUrlList      List of images to give the product.
     * @param price             Price of the product.
     */
    public Product(@JsonProperty("name") String name,
            @JsonProperty("quantity") int quantity, 
            @JsonProperty("imageUrlList") ArrayList<String> imageUrlList, 
            @JsonProperty("price") double price,
            @JsonProperty("sections") ArrayList<String> sections,
            @JsonProperty("colors") ArrayList<String> colors,
            @JsonProperty("material") String material,
            @JsonProperty("size") double size,
            @JsonProperty("expediency") double expediency,
            @JsonProperty("gift") boolean gift ){
        this.name = name;
        if(quantity < 0) quantity = 0;
        else this.quantity = quantity;
        this.imageUrlList = imageUrlList;
        if(price < 0) price = 0;
        else this.price = price;
        this.sections = sections;
        this.colors = colors;
        this.material = material;
        if(size <= 0) size = 100;
        else this.size = size;
        this.expediency = expediency;
        this.gift = gift;
        this.ratingList = new ArrayList<>();
        this.avgRating = 0.0;
    }
    // ****************************************************
    // basic getters
    // ****************************************************
    /***
     * Get the name.
     * @return  The name.
     */
    public String getName() { return name; }

    /***
     * Get the ID.
     * @return  The ID.
     */
    public int getId() { return productIdNum; }

    /***
     * Gets the quantity.
     * @return  The quantity.
     */
    public int getQuantity() { return quantity; }

    /***
     * Gets the list of images.
     * @return  The list of images.
     */
    public ArrayList<String> getImageUrlList() { return imageUrlList; }

    /***
     * Gets an image at an index.
     * @param index The index of the imageUrlList to get the image from.
     * @return      The image URL String, or an empty string if it does not exist.
     */
    public String getImage(int index) {
        if(imageUrlList == null || index >= imageUrlList.size()) { return ""; } // TODO -- ADD "IMAGE NOT FOUND" IMAGE LINK
        return imageUrlList.get(index);
    }

    /***
     * Gets the price.
     * @return  The price.
     */
    public double getPrice() { return price; }

    public String getSection(int index) {
        if(sections == null || index >= sections.size()) { return ""; }
        return sections.get(index);
    }

    public ArrayList<String> getSections() { return this.sections; }

    public String getColor(int index) {
        if(colors == null || index >= colors.size()) { return ""; }
        return colors.get(index);
    }

    public String getColor(String section) {
        if(colors == null) return "";
        int index = sections.indexOf(section);
        if (index == -1) return ""; 
        else return colors.get(index);
    }

    public ArrayList<String> getColors() { return this.colors; }

    public String getMaterial() { return material; }

    public double getSize() { return size; }

    public double getExpediency() { return expediency; }

    public boolean isGift() { return gift; }

    /**
     * Get the product rating
     * @return rating
     */
    public ArrayList<Integer> getRatingList() {
        return ratingList;
    }

    public double getAvgRating() {
        updateAvgRating();
        return avgRating;
    }

    // ****************************************************
    // basic setters
    // ****************************************************
    /***
     * Sets the name
     * @param name  name to set the name to
     */
    public void setName(String name) { this.name = name; }

    /***
     * Sets the ID
     * @param productIdNum  id to set the id to
     */
    public void setId(int productIdNum) { this.productIdNum = productIdNum; }

    /***
     * Sets the quantity
     * @param quantity  quantity to set the quantity to
     */
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /***
     * Sets the price
     * @param price price to set the price to
     */
    public void setPrice(double price) { this.price = price; }

    public void setMaterial(String material) { this.material = material; }

    public void setSize(double size) { this.size = size; }

    public void setExpediency(double expediency) { this.expediency = expediency; }

    public void setGift(boolean gift) { this.gift = gift; }

    // ****************************************************
    // more elaborate methods with individual descriptions
    // ****************************************************

    /***
     * returns true if the product name contains a certain string (regardless of capitalization)
     * @param container Partial name to search for.
     * @return          True if the product contains that name, false otherwise.
     */
    public boolean productNameContains(String container) {
        return this.name.toLowerCase().contains(container.toLowerCase()); 
    }

    /***
     * adds a new imageUrl to a list; creates a list if one does not exist.
     * @param imageUrl  Image URL to add
     */
    public void addImage(String imageUrl) { 
        if(imageUrlList == null) { imageUrlList = new ArrayList<String>(); }
        imageUrlList.add(imageUrl); 
    }

    /***
     * adds a new imageUrl to a list AT AN INDEX.
     * @param imageUrl  Image URL to add
     * @param index     Index to add the image at
     * @return          True if the URL was successfully added, false otherwise.
     */
    public boolean addImage(String imageUrl, int index) { 
        if(index < 0 || index > imageUrlList.size() - 1) { 
            return false; 
        } imageUrlList.add(index, imageUrl);
        return true;
    }

    /***
     * remove image based on a link; returns false if it does not exist
     * @param index     Index of the image to remove
     * @return          True if the URL was deleted successfully, false otherwise.
     */
    public boolean removeImage(int index) {
        if(imageUrlList == null || index >= imageUrlList.size()) { return false; }
        imageUrlList.remove(index);
        return true;
    }

    /***
     * remove image based on a link; returns false if it does not exist
     * @param imageUrl  String URL of the image to remove
     * @return          True if the URL was deleted successfully, false otherwise.
     */
    public boolean removeImage(String imageUrl) {
        if(imageUrlList != null) {
            for(int i = 0; i < imageUrlList.size(); i++) {
                if(imageUrlList.get(i).equals(imageUrl)) {
                    imageUrlList.remove(i);
                    return true;
                }
            } 
        } return false;
    }

    /***
     * remove image if it is contains a given String; returns false if it does not exist
     * @param container portion of the image name to search for
     * @return          true if an image was removed, false otherwise.
     */
    public boolean removeImageContains(String container) {
        if(imageUrlList != null) {
            for(int i = 0; i < imageUrlList.size(); i++) {
                if(imageUrlList.get(i).toLowerCase().contains(container.toLowerCase())) {
                    imageUrlList.remove(i);
                    return true;
                }
            }
        } return false;
    }

    /***
     *  erases the contents of imageUrlList and makes a new list to replace it.
     */
    public void resetImageUrlList() {
        imageUrlList = new ArrayList<String>();
    }

    public void createSection (String section) {
        sections.add(section);
        colors.add("");
    }

    public void setColor (String color, String section) {
        int index = sections.indexOf(section);
        colors.set(index, color);
    }

    public void setColorCustomization (ArrayList<String> sections, ArrayList<String> colors) {
        this.sections = sections;
        this.colors = colors;
    }

    /***
     * decrements quantity by 1
     * @return  True if the quantity was successfully decremented, false otherwise.
     */
    public boolean decrementQuantity() {
        if(quantity - 1 >= 0) {
            quantity--;
            return true;
        } return false;
    }



    /***
     * increments quantity by 1
     */
    public void incrementQuantity() { quantity++; }

    /**
     * calculates and return the average rating
     * @return average rating of this product
     */
    public void updateAvgRating(){
        if(ratingList.size() == 0){
            avgRating = 0.0;
            return;
        }

        double total = 0.0;
        for (double d: ratingList){
            total += d;
        }
        avgRating = total/ratingList.size();
    }

    /***
     * toString, mostly for debugging
     * @return  String form of the product.
     */
    @Override
    public String toString(){
        return String.format(STRING_FORMAT, name, productIdNum, quantity, price);
    }

    /** 
     * checks if two products are the same; Two products are considered the same IFF:
     *      1. They are both products.
     *      2. They both have null image lists OR the image lists are the same
     *      3. They have the same name and price
     * This method is built so that two products will STILL "equal" each other even if:
     *      1. They have different ids.
     *      2. They have different quantities.
     * This makes it possible to increment a product in the cart if it is "added again";
     * if a more traditional equals is needed, a different function should be made.
     * @param Obj   An object to compare with the current product.
     * @return      If the object and current product are the same based on above parameters.
    */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Product) {
            Product product = (Product) obj;
            if(product.getImageUrlList() == null || this.imageUrlList == null) {
                if(!(product.getImageUrlList() == null && this.imageUrlList == null)) return false;
                return  product.getName().equals(this.name) && product.getPrice() == this.price;
            }
            return product.getName().equals(this.name) && product.getImageUrlList().equals(this.imageUrlList) &&
                    product.getPrice() == this.price;
        } return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
