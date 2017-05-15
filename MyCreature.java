
import cosc343.assig2.Creature;
import java.util.*;

/**
 * The MyCreate extends the cosc343 assignment 2 Creature. Here you implement
 * creatures chromosome and the agent function that maps creature percepts to
 * actions.
 *
 * @author
 * @version 1.0
 * @since 2017-04-05
 */
public class MyCreature extends Creature {

    // Random number generator
    Random rand = new Random();

    //Chromosome Lookup Tables
    private float[][] monsterGenes;
    private float[][] foodGenes;
    private float[][] creatureGenes;
    private float[][] exploreGene;

    //Chromosome hunger values
    private float greenHunger;
    private float redHunger;

    //Enumberated types for class.
    private enum Strawberry {
        NONE, GREEN, RED
    };

    private enum GeneSelect {
        MONSTER, FOOD, CREATURE, EXPLORE
    };


    /* Empty constructor - might be a good idea here to put the code that 
   initialises the chromosome to some random state   
  
   Input: numPercept - number of percepts that creature will be receiving
          numAction - number of action output vector that creature will need
                      to produce on every turn
     */
    public MyCreature(int numPercepts, int numActions) {
        this();
    }

    public MyCreature() {

        this.greenHunger = rand.nextFloat();
        this.redHunger = rand.nextFloat();

        this.monsterGenes = new float[1024][];
        this.foodGenes = new float[1024][];
        this.creatureGenes = new float[1024][];
        this.exploreGene = new float[1][];

        fillArrayWithRandomAction(monsterGenes);
        fillArrayWithRandomAction(foodGenes);
        fillArrayWithRandomAction(creatureGenes);
        fillArrayWithRandomAction(exploreGene);

    }

    /**
     * Fills given array with action vectors using following method.
     *
     * @param gene - 2D array to fill will action vectors.
     */
    private void fillArrayWithRandomAction(float[][] gene) {
        for (int row = 0; row < gene.length; row++) {
            gene[row] = generateRandomActionArray();
            //System.out.println(Arrays.toString(gene[row]));
        }

    }

    /**
     * Creates an size 11 action array with one random value set to 1. Will not
     * create one in index 9 as this is the eat command. Eat command handled by
     * hunger.
     *
     * @return random action array.
     */
    private float[] generateRandomActionArray() {
        float[] chances = new float[11];
        int index = rand.nextInt(11);
        while (index == 9) {
            index = rand.nextInt(11);
        }
        chances[index] = 1f;
        //System.out.println(Arrays.toString(chances));
        return chances;
    }
    

    /* This function must be overridden by MyCreature, because it implements
     the AgentFunction which controls creature behavoiur.  This behaviour
     should be governed by a model (that you need to come up with) that is
     parameterise by the chromosome.  
  
     Input: percepts - an array of percepts
            numPercepts - the size of the array of percepts depend on the percept
                          chosen
            numExpectedAction - this number tells you what the expected size
                                of the returned array of percepts should bes
     Returns: an array of actions 
     */
    @Override
    public float[] AgentFunction(int[] percepts, int numPercepts, int numExpectedActions) {

        // This is where your chromosome gives rise to the model that maps
        // percepts to actions.  This function governs your creature's behaviour.
        // You need to figure out what model you want to use, and how you're going
        // to encode its parameters in a chromosome.
        // At the moment, the actions are chosen completely at random, ignoring
        // the percepts.  You need to replace this code.
        //System.out.println(Arrays.toString(percepts));
        //Generate the percept codes for each monsters, food and creatures.
        float actions[] = new float[numExpectedActions];
        int monsterCode = generatePerceptCode(percepts, GeneSelect.MONSTER);
        int foodCode = generatePerceptCode(percepts, GeneSelect.FOOD);
        int creatureCode = generatePerceptCode(percepts, GeneSelect.CREATURE);
        Strawberry ripeCode = generateRipeCode(percepts);

        //System.out.println(monsterCode);
        //System.out.println(foodCode);
        //System.out.println(creatureCode);
        //System.out.println(ripeCode);
        float[] chances = new float[numExpectedActions];
        if (monsterCode != 0) {
            chances = this.monsterGenes[monsterCode];
            //there is monster nearby
        } else {
            if (ripeCode != Strawberry.NONE) {
                //you're sitting on food
                actions = rollHunger(ripeCode);
                return actions;

            } else {
                if (foodCode != 0) {
                    //there is food nearby
                    chances = this.foodGenes[foodCode];
                } else {
                    if (creatureCode != 0) {
                        //creature nearyby
                        chances = this.creatureGenes[creatureCode];
                    } else {
                        chances = this.exploreGene[0];
                    }
                }
            }
        }
        //S ystem.out.println(Arrays.toString(chances));
        //actions = randomActionPick(chances);
        actions = chances;

        return actions;
    }

    /**
     * Decides whether the creature should eat when it is on a strawberry.
     *
     * @param ripeCode -
     * @return
     */
    private float[] rollHunger(Strawberry ripeCode) {
        float[] actions = new float[11];
        if (ripeCode == Strawberry.RED) {
            //if (this.redHunger > (float) getEnergy() / 100) {
            if (this.redHunger > rand.nextFloat()) {
                actions = Actions.eat;
            } else {
                actions = Actions.moveRandom;
            }
        } else if (ripeCode == Strawberry.GREEN) {
            //if (this.greenHunger > (float) getEnergy() / 100) {
            if (this.greenHunger > rand.nextFloat()) {
                actions = Actions.eat;
            } else {
                actions = Actions.moveRandom;
            }
        }
        return actions;
    }

    private Strawberry generateRipeCode(int[] percepts) {
        int ripeCode = percepts[4];
        if (ripeCode == 0) {
            return Strawberry.NONE;
        } else {
            if (ripeCode == 2) {
                return Strawberry.RED;
            } else {
                if (ripeCode == 1) {
                    return Strawberry.GREEN;
                } else {
                    System.out.println("ripecode cannot be generated");
                    return Strawberry.NONE;
                }
            }
        }
    }

    private int generatePerceptCode(int[] percepts, GeneSelect choice) {

        /* Debug code        
        int[] perceptsCopy = Arrays.copyOf(percepts, percepts.length);
        //System.out.println(Arrays.toString(perceptsCopy));

        StringBuilder perceptString = new StringBuilder();
        
        for (int i = 0; i < percepts.length; i++) {
            perceptString.append(percepts[i]);
        }
         */
        StringBuilder monsterString = new StringBuilder();
        StringBuilder foodString = new StringBuilder();
        StringBuilder creatureString = new StringBuilder();

        //Generate Monster Code.
        for (int i = 0; i < percepts.length; i++) {
            if (i != 4) {
                if (percepts[i] == 1) {
                    monsterString.append("1");
                } else {
                    monsterString.append("0");
                }
            }
        }
        //System.out.println(monsterString);

        //Generate Creature Code
        for (int i = 0; i < percepts.length; i++) {
            if (i != 4) {
                if (percepts[i] == 2) {
                    creatureString.append("1");
                } else {
                    creatureString.append("0");
                }
            }
        }

        //System.out.println(creatureString);
        //Generate Food Code
        for (int i = 0; i < percepts.length; i++) {
            if (i != 4) {
                if (percepts[i] == 3) {
                    foodString.append("1");
                } else {
                    foodString.append("0");
                }
            }
        }
        //System.out.println(foodString);

        //codes return value from 0 to 8 depending on location of nearest monster, creature, food etc.
        int monsterCode = Integer.parseInt(monsterString.toString(), 2);
        int creatureCode = Integer.parseInt(creatureString.toString(), 2);
        int foodCode = Integer.parseInt(foodString.toString(), 2);
        //int perceptCode = Integer.parseInt(perceptString.toString(), 4);

        switch (choice) {
            case MONSTER:
                return monsterCode;
            case FOOD:
                return foodCode;
            case CREATURE:
                return creatureCode;

            default:
                System.out.println("Error choice of percept code invalid");
                return -1;
        }
    }

    public float[] randomActionPick(float[] chances) {

        if (chances.length != 11) {
            System.out.println("Length does not match action vector");
            System.exit(0);
        }
        float[] action;
        int index = pickIndex(chances);
        action = Actions.pickAction(index);
        return action;
    }

    public int pickIndex(float[] chances) {
        int index = 0;
        float number = rand.nextFloat();
        while (number > chances[index]) {
            number = number - chances[index];
            index++;
        }
        return index;
    }
    
    /**
     * Public setter method to create random gene.
     * @param gene - data field gene to scramble
     * @return the scramble gene.
     */
    public float[][] setGeneToRandom(float[][] gene) {
        for (int row = 0; row < gene.length; row++) {
            gene[row] = generateRandomActionArray();
            //System.out.println(Arrays.toString(gene[row]));
        }
        return gene;
    }

    public float[][] getMonsterGenes() {
        return this.monsterGenes;
    }

    public float[][] getFoodGenes() {
        return this.foodGenes;
    }

    public float[][] getCreatureGenes() {
        return this.creatureGenes;
    }

    public float[][] getExploreGenes() {
        return this.exploreGene;
    }

    public float getGreenHunger() {
        return this.greenHunger;
    }

    public float getRedHunger() {
        return this.redHunger;
    }

    public void setMonsterGenes(float[][] newGenes) {
        this.monsterGenes = newGenes;
    }

    public void setFoodGenes(float[][] newGenes) {
        this.foodGenes = newGenes;
    }

    public void setCreatureGenes(float[][] newGenes) {
        this.creatureGenes = newGenes;
    }

    public void setExploreGenes(float[][] newGenes) {
        this.exploreGene = newGenes;
    }

    public void setGreenHunger(float newHunger) {
        this.greenHunger = newHunger;
    }

    public void setRedHunger(float newHunger) {
        this.redHunger = newHunger;
    }

}
