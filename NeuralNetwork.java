/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;

/**
 *
 * @author abarltrop
 */
public class NeuralNetwork {

    static final Random rand = new Random();

    //don't forget to add bias -1 to end of inputs
    // Add bias weights to end last row of weights
    // 1x10 time 10x11
    static int numInputs = 9;
    static float[][] inputs = new float[1][numInputs];
    static int numFirstNodes = 8;
    static int numSecondNodes = 8;

    static float[][] firstNodeWeights = new float[9][numFirstNodes];
    static float[][] testCross = new float[9][numFirstNodes];
    //static float[][] secondNodeWeights = new float[numFirstNodes][11];

    public static void main(String[] args) {
        for (int row = 0; row < firstNodeWeights.length; row++) {
            for (int col = 0; col < firstNodeWeights[row].length; col++) {
                firstNodeWeights[row][col] = rand.nextFloat();
            }
        }

        //System.out.println(Arrays.toString(NeuralNetwork.inputFunction(inputs, weights)));
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

    //public static linearFunction
    public static float[][] normalise(float[][] inputs) {
        float sum = 0;
        for (int i = 0; i < inputs[0].length; i++) {
            sum += inputs[0][i];
        }
        //System.out.println(sum);
        for (int i = 0; i < inputs[0].length; i++) {
            inputs[0][i] /= sum;
        }
        return inputs;
    }

    /**
     * input function =wT*x-w0; results 2D should be 1x11
     */
    public static float[][] inputFunction(float[][] inputs, float[][] weights) {
        float[][] results = Matrix.multiply(inputs, weights);

        if (results.length != 1) {
            System.out.println("Error. result vector not a row vector");
            System.exit(0);
            return new float[0][0];
        } else {
            return results;
        }
    }

    public static float[][] crossOverWeights(float[][] weights1, float[][] weights2) {
        if (weights1.length == weights2.length) {
            float[][] crossedWeights = weights1;
            for (int row = 0; row < weights1.length; row++) {
                for (int col = 0; col < weights1[row].length; col++) {
                    float newWeight = weights1[row][col];
                    newWeight += weights2[row][col];
                    newWeight /= 2;
                    crossedWeights[row][col] = newWeight;
                }
            }

            return crossedWeights;
        } else {
            System.out.println("Error weights size mismatch");
            return new float[0][0];
        }
    }

    static float[][] trainNN(float[][] weights, float fitness, float maxFitness) {
        float alpha = 0.05f;
        for (int row = 0; row < weights.length; row++) {
            for (int col = 0; col < weights[row].length; col++) {
                weights[row][col] -= (alpha * (1 - (fitness / maxFitness)));
            }
        }
        return weights;
    }

}
