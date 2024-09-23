package farm.inventory;

import farm.core.FailedTransactionException;
import farm.core.InvalidStockRequestException;
import farm.inventory.product.*;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import org.junit.*;

import java.util.*;

public class FancyInventoryTest {
    Inventory inventory;

    /**
     * Reset the inventory before each test.
     */
    @Before
    public void setup() {
        inventory = new FancyInventory();
    }

    /**
     * Checks the null case
     * When no products are added, the inventory should return an empty list.
     */
    @Test
    public void addNoProductsTest() {
        Assert.assertEquals(0, inventory.getAllProducts().size());
        Assert.assertFalse(inventory.existsProduct(Barcode.MILK));
    }

    /**
     * One product is added to the inventory.
     * The inventory should return a list with that product.
     * The products should be ordered by barcode.values()
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
     * The products should be ordered by barcode.values()
     */
    @Test
    public void addMultipleProductsOneByOneTest() {
        List<Product> basicExpected = new ArrayList<>();
        //Add regular milk
        inventory.addProduct(Barcode.MILK, Quality.REGULAR);
        basicExpected.add(new Milk(Quality.REGULAR));
        //Add gold milk
        inventory.addProduct(Barcode.MILK, Quality.GOLD);
        basicExpected.add(new Milk(Quality.GOLD));
        //Add iridium coffee
        inventory.addProduct(Barcode.COFFEE, Quality.IRIDIUM);
        basicExpected.add(new Coffee(Quality.IRIDIUM));
        //Add silver bread
        inventory.addProduct(Barcode.BREAD, Quality.SILVER);
        basicExpected.add(new Bread(Quality.SILVER));
        //Add regular wool
        inventory.addProduct(Barcode.WOOL, Quality.REGULAR);
        basicExpected.add(new Wool(Quality.REGULAR));

        List<Product> fancyExpected = sortProducts(basicExpected);

        List<Product> actual = inventory.getAllProducts();
        Assert.assertEquals(fancyExpected, actual);
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
     * Check if multiple products can be added and returned.
     * Multiple products are added at once.
     * The products should be ordered by barcode.values()
     */
    @Test
    public void addMultipleProductAtOnceTest() {
        List<Product> basicExpected = new ArrayList<>();
        int sumOfProducts = 0;
        try {
            //Add regular milk
            inventory.addProduct(Barcode.MILK, Quality.REGULAR, 1);
            basicExpected.add(new Milk(Quality.REGULAR));
            sumOfProducts += 1;
            //Add gold milk
            inventory.addProduct(Barcode.MILK, Quality.GOLD, 2);
            for (int i = 0; i < 2; i++) {
                basicExpected.add(new Milk(Quality.GOLD));
                sumOfProducts += 1;
            }
            //Add iridium coffee
            inventory.addProduct(Barcode.COFFEE, Quality.IRIDIUM, 3);
            for (int i = 0; i < 3; i++) {
                basicExpected.add(new Coffee(Quality.IRIDIUM));
                sumOfProducts += 1;
            }
            //Add silver bread
            inventory.addProduct(Barcode.BREAD, Quality.SILVER, 4);
            for (int i = 0; i < 4; i++) {
                basicExpected.add(new Bread(Quality.SILVER));
                sumOfProducts += 1;
            }
            //Add regular wool
            inventory.addProduct(Barcode.WOOL, Quality.REGULAR, 5);
            for (int i = 0; i < 5; i++) {
                basicExpected.add(new Wool(Quality.REGULAR));
                sumOfProducts += 1;
            }
        } catch (InvalidStockRequestException e) {
            Assert.fail("This should not throw an exception");
        }

        List<Product> fancyExpected = sortProducts(basicExpected);

        List<Product> actual = inventory.getAllProducts();
        Assert.assertEquals(fancyExpected, actual);
        Assert.assertTrue(inventory.existsProduct(Barcode.MILK));
        Assert.assertTrue(inventory.existsProduct(Barcode.COFFEE));
        Assert.assertTrue(inventory.existsProduct(Barcode.BREAD));
        Assert.assertTrue(inventory.existsProduct(Barcode.WOOL));
        Assert.assertFalse(inventory.existsProduct(Barcode.EGG));
        Assert.assertFalse(inventory.existsProduct(Barcode.JAM));
        // There should only be products in the inventory
        Assert.assertEquals(sumOfProducts, inventory.getAllProducts().size());
    }

    /**
     * Adding negative quantity of products should throw an Exception.
     */
    @Test(expected = Exception.class)
    public void addNegativeQuantityTest() throws Exception {
        //inventory.addProduct(Barcode.MILK, Quality.REGULAR, 0);
        inventory.addProduct(Barcode.MILK, Quality.REGULAR, -1);
        inventory.addProduct(Barcode.MILK, Quality.REGULAR, -200);
        Assert.fail("This should have thrown an exception!");
    }

    /**
     * When a product is removed, the removed product should no longer exist in the inventory.
     */
    @Test
    public void removedProductShouldNotExistTest() {
        inventory.addProduct(Barcode.BREAD, Quality.REGULAR);
        inventory.addProduct(Barcode.MILK, Quality.REGULAR);
        inventory.removeProduct(Barcode.MILK);
        // Milk should not be in inventory because it was removed.
        Assert.assertFalse(inventory.existsProduct(Barcode.MILK));
        // removing one product should not affect the others
        Assert.assertTrue(inventory.existsProduct(Barcode.BREAD));
        Assert.assertEquals(1, inventory.getAllProducts().size());
    }

    /**
     * Removing multiple products at once
     * The removed product should be the highest quality that is
     * available in the inventory.
     */
    @Test
    public void removeMultipleProductsAtOnceTest() {
        try {
            inventory.addProduct(Barcode.MILK, Quality.GOLD, 1);
            inventory.addProduct(Barcode.MILK, Quality.SILVER, 2);
            inventory.addProduct(Barcode.MILK, Quality.IRIDIUM, 1);
            inventory.addProduct(Barcode.MILK, Quality.REGULAR, 2);

            List<Product> removedProducts = inventory.removeProduct(Barcode.MILK, 3);
            List<Product> expected = new ArrayList<>();
            expected.add(new Milk(Quality.IRIDIUM));
            expected.add(new Milk(Quality.GOLD));
            expected.add(new Milk(Quality.SILVER));

            Assert.assertEquals(3, removedProducts.size()); // defensive programming
            Assert.assertEquals(expected, removedProducts);
        } catch (InvalidStockRequestException | FailedTransactionException e) {
            Assert.fail("This should not throw an exception" + e.getMessage());
        }
    }

    /**
     * Helper method for sorting products.
     * Sorts products by barcode.values().
     */
    private List<Product> sortProducts(List<Product> unsortedProducts) {
        List<Product> sorted = new ArrayList<>();
        for (Barcode barcode : Barcode.values()) {
            for (Product product : unsortedProducts) {
                if (product.getBarcode() == barcode) {
                    sorted.add(product);
                }
            }
        }
        return sorted;
    }
}
