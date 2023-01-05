package de.uniks.abacus.model;


import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.reflect.Link;

import java.util.List;
@SuppressWarnings("unused")
public class GenModel implements ClassModelDecorator{
    //FIXME i still cannot see the class model!!! how to automatically generate it???
    class Player {
        int rightSum;
        int wrongSum;
        @Link("player")
        List<History> histories;
    }

    class History {
        int rightResultTotal;
        int wrongResultTotal;
        String time;
        String finish;
        @Link("histories")
        Player player;
        @Link("history")
        List<Result> results;
    }

    class Result {
        String resultStatus;
        char operation;
        int firstVal;
        int secondVal;
        int resultVal;
        int rightVal;
        @Link("results")
        History history;
    }

    @Override
    public void decorate( ClassModelManager m ) {
        m.haveNestedClasses(GenModel.class);
    }
}
