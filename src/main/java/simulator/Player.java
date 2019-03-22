package simulator;

import algorithms.DummyAlgorithm;
import algorithms.IAlgorithm;
import algorithms.ShalomsAlgorithm;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;


public abstract class Player {
    private AtomicInteger myCounter = new AtomicInteger(0);
    private List<Player> allPlayers;
    private CyclicBarrier barrier;
    private int numOfClockValues;
    private int index;

    protected int[] sendClockValues;

    private boolean lastIncrement = false;

    private IAlgorithm algorithm;// = new DummyAlgorithm();

    public Player(CyclicBarrier barrier, int index) {
        this.barrier = barrier;
        this.index = index;
    }


    public Player(CyclicBarrier barrier, int index , int numOfPlayers , int numOfByzantines , int numOfClockValue) {
        this.barrier = barrier;
        this.index = index;
        this.sendClockValues = new int[numOfPlayers];
        this.numOfClockValues = numOfClockValue;
        this.algorithm = new ShalomsAlgorithm(numOfPlayers , numOfByzantines , numOfClockValues);
    }


    public void setAllPlayers(List<Player> allPlayers){
        this.allPlayers = allPlayers;
    }

    /**
     * each pulse the the Player / Processor execute this function
     */
    public void advance() {
        int nextValue = algorithm.calcNext(this, allPlayers);
        try {
            barrier.await();
            System.out.println("advancing the value of " + index + " to be " + nextValue);
            myCounter.set(nextValue);
            sendMyValueToNeighbors();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public abstract void sendMyValueToNeighbors();

    public abstract boolean isByzantine();

    public int getMessage(int index) {
        return sendClockValues[index];
    }

    public int addAndGet(int delta){
        return myCounter.addAndGet(delta);
    }

    public int get(){
        return myCounter.get();
    }

    public int getIndex() {
        return index;
    }

    public boolean isLastIncrement() {
        return lastIncrement;
    }

    public void setLastIncrement(boolean lastIncrement) {
        this.lastIncrement = lastIncrement;
    }


    @Override
    public String toString() {
        return "Player "+ index + " counter showing: " + myCounter + (isByzantine() ? " !WARNING! Byzantine " : "");
    }

    public void initSendClockValue(int newValue) {
        for (int j = 0; j < sendClockValues.length; j++) {
            sendClockValues[j] = newValue;

        }
    }
}
