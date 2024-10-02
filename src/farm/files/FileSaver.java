package farm.files;

import farm.core.farmgrid.farmgrids.Grid;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Saves the game
 */
public class FileSaver {

    /**
     * Constructor for the FileSaver class.
     */
    public FileSaver() {

    }

    /**
     * Saves the contents of the provided grid and farmtype to a file with specified name.
     * @param filename name of the file
     * @param grid the grid instance to save to a file
     * @throws IOException when a file saving fails
     */
    public void save(String filename, Grid grid) throws IOException {
        String farmType;
        String symbol;
        List<List<String>> farmState = grid.getStats();
        farmType = determineFarmType(farmState);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(grid.getRows() + " " + grid.getColumns() + " "
                    + determineFarmType(grid.getStats()));
            writer.newLine();

            int[] coordinates = {0, 0};

            if (farmType.equals("plant") || farmType.equals("animal")) {
                // symbol, currentRow, currentColumn
                for (List<String> item : farmState) {
                    if (item.getFirst().equals("ground")) {
                        writer.write("ground");
                        writer.newLine();
                    } else {
                        symbol = item.get(1).strip();
                        writer.write(symbol + " "
                                + coordinates[0] + " "
                                + coordinates[1]);
                        writer.newLine();
                    }

                    coordinates = incrementCoordinates(coordinates, grid);

                }
            } else {
                throw new IllegalArgumentException(farmState
                        + "has not been implemented in FileSaver");
            }


        } catch (IOException e) {
            throw new IOException("IOException: " + e.getMessage());
        }
    }

    /**
     * requires that the farm state is type, animal or plant.
     * If the given farm is empty (nothing has been placed in the farm)
     * then, this method assumes the farm is a plant farm.
     *
     * @param farmState only plant and animal farms accepted
     * @return plant farm or animal farm
     */
    private String determineFarmType(List<List<String>> farmState) {

        for (List<String> entry : farmState) {
            if (entry.size() == 3) {
                return "plant";
            }
            if (entry.size() == 4) {
                return "animal";
            }
        }
        return "plant";
    }

    private int[] incrementCoordinates(int[] coordinates, Grid grid) {
        int currentRow = coordinates[0];
        int currentColumn = coordinates[1];

        if (currentColumn < grid.getColumns() - 1) {
            currentColumn++;
        } else {
            currentColumn = 0;
            currentRow++;
        }
        return new int[]{currentRow, currentColumn};
    }
}
