package model;

public class WinningMove {
    public int xA, yA, xB, yB, xC, yC;
    public double victoryToken;
    public boolean isDraw;

    public WinningMove(int xA, int yA, int xB, int yB, int xC, int yC, double victoryToken) {
        this.xA = xA;
        this.yA = yA;
        this.xB = xB;
        this.yB = yB;
        this.xC = xC;
        this.yC = yC;
        this.victoryToken = victoryToken;
        this.isDraw = false;
    }

    public WinningMove() {
        this.isDraw = true;
    }
}
