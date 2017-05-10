
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
    private float[][] monsterGenes;
    private float[][] foodGenes;
    private float[][] creatureGenes;
    private float[][] exploreGene;
    //int chromosomeLength;
    private float greenHunger;
    private float redHunger;


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

        this.monsterGenes = new float[1024][];
        this.foodGenes = new float[1024][];
        this.creatureGenes = new float[1024][];
        this.exploreGene = new float[9][];

        fillArrayWithRandomChances(monsterGenes);
        fillArrayWithRandomChances(foodGenes);
        fillArrayWithRandomChances(creatureGenes);
        fillArrayWithRandomChances(exploreGene);

    }

    public void fillArrayWithRandomChances(float[][] gene) {
        for (int row = 0; row < gene.length; row++) {
            gene[row] = generateRandomChance();
            //System.out.println(Arrays.toString(gene[row]));
        }

    }
    
    public float[][] fillRandomArray(float[][] gene) {
        for (int row = 0; row < gene.length; row++) {
            gene[row] = generateRandomChance();
            //System.out.println(Arrays.toString(gene[row]));
        }
        return gene;
    }

    public MyCreature() {

        this.greenHunger = rand.nextFloat();
        this.redHunger = rand.nextFloat();
        this.greenHunger = rand.nextFloat();
        this.redHunger = rand.nextFloat();

        this.monsterGenes = new float[1024][];
        this.foodGenes = new float[1024][];
        this.creatureGenes = new float[1024][];
        this.exploreGene = new float[1][];

        fillArrayWithRandomChances(monsterGenes);
        fillArrayWithRandomChances(foodGenes);
        fillArrayWithRandomChances(creatureGenes);
        fillArrayWithRandomChances(exploreGene);

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
        int foodCode = generatePerceptCode(percepts, 2);
        int creatureCode = generatePerceptCode(percepts, 3);
        int ripeCode = generatePerceptCode(percepts, 4);
        
        //System.out.println(monsterCode);
        //System.out.println(foodCode);
        //System.out.println(creatureCode);
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

        
        float[] chances = new float[numExpectedActions];

        if (monsterCode != 0) {
            chances = this.monsterGenes[monsterCode];
            //there is monster nearby
        } else {
            if (ripeCode != 0) {
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
        actions = randomActionPick(chances);

        return actions;
    }

    public float[] rollHunger(int ripeCode) {
        float[] actions = new float[11];
        if (ripeCode == 2) {
            if (this.redHunger > (float) getEnergy() / 100) {
                actions = Actions.eat;
            } else {
                actions = Actions.moveRandom;
            }
        } else if (ripeCode == 1) {
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
        //System.out.println(Arrays.toString(perceptsCopy));

        StringBuilder perceptString = new StringBuilder();
        StringBuilder monsterString = new StringBuilder();
        StringBuilder foodString = new StringBuilder();
        StringBuilder ripeString = new StringBuilder();
        StringBuilder creatureString = new StringBuilder();

        for (int i = 0; i < percepts.length; i++) {
            perceptString.append(percepts[i]);
        }
        
        //Generate Monster Code.
        for (int i = 0; i < percepts.length; i++) {
          if (percepts[i] == 1){
            monsterString.append("1");
          }else{
              monsterString.append("0");
          }
        }
        //System.out.println(monsterString);
        
        //Generate Creature Code
        for (int i = 0; i < percepts.length; i++) {
            if(percepts[i] == 2){
            creatureString.append("1");
            }else{
                creatureString.append("0");
            }
        }
        
        //System.out.println(creatureString);
        //Generate Food Code
        for (int i = 0; i < percepts.length; i++) {
            if (percepts[i] == 3){
            foodString.append("1");
            }else{
                foodString.append("0");                
            }
        }
        //Generate Ripe Code
        //ripeString.append(percepts[4]);
        

        //System.out.println(foodString);
        //codes return value from 0 to 8 depending on location of nearest monster, creature, food etc.
        int monsterCode = Integer.parseInt(monsterString.toString(), 2);
        int creatureCode = Integer.parseInt(creatureString.toString(), 2);
        int foodCode = Integer.parseInt(foodString.toString(), 2);
        int perceptCode = Integer.parseInt(perceptString.toString(), 4);
        int ripeCode = percepts[4];

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
    
    public float getRedHunger(){
        return this.redHunger;
    }
    
    public void setMonsterGenes(float[][] newGenes) {
        this.monsterGenes= newGenes;
    }

    public void setFoodGenes(float[][] newGenes) {
        this.foodGenes = newGenes;
    }

    public void setCreatureGenes(float[][] newGenes) {
        this.creatureGenes= newGenes;
    }

    public void setExploreGenes(float[][] newGenes) {
        this.exploreGene= newGenes;
    }

    public void setGreenHunger(float newHunger) {
        this.greenHunger = newHunger;
    }
    
    public void setRedHunger(float newHunger) {
        this.redHunger = newHunger;
    }
    
    

}
