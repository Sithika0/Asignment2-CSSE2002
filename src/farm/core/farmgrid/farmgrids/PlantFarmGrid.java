package farm.core.farmgrid.farmgrids;

import farm.core.UnableToInteractException;
import farm.core.farmgrid.plants.Berry;
import farm.core.farmgrid.plants.Coffee;
import farm.core.farmgrid.plants.Plant;
import farm.core.farmgrid.plants.Wheat;
import farm.inventory.product.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Farm grid for a plant farm
 */
public class PlantFarmGrid extends FarmGrid {

    private final List<Plant> plantGrid = new ArrayList<>();

    /**
     * Creates a farm that handles plants only.
     * @param rows on the grid
     * @param columns on the grid
     * requires rows > 0 && columns > 0
     */
    public PlantFarmGrid(int rows, int columns) {
        super(rows, columns, "plant");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.plantGrid.add(null);
            }
        }
    }

    @Override
    public boolean place(int row, int column, char symbol) {
        if (!isWithinBounds(row, column)) {
            return false;
        }
        Plant plant = switch (symbol) {
            case '.' -> new Berry();
            case 'o' -> new Berry((byte) 2);
            case '@' -> new Berry((byte) 3);
            case ':' -> new Coffee();
            case ';' -> new Coffee((byte) 2);
            case '*' -> new Coffee((byte) 3);
            case '%' -> new Coffee((byte) 4);
            case 'ἴ' -> new Wheat();
            case '#' -> new Wheat((byte) 2);
            default -> null;
        };
        if (plant == null) {
            return false;
        }
        this.plantGrid.set(index(row, column), plant);
        return true;
    }

    /**
     * Removes a plant from the grid
     * @param row of the unwanted plant
     * @param column of the unwanted plant
     */
    public void remove(int row, int column) {
        if (isWithinBounds(row, column)) {
            this.plantGrid.set(index(row, column), null);
        }
    }

    /**
     * All plants grow at the end of the day
     */
    public void endDay() {
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                Plant plant = this.plantGrid.get(index(i, j));
                if (plant != null) {
                    plant.grow();
                }
            }
        }
    }

    @Override
    public Product harvest(int row, int column) throws UnableToInteractException {
        if (!isWithinBounds(row, column)) {
            throw new UnableToInteractException("You can't harvest this location");
        }
        Plant plant = this.plantGrid.get(index(row, column));
        if (plant == null) {
            throw new UnableToInteractException("Cannot harvest from empty spot");
        }
        return getFarmer().harvest(plant);
    }

    @Override
    public List<List<String>> getStats() {
        List<List<String>> stats = new ArrayList<>();
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {

                Plant plant = this.plantGrid.get(index(i, j));

                if (plant != null) {
                    String name = plant.toString();
                    String symbol = String.valueOf(plant.getSymbol());
                    String stage = "Stage: " + plant.getStage();
                    stats.add(List.of(name, symbol, stage));
                } else {
                    stats.add(List.of("ground", " "));
                }

            }
        }
        return stats;
    }
}
