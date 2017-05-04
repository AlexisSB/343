
import cosc343.assig2.World;
import cosc343.assig2.Creature;
import java.util.*;

/**
 * The MyWorld extends the cosc343 assignment 2 World. Here you can set some
 * variables that control the simulations and override functions that generate
 * populations of creatures that the World requires for its simulations.
 *
 * @author
 * @version 1.0
 * @since 2017-04-05
 */
public class MyWorld extends World {

    /* Here you can specify the number of turns in each simulation
   * and the number of generations that the genetic algorithm will 
   * execute.
     */
    private final int _numTurns = 100;
    private final int _numGenerations = 1000;

    /* Constructor.  
   
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

    /* The main function for the MyWorld application

     */
    public static void main(String[] args) {
        // Here you can specify the grid size, window size and whether torun
        // in repeatable mode or not
        int gridSize = 24;
        int windowWidth = 1200;
        int windowHeight = 1200;
        boolean repeatableMode = true;

        /* Here you can specify percept format to use - there are three to
         chose from: 1, 2, 3.  Refer to the Assignment2 instructions for
         explanation of the three percept formats.
         */
        int perceptFormat = 1;

        // Instantiate MyWorld object.  The rest of the application is driven
        // from the window that will be displayed.
        MyWorld sim = new MyWorld(gridSize, windowWidth, windowHeight, repeatableMode, perceptFormat);
    }


    /* The MyWorld class must override this function, which is
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

        // Here is how you can get information about old creatures and how
        // well they did in the simulation
        float avgLifeTime = 0f;
        int nSurvivors = 0;
        for (MyCreature creature : old_population) {
            // The energy of the creature.  This is zero if creature starved to
            // death, non-negative oterhwise.  If this number is zero, but the 
            // creature is dead, then this number gives the enrgy of the creature
            // at the time of death.
            int energy = creature.getEnergy();

            // This querry can tell you if the creature died during simulation
            // or not.  
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
        }

        float[] oldFitness = calculatePopulationFitness(old_population, numCreatures);
        float fitnessSum = sumOfFitness(old_population, numCreatures);

        // Right now the information is used to print some stats...but you should
        // use this information to access creatures fitness.  It's up to you how
        // you define your fitness function.  You should add a print out or
        // some visual display of average fitness over generations.
        avgLifeTime /= (float) numCreatures;
        System.out.println("Simulation stats:");
        System.out.println("  Survivors    : " + nSurvivors + " out of " + numCreatures);
        System.out.println("  Avg life time: " + avgLifeTime + " turns");
        System.out.println("Avg Fitness: " + ((float) fitnessSum / numCreatures));
        // Having some way of measuring the fitness, you should implement a proper
        // parent selection method here and create a set of new creatures.  You need
        // to create numCreatures of the new creatures.  If you'd like to have
        // some elitism, you can use old creatures in the next generation.  This
        // example code uses all the creatures from the old generation in the
        // new generation.

        //Elitism
        int numberOfElites = numCreatures / 4;
        TreeMap tm = new TreeMap();

        for (int i = 0; i < numCreatures; i++) {
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

        // Roulette Selection //Set up array for roulette selection
        float[] rouletteFitness = Arrays.copyOf(oldFitness, numCreatures);
        for (int i = 0; i < numCreatures; i++) {
            rouletteFitness[i] /= fitnessSum;
        }
        //System.out.println(Arrays.toString(rouletteFitness));
        //Start new population loop
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
                //may want to create copy instead of copying pointer
                MyCreature child = new MyCreature();
                //crossover    //takes average of parents

                //crossover green hunger
                //check mutation first//mutation overwrites learned hunger.
                float mutationChance = 0.02f;
                if (rollChance(mutationChance)) {
                    child.setGreenHunger(rand.nextFloat());
                } else {
                    float newGreenHunger = parent1.getGreenHunger();
                    newGreenHunger += parent2.getRedHunger();
                    newGreenHunger /= 2;
                    child.setGreenHunger(newGreenHunger);
                }

                //cross over red hunger
                mutationChance = 0.02f;
                if (rollChance(mutationChance)) {
                    child.setRedHunger(rand.nextFloat());
                } else {
                    float newRedHunger = parent1.getRedHunger();
                    newRedHunger += parent2.getRedHunger();
                    newRedHunger /= 2;
                    child.setRedHunger(newRedHunger);
                    //site for mutation
                }

                //cross over monsterGenes
                float[][] parent1MonsterGenes = parent1.getMonsterGenes();
                float[][] parent2MonsterGenes = parent2.getMonsterGenes();
                float[][] childMonsterGenes = child.getMonsterGenes();

                mutationChance = 0.05f;

                for (int row = 0; row < childMonsterGenes.length; row++) {
                    if (rollChance(mutationChance)) {
                        childMonsterGenes[row] = child.generateRandomChance();
                    } else {
                        for (int col = 0; col < childMonsterGenes[row].length; col++) {
                            float newGene = parent1MonsterGenes[row][col];
                            newGene += parent2MonsterGenes[row][col];
                            newGene /= 2;
                            childMonsterGenes[row][col] = newGene;
                        }
                    }

                }

                child.setMonsterGenes(childMonsterGenes);

                //cross over food Genes
                float[][] parent1FoodGenes = parent1.getFoodGenes();
                float[][] parent2FoodGenes = parent2.getFoodGenes();
                float[][] childFoodGenes = child.getFoodGenes();

                mutationChance = 0.05f;

                for (int row = 0; row < childFoodGenes.length; row++) {
                    if (rollChance(mutationChance)) {
                        childFoodGenes[row] = child.generateRandomChance();
                    } else {
                        for (int col = 0; col < childFoodGenes[row].length; col++) {
                            float newGene = parent1FoodGenes[row][col];
                            newGene += parent2FoodGenes[row][col];
                            newGene /= 2;
                            childFoodGenes[row][col] = newGene;
                        }
                    }
                }

                child.setFoodGenes(childFoodGenes);

                //cross over CreatureGenes
                float[][] parent1CreatureGenes = parent1.getCreatureGenes();
                float[][] parent2CreatureGenes = parent2.getCreatureGenes();
                float[][] childCreatureGenes = child.getCreatureGenes();

                mutationChance = 0.05f;

                for (int row = 0; row < childCreatureGenes.length; row++) {
                    if (rollChance(mutationChance)) {
                        childCreatureGenes[row] = child.generateRandomChance();
                    } else {
                        for (int col = 0; col < childCreatureGenes[row].length; col++) {
                            float newGene = parent1CreatureGenes[row][col];
                            newGene += parent2CreatureGenes[row][col];
                            newGene /= 2;
                            childCreatureGenes[row][col] = newGene;
                        }
                    }
                }

                child.setCreatureGenes(childCreatureGenes);

                //cross over exploreGene
                float[][] parent1ExploreGenes = parent1.getExploreGenes();
                float[][] parent2ExploreGenes = parent2.getExploreGenes();
                float[][] childExploreGenes = child.getExploreGenes();
                
                mutationChance = 0.05f;

                for (int row = 0; row < childExploreGenes.length; row++) {
                    if (rollChance(mutationChance)) {
                        childExploreGenes[row] = child.generateRandomChance();
                    } else {
                        for (int col = 0; col < childExploreGenes[row].length; col++) {
                            float newGene = parent1ExploreGenes[row][col];
                            newGene += parent2ExploreGenes[row][col];
                            newGene /= 2;
                            childExploreGenes[row][col] = newGene;
                        }
                    }
                    
                }

                child.setExploreGenes(childExploreGenes);

                new_population[creature] = child;
            }

        }
        return new_population;
    }

    public float[] calculatePopulationFitness(MyCreature[] population, int numCreatures) {
        //Fitness goes here.
        float[] fitness = new float[numCreatures];
        int turnBias = 4;
        float denominator = ((float) _numTurns * turnBias) + 100;
        for (int creature = 0; creature < numCreatures; creature++) {
            boolean alive = !(population[creature].isDead());
            if (alive) {
                float baseFitness = ((float) _numTurns * turnBias) + population[creature].getEnergy();
                //float normalisedFitness = baseFitness / denominator;
                //fitness[creature] = normalisedFitness;
                fitness[creature] = baseFitness;
            } else {
                float baseFitness = (population[creature].timeOfDeath() * turnBias) + population[creature].getEnergy();
                //float normalisedFitness = baseFitness / denominator;
                //fitness[creature] = normalisedFitness;
                fitness[creature] = baseFitness;
            }

        }
        return fitness;
    }

    public float sumOfFitness(MyCreature[] population, int numCreatures) {
        float[] fitness = calculatePopulationFitness(population, numCreatures);
        float sum = 0;
        for (int creature = 0; creature < numCreatures; creature++) {
            sum += fitness[creature];
        }

        return sum;

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

}
