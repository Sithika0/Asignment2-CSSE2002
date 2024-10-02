package farm.core.farmgrid.plants;

/**
 * A wheat on the farm.
 */
public class Wheat extends Plant {
    /**
     * Creates a new instance of Wheat
     * Stage 0 = ἴ
     * Stage 1 = ἴ
     * Stage 2 = #
     */
    public Wheat() {
        super("wheat", new char[]{'ἴ', 'ἴ', '#'});
    }

    /**
     * Loading and saving files
     * @param stage of plant
     */
    public Wheat(byte stage) {
        super("wheat", new char[]{'ἴ', 'ἴ', '#'}, stage);
    }
}
