/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Arrays;

/**
 *
 * @author abarltrop
 */
public class NeuralNetwork {

    //don't forget to add bias -1 to end of inputs
    // Add bias weights to end last row of weights
    // 1x10 time 10x11
    static float[][] inputs = {{1, 1, 1, 1, 1, 1, 1, 1, 1, -1}};
    static float[][] weights = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    {1, 1, 1, 1, 1, 1, 1, 1, 1, 10, 1}};

    public static void main(String[] args) {
        System.out.println(Arrays.toString(NeuralNetwork.inputFunction(inputs, weights)));
    }

    /**
     * If value larger than zero return the value. If less than zero return
     * zero.
     *
     * @param value
     */
    public static float reluFunction(float value) {
        if (value > 0) {
            return value;
        } else {
            return 0;
        }
    }
    
    public static float[] normalise(float[] inputs){
        float sum = 0;
        for (int i = 0; i<inputs.length; i++){
            sum += inputs[i];
        }
        System.out.println(sum);
        for (int i = 0; i<inputs.length; i++){
            inputs[i] /= sum;
        }
        return inputs;
    }
    
    

    /**
     * input function =wT*x-w0; results 2D should be 1x11
     */
    public static float[] inputFunction(float[][] inputs, float[][] weights) {
        float[][] results = Matrix.multiply(inputs, weights);
        float[] output = new float[results[0].length];

        if (results.length != 1) {
            System.out.println("Error. result vector not a row vector");
        } else {

            for (int i = 0; i < results[0].length; i++) {
                output[i] = results[0][i];
            }
        }
        return output;
    }

}
