package simulator;

import java.util.concurrent.CyclicBarrier;

public class GoodPlayer extends Player {

    public GoodPlayer(CyclicBarrier barrier, int index) {
        super(barrier, index);
    }

    public GoodPlayer(CyclicBarrier barrier, int index, int numOfPlayers, int numOfByzantines, int numOfClockValue) {
        super(barrier, index, numOfPlayers, numOfByzantines, numOfClockValue);
    }

    @Override
    public void sendMyValueToNeighbors() {
        initSendClockValue(get());
    }

    @Override
    public boolean isByzantine() {
        return false;
    }
}
