package simulator;

import algorithms.ShalomsAlgorithm;

import java.util.concurrent.CyclicBarrier;

public class ByzantinePlayer extends Player {

    public ByzantinePlayer(CyclicBarrier barrier, int index, int numOfPlayers, int numOfByzantines, int numOfClockValue) {
        super(barrier, index, numOfPlayers, numOfByzantines, numOfClockValue);
    }

    @Override
    public void sendMyValueToNeighbors() {
        for (int i = 0; i < sendClockValues.length; i++) {
            sendClockValues[i] = ShalomsAlgorithm.randomBetweenOneAndZero();
        }
    }

    @Override
    public boolean isByzantine() {
        return true;
    }
}
