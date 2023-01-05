package de.uniks.abacus.model;


import de.uniks.abacus.Constant;
import org.junit.jupiter.api.Test;

import static de.uniks.abacus.Constant.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppServiceTest {

    /*
     * PROGRAMMING'S RULE:
     * The Player should be created each time the user want to create a new player.
     * The Task should be created within createNewResult() Method!
     * The userInput of the calculation's result should be ONLY accessed by createNewResult()
     * The Player should not take the userInput's value directly!
     */

    final AppService AS = new AppService();
    @Test
    public void computerCalculateTest() {
        /*
        * Calculate() gives a value of the computer calculation
        * */
        int firstVal = 2, secondVal = 3, playerInput = 7;
        char operation = '*';

        Player player = new Player();
        Result result = AS.creatNewResult(player, firstVal, operation, secondVal, playerInput,0, 1000);

        assertEquals(firstVal * secondVal, result.getRightVal());

    }

    @Test
    public void playerCalculationTest() {
        Player player = new Player().setName("Agha");
        int firstVal = 2, secondVal = 3;
        int playerInput = 7;

        Result rightVal = AS.creatNewResult(player,firstVal,'+',secondVal,playerInput,0,10000);

        assertEquals(rightVal.getRightVal() == playerInput, rightVal.getResultStatus().contains(CORRECT));
    }

    @Test
    public void checkDivisionTest(){
        int firstVal = 3, secondVal = 2;
        Player player = new Player().withHistories(new History().withResults(
                new Result().setFirstVal(firstVal)
                        .setSecondVal(secondVal)
                        .setOperation('/')
        ));

        AS.checkDivision(player, 0, 1000);

        int historyIndex = player.getHistories().size() - 1;
        int resultIndex = player.getHistories().get(historyIndex).getResults().size() - 1;
        Result currentResult = player.getHistories().get(historyIndex).getResults().get(resultIndex);

        int firstValNow = currentResult.getFirstVal();
        int secondValNow = currentResult.getSecondVal();
        assertNotEquals(firstVal,firstValNow);
        assertNotEquals(secondVal, secondValNow);

        /* why (float) ?
         * https://stackoverflow.com/questions/65678297/solve-integer-division-in-floating-point-context
         */
        float resultTest = (float) (firstVal/secondVal)*10;
        //it should return true, if the (result*10)%10 should be 0
        assertEquals(0, (resultTest)%10);
    }
}
