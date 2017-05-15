
import cosc343.assig2.World;
import cosc343.assig2.Creature;
import java.util.*;
import java.io.*;

/**
 * The MyWorld extends the cosc343 assignment 2 World. Here you can set some
 * variables that control the simulations and override functions that generate
 * populations of creatures that the World requires for its simulations.
 *
 * @author Alexis Barltrop
 * @version 1.0
 * @since 2017-04-05
 */
public class MyWorld extends World {

    /* Here you can specify the number of turns in each simulation
   * and the number of generations that the genetic algorithm will 
   * execute.
     */
    private final int _numTurns = 100;
    private final int _numGenerations = 2000;

    private static final String DATAFILENAME = "dataout100002percent.csv";

    /* The main function for the MyWorld application
     */
    public static void main(String[] args) {
        // Here you can specify the grid size, window size and whether torun
        // in repeatable mode or not
        int gridSize = 35;
        int windowWidth = 1400;
        int windowHeight = 1400;
        boolean repeatableMode = true;

        /* Here you can specify percept format to use - there are three to
         chose from: 1, 2, 3.  Refer to the Assignment2 instructions for
         explanation of the three percept formats.
         */
        int perceptFormat = 3;
        
        File outputFile = new File(DATAFILENAME);
            outputFile.delete();

        // Instantiate MyWorld object.  The rest of the application is driven
        // from the window that will be displayed.
        MyWorld sim = new MyWorld(gridSize, windowWidth, windowHeight, repeatableMode, perceptFormat);
    }

    /** Constructor.  
   
     Input: griSize - the size of the world
            windowWidth - the width (in pixels) of the visualisation window
            windowHeight - the height (in pixels) of the visualisation window
            repeatableMode - if set to true, every simulation in each
                             generation will start from the same state
            perceptFormat - format of the percepts to use: choice of 1, 2, or 3
     */
    public MyWorld(int gridSize, int windowWidth, int windowHeight, boolean repeatableMode, int perceptFormat) {
        // Initialise the parent class - don't remove this
        super(gridSize, windowWidth, windowHeight, repeatableMode, perceptFormat);

        // Set the number of turns and generations
        this.setNumTurns(_numTurns);
        this.setNumGenerations(_numGenerations);

    }

    /** The MyWorld class must override this function, which is
     used to fetch a population of creatures at the beginning of the
     first simulation.  This is the place where you need to  generate
     a set of creatures with random behaviours.
  
     Input: numCreatures - this variable will tell you how many creatures
                           the world is expecting
                            
     Returns: An array of MyCreature objects - the World will expect numCreatures
              elements in that array     
     */
    @Override
    public MyCreature[] firstGeneration(int numCreatures) {

        int numPercepts = this.expectedNumberofPercepts();
        int numActions = this.expectedNumberofActions();

        // This is just an example code.  You may replace this code with
        // your own that initialises an array of size numCreatures and creates
        // a population of your creatures
        MyCreature[] population = new MyCreature[numCreatures];
        for (int i = 0; i < numCreatures; i++) {
            population[i] = new MyCreature(numPercepts, numActions);
        }
        return population;
    }

    /* The MyWorld class must override this function, which is
     used to fetch the next generation of the creatures.  This World will
     proivde you with the old_generation of creatures, from which you can
     extract information relating to how they did in the previous simulation...
     and use them as parents for the new generation.
  
     Input: old_population_btc - the generation of old creatures before type casting. 
                              The World doesn't know about MyCreature type, only
                              its parent type Creature, so you will have to
                              typecast to MyCreatures.  These creatures 
                              have been simulated over and their state
                              can be queried to compute their fitness
            numCreatures - the number of elements in the old_population_btc
                           array
                        
                            
  Returns: An array of MyCreature objects - the World will expect numCreatures
           elements in that array.  This is the new population that will be
           use for the next simulation.  
     */
    @Override
    public MyCreature[] nextGeneration(Creature[] old_population_btc, int numCreatures) {
        // Typcast old_population of Creatures to array of MyCreatures
        MyCreature[] old_population = (MyCreature[]) old_population_btc;
        // Create a new array for the new population
        MyCreature[] new_population = new MyCreature[numCreatures];

        // Simulation Stats for old population
        float avgLifeTime = 0f;
        int nSurvivors = 0;
        float fitnessSum = 0;
        for (MyCreature creature : old_population) {
            boolean dead = creature.isDead();
            if (dead) {
                // If the creature died during simulation, you can determine
                // its time of death (in turns)
                int timeOfDeath = creature.timeOfDeath();
                avgLifeTime += (float) timeOfDeath;
            } else {
                nSurvivors += 1;
                avgLifeTime += (float) _numTurns;
            }
            fitnessSum += getFitness(creature);
        }

        printHungerStats(old_population);
        //System.out.println(fitnessSum);

        // Right now the information is used to print some stats...but you should
        // use this information to access creatures fitness.  It's up to you how
        // you define your fitness function.  You should add a print out or
        // some visual display of average fitness over generations.
        avgLifeTime /= (float) numCreatures;
        float avgFitness = fitnessSum / numCreatures;
        System.out.println("Simulation stats:");
        System.out.println("\tSurvivors    : " + nSurvivors + " out of " + numCreatures);
        System.out.println("\tAvg life time: " + avgLifeTime + " turns");
        System.out.println("\tAvg Fitness: " + avgFitness);
        System.out.println("\tAvg Normalised Fitness" + (avgFitness / ((_numTurns * 4) + 100)));

        // Having some way of measuring the fitness, you should implement a proper
        // parent selection method here and create a set of new creatures.  You need
        // to create numCreatures of the new creatures.  If you'd like to have
        // some elitism, you can use old creatures in the next generation.  This
        // example code uses all the creatures from the old generation in the
        // new generation.
        //Elitism
        int numberOfElites = numCreatures / 4;
        new_population = addElites(new_population, old_population, numberOfElites);

        //Roulette Selection 
        //Set up array for roulette selection
        float[] rouletteFitness = new float[numCreatures];
        for (int creature = 0; creature < numCreatures; creature++) {
            rouletteFitness[creature] = getFitness(old_population[creature]);
            rouletteFitness[creature] /= fitnessSum;
        }
        //System.out.println(Arrays.toString(rouletteFitness));

        //Start creating new population.
        // Case where only one creature in population used for testing.
        //
        if (numCreatures == 1) {
            new_population[0] = old_population[0];

        } else {

            for (int creature = numberOfElites; creature < numCreatures; creature++) {
                //parent pick
                MyCreature parent1 = old_population[pickParentIndex(rouletteFitness)];
                MyCreature parent2 = old_population[pickParentIndex(rouletteFitness)];
                //check equivalence here.
                while (parent1 == parent2) {
                    parent2 = old_population[pickParentIndex(rouletteFitness)];
                }

                //crossover    
                //crossover green hunger
                MyCreature child = new MyCreature();

                // Set mutation chance for hunger crossover
                float hungerMutationChance = 0.02f;

                if (rollChance(hungerMutationChance)) {
                    child.setGreenHunger(rand.nextFloat());
                } else {
                    float newGreenHunger = parent1.getGreenHunger();
                    child.setGreenHunger(newGreenHunger);
                }

                //cross over red hunger
                if (rollChance(hungerMutationChance)) {
                    child.setRedHunger(rand.nextFloat());
                } else {
                    float newRedHunger = parent2.getRedHunger();
                    child.setRedHunger(newRedHunger);
                }

                // Set mutation chance for food,monster etc genes.
                float geneMutationChance = 0.02f;

                //cross over monsterGenes
                float[][] childMonsterGenes = crossOver(child,
                        parent1.getMonsterGenes(),
                        parent2.getMonsterGenes(),
                        geneMutationChance);

                child.setMonsterGenes(childMonsterGenes);

                //cross over food Genes
                float[][] childFoodGenes = crossOver(child,
                        parent1.getFoodGenes(),
                        parent2.getFoodGenes(),
                        geneMutationChance);
                child.setFoodGenes(childFoodGenes);

                //cross over CreatureGenes
                float[][] childCreatureGenes = crossOver(child,
                        parent1.getCreatureGenes(),
                        parent2.getCreatureGenes(),
                        geneMutationChance);

                child.setCreatureGenes(childCreatureGenes);

                //cross over exploreGene
                float[][] childExploreGenes = crossOver(child,
                        parent1.getExploreGenes(),
                        parent2.getExploreGenes(),
                        geneMutationChance);
                
                child.setExploreGenes(childExploreGenes);

                new_population[creature] = child;
            }

        }

        // Write average fitness to file for graphing output.
        try {
            FileWriter fw = new FileWriter(DATAFILENAME, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(Float.toString(avgFitness) + ","
                    + Float.toString(nSurvivors)+ ","
            + getAverageRedHunger(old_population)+ ","
                    + getAverageGreenHunger(old_population)    );
            bw.newLine();
            bw.close();
            fw.close();

        } catch (IOException exception) {

        }

        return new_population;

    }

    public int pickParentIndex(float[] fitnessArray) {
        int index = 0;
        float number = rand.nextFloat();
        while (number > fitnessArray[index]) {
            number = number - fitnessArray[index];
            index++;
        }
        return index;

    }

    public float getFitness(MyCreature creature) {
        float fitness = 0.0f;
        int turnBias = 4;
        float denominator = ((float) _numTurns * turnBias) + 100;
        boolean alive = !(creature.isDead());
        if (alive) {
            float baseFitness = ((float) _numTurns * turnBias) + creature.getEnergy();
            //float normalisedFitness = baseFitness / denominator;
            //fitness[creature] = normalisedFitness;
            fitness = baseFitness;
        } else {
            float baseFitness = (creature.timeOfDeath() * turnBias) + creature.getEnergy();
            //float normalisedFitness = baseFitness / denominator;
            //fitness[creature] = normalisedFitness;
            fitness = baseFitness;
        }
        return fitness;

    }

    public boolean rollChance(float chance) {
        if (chance > rand.nextFloat()) {
            return true;
        } else {
            return false;
        }
    }

    private MyCreature[] addElites(MyCreature[] new_population, MyCreature[] old_population, int numberOfElites) {
        TreeMap tm = new TreeMap();

        for (int i = 0; i < old_population.length; i++) {
            tm.put(getFitness(old_population[i]), i);
        }

        //System.out.println(tm.toString());
        //want last entry
        for (int i = 0; i < numberOfElites; i++) {
            int index = ((Integer) tm.get(tm.lastKey()));
            tm.remove(tm.lastKey());
            new_population[i] = old_population[index];
            //System.out.println(new_population[i]);
        }
        return new_population;
    }

    private float[][] crossOver(MyCreature child, float[][] parent1Genes, float[][] parent2Genes, float mutationChance) {
        float[][] childGenes = new float[parent1Genes.length][parent1Genes[0].length];
        if (rollChance(mutationChance)) {
            System.out.println("Mutation");
            childGenes = child.setGeneToRandom(childGenes);
            return childGenes;
            /*//mutation bump one up to max.
                    if (rollChance(mutationChance)) {
                        //childMonsterGenes[row] = child.generateRandomChance();
                        float max = parent1MonsterGenes[row][0];
                        int index = 0;
                        for (int col = 1; col < childMonsterGenes[row].length; col++) {
                            if (parent1MonsterGenes[row][col] > max) {
                                max = parent1MonsterGenes[row][col];
                                index = col;
                            }
                        }
                        childMonsterGenes[row][index] = 1f;
                        //System.out.println(Arrays.toString(childMonsterGenes[row]));
             */
        } else {

            /* Alternating Crossover
            for (int row = 0; row < childGenes.length; row++) {
                if (row % 2 == 0) {
                    childGenes[row] = parent1Genes[row];

                } else {
                    childGenes[row] = parent2Genes[row];
                }
            }
             */
            //Block Crossover
            for (int row = 0; row < childGenes.length; row++) {
                if (row < (childGenes.length / 2)) {
                    for (int col = 0; col < childGenes[row].length; col++) {
                        childGenes[row][col] = parent1Genes[row][col];
                    }
                } else {
                    for (int col = 0; col < childGenes[row].length; col++) {
                        childGenes[row][col] = parent2Genes[row][col];
                    }
                }
            }

            /*
                AVerage Crossover
                for (int col = 0; col < childGenes[row].length; col++) {
                    float newGene = parent1Genes[row][col];
                    newGene += parent2Genes[row][col];
                    newGene /= 2;
                    childGenes[row][col] = newGene;
                }
                
             */
        }
        return childGenes;
    }

    /**
     * Calculates statistics for hunger in a population of MyCreatures. Returns
     * max and average for red and green hunger. Prints the values to the
     * console.
     *
     * @param old_population - population to gather statistics on.
     */
    private void printHungerStats(MyCreature[] population) {
        float redHungerSum = 0;
        float greenHungerSum = 0;
        int numCreatures = population.length;
        float maxGreenHunger = 0;
        float maxRedHunger = 0;

        for (int i = 0; i < numCreatures; i++) {
            if (maxGreenHunger < population[i].getGreenHunger()) {
                maxGreenHunger = population[i].getGreenHunger();
            }
            if (maxRedHunger < population[i].getRedHunger()) {
                maxRedHunger = population[i].getRedHunger();
            }
            redHungerSum += population[i].getRedHunger();
            greenHungerSum += population[i].getGreenHunger();
        }
        float averageRedHunger = (((float) redHungerSum) / numCreatures);
        float averageGreenHunger = (((float) greenHungerSum) / numCreatures);

        System.out.println("Avg. Green Hunger" + averageGreenHunger);
        System.out.println("Avg. Red Hunger " + averageRedHunger);
        System.out.println("Max Green Hunger" + maxGreenHunger);
        System.out.println("Max Red Hunger " + maxRedHunger);
    }
    
    private float getAverageGreenHunger(MyCreature[] population){
        float greenHungerSum = 0;
        int numCreatures = population.length;
        for (int i = 0; i < numCreatures; i++) {
                       greenHungerSum += population[i].getGreenHunger();
        }
         float averageGreenHunger = (((float) greenHungerSum) / numCreatures);
         return averageGreenHunger;
    }
    
    private float getAverageRedHunger(MyCreature[] population){
        float greenHungerSum = 0;
        int numCreatures = population.length;
        for (int i = 0; i < numCreatures; i++) {
                       greenHungerSum += population[i].getRedHunger();
        }
         float averageRedHunger = (((float) greenHungerSum) / numCreatures);
         return averageRedHunger;
    }
}
