package farm.core.farmgrid;

import farm.core.UnableToInteractException;
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
public class PlantFarmGrid implements Grid {

    private final int rows;
    private final int columns;
    private List<Plant> grid;
    private final Farmer farmer = new Farmer();

    public PlantFarmGrid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid.add(null);
            }
        }
    }

    @Override
    public boolean interact(String command, int row, int column) throws UnableToInteractException {
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

    @Override
    public boolean place(int row, int column, char symbol) {
        if (!isWithinBounds(row, column)) {
            return false;
        }
        Plant plant = switch (symbol) {
            case '.' -> new Berry();
            case ':' -> new Coffee();
            case 'ἴ' -> new Wheat();
            default -> null;
        };
        if (plant == null) {
            return false;
        }
        grid.set(index(row, column), plant);
        return true;
    }

    public void remove(int row, int column) {
        if (isWithinBounds(row, column)) {
            grid.set(index(row, column), null);
        }
    }

    public void endDay() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Plant plant = grid.get(index(i, j));
                if (plant != null) {
                    plant.grow();
                }
            }
        }
    }

    @Override
    public Product harvest(int row, int column) throws UnableToInteractException {
        if (!isWithinBounds(row, column)) {
            throw new UnableToInteractException("Cannot harvest outside of the farm");
        }
        Plant plant = grid.get(index(row, column));
        if (plant == null) {
            throw new UnableToInteractException("Cannot harvest from empty spot");
        }
        return farmer.harvest(plant);
    }

    @Override
    public String farmDisplay() {
        List<List<String>> farmState = getStats();
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

        for (int i = 0; i < this.rows; i++) { // did not increment i
            farmDisplay += "| ";
            for (int j = 0; j < this.columns; j++) {
                farmDisplay += farmState.get(index(i, j)).get(1) + " ";
            }
            farmDisplay += "|" + System.lineSeparator();
            count++;
        }
        farmDisplay += horizontalFence + System.lineSeparator();
        return farmDisplay;
    }

    @Override
    public List<List<String>> getStats() {
        List<List<String>> stats = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                Plant plant = grid.get(index(i, j));

                if (plant != null) {
                    String name = plant.toString();
                    String symbol = String.valueOf(plant.getSymbol());
                    String stage = "Stage: " + (plant.getStage() + 1);
                    stats.add(List.of(name, symbol, stage));
                } else {
                    stats.add(List.of("ground", " "));
                }

            }
        }
        return stats;
    }

    @Override
    public int getRows() {
        return rows;
    }

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
