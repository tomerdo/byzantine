package algorithms;

import simulator.Player;

import java.util.Collection;

public class DummyAlgorithm implements IAlgorithm {
    private final int MAX_VALUE = 10;
    public DummyAlgorithm() {
    }

    @Override
    public int calcNext(Player me, Collection<Player> players) {
        return me.addAndGet(1) % MAX_VALUE ;
    }
}
