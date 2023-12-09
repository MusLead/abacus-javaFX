package de.uniks.abacus.model;

import org.junit.Test;

import static de.uniks.abacus.Constant.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class AppServiceTest {

    /*
     * PROGRAMMING'S RULE:
     * The Player should be created each time the user wants to create a new player.
     * The Task should be created within createNewResult() Method!
     * The userInput of the calculation's result should be ONLY accessed by createNewResult()
     * The Player should not take the userInput's value directly!
     *
     * For the bound and the userInputs can Only accept 10 digits number (because of the
     * userInput = Integer.parseInt(answerField.getText()); (CalculationController.class)
     * by default, only accept 10 Digits.
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
        Result result = AS.creatNewResult(player, firstVal, operation, secondVal, playerInput);

        assertEquals(firstVal * secondVal, result.getRightVal());

    }

    @Test
    public void playerCalculationTest() {
        Player player = new Player().setName("Agha");
        int firstVal = 2, secondVal = 3;
        int playerInput = 7;

        Result rightVal = AS.creatNewResult(player,firstVal,'+',secondVal,playerInput);

        assertEquals(rightVal.getRightVal() == playerInput, rightVal.getResultStatus().contains(CORRECT));
    }

    @Test
    public void checkDivisionTest(){
        int firstVal = 3, secondVal = 2;
        for (int i = 0; i < 100; i++) {
            Result result = AS.checkDivision(0, 1000, firstVal, secondVal);
            /* why (float) with brackets?
             * https://stackoverflow.com/questions/65678297/solve-integer-division-in-floating-point-context
             */
            float resultTest = (float) (result.getFirstVal() / result.getSecondVal()) * 10;
            //it should return true if the (result*10)%10 should be 0
            assertEquals(0, (resultTest) % 10,0);
            assertTrue(result.getFirstVal() > result.getSecondVal()); // test if the firstValue is bigger than secondvalue!
        }
    }

    @Test
    public void checkMultiplicationLimit(){
        AppService appService = new AppService();
        Result result = appService.checkMultiplicationLimit(0,10000,322323,2332332);
        System.out.println(result.getFirstVal() * result.getSecondVal());
        assertTrue(result.getFirstVal() * result.getSecondVal() <= 999999999);
    }
}
