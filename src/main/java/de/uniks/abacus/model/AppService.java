package de.uniks.abacus.model;

import java.util.Random;

import static de.uniks.abacus.Constant.*;

public class AppService {

    final private Random random = new Random();
    private int calculate(int firstVal, char operation, int secondVal){
        int resultVal = 0;
        switch (operation) {
            case '+' -> resultVal = firstVal + secondVal;
            case '-' -> resultVal = firstVal - secondVal;
            case '*' -> resultVal = firstVal * secondVal;
            case '/' -> {
                if(secondVal != 0) {
                    resultVal = firstVal / secondVal;
                } else {
                    throw new ArithmeticException("Prohibited operation: secondValue is 0");
                }
            }
        }

        return resultVal;
    }

    /**
     * Check the result whether the user answer correct or wrong
     * either wrong or correct.
     * the player and the history should increase their
     * sum and total of (wrong or correct).
     * This method should only be used if the
     * right result has been calculated and the result has
     * a player and a history
     *
     * @param result object that fulfill the condition
     */
    private void checkResult(Result result) {
        if(result.getPlayer() == null || result.getHistory() == null || result.getRightVal() == 0){
            throw new IllegalArgumentException("The program is incomplete!");
        }
        boolean isCorrect = result.getRightVal() == result.getResultVal();
        Player currentPlayer = result.getPlayer();
        History currentHistory = result.getHistory();
        if(isCorrect){
            currentPlayer.setRightSum(currentPlayer.getRightSum() + 1);
            currentHistory.setRightResultTotal(currentHistory.getRightResultTotal() + 1);
            result.setResultStatus(CORRECT);
        } else {
            currentPlayer.setWrongSum(currentPlayer.getWrongSum() + 1);
            currentHistory.setWrongResultTotal(currentHistory.getWrongResultTotal() + 1);
            result.setResultStatus(WRONG);
        }
    }

    /**
     * This method find a new task if the current Task has the result of not natural numbers
     * @param player to get the value of current player
     * @param origin the lowest value for the random
     * @param upperBound the highest value for the random
     */
    protected void checkDivision(Player player, int origin, int upperBound) {
        int historyIndex = player.getHistories().size() - 1;
        int resultIndex = player.getHistories().get(historyIndex).getResults().size() - 1;
        Result currentResult = player.getHistories().get(historyIndex).getResults().get(resultIndex);
        float firstVal =  currentResult.getFirstVal();
        float secondVal = currentResult.getSecondVal();
        int intVal = (int) (firstVal/secondVal);
        /*
        * example:
        * fistVal = 3 secondVal = 2;
        * intVal = 1
        * firstval/secondVal = 1.5
        * because 1 != 1.5 find another number until they have the same value!
        * */
        while ((firstVal/secondVal) != intVal) {
            firstVal = random.nextInt(origin,upperBound);
            secondVal = random.nextInt(origin, (int) firstVal);
            intVal = (int) (firstVal/secondVal);
        }

        // after quit from the loop, set all the new value to the player!
        currentResult.setFirstVal((int) firstVal)
                .setSecondVal((int) secondVal);
    }

    /**
     * This function only be used after the user gives an Answer
     * We want to know everytime the new results is being created, the results should be able
     * to access either from player or from the history.
     * The origin and bound are for the checkDivision, to find another alternative number
     * if the right result is not within the set of Natural Number
     * @param player current player
     * @param firstVal first value
     * @param operation operation
     * @param secondVal second value
     * @param origin for the random in checkDivision
     * @param bound for the random in checkDivision
     * @return current result
     */
    public Result creatNewResult(Player player, int firstVal, char operation, int secondVal, int userInput
            , int origin, int bound) {
        if(operation == '/') // check for other possibility! look at the method description!
            checkDivision(player, origin, bound);

        Result result = new Result()
                .setPlayer(player)
                .setFirstVal(firstVal)
                .setSecondVal(secondVal)
                .setOperation(operation)
                .setRightVal(calculate(firstVal,operation,secondVal))
                .setResultVal(userInput);

        int currentHistorySize = player.getHistories().size();
        if(currentHistorySize != 0) {
            //take the last history that is being created and put the result in
            History currentHistory = player.getHistories().get(currentHistorySize - 1);
            currentHistory.withResults(result);
        } else {
            History currentHistory = new History().withResults(result);
            player.withHistories(currentHistory);
        }

        // check the result that has a Player and also a History
        checkResult(result);

        return result;
    }
}
