package farm.core.farmgrid.farmgrids;

import farm.core.UnableToInteractException;
import farm.core.farmgrid.Farmer;
import farm.inventory.product.*;
import farm.inventory.product.data.RandomQuality;

import java.util.List;


/**
 * The FarmGrid is a mini-game of farm game
 * where you can place animals and plants on a grid.
 */
public abstract class FarmGrid implements Grid {

    private final RandomQuality randomQualityGenerator = new RandomQuality();
    private final int rows;
    private final int columns;
    protected final Farmer farmer = new Farmer();

    /**
     * Constructor for the FarmGrid, creating a farm of specified type.
     * @param rows the number of rows on the grid
     * @param columns the number of columns on the grid
     * @requires rows > 0 && columns > 0
     */
    public FarmGrid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public abstract boolean place(int row, int column, char symbol);

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getColumns() {
        return this.columns;
    }

    /**
     * Removes an item from the grid
     * @param row of the item
     * @param column of the item
     */
    public abstract void remove(int row, int column);

    @Override
    public String farmDisplay() {
        List<List<String>> farmState = this.getStats();
        // create the fence at the top of the farm
        // two lines for each column of the farm, plus two for edges
        // and one for extra space
        String horizontalFence = "-".repeat((this.columns * 2) + 3);

        String farmDisplay = horizontalFence + System.lineSeparator();
        int count = 0;

        // start each line with a "|" fence character
        // then display symbols with a space either side

        // note System.lineSeparator() is just \n but ensures it works
        // on all operating systems.

        for (int i = 0; i < rows; i++) { // did not increment i
            farmDisplay += "| ";
            for (int j = 0; j < columns; j++) {
                farmDisplay += farmState.get(index(i, j)).get(1) + " ";
            }
            farmDisplay += "|" + System.lineSeparator();
            count++;
        }
        farmDisplay += horizontalFence + System.lineSeparator();
        return farmDisplay;
    }

    @Override
    public abstract List<List<String>> getStats();

    @Override
    public abstract Product harvest(int row, int column) throws UnableToInteractException;


    @Override
    public boolean interact(String command, int row, int column) throws UnableToInteractException {
        // if feeding an animal
        switch (command) {
            case "end-day" -> {
                this.endDay();
                return true;
            }
            case "remove" -> {
                this.remove(row, column);
                return true;
            }
        }
        throw new UnableToInteractException("Unknown command: " + command);
    }

    /**
     * Ends the current day, and moves farm to the next day.
     * For plants, this grows the plant if possible.
     * For animals, this sets fed and collection status to false.
     */
    public abstract void endDay();

    protected boolean isWithinBounds(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    protected int index(int row, int column) {
        return row * this.columns + column;
    }

}
