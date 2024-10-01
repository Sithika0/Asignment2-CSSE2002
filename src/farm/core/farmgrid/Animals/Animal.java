package farm.core.farmgrid.Animals;

import java.util.Objects;

/**
 * An animal on the farm.
 * '৬' -> "chicken"
 * '४' -> "cow"
 * 'ඔ' -> "sheep"
 */
public abstract class Animal {

    private final String name;
    private final char symbol;
    private boolean fed = false;
    private boolean collected = false;

    /**
     * Creates a new instance of Animal
     * All animals have a name and a symbol.
     * Animals have not been fed or collected when they are created.
     * @param name of the animal
     * @param symbol of the animal
     */
    protected Animal(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    /**
     * name of the animal.
     * @return the name of the animal
     */
    public String getAnimalName() {
        return name;
    }

    /**
     * symbol of the animal.
     * @return the symbol representing the animal
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * feed animal.
     */
    public void feed() {
        fed = true;
    }

    /**
     * reset animal fed status.
     */
    public void resetFed() {
        fed = false;
    }

    /**
     * collect objects from the animal.
     */
    public void collect() {
        collected = true;
    }

    /**
     * reset animal collected status.
     */
    public void resetCollected() {
        collected = false;
    }

    /**
     * Check if animal has been fed.
     */
    public boolean isFed() {
        return fed;
    }

    /**
     * Check if animal has been collected.
     */
    public boolean isCollected() {
        return collected;
    }

    @Override
    public String toString() {
        return name;
    }
}
