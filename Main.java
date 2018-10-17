import src.*;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;


public class Main {

    public static void main(String[] args) throws Exception {

        CentralsRepresentation r;

        if (args[0].equals("r"))     r = new CentralsRepresentation(args[2]);
        else {
            int numClients = Integer.parseInt(args[3]);
            double[] propTypeClients = new double[] {Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6])};
            double propClientsGuaranteed = Double.parseDouble(args[7]);
            int[] propTypeCentrals = new int[] {Integer.parseInt(args[8]), Integer.parseInt(args[9]), Integer.parseInt(args[10])};
            int seed = Integer.parseInt(args[11]);

            r = new CentralsRepresentation(args[2], numClients, propTypeClients, propClientsGuaranteed, propTypeCentrals, seed);
        }

        if (args[1].equals("hc"))    CentralsHillClimbing(r);
        else    CentralsSimulatedAnnealing(r);
    }

    private static void CentralsHillClimbing(CentralsRepresentation r) throws Exception {
            Problem problem = new Problem(r,
                    new CentralsSuccessorFunction(),
                    new CentralsGoalTest(),
                    new CentralsHeuristicFunction());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem, search);

            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
    }

    private static void CentralsSimulatedAnnealing(CentralsRepresentation r) {
        try {
            Problem problem = new Problem(r,
                    new CentralsSuccessorFunctionSA(),
                    new CentralsGoalTest(),
                    new CentralsHeuristicFunction());
            SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(2000,100,5,0.001D);
            SearchAgent agent = new SearchAgent(problem, search);

            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); ++i) {
            String action = (String)actions.get(i);
            System.out.println(action);
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }

}