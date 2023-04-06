package model;

import ai.Coup;
import ai.MultiLayerPerceptron;

public class AiPlayer {
    private static AiPlayer INSTANCE;
    private MultiLayerPerceptron multiLayerPerceptron;

    private AiPlayer(MultiLayerPerceptron multiLayerPerceptron) {
        setMultiLayerPerceptron(multiLayerPerceptron);
    }

    public static AiPlayer getInstance(MultiLayerPerceptron multiLayerPerceptron) {
        if(INSTANCE == null) {
            INSTANCE = new AiPlayer(multiLayerPerceptron);
        }
        return INSTANCE;
    }

    public void setMultiLayerPerceptron(MultiLayerPerceptron multiLayerPerceptron) {
        if(multiLayerPerceptron != null) {
            this.multiLayerPerceptron = multiLayerPerceptron;
        }
    }

    public MoveCoordinates getNextMoveCoordinates(Coup coup) {
        double[] res = getAiMoveTable(coup);

        int index = 0;
        double max = 0;
        for(int i=0; i<9; i++) {
            if(coup.in[i] == 0.0 && res[i] > max) {
                max = res[i];
                index = i;
            }
        }

        int xAiMoveCoordinates, yAiMoveCoordinates;
        if(index < 3) {
            xAiMoveCoordinates = index;
            yAiMoveCoordinates = 0;
        } else if(index < 6) {
            xAiMoveCoordinates = index-3;
            yAiMoveCoordinates = 1;
        } else {
            xAiMoveCoordinates = index-6;
            yAiMoveCoordinates = 2;
        }

        return new MoveCoordinates(xAiMoveCoordinates, yAiMoveCoordinates);
    }

    private double[] getAiMoveTable(Coup coup) {
        double[] res = multiLayerPerceptron.forwardPropagation(coup.in);
        return res;
    }
}
