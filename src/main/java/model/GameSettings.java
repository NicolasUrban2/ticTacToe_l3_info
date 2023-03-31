package model;

import ai.MultiLayerPerceptron;

public class GameSettings {
    private static GameSettings INSTANCE;

    private MultiLayerPerceptron multiLayerPerceptron;

    private String gameMode;
    private String difficulty;

    private GameSettings() {

    }

    public static GameSettings getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new GameSettings();
        }
        return INSTANCE;
    }

    public GameSettings setGameMode(String gameMode) {
        if(GameModes.isGameModeAccepted(gameMode)) {
            this.gameMode = gameMode;
        }
        return this;
    }

    public String getGameMode() {
        return this.gameMode;
    }

    public GameSettings setDifficulty(String difficulty) {
        if(Difficulties.isDifficultyAccepted(difficulty)) {
            this.difficulty = difficulty;
        }
        return this;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public MultiLayerPerceptron getMultiLayerPerceptron() {
        return multiLayerPerceptron;
    }

    public void setMultiLayerPerceptron(MultiLayerPerceptron multiLayerPerceptron) {
        this.multiLayerPerceptron = multiLayerPerceptron;
    }
}
