package de.uniks.abacus.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppServiceTest {
    final AppService AS = new AppService();
    @Test
    public void computerCalculateTest() {
        /*
        * Calculate gives a value of the computer calculation
        * */
        int firstVal = 2, secondVal = 3;

        char operation = '*';
        assertEquals(firstVal * secondVal, AS.calculate(firstVal,operation,secondVal));

    }

    @Test
    public void playerCalculationTest() {
        Player player = new Player();
        int firstVal = 2, secondVal = 3;
        int rightVal = AS.calculate(firstVal,'+',secondVal);

        player.setName("Agha").withHistories(new History().withResults(
                new Result().setFirstVal(firstVal)
                        .setSecondVal(secondVal)
                        .setOperation('+')
                        .setRightVal(rightVal)
        ));

        Result resultPlayer = player.getHistories().get(0).getResults().get(0);
        int playerInput = 7;
        boolean howResult = AS.checkResult(resultPlayer.setResultVal(playerInput));

        assertEquals(rightVal == playerInput, howResult);

    }
}
