package farm.core.farmgrid.Plants;

/**
 * A berry on the farm.
 */
public class Berry extends Plant {
    /**
     * Creates a new instance of Berry
     * Stage 0 = .
     * Stage 1 = .
     * Stage 2 = o
     * Stage 3 = @
     */
    public Berry() {
        super("berry", new char[]{'.', '.', 'o', '@'});
    }
}
