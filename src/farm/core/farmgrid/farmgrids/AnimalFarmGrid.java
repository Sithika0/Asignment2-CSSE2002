package farm.core.farmgrid.farmgrids;

import farm.core.UnableToInteractException;
import farm.core.farmgrid.Animals.Animal;
import farm.core.farmgrid.Animals.Chicken;
import farm.core.farmgrid.Animals.Cow;
import farm.core.farmgrid.Animals.Sheep;
import farm.core.farmgrid.Farmer;
import farm.inventory.product.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Farm grid for an Animal farm
 */
public class AnimalFarmGrid extends FarmGrid {

    private final List<Animal> animalGrid = new ArrayList<>();

    /**
     * Creates a farm that handles animals only.
     * @param rows on the grid
     * @param columns on the grid
     * requires rows > 0 && columns > 0
     */
    public AnimalFarmGrid(int rows, int columns) {
        super(rows, columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.animalGrid.add(null);
            }
        }
    }

    /**
     * Overrides interact in FarmGrid to allow animal feeding.
     * @param command the interaction to perform
     * @param row the row coordinate
     * @param column the column coordinate
     * @return true iff the interaction is successful.
     * @throws UnableToInteractException when unknown command is given
     */
    @Override
    public boolean interact(String command, int row, int column) throws UnableToInteractException {
        if (command.equals("feed")) {
            Animal animal = this.animalGrid.get(index(row, column));
            farmer.feed(animal);
            return true;
        }
        return super.interact(command, row, column);
    }

    @Override
    public boolean place(int row, int column, char symbol) {
        if (!isWithinBounds(row, column)) {
            return false;
        }
        Animal animal = switch (symbol) {
            case '৬' -> new Chicken();
            case '४' -> new Cow();
            case 'ඔ' -> new Sheep();
            default -> null;
        };
        if (animal == null) {
            return false;
        }
        this.animalGrid.set(index(row, column), animal);
        return true;
    }

    /**
     * Removes an item from the grid
     *
     * @param row    of the item
     * @param column of the item
     */
    @Override
    public void remove(int row, int column) {
        if (isWithinBounds(row, column)) {
            this.animalGrid.set(index(row, column), null);
        }
    }

    /**
     * Returns the current state of the farm.
     * [name, symbol, fed, collected], ...
     * @return the current state of the farm.
     */
    @Override
    public List<List<String>> getStats() {
        List<List<String>> stats = new ArrayList<>();
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {

                Animal animal = this.animalGrid.get(index(i, j));

                if (animal != null) {
                    String name = animal.toString();
                    String symbol = String.valueOf(animal.getSymbol());
                    String fed = "Fed: " + animal.isFed();
                    String collected = "Collected: " + animal.isCollected();
                    stats.add(List.of(name, symbol, fed, collected));
                } else {
                    stats.add(List.of("ground", " "));
                }

            }
        }
        return stats;
    }

    @Override
    public Product harvest(int row, int column) throws UnableToInteractException {
        if (!isWithinBounds(row, column)) {
            throw new UnableToInteractException("You can't harvest this location");
        }
        Animal animal = this.animalGrid.get(index(row, column));
        if (animal == null) {
            throw new UnableToInteractException("Cannot harvest from empty spot");
        }
        if (animal.isCollected()) {
            throw new UnableToInteractException("This animal has produced "
                    + "an item already today!");
        }
        if (!animal.isFed()) {
            throw new UnableToInteractException("You have not fed this animal today!");
        }
        return farmer.harvest(animal);
    }

    /**
     * Ends the current day, and moves farm to the next day.
     * For plants, this grows the plant if possible.
     * For animals, this sets fed and collection status to false.
     */
    @Override
    public void endDay() {
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                Animal animal = this.animalGrid.get(index(i, j));
                if (animal != null) {
                    animal.resetFed();
                    animal.resetCollected();
                }
            }
        }
    }
}
