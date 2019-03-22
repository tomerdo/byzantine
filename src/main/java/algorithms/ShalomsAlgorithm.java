package algorithms;

import simulator.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * this class implements Prof's Shlomi algorithm for byzantines Fault-Tollerance
 */
public class ShalomsAlgorithm implements IAlgorithm {

    private int n; // num of players / processors
    private int f; // num of byzantines

    private int MAX_VALUE = 10;


    public ShalomsAlgorithm(int n, int f) {
        this.n = n;
        this.f = f;
    }

    public ShalomsAlgorithm(int numOfPlayers, int numOfByzantines, int numOfClockValues) {
        this(numOfPlayers , numOfByzantines);
        MAX_VALUE = numOfClockValues;
    }

    @Override
    public int calcNext(Player me, Collection<Player> players) {
        int suggestedNextValue;
        // sending myClock value to all
        me.sendMyValueToNeighbors();

        List<Integer> neighborsValues = new ArrayList<>();

        // receiving all the clock values
        for (Iterator<Player> playerIterator = players.iterator(); playerIterator.hasNext(); ) {
            Player next =  playerIterator.next();

            if (next.getIndex() != me.getIndex()){
                int neighborValue = next.getMessage(me.getIndex());
                neighborsValues.add(neighborValue);
            }
        }


        long consensusCount = neighborsValues.stream().filter(neighborClockValue -> neighborClockValue == me.get()).count();

        System.out.println("consensus value for index: " + me.getIndex() + " and value: " + me.get() + " is: " + consensusCount + " consensus facor is: " + (n-f-1));

        if (consensusCount < n - f - 1){ // if i am not in consensus i need to reset to "think"
            suggestedNextValue = 0;
            me.setLastIncrement(false);
        }
        else { // you are with the consensus
            if (me.get() != 0){
                suggestedNextValue = me.addAndGet(1) % MAX_VALUE;
                me.setLastIncrement(true);
            }
            else { // last value was 0
                if (me.isLastIncrement()){
                    suggestedNextValue = 1;
                }
                else {
                    suggestedNextValue = randomBetweenOneAndZero();
                    System.out.println("player " + me.getIndex() + " random value is: " + suggestedNextValue);
                }
                if (suggestedNextValue == 1){
                    me.setLastIncrement(true);
                }
                else{
                    me.setLastIncrement(false);
                }
            }
        }
        return suggestedNextValue;
    }

    /**
     *
     * @return if the random number is even return 0 , else 1
     */
    public static int randomBetweenOneAndZero() {
        return (int) (Math.ceil(Math.random() * 10000) ) % 2;
    }
}
