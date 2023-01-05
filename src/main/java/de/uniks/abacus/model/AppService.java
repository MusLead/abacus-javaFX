package de.uniks.abacus.model;

public class AppService {
    public int calculate(int firstVal, char operation, int secondVal){
        int resultVal = 0;
        switch (operation){
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

    public boolean checkResult(Result result) {
        return result.getRightVal() == result.getResultVal();
    }
}
