package model;

public class Difficulties {
    private static String[] difficultiesList = {"F", "M", "D"};

    public static boolean isDifficultyAccepted(String difficulty) {
        for(int i = 0; i< getDifficultiesAmount(); i++) {
            if(difficulty.equals(difficultiesList[i])) {
                return true;
            }
        }
        return false;
    }

    public static String getDifficulty(int index) {
        if(isIndexInRange(index)) {
            return difficultiesList[index];
        }
        return null;
    }

    private static boolean isIndexInRange(int index) {
        if(index >= 0 && index < getDifficultiesAmount()) {
            return true;
        }
        return false;
    }

    public static int getDifficultiesAmount() {
        return difficultiesList.length;
    }
}
