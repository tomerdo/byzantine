package simulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

public class GameSimulator implements ISimulator{

    private ScheduledExecutorService scheduledExecutorService;
    private static final long ROUND_TIME = 10L;
    private static final int NUM_OF_CLOCK_VALUES = 12;
    private int roundNum = 0;
    private CyclicBarrier barrier ;
    private int numOfByzantines;
    private int numOfPlayers;
    private ThreadPoolExecutor threadPoolExecutor;
    private List<Player> playersList;

    public GameSimulator(int numOfPlayers , int numOfByzantines) {
        this.numOfByzantines = numOfByzantines;
        this.numOfPlayers = numOfPlayers;

        if (numOfPlayers <= numOfByzantines * 3){
            throw new RuntimeException("Illegal num of byzantines - no solution!!!");
        }
        else {
            playersList = createPlayers(numOfPlayers , numOfByzantines);
            threadPoolExecutor = new ThreadPoolExecutor(numOfPlayers , numOfPlayers , 20 , SECONDS , new ArrayBlockingQueue<Runnable>(numOfPlayers));
        }
    }

    private List<Player> createPlayers(int numOfPlayers, int numOfByzantines) {
        List<Player> lst = new ArrayList(numOfPlayers);
        barrier = new CyclicBarrier(numOfPlayers);

        int i = 0;
        for (; i < numOfPlayers - numOfByzantines; i++) { // creating the "good" players
            lst.add(new GoodPlayer(barrier , i , numOfPlayers , numOfByzantines , NUM_OF_CLOCK_VALUES ));

        }
        for (int j = i; j < numOfPlayers; j++) { // creating the byzantine player
            lst.add(new ByzantinePlayer(barrier , j , numOfPlayers , numOfByzantines , NUM_OF_CLOCK_VALUES));

        }

        lst.forEach(player -> player.setAllPlayers(lst));
        System.out.println("num of players is: " + numOfPlayers + " num of byzantines is: " + numOfByzantines);
        return lst;
    }

    private void pulse() {
        System.out.println("Starting round: #" + roundNum + " (the time is: " + new Date().toString()+ ")");
        playersList.parallelStream().forEach(player -> threadPoolExecutor.execute(() -> player.advance()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // check only the good players to end the run (the byzantines are irrelevant)
        if (playersList.parallelStream().filter(player -> !player.isByzantine()).allMatch(player -> player.get() == NUM_OF_CLOCK_VALUES - 1) ){
            System.out.println("Success!!! end of synchronization. all good players values are: " + NUM_OF_CLOCK_VALUES);
            threadPoolExecutor.shutdown();
            scheduledExecutorService.shutdown();
        }
        else {
            playersList.parallelStream().forEach(player -> System.out.println("after sync algorithm -->" + player));
            System.out.println("end of round #" + roundNum++);
        }
    }

    public void start(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(() -> pulse() , ROUND_TIME , ROUND_TIME  , SECONDS);
    }


}
