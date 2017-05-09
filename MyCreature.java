
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
    private float[][] genes;
    private float[][] outputWeights;
    
    
    /* Empty constructor - might be a good idea here to put the code that 
   initialises the chromosome to some random state   
  
   Input: numPercept - number of percepts that creature will be receiving
          numAction - number of action output vector that creature will need
                      to produce on every turn
     */
    public MyCreature(int numPercepts, int numActions) {
        int numRows = numPercepts + 1;
        int numColumns = 8;
        genes = new float[10][8];
        for (int row = 0; row < this.genes.length; row++) {
            for (int col = 0; col < this.genes[row].length; col++) {
                genes[row][col] = rand.nextFloat();
            }
        }
        outputWeights = new float[9][11];
        for (int row = 0; row < this.outputWeights.length; row++) {
            for (int col = 0; col < this.outputWeights[row].length; col++) {
                this.outputWeights[row][col] = rand.nextFloat();
            }
        }
        
        //Matrix.toString(genes);
        //System.out.println("End");
    }

    public void fillArrayWithRandomChances(float[][] gene) {
        for (int row = 0; row < gene.length; row++) {
            gene[row] = generateRandomChance();
            //System.out.println(Arrays.toString(gene[row]));
        }

    }

    public MyCreature(float[][] genes) {
        this.genes = genes;

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

        //Sets up percepts for multiplication
        //plus one for bias weight == -1
        float[][] percepts2D = new float[1][numPercepts + 1];
        for (int col = 0; col < percepts2D[0].length - 1; col++) {
            percepts2D[0][col] = (float) percepts[col];
        }
        percepts2D[0][numPercepts] = -1f;
               
        //float[][] firstNode = new float[1][genes[0].length];
        

        //inputfunciton matrix multiplication
        float[][] firstNode = NeuralNetwork.inputFunction(percepts2D, this.genes);
        float[][] firstNode2D = new float[1][9];
        //relu funciton on outputs
        //convert to 2D
        for (int i = 0; i < firstNode.length; i++) {
            firstNode[0][i] = NeuralNetwork.reluFunction(firstNode[0][i]);
            
        }
        
        for (int col = 0; col < firstNode2D[0].length - 1; col++) {
            firstNode2D[0][col] = (float) firstNode[0][col];
        }
        firstNode2D[0][8] = -1f;
        
        //next output layer
        float[][] secondNode = NeuralNetwork.inputFunction(firstNode2D, this.outputWeights);
        
       
        //softmax
        secondNode = NeuralNetwork.normalise(secondNode);
        
        //convert to 1D
        float[] output = new float[numExpectedActions];
        for (int i = 0; i < output.length; i++) {
            output[i] = secondNode[0][i];
        }

        //actions = randomActionPick(chances);
        actions = output;

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

    public float[][] getGenes() {
        return this.genes;
    }

    public void setGenes(float[][] newGenes) {
        this.genes = newGenes;
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

}
