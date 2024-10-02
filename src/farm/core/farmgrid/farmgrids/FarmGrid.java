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

    private final int rows;
    private final int columns;
    private final String typeOfFarm;
    private final Farmer farmer = new Farmer();

    /**
     * Constructor for the FarmGrid, creating a farm of specified type.
     * @param rows the number of rows on the grid
     * @param columns the number of columns on the grid
     * requires rows > 0 && columns > 0
     */
    public FarmGrid(int rows, int columns, String typeOfFarm) {
        this.rows = rows;
        this.columns = columns;
        this.typeOfFarm = typeOfFarm;
    }

    /**
     * The type of farm -> animal or plant
     * Used for saving files.
     * @return the type of farm
     */
    public String getTypeOfFarm() {
        return typeOfFarm;
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getColumns() {
        return this.columns;
    }

    @Override
    public abstract boolean place(int row, int column, char symbol);

    /**
     * Removes an item from the grid
     * @param row of the item
     * @param column of the item
     */
    public abstract void remove(int row, int column);

    @Override
    public abstract Product harvest(int row, int column) throws UnableToInteractException;

    /**
     * Ends the current day, and moves farm to the next day.
     * For plants, this grows the plant if possible.
     * For animals, this sets fed and collection status to false.
     */
    public abstract void endDay();

    @Override
    public abstract List<List<String>> getStats();

    @Override
    public String farmDisplay() {
        List<List<String>> farmState = this.getStats();
        // create the fence at the top of the farm
        // two lines for each column of the farm, plus two for edges
        // and one for extra space
        String horizontalFence = "-".repeat((this.columns * 2) + 3);

        String farmDisplay;
        int count = 0;

        // start each line with a "|" fence character
        // then display symbols with a space either side

        // note System.lineSeparator() is just \n but ensures it works
        // on all operating systems.

        StringBuilder farmDisplayBuilder = new StringBuilder(horizontalFence
                + System.lineSeparator());
        for (int i = 0; i < rows; i++) { // did not increment i
            farmDisplayBuilder.append("| ");
            for (int j = 0; j < columns; j++) {
                farmDisplayBuilder.append(farmState.get(index(i, j)).get(1)).append(" ");
            }
            farmDisplayBuilder.append("|").append(System.lineSeparator());
            count++;
        }
        farmDisplay = farmDisplayBuilder.toString();
        farmDisplay += horizontalFence + System.lineSeparator();
        return farmDisplay;
    }

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

    // helper methods
    protected boolean isWithinBounds(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    protected int index(int row, int column) {
        return row * this.columns + column;
    }

    protected Farmer getFarmer() {
        return farmer;
    }
}
