/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alexis
 */
public class Actions {

    public static final float[] moveX1 = {1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    public static final float[] moveX2 = {0f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    public static final float[] moveX3 = {0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    public static final float[] moveX4 = {0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    public static final float[] moveX5 = {0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 0f};
    public static final float[] moveX6 = {0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f};
    public static final float[] moveX7 = {0f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f};
    public static final float[] moveX8 = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f};
    public static final float[] moveX9 = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f};
    public static final float[] eat = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1f, 0f};
    public static final float[] moveRandom = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1f};
    public static final float[] doNothing = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};

    public static float[] pickAction(int actionCode) {
        float[] actions = new float[11];
        switch (actionCode) {
            case 0:
                actions = Actions.moveX1;
                break;
            case 1:
                actions = Actions.moveX2;
                break;
            case 2:
                actions = Actions.moveX3;
                break;
            case 3:
                actions = Actions.moveX4;
                break;
            case 4:
                actions = Actions.moveX5;
                break;
            case 5:
                actions = Actions.moveX6;
                break;
            case 6:
                actions = Actions.moveX7;
                break;
            case 7:
                actions = Actions.moveX8;
                break;
            case 8:
                actions = Actions.moveX9;
                break;
            case 9:
                actions = Actions.eat;
                break;
            case 10:
                actions = Actions.moveRandom;
                break;
            default:
                actions = Actions.doNothing;
                System.out.println("Nothing happening");

        }
        return actions;

    }

}
