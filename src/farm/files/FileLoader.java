package farm.files;

import farm.core.farmgrid.farmgrids.AnimalFarmGrid;
import farm.core.farmgrid.farmgrids.Grid;
import farm.core.farmgrid.farmgrids.PlantFarmGrid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Loads a farm game
 */
public class FileLoader {
    private String farmType;
    private int rows;
    private int columns;
    private Grid grid;

    /**
     * Constructor for the FileLoader
     */
    public FileLoader() {
    }

    /**
     * Loads contents of the specified file into a Grid.
     * @param filename the String filename to read contents from.
     * @return a grid instance.
     * @throws IOException when file loading fails.
     */
    public Grid load(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] firstLine = reader.readLine().split(" ");
            rows = Integer.parseInt(firstLine[0]);
            columns = Integer.parseInt(firstLine[1]);
            farmType = firstLine[2];
            grid = switch (farmType) {
                case "plant" -> new PlantFarmGrid(rows, columns);
                case "animal" -> new AnimalFarmGrid(rows, columns);
                default -> null;
            };
            String line;
            while ((line = reader.readLine()) != null) {

                String[] items = line.split(" ");
                if (!items[0].equals("ground")) {
                    char symbol = items[0].charAt(0);
                    int currentRow = Integer.parseInt(items[1]);
                    int currentColumn = Integer.parseInt(items[2]);
                    grid.place(currentRow, currentColumn, symbol);
                }

            }
        } catch (IOException e) {
            throw new IOException("IOException: " + e.getMessage());
        }
        return this.grid;
    }

}
