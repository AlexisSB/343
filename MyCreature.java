import cosc343.assig2.Creature;
import java.util.*;

/**
* The MyCreate extends the cosc343 assignment 2 Creature.  Here you implement
* creatures chromosome and the agent function that maps creature percepts to
* actions.  
*
* @author  
* @version 1.0
* @since   2017-04-05 
*/
public class MyCreature extends Creature {

  // Random number generator
  Random rand = new Random();
  int chromosomeLength ;
  float[] chromosome1;
  

  /* Empty constructor - might be a good idea here to put the code that 
   initialises the chromosome to some random state   
  
   Input: numPercept - number of percepts that creature will be receiving
          numAction - number of action output vector that creature will need
                      to produce on every turn
  */
  public MyCreature(int numPercepts, int numActions) {
    chromosomeLength = (int)Math.pow(3,numPercepts)*numActions;
      chromosome1 = new float[chromosomeLength];
      for(int gene =0;gene<chromosomeLength;gene++){
            chromosome1[gene]=rand.nextFloat();
            //System.out.println(chromosome1[row][col]);
          }
      //System.out.println((Arrays.toString(chromosome1)));
  }
  
  public MyCreature(float[] chromosome){
      this.chromosome1= chromosome;
      this.chromosomeLength = chromosome.length;
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
      
      float actions[] = new float[numExpectedActions];
      int[] perceptsCopy = Arrays.copyOf(percepts,percepts.length);
      //System.out.println(Arrays.toString(perceptsCopy));
      
      for (int i=0;i<perceptsCopy.length;i++){
          if(perceptsCopy[i]>=2){
              perceptsCopy[i]=2;
          }else{
          perceptsCopy[i]++;
          }
      }
      StringBuilder perceptString = new StringBuilder();
      for (int percept :perceptsCopy){
        perceptString.append(percept);
      }
      int baseThree = Integer.parseInt(perceptString.toString(),3);
      for (int index = 0;index<numExpectedActions;index++){
          actions[index]=chromosome1[(baseThree*numExpectedActions)+index];
      }
      return actions;
  }
  
  public void printChromosome(){
    for (int gene = 0;gene<11; gene++){
          System.out.println(chromosome1[gene]);
    }
     System.out.println(); 
      
  }
  
  public float[] getChromosome(){
      return chromosome1;
  }
  
  public float getChromosomeAt(int index){
      return chromosome1[index];
  }
  public void setChromosome(float[] chromosome){
      this.chromosome1 = Arrays.copyOf(chromosome,chromosome.length);
  }
  public void setChromosome(float value,int index){
      chromosome1[index] = value;
  }
}