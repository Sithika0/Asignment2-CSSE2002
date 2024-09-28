package farm.debugged;

import farm.core.UnableToInteractException;
import farm.core.farmgrid.Grid;
import farm.inventory.product.*;
import farm.inventory.product.data.Quality;
import farm.inventory.product.data.RandomQuality;

import java.util.ArrayList;
import java.util.List;


/**
 * The FarmGrid is a mini-game of farm game
 * where you can place animals and plants on a grid.
 */
public class FarmGrid implements Grid {

    private final List<List<String>> farmState = new ArrayList<>();
    private final RandomQuality randomQuality = new RandomQuality();
    private final int rows;
    private final int columns;
    private final String farmType;

    /**
     * Constructor for the FarmGrid, creating a farm of specified type.
     * @param rows the number of rows on the grid
     * @param columns the number of columns on the grid
     * @requires rows > 0 && columns > 0
     */
    public FarmGrid(int rows, int columns, String farmType) {

        this.farmType = farmType;
        this.rows = rows; // swap rows and columns
        this.columns = columns;

        // populate the initial farm with empty ground
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                List<String> spotOnGrid = new ArrayList<>();
                spotOnGrid.add("ground");
                spotOnGrid.add(" ");
                farmState.add(spotOnGrid);
            }
        }
    }

    /**
     * Default constructor for the FarmGrid, creating a plant farm.
     * NOTE: whatever class you implement that extends Grid *must* have a constructor
     * with this signature for testing purposes.
     *
     * @param rows the number of rows on the grid
     * @param columns the number of columns on the grid
     * @requires rows > 0 && columns > 0
     */
    public FarmGrid(int rows, int columns) {
        this.farmType = "plant";
        this.rows = rows;
        this.columns = columns;

        // populate the initial farm with empty ground
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                List<String> spotOnGrid = new ArrayList<>();
                spotOnGrid.add("ground");
                spotOnGrid.add(" ");
                farmState.add(spotOnGrid);
            }
        }
    }

    /**
     * return the type of farm
     * @return the type of farm
     */
    public String getFarmType() {
        return this.farmType;
    }

    @Override
    public boolean place(int row, int column, char symbol) {

        int positionIndex = row * this.columns + column;
        // if coordinates are out of bounds return false
        if (row > this.rows - 1 || row < 0) {
            return false;
        }
        if (column > this.columns - 1 || column < 0) {
            return false;
        }

        // get the name of the item based on the character
        String itemName = switch (symbol) {
            case '.' -> "berry";
            case ':' -> "coffee";
            case 'ἴ' -> "wheat";
            case '৬' -> "chicken";
            case '४' -> "cow";
            case 'ඔ' -> "sheep";
            default -> "false";
        };

        if (itemName.equals("false")) {
            return false;
        }

        String symbolString = String.valueOf(symbol);

        try {
            if (!this.farmState.get(positionIndex).get(1).equals(" ")) {
                throw new IllegalStateException("Something is already there!");
            } else {
                this.farmState.get(positionIndex).set(1, symbolString);
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }


        // if the item to place is an animal
        if (this.farmType.equals("animal")) {
            if (itemName.equals("chicken") || itemName.equals("cow") || itemName.equals("sheep")) {
                List<String> newPositionInfo = List.of(itemName, symbolString, "Fed: false",
                        "Collected: false");
                this.farmState.set(positionIndex, newPositionInfo);
                return true;
            } else {
                throw new IllegalArgumentException("You cannot place that on a animal farm!");
            }
        } else if (this.farmType.equals("plant")) {
            if (itemName.equals("berry") || itemName.equals("coffee") || itemName.equals("wheat")) {
                List<String> newPositionInfo = List.of(itemName, symbolString, "Stage: 1");
                this.farmState.set(positionIndex, newPositionInfo);
                return true;
            } else {
                throw new IllegalArgumentException("You cannot place that on a plant farm!");
            }
        }
        throw new IllegalArgumentException("Something went wrong while placing");
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getColumns() {
        return this.columns;
    }

    /**
     * Removes an item from the grid
     * @param row of the item
     * @param column of the item
     */
    public void remove(int row, int column) {
        // if coordinates are out of bounds return none
        if (row > this.rows - 1 || row < 0) {
            return;
        }
        if (column > this.columns - 1 || column < 0) {
            return;
        }

        int positionIndex = row * this.columns + column;

        // replace the spot with empty ground
        List<String> spotOnGrid = new ArrayList<>();
        spotOnGrid.add("ground");
        spotOnGrid.add(" ");
        farmState.set(positionIndex, spotOnGrid);
    }

    @Override
    public String farmDisplay() {
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
                int positionIndex = i * this.columns + j;
                farmDisplay += farmState.get(positionIndex).get(1) + " ";
            }
            farmDisplay += "|" + System.lineSeparator();
            count++;
        }
        farmDisplay += horizontalFence + System.lineSeparator();
        return farmDisplay;
    }

    @Override
    public List<List<String>> getStats() {
        return getTheFarmStatsList();
    }

    @Override
    public Product harvest(int row, int column) throws UnableToInteractException {
        int positionIndex = row * this.columns + column;

        if (row > this.rows - 1 || row < 0) {
            throw new UnableToInteractException("You can't harvest this location");
        }
        if (column > this.columns - 1 || column < 0) {
            throw new UnableToInteractException("You can't harvest this location");
        }

        List<String> positionInfo;
        try {
            positionInfo = farmState.get(positionIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new UnableToInteractException("You can't harvest this location");
        }

        // throw an exception if you try to harvest empty ground
        if (positionInfo.get(0).equals("ground")) {
            throw new UnableToInteractException("You can't harvest an empty spot!");
        }


        // if the position contains an animal
        if ((positionInfo.get(0).equals("cow") || positionInfo.get(0).equals("chicken")
                || positionInfo.get(0).equals("sheep")) && this.farmType.equals("animal")) {
            // check fed and collected status of animal
            if (positionInfo.get(2).equals("Fed: false")) {
                throw new UnableToInteractException("You have not fed this animal today!");
            } else if (positionInfo.get(3).equals("Collected: true")) {
                //animals can only produce once per day
                throw new UnableToInteractException("This animal has produced "
                        + "an item already today!");
            } else if (positionInfo.get(2).equals("Fed: true")
                    && positionInfo.get(3).equals("Collected: false")) {
                // get a random quality

                Quality quality = randomQuality.getRandomQuality();

                //return product and set collected to true
                switch (positionInfo.get(0)) {
                    case "cow" -> {
                        positionInfo = List.of(positionInfo.get(0),
                                positionInfo.get(1), positionInfo.get(2),
                                "Collected: true");
                        farmState.set(positionIndex, positionInfo);
                        return new Milk(quality); // Why is a cow returning eggs?
                    }
                    case "chicken" -> {
                        positionInfo = List.of(positionInfo.get(0),
                                positionInfo.get(1), positionInfo.get(2),
                                "Collected: true");
                        farmState.set(positionIndex, positionInfo);
                        return new Egg(quality);
                    }
                    case "sheep" -> {
                        positionInfo = List.of(positionInfo.get(0),
                                positionInfo.get(1), positionInfo.get(2),
                                "Collected: true");
                        farmState.set(positionIndex, positionInfo);
                        return new Wool(quality);
                    }
                }
            } else {
                throw new UnableToInteractException("You have an animal on a plant farm!");
            }
        } else if ((positionInfo.get(0).equals("wheat")
                || positionInfo.get(0).equals("berry") || positionInfo.get(0).equals("coffee"))
                && this.farmType.equals("plant")) {
            // get a random quality
            Quality quality = randomQuality.getRandomQuality();


            // check stage
            switch (positionInfo.get(0)) {
                case "wheat" -> {
                    if (positionInfo.get(1).equals("#")) {
                        positionInfo = List.of(positionInfo.getFirst(), "ἴ", "Stage: 0");
                        farmState.set(positionIndex, positionInfo);
                        return new Bread(quality);
                    } else {
                        throw new UnableToInteractException("The crop is not fully grown!");
                    }
                }
                case "coffee" -> {
                    if (positionInfo.get(1).equals("%")) {
                        positionInfo = List.of(positionInfo.getFirst(), ":", "Stage: 0");
                        farmState.set(positionIndex, positionInfo);
                        return new Coffee(quality);
                    } else {
                        throw new UnableToInteractException("The crop is not fully grown!");
                    }
                }
                case "berry" -> {
                    if (positionInfo.get(1).equals("@")) {
                        positionInfo = List.of(positionInfo.getFirst(), ".", "Stage: 0");
                        farmState.set(positionIndex, positionInfo);
                        return new Jam(quality);
                    } else {
                        throw new UnableToInteractException("The crop is not fully grown!");
                    }
                }
            }
        } else {
            throw new UnableToInteractException("You have a plant on a animal farm!");
        }
        return null;
    }

    @Override
    public boolean interact(String command, int row, int column) throws UnableToInteractException {
        // if feeding an animal
        switch (command) {
            case "feed" -> {
                if (this.farmType.equals("animal")) {
                    return feed(row, column);
                } else {
                    throw new UnableToInteractException("You cannot feed something "
                            + "that is not an animal!");
                }
            }
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

    /**
     * Feed an animal at the specified location.
     * @param row the row coordinate
     * @param column the column coordinate
     * @return true iff the animal was fed, else false.
     */
    public boolean feed(int row, int column) {
        // if coordinates are out of bounds return false
        if (row > this.rows - 1 || row < 0) {
            return false;
        }
        if (column > this.columns - 1 || column < 0) {
            return false;
        }

        int positionIndex = row * this.columns + column;

        List<String> animalsInAList = List.of("cow", "chicken", "sheep");
        // if the position coordinate is an animal
        if (animalsInAList.contains(this.farmState.get(positionIndex).getFirst())) {
            List<String> positionInfo = farmState.get(positionIndex);
            // set fed to true
            farmState.set(positionIndex, List.of(positionInfo.get(0), positionInfo.get(1),
                    "Fed: true", positionInfo.get(3)));
            return true;
        }
        return false;
    }


    /**
     * Ends the current day, and moves farm to the next day.
     * For plants, this grows the plant if possible.
     * For animals, this sets fed and collection status to false.
     */
    public void endDay() {
        List<String> plantsThatCanGrow = List.of(".", "o", "ἴ", ":", ";", "*");
        List<String> animalSymbols = List.of("४", "ඔ", "৬");

        int i = 0;
        for (List<String> itemInfo : farmState) {
            if (this.farmType.equals("plant")) {
                // if the plant is not at a final stage, increment stage and symbol
                if (plantsThatCanGrow.contains(itemInfo.get(1))) {
                    // berries
                    switch (itemInfo.get(1)) {
                        case "." -> {
                            if (itemInfo.get(2).equals("Stage: 0")) {
                                farmState.set(i, List.of("berry", ".", "Stage: 1"));
                            } else if (itemInfo.get(2).equals("Stage: 1")) {
                                farmState.set(i, List.of("berry", "o", "Stage: 2"));
                            }
                        }
                        case "o" -> farmState.set(i, List.of("berry", "@", "Stage: 3"));
                        case "ἴ" -> {
                            // wheat
                            if (itemInfo.get(2).equals("Stage: 0")) {
                                farmState.set(i, List.of("wheat", "ἴ", "Stage: 1"));
                            } else {
                                farmState.set(i, List.of("wheat", "#", "Stage: 2"));
                            }
                        }
                        case ":" -> {
                            // coffees
                            if (itemInfo.get(2).equals("Stage: 0")) {
                                farmState.set(i, List.of("coffee", ":", "Stage: 1"));
                            } else if (itemInfo.get(2).equals("Stage: 1")) {
                                farmState.set(i, List.of("coffee", ";", "Stage: 2"));
                            }
                        }
                        case ";" -> farmState.set(i, List.of("coffee", "*", "Stage: 3"));
                        case "*" -> farmState.set(i, List.of("coffee", "%", "Stage: 4"));
                    }
                }
            } else if (this.farmType.equals("animal")) {
                // if animal, reset fed and collected to false
                if (animalSymbols.contains(itemInfo.get(1))) {
                    String symbol = itemInfo.get(1);
                    String name = itemInfo.get(0);
                    farmState.set(i, List.of(name, symbol, "Fed: false", "Collected: false"));
                }
            }

            // increment position in list
            i++;
        }
    }

    /**
     * Method for retrieving the stats for the current farm.
     * @return the list describing the current farm state
     */
    public List<List<String>> getTheFarmStatsList() {
        return farmState;
    }

}
