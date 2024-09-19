package farm.inventory;

import farm.core.FailedTransactionException;
import farm.core.InvalidStockRequestException;
import farm.inventory.product.*;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BasicInventoryTest {
    Inventory inventory;

    @Before
    public void setup() {
        inventory = new BasicInventory();
    }

    /**
     * Checks the null case
     * When no products are added, the inventory should return an empty list.
     */
    @Test
    public void addNoProductsTest() {
        List<Product> actual = inventory.getAllProducts();
        List<Product> expected = new ArrayList<>();
        Assert.assertEquals(expected, actual);
        Assert.assertFalse(inventory.existsProduct(Barcode.MILK));
    }

    /**
     * Check if one product can be added and returned.
     */
    @Test
    public void addOneProductTest() {
        inventory.addProduct(Barcode.MILK, Quality.REGULAR);
        List<Product> actual = inventory.getAllProducts();
        List<Product> expected = new ArrayList<>();
        expected.add(new Milk(Quality.REGULAR));
        Assert.assertEquals(expected, actual);
        Assert.assertTrue(inventory.existsProduct(Barcode.MILK));
    }

    /**
     * Check if multiple products can be added and returned.
     */
    @Test
    public void addMultipleProductTest() {
        List<Product> expected = new ArrayList<>();
        //Add regular milk
        inventory.addProduct(Barcode.MILK, Quality.REGULAR);
        expected.add(new Milk(Quality.REGULAR));
        //Add gold milk
        inventory.addProduct(Barcode.MILK, Quality.GOLD);
        expected.add(new Milk(Quality.GOLD));
        //Add iridium coffee
        inventory.addProduct(Barcode.COFFEE, Quality.IRIDIUM);
        expected.add(new Coffee(Quality.IRIDIUM));
        //Add silver bread
        inventory.addProduct(Barcode.BREAD, Quality.SILVER);
        expected.add(new Bread(Quality.SILVER));
        //Add regular wool
        inventory.addProduct(Barcode.WOOL, Quality.REGULAR);
        expected.add(new Wool(Quality.REGULAR));

        List<Product> actual = inventory.getAllProducts();
        Assert.assertEquals(expected, actual);
        Assert.assertTrue(inventory.existsProduct(Barcode.MILK));
        Assert.assertTrue(inventory.existsProduct(Barcode.COFFEE));
        Assert.assertTrue(inventory.existsProduct(Barcode.BREAD));
        Assert.assertTrue(inventory.existsProduct(Barcode.WOOL));
        Assert.assertFalse(inventory.existsProduct(Barcode.EGG));
    }

    /**
     * Check if existsProduct returns true for an existing product.
     * Check if existsProduct returns false for a non-existing product.
     */
    @Test
    public void existsProductTrue() {
        inventory.addProduct(Barcode.MILK, Quality.REGULAR);
        inventory.addProduct(Barcode.MILK, Quality.IRIDIUM);
        Assert.assertTrue(inventory.existsProduct(Barcode.MILK));
        Assert.assertFalse(inventory.existsProduct(Barcode.COFFEE));
    }

    /**
     * Add multiple products at once should throw an exception.
     * Check the exception message.
     */
    @Test(expected = InvalidStockRequestException.class)
    public void addMultipleProductsAtOnceTest() throws InvalidStockRequestException {
        try {
            inventory.addProduct(Barcode.MILK, Quality.REGULAR, 2);
            Assert.fail("This should have thrown an exception");
        } catch (InvalidStockRequestException e) {
            Assert.assertEquals("Current inventory is not fancy enough. Please supply products one at a time.",
                    e.getMessage());
            throw e;
        }
    }

    /**
     * Removing a product that is not in the inventory should return an empty list.
     */
    @Test
    public void removeNonExistentProductTest() {
        inventory.addProduct(Barcode.EGG, Quality.REGULAR);
        List<Product> actual = inventory.removeProduct(Barcode.MILK);
        List<Product> expected = new ArrayList<>();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Removing a product that is in the inventory should return a list with that product.
     * The inventory should remove the first product from the list with the given barcode.
     */
    @Test
    public void removeProductTest() {
        //Add regular milk
        inventory.addProduct(Barcode.MILK, Quality.IRIDIUM);
        inventory.addProduct(Barcode.MILK, Quality.GOLD);
        inventory.addProduct(Barcode.MILK, Quality.SILVER);
        inventory.addProduct(Barcode.MILK, Quality.REGULAR);

        List<Product> actual = inventory.removeProduct(Barcode.MILK);

        List<Product> expected = new ArrayList<>();
        expected.add(new Milk(Quality.IRIDIUM));

        Assert.assertEquals(expected, actual);
        Assert.assertEquals(3, inventory.getAllProducts().size());

        actual = inventory.removeProduct(Barcode.MILK);
        expected.clear();
        expected.add(new Milk(Quality.GOLD));
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(2, inventory.getAllProducts().size());

        actual = inventory.removeProduct(Barcode.MILK);
        expected.clear();
        expected.add(new Milk(Quality.SILVER));
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(1, inventory.getAllProducts().size());
    }

    /**
     * When a product is removed, the removed product should no longer exist in the inventory.
     */
    @Test
    public void removedProductShouldNotExistTest() {
        inventory.addProduct(Barcode.BREAD, Quality.REGULAR);
        inventory.addProduct(Barcode.MILK, Quality.REGULAR);
        inventory.removeProduct(Barcode.MILK);
        Assert.assertFalse(inventory.existsProduct(Barcode.MILK));
        Assert.assertTrue(inventory.existsProduct(Barcode.BREAD));
        Assert.assertEquals(1, inventory.getAllProducts().size());
    }

    /**
     * Removing multiple products at once should throw an exception.
     */
    @Test(expected = FailedTransactionException.class)
    public void removeMultipleProductsAtOnceTest() throws FailedTransactionException {
        try {
            inventory.addProduct(Barcode.MILK, Quality.REGULAR);
            inventory.addProduct(Barcode.MILK, Quality.REGULAR);
            inventory.removeProduct(Barcode.MILK, 2);
            Assert.fail("This should have thrown an exception");
        } catch (FailedTransactionException e) {
            Assert.assertEquals("Current inventory is not fancy enough. Please purchase products one at a time.",
                    e.getMessage());
            throw e;
        }
    }


}
