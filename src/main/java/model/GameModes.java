package model;

import java.util.List;

public class GameModes {
    private static String[] gameModesList = {"pvp", "pve"};

    public static boolean isGameModeAccepted(String gameMode) {
        for(int i=0; i<getGameModesAmount(); i++) {
            if(gameMode.equals(gameModesList[i])) {
                return true;
            }
        }
        return false;
    }

    public static int getGameModesAmount() {
        return gameModesList.length;
    }
}
