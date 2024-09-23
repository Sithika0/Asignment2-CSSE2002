package farm.inventory;

import farm.core.FailedTransactionException;
import farm.core.InvalidStockRequestException;
import farm.inventory.product.*;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import org.junit.*;

import java.util.*;

/**
 * Testing the BasicInventory class.
 */
public class BasicInventoryTest {
    Inventory inventory;

    /**
     * Reset the inventory before each test.
     */
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
     * One product is added to the inventory.
     * The inventory should return a list with that product.
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
     * The products are added one at a time.
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
        Assert.assertFalse(inventory.existsProduct(Barcode.JAM));
        // There should only be 5 products in the inventory
        Assert.assertEquals(5, inventory.getAllProducts().size());
    }

    /**
     * existsProduct returns true for an existing product.
     */
    @Test
    public void existsProductTrue() {
        inventory.addProduct(Barcode.MILK, Quality.REGULAR);
        Assert.assertTrue(inventory.existsProduct(Barcode.MILK));
        Assert.assertFalse(inventory.existsProduct(Barcode.COFFEE));
    }

    /**
     * existsProduct returns false for a non-existing product.
     */
    @Test
    public void existsProductFalse() {
        // Inventory with no products, should return false for all barcodes
        for (Barcode barcode : Barcode.values()) {
            Assert.assertFalse(inventory.existsProduct(barcode));
        }
    }

    /**
     * Adding multiple products at once should throw an InvalidStockRequestException.
     * Check the exception message.
     */
    @Test(expected = InvalidStockRequestException.class)
    public void addMultipleProductsAtOnceTest() throws InvalidStockRequestException {
        Barcode barcode = Barcode.COFFEE;
        try {
            inventory.addProduct(barcode, Quality.REGULAR, 2);
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
        List<Product> actual = inventory.removeProduct(Barcode.MILK); // Milk was never added
        List<Product> expected = new ArrayList<>();
        Assert.assertEquals(expected, actual);
    }

    /**
     * The inventory should remove the first product from the list with the given barcode.
     * Make sure the product is actually removed from the inventory.
     */
    @Test
    public void removeProductTest() {
        //Add regular milk
        inventory.addProduct(Barcode.MILK, Quality.IRIDIUM); // 1st milk
        inventory.addProduct(Barcode.COFFEE, Quality.REGULAR); // random product
        inventory.addProduct(Barcode.MILK, Quality.GOLD); // 2nd milk
        inventory.addProduct(Barcode.EGG, Quality.REGULAR); // random product
        inventory.addProduct(Barcode.MILK, Quality.SILVER); // 3rd milk
        inventory.addProduct(Barcode.BREAD, Quality.REGULAR); // random product
        inventory.addProduct(Barcode.MILK, Quality.REGULAR); // 4th milk

        List<Product> actual = inventory.removeProduct(Barcode.MILK);
        List<Product> expected = new ArrayList<>();
        expected.add(new Milk(Quality.IRIDIUM)); // The first milk is iridium.
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(6, inventory.getAllProducts().size());

        actual = inventory.removeProduct(Barcode.MILK);
        expected.clear();
        expected.add(new Milk(Quality.GOLD)); // The second milk is gold.
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(5, inventory.getAllProducts().size());

        actual = inventory.removeProduct(Barcode.MILK);
        expected.clear();
        expected.add(new Milk(Quality.SILVER)); // The third milk is silver.
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(4, inventory.getAllProducts().size());

        actual = inventory.removeProduct(Barcode.MILK);
        expected.clear();
        expected.add(new Milk(Quality.REGULAR)); // The fourth milk is regular.
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(3, inventory.getAllProducts().size());
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
        // removing one product should not affect the others
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
