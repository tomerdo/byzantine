package algorithms;

import simulator.Player;

import java.util.Collection;

public interface IAlgorithm {
    int calcNext(Player me, Collection<Player> players);
}
