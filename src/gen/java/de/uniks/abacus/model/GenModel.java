package de.uniks.abacus.model;

import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.reflect.Link;

import java.util.List;

@SuppressWarnings("unused")
public class GenModel implements ClassModelDecorator{

    class Game {
        @Link
        List<Player> players;
    }

    class Player {
        int rightSum;
        int wrongSum;
        String name;
        int id;
        @Link("player")
        List<History> histories;
        @Link("player")
        List<Result> results;
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
        String operation; //because the yaml cannot accept - (minus) as char
        int firstVal;
        int secondVal;
        int resultVal;
        int rightVal;
        @Link("results")
        History history;
        @Link("results")
        Player player;
    }

    @Override
    public void decorate( ClassModelManager m ) {
        m.haveNestedClasses(GenModel.class);
    }
}
