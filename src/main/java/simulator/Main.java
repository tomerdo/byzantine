package simulator;

public class Main {


    /**
     * this program simulates byzantine algorithm of synchronizing clocks with byzantine node communicating
     * @param args
     */
    public static void main(String[] args){
        int numOfPlayers = 4;
        int numOfByzantines = 1;
        int gameType = 0;
        ISimulator simulator = null;

        if (args.length >= 2){
            numOfPlayers = Integer.parseInt(args[0]);
            numOfByzantines = Integer.parseInt(args[1]);
        }
        if (args.length >= 3){
            gameType = Integer.parseInt(args[1]);
        }

        switch (gameType){
            case 1:
                //all good simulator
                break;
            case 2:
                // user input control byzantine
                break;
            default:
                simulator = new GameSimulator(numOfPlayers,numOfByzantines);
        }

        System.out.println("staring simulator :)");
        simulator.start();
    }
}
