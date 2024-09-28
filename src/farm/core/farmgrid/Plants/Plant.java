package farm.core.farmgrid.Plants;

import java.util.Arrays;
import java.util.Objects;

/**
 * A plant on the farm.
 */
public abstract class Plant {

    private final String name;
    private final char[] symbols;
    private byte stage = 0; // the highest possible stage of a plant is 127.

    /**
     * Creates a new instance of Plant
     * All plants start at stage 0.
     * @param name the name of the plant
     * @param symbols the symbol representing the plant
     */

    protected Plant(String name, char[] symbols) {
        this.name = name;
        this.symbols = symbols;
    }

    /**
     * name of the plant.
     * @return the name of the plant
     */
    public String getPlantName() {
        return name;
    }

    /**
     * symbol of the plant.
     * @return the symbol representing the plant
     */
    public char[] getSymbols() {
        return symbols;
    }

    /**
     * symbol of a plant depending on its stage.
     * @return symbol of the plant at its current stage
     */
    public char getSymbol() {
        return symbols[stage];
    }

    /**
     * stage of the plant.
     * @return the stage of the plant
     */
    public byte getStage() {
        return stage;
    }

    /**
     * The plant will grow to the next stage.
     * If plant is in last stage it will stop growing.
     */
    public void grow() {
        if (stage < symbols.length - 1) {
            stage++;
        }
    }

    /**
     * reset plant to stage 0.
     */
    public void resetPlantGrowth() {
        stage = 0;
    }

    @Override
    public String toString() {
        return String.valueOf(symbols[stage]);
    }

    /**
     * Plants are equal iff they have the same name, stage, and symbols array.
     * @param obj the object to compare
     * @return true iff the objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Plant plant = (Plant) obj;
        return getPlantName().equals(plant.getPlantName()) && getStage() == plant.getStage();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, Arrays.hashCode(symbols));
    }
}
