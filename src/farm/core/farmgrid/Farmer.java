package farm.core.farmgrid;

import farm.core.UnableToInteractException;
import farm.core.farmgrid.animals.Animal;
import farm.core.farmgrid.plants.Plant;
import farm.inventory.product.*;
import farm.inventory.product.data.Quality;
import farm.inventory.product.data.RandomQuality;

/**
 * A person who works on the farm.
 * The farmer can harvest plants or animals.
 * The farmer can also feed animals.
 */
public class Farmer {
    private final RandomQuality randomQuality = new RandomQuality();

    /**
     * Harvest the plant and reset the plant.
     * @param plant that is to be harvested.
     * @return the product of the plant.
     * @throws UnableToInteractException if the plant is not ready to be harvested.
     */
    public Product harvest(Plant plant) throws UnableToInteractException {
        if (isReadyToHarvest(plant)) {
            plant.resetPlantGrowth();
            return process(plant.getPlantName());
        } else {
            throw new UnableToInteractException("The crop is not fully grown!");
        }
    }

    /**
     * Harvest the animal if possible. When the animal is harvested, it is collected.
     * @param animal that is to be harvested.
     * @return the product of the animal.
     * @throws UnableToInteractException if the animal has not been fed or has already been collected.
     */
    public Product harvest(Animal animal) throws UnableToInteractException {
        if (isReadyToHarvest(animal)) {
            animal.collect();
            return process(animal.getAnimalName());
        } else {
            throw new UnableToInteractException("The animal is not ready to be harvested!");
        }
    }

    /**
     * The farmer feeds the animal.
     * @param animal that is to be fed.
     */
    public void feed(Animal animal) {
        animal.feed();
    }

    // Helper methods

    /**
     * Return true iff the plant is ready to be harvested.
     * @return true iff the plant is in its final stage
     */
    private boolean isReadyToHarvest(Plant plant) {
        return plant.getSymbol() == plant.getSymbols()[plant.getSymbols().length - 1];
    }

    /**
     * Return true iff the animal is ready to be harvested.
     * @return true iff animal has been fed and not collected.
     */
    private boolean isReadyToHarvest(Animal animal) {
        return animal.isFed() && !animal.isCollected();
    }

    /**
     * Process the animal or plant into a product.
     * @param plantOrAnimal that is to be processed
     * @return the product of the processing
     */
    private Product process(String plantOrAnimal) throws UnableToInteractException {
        Quality quality = randomQuality.getRandomQuality();
        switch (plantOrAnimal) {
            case "wheat" -> {
                return new Bread(quality);
            }
            case "coffee" -> {
                return new Coffee(quality);
            }
            case "berry" -> {
                return new Jam(quality);
            }
            case "chicken" -> {
                return new Egg(quality);
            }
            case "cow" -> {
                return new Milk(quality);
            }
            case "sheep" -> {
                return new Wool(quality);
            }
            default -> throw new IllegalArgumentException("Unknown: " + plantOrAnimal
                    + " has not been implemented in "
                    + "the farmer class.");
        }
    }
}
