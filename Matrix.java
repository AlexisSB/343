import java.util.*;

/** Matrix class handles matrices as 2D float arrays.
  * Will multiply these matrices.
  * @author Alexis Barltrop
  */
public class Matrix{
  static float[][] result;
  /*
  public static void main(String[] args){
    float[][] first = {{1,1,1,1,1,1,1,1,1}};
    float[][] second = {{1,1,1,1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1,1,1,1},
      {1,1,1,1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1,1,1,1},
      {1,1,1,1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1,1,1,1}};
    
    multiply(first,second);
    toString(result);
    
    
  }
*/
  
  public static float[][] multiply(float[][] first, float[][] second){
    int firstRows = first.length;
    int firstColumns = first[0].length;
    
    int secondRows = second.length;
    int secondColumns = second[0].length;
    
    if (firstColumns !=secondRows){
      System.out.println("Cannot multiply matrices.Size mismatch.");
    }else{
      result = new float[firstRows][secondColumns];
      
      float sum = 0;
      
      for( int i = 0; i < firstRows; i ++){
        for (int j = 0; j< secondColumns; j++){
          for( int k = 0; k < secondRows;k++){
            sum += first[i][k]*second[k][j];
          }
          result[i][j] = sum;
          sum = 0;
        }
      }
      
    }
    
    return result;
  }
      
  public static void toString(float[][] matrix){
    int rows = matrix.length;
    //System.out.println(rows);
    //int columns = matrix[0].length;
    
    for ( int i  = 0 ; i < rows ; i++ ){
      System.out.println(Arrays.toString(matrix[i]));
    }
  }

    
}

