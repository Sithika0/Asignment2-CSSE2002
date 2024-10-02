package farm.core.farmgrid.plants;

/**
 * A coffee on the farm.
 */
public class Coffee extends Plant {
    /**
     * Creates a new instance of Coffee
     * Stage 0 = :
     * Stage 1 = :
     * Stage 2 = ;
     * Stage 3 = *
     * Stage 4 = %
     */
    public Coffee() {
        super("coffee", new char[]{':', ':', ';', '*', '%'});
    }

    /**
     * Loading and saving files
     * @param stage of plant
     */
    public Coffee(byte stage) {
        super("coffee", new char[]{':', ':', ';', '*', '%'}, stage);
    }
}
