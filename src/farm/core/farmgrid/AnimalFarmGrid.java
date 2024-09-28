package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.core.farmgrid.Animals.Animal;
import farm.core.farmgrid.Plants.Berry;
import farm.core.farmgrid.Plants.Coffee;
import farm.core.farmgrid.Plants.Plant;
import farm.core.farmgrid.Plants.Wheat;
import farm.inventory.product.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Farm grid for a plant farm
 */
public class AnimalFarmGrid extends FarmGrid {

    private final int rows;
    private final int columns;
    private List<Animal> grid;
    private final Farmer farmer = new Farmer();

    /**
     * Similar to PlantFarmGrid
     * @param rows of the grid
     * @param columns of the grid
     */
    public AnimalFarmGrid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid.add(null);
            }
        }
    }

    /**
     * Interact with the specified coordinate, based on the command provided.
     * Used for interactions other than placing, removing, and harvesting.
     *
     * @param command the interaction to perform
     * @param row     the row coordinate
     * @param column  the column coordinate
     * @return true iff the interaction was successful.
     */
    @Override
    public boolean interact(String command, int row, int column) throws UnableToInteractException {
        return false;
    }

    /**
     * Place an item on the grid at the given coordinate, based on the symbol identifying it.
     * An item cannot be placed if another item already exists at that coordinate.
     *
     * @param row    the row coordinate
     * @param column the column coordinate
     * @param symbol character representing the item to be placed
     * @return true iff the item was successfully placed.
     */
    @Override
    public boolean place(int row, int column, char symbol) {
        return false;
    }

    /**
     * Attempts to harvest the product at the specified coordinate, placing the
     * resulting product into the inventory.
     *
     * @param row    row coordinate
     * @param column column coordinate
     * @return product gathered.
     */
    @Override
    public Product harvest(int row, int column) throws UnableToInteractException {
        return null;
    }

    /**
     * Generates the grid display of the farm as a String.
     */
    @Override
    public String farmDisplay() {
        return "";
    }

    /**
     * Generates information about each position on the grid, and the string representation
     * of the grid itself.
     *
     * @return List containing the type and symbol of item in each position.
     * For plants, this should include stage, for animals, fed and collection status.
     * <p>
     * Example:
     * [[berry, @, stage 3], [berry, ., stage 0], [berry, ., stage 1],
     * [coffee, %, stage 4], [@, ., ., %]]
     */
    @Override
    public List<List<String>> getStats() {
        return List.of();
    }

    /**
     * rows on the grid
     * @return the number of rows in the grid.
     */
    @Override
    public int getRows() {
        return rows;
    }

    /**
     * columns on the grid
     * @return the number of columns in the grid.
     */
    @Override
    public int getColumns() {
        return columns;
    }

    private boolean isWithinBounds(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    private int index(int row, int column) {
        return row * this.columns + column;
    }
}
