
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
    //float[][] chromosome;
    float[][] monsterGenes;
    float[][] foodGenes;
    float[][] creatureGenes;
    //int chromosomeLength;
    float greenHunger;
    float redHunger;


    /* Empty constructor - might be a good idea here to put the code that 
   initialises the chromosome to some random state   
  
   Input: numPercept - number of percepts that creature will be receiving
          numAction - number of action output vector that creature will need
                      to produce on every turn
     */
    public MyCreature(int numPercepts, int numActions) {
        //this.chromosomeLength = 1;
        //this.chromosome = new float[chromosomeLength][];
        this.greenHunger = rand.nextFloat();
        this.redHunger = rand.nextFloat();
        this.chromosomeLength = 28;
        this.chromosome = new float[this.chromosomeLength][];
        for (int row = 0; row < this.chromosome.length - 1; row++) {
            this.chromosome[row] = generateRandomChance();
            System.out.println(Arrays.toString(chromosome[row]));
        }
      

    }

    public MyCreature() {
        this.greenHunger = rand.nextFloat();
        this.redHunger = rand.nextFloat();
        this.chromosomeLength = 28;
        this.chromosome = new float[this.chromosomeLength][];
        for (int row = 0; row < this.chromosome.length - 1; row++) {
            this.chromosome[row] = generateRandomChance();
            //System.out.println(chromosome[row][col]);
        }
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
        float actions[] = new float[numExpectedActions];
        int monsterCode = generatePerceptCode(percepts, 1);
        int foodCode = generatePerceptCode(percepts, 2) + 9;
        int creatureCode = generatePerceptCode(percepts, 3) + 18;
        int ripeCode = generatePerceptCode(percepts, 4);
        //System.out.println(ripeCode);
        /*
        if (percepts[6] == 1) {
            actions = rollHunger(ripeCode);
            //System.out.println("eat");
        } else if (percepts[7] == 1) {
            actions = rollHunger(ripeCode);
            //System.out.println("eat");
        } else {

            for (int i = 0; i < numExpectedActions; i++) {
                actions[i] = rand.nextFloat();
            }
        }
         */

        //System.out.println(monsterCode);
        //System.out.println(foodCode);
        //System.out.println(creatureCode);
        //System.out.println(ripeCode);
        float[] chances = new float[numExpectedActions];

        if (monsterCode != 4) {
            chances = chromosome[monsterCode];
            //there is monster nearby
        } else {
            if (ripeCode != 0) {
                //you're sitting on food
                actions = rollHunger(ripeCode);
                return actions;

            } else {
                if (foodCode != 13) {
                    //there is food nearby
                    chances = chromosome[foodCode];
                } else {
                    if (creatureCode != 22) {
                        //creature nearyby
                        chances = chromosome[creatureCode];
                    } else {
                        chances = chromosome[27];
                    }
                }
            }
        }
        actions = randomActionPick(chances);

        return actions;
    }

    public float[] rollHunger(int ripeCode) {
        float[] actions = new float[11];
        if (ripeCode == 1) {
            if (this.redHunger > (float) getEnergy() / 100) {
                actions = Actions.eat;
            } else {
                actions = Actions.moveRandom;
            }
        } else if (ripeCode == 2) {
            if (this.greenHunger > (float) getEnergy() / 100) {
                actions = Actions.eat;
            } else {
                actions = Actions.moveRandom;
            }

        }

        return actions;
    }

    public float[] generateRandomChance() {
        float[] chances = new float[11];

        //add one to onyl one entry
        int index = rand.nextInt(11);
        while (index == 9) {
            index = rand.nextInt(11);
        }
        chances[index] = 1f;
        //System.out.println(Arrays.toString(chances));

        /*Creates completely random chance
        float sum = 0;
        for (int i = 0; i < chances.length; i++) {
            chances[i] = rand.nextFloat();
            sum += chances[i];
        }

        //normalise
        for (int i = 0; i < chances.length; i++) {
            chances[i] /= sum;
        }
         */
        return chances;
    }

    public int generatePerceptCode(int[] percepts, int choice) {
        int[] perceptsCopy = Arrays.copyOf(percepts, percepts.length);
        for (int i = 0; i < perceptsCopy.length; i++) {
            perceptsCopy[i]++;
        }
        //System.out.println(Arrays.toString(perceptsCopy));

        StringBuilder perceptString = new StringBuilder();
        StringBuilder monsterString = new StringBuilder();
        StringBuilder foodString = new StringBuilder();
        StringBuilder ripeString = new StringBuilder();
        StringBuilder creatureString = new StringBuilder();

        for (int i = 0; i < percepts.length; i++) {
            perceptString.append(perceptsCopy[i]);
        }

        for (int i = 0; i <= 1; i++) {
            monsterString.append(perceptsCopy[i]);
        }
        //System.out.println(monsterString);
        for (int i = 2; i <= 3; i++) {
            creatureString.append(perceptsCopy[i]);
        }
        //System.out.println(creatureString);
        for (int i = 4; i <= 5; i++) {
            foodString.append(perceptsCopy[i]);
        }

        for (int i = 6; i < percepts.length; i++) {
            ripeString.append(percepts[i]);
        }

        //System.out.println(foodString);
        //codes return value from 0 to 8 depending on location of nearest monster, creature, food etc.
        int monsterCode = Integer.parseInt(monsterString.toString(), 3);
        int creatureCode = Integer.parseInt(creatureString.toString(), 3);
        int foodCode = Integer.parseInt(foodString.toString(), 3);
        int perceptCode = Integer.parseInt(perceptString.toString(), 3);
        int ripeCode = Integer.parseInt(ripeString.toString(), 2);

        switch (choice) {
            case 1:
                return monsterCode;
            case 2:
                return foodCode;
            case 3:
                return creatureCode;
            case 4:
                return ripeCode;
            case 5:
                return perceptCode;
            default:
                System.out.println("Error choice of percept code incorrect");
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

    public float[] getStrand(int index) {
        float[] strandCopy = Arrays.copyOf(this.chromosome[index], this.chromosome[index].length);

        return strandCopy;
    }
    

}
