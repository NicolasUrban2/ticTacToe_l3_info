package model;

import ai.Coup;

import java.util.Arrays;

public class GameManager {
    private static GameManager INSTANCE;

    private Coup coup = new Coup(9, "");

    private GameManager() {}

    public static GameManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new GameManager();
        }
        return INSTANCE;
    }

    public void emptyCoup() {
        double[] in = coup.in;
        Arrays.fill(in, 0.0);
        coup.addInBoard(in);
    }

    public void addMoveToCoup(int x, int y, double token) {
        int index = x + y *3;
        addMoveToCoup(index, token);
    }

    public void addMoveToCoup(int index, double token) {
        if(index >= 0 && index < coup.in.length) {
            double[] in = coup.in;
            in[index] = token;
            coup.addInBoard(in);
        }
    }

    // Réalise tous les tests afin de savoir
    public WinningMove gameFinished(int x, int y) {
        int playerToken = (int) coup.in[x+3*y];

        // Test horizontal
        boolean isWiningLine = true;
        int i=0;
        while(i<3 && isWiningLine) {
            if(coup.in[i+3*y] != playerToken) {
                isWiningLine = false;
            }
            //System.out.println("horizontal : "+coup.in[i+3*y]);
            i++;
        }
        if(isWiningLine) {
            return new WinningMove(0, y, 1, y, 2, y, getVictoryForPlayerToken(playerToken));
        }

        // Test vertical
        isWiningLine = true;
        i=0;
        while(i<3 && isWiningLine) {
            if(coup.in[x+3*i] != playerToken) {
                isWiningLine = false;
            }
            //System.out.println("vertical : "+coup.in[x+3*i]);
            i++;
        }
        if(isWiningLine) {
            return new WinningMove(x, 0, x, 1, x, 2, getVictoryForPlayerToken(playerToken));
        }

        // Test diagonal from upper left
        isWiningLine = true;
        i=0;
        while(i<3 && isWiningLine) {
            if(coup.in[i+3*i] != playerToken) {
                isWiningLine = false;
            }
            //System.out.println("diagonal ul : "+coup.in[i+3*i]);
            i++;
        }
        if(isWiningLine) {
            return new WinningMove(0, 0, 1, 1, 2, 2, getVictoryForPlayerToken(playerToken));
        }

        // Test diagonal from upper right
        isWiningLine = true;
        i=2;
        int j=0;
        while(i>=0 && isWiningLine) {
            if(coup.in[i+3*j] != playerToken) {
                isWiningLine = false;
            }
            //System.out.println("diagonal ur : "+coup.in[i+3*j]);
            i--;
            j++;
        }
        if(isWiningLine) {
            return new WinningMove(2, 0, 1, 1, 0, 2, getVictoryForPlayerToken(playerToken));
        }

        boolean isDraw = true;
        int t=0;

        while(t < coup.in.length && isDraw) {
            if(coup.in[t] == 0) {
                isDraw = false;
            }
            //System.out.println("draw : "+coup.in[t]);
            t++;
        }

        if(isDraw) {
            return new WinningMove();
        }
        return null;
    }

    // Renvoie à qui est la victoire en fonction du dernier pion qui a été joué
    private int getVictoryForPlayerToken(int playerToken) {
        return switch (playerToken) {
            case -1 -> 1;
            case 1 -> 2;
            default -> -1;
        };
    }

    public Coup getCoup() {
        return coup;
    }
}
