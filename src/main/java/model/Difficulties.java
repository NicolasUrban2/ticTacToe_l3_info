package model;

public class Difficulties {
    private static String[] difficultiesList = {"F", "M", "D"};

    public static int getDifficultiesAmount() {
        return difficultiesList.length;
    }

    public static boolean isDifficultyAccepted(String difficulty) {
        for(int i = 0; i< getDifficultiesAmount(); i++) {
            if(difficulty.equals(difficultiesList[i])) {
                return true;
            }
        }
        return false;
    }
}
