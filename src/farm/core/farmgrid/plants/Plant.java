package farm.core.farmgrid.plants;

/**
 * A plant on the farm.
 */
public abstract class Plant {

    private final String name;
    private final char[] symbols;
    // Plants start at stage 1.
    private byte stage = 1; // the highest possible stage of a plant is 127.

    /**
     * Creates a new instance of Plant
     * When a plant is placed is starts at stage 1.
     * When a plant is harvested it goes to stage 0.
     * Stage 0 and Stage 1 have the same symbol.
     * @param name the name of the plant
     * @param symbols the symbol representing the plant
     */

    protected Plant(String name, char[] symbols) {
        this.name = name;
        this.symbols = symbols;
    }

    protected Plant(String name, char[] symbols, byte stage) {
        this.name = name;
        this.symbols = symbols;
        this.stage = stage;
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
        return name;
    }

}
