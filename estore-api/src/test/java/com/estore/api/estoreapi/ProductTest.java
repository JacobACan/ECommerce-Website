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
 * 10/04/2022   <CRN>   File created, created tests for 100% code coverage.
 * 10/05/2022   <CRN>   Added test for new .equals() method.
 * 10/06/2022   <CRN>   Added documentation.
 * 10/07/2022   <CRN>   Added a check to make sure a product price cannot be negative.
 *****************************************************/

package com.estore.api.estoreapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.Objects.Product;

@Tag("Model-tier")
public class ProductTest {

    @Test
    public void testConstructor(){
        Product product = new Product("Test", 1, null, 10, 
                                      null, null, "Carbon", 1, 1, true);

        // test all getters with an unedited product
        assertEquals("Test", product.getName());
        assertEquals(-1, product.getId());
        assertEquals(1, product.getQuantity());
        assertEquals(null, product.getImageUrlList());
        assertEquals("", product.getImage(0));
        assertEquals(10, product.getPrice());
        assertEquals("", product.getSection(0));
        assertEquals(null, product.getSections());
        assertEquals("", product.getColor(0));
        assertEquals(null, product.getColors());
        assertEquals("Carbon", product.getMaterial());
        assertEquals("", product.getColor("NO"));
        assertEquals(1, product.getSize());
        assertEquals(1, product.getExpediency());
        assertEquals(true, product.isGift());
        assertEquals(true, product.equals(product));
        assertEquals(0.0, product.getAvgRating());
        assertEquals(new ArrayList<Integer>(), product.getRatingList());
        assertEquals(true, product.productNameContains("Te"));
        assertEquals(product.toString(), product.toString());
        assertEquals(product.hashCode(), product.hashCode());
        assertEquals(false, product.removeImage("nnn"));
        assertEquals(false, product.removeImage(999));
        assertEquals(false, product.removeImageContains("AAAAAAAAAA"));

        // special cases in the constructor
        product = new Product("Test_New", -1, null, -1, 
                              null, null, "Carbon", -1, 0, false);
        assertEquals(0, product.getQuantity());
        assertEquals(0, product.getPrice());


        // setters
        product.setQuantity(0);
        assertEquals(false, product.decrementQuantity());
        product.setQuantity(20);
        assertEquals(20, product.getQuantity());
        product.setName("Test");
        assertEquals("Test", product.getName());
        assertEquals(true, product.decrementQuantity());
        assertEquals(19, product.getQuantity());
        product.incrementQuantity();
        assertEquals(20, product.getQuantity());
        product.setPrice(10.0);
        assertEquals(10.0, product.getPrice());
        product.setMaterial("Metal");
        assertEquals("Metal", product.getMaterial());
        product.setSize(200);
        assertEquals(200, product.getSize());
        product.setExpediency(20);
        assertEquals(20, product.getExpediency());
        product.setGift(true);
        assertEquals(true, product.isGift());

        product.addImage("test");
        product.addImage("test2");
        assertEquals(true, product.addImage("test3", 1));
        assertEquals(false, product.addImage("No", -1));
        assertEquals(false, product.addImage("test", 99999));
        assertEquals("test", product.getImage(0));
        assertEquals(false, product.removeImage(99999));
        assertEquals(true, product.removeImage(0));
        assertEquals(true, product.removeImage("test3"));
        assertEquals(false, product.removeImageContains("AAAAAAAAAA"));
        product.addImage("test");
        assertEquals(true, product.removeImageContains("te"));

        product.resetImageUrlList();
        assertEquals(new ArrayList<String>(), product.getImageUrlList());

        ArrayList<String> testList = new ArrayList<>();
        testList.add("n");
        testList.add("AAA");
        product.setColorCustomization(testList, testList);
        assertEquals(testList, product.getColors());
        assertEquals(testList, product.getSections());
        assertEquals("n", product.getColor(0));
        assertEquals("n", product.getColor("n"));
        assertEquals("", product.getColor("NO"));
        assertEquals("n", product.getSection(0));
        assertEquals("", product.getSection(99999));
        assertEquals(true, product.equals(product));
        assertEquals(false, product.equals(new ArrayList<String>()));
        product.createSection("test");
        product.setColor("color", "AAA");
    }

    @Test
    public void testEquals(){
        Product product = new Product("bruh", 1, null, 0.1, null, null, "", 0, 0, false);
        assertEquals(false, product.equals("test"));

        Product product2 = new Product("WRONG NAME", 1, null, 0.1, null, null, null, 0, 0, false);
        assertEquals(false, product.equals(product2));

        product2 = new Product("bruh", 1, null, 9999, null, null, null, 0, 0, false);
        assertEquals(false, product.equals(product2));

        product2 = new Product("bruh", 1, null, 0.1, null, null, null, 0, 0, false);
        assertEquals(true, product.equals(product2));

        product.addImage("a");
        assertEquals(false, product.equals(product2));

        product2.addImage("a");
        assertEquals(true, product.equals(product2));

        product2.setName("WRONG NAME");
        assertEquals(false, product.equals(product2));
        product2.setName("bruh");

        product2.setPrice(99999);
        assertEquals(false, product.equals(product2));
        product2.setPrice(0.1);

        product2.addImage("a");
        assertEquals(false, product.equals(product2));

        product = new Product("bruh", 1, null, 0.1, null, null, null, 0, 0, false);
        assertEquals(false, product.equals(product2));
        
    }
}
