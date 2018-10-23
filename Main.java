import IA.Energia.Central;
import src.*;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import src.*;

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

            printActions(agent.getActions(), false);
            printInstrumentation(agent.getInstrumentation());
    }

    private static void CentralsSimulatedAnnealing(CentralsRepresentation r) {
        try {
            Problem problem = new Problem(r,
                    new CentralsSuccessorFunctionSA(),
                    new CentralsGoalTest(),
                    new CentralsHeuristicFunction());
            SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(50000000,100,10,0.001D);
            SearchAgent agent = new SearchAgent(problem, search);


            printActions(agent.getActions(), true);
            printInstrumentation(agent.getInstrumentation());
            System.out.println("Finished");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void printActions(List actions, boolean sa) {
        if (sa) {
            CentralsHeuristicFunction hf = new CentralsHeuristicFunction();
            System.out.println(hf.getHeuristicValue(actions.get(0)));

            Integer A = 0;
            Integer B = 0;
            Integer C = 0;
            for (int centralID = 0; centralID < CentralsRepresentation.centrals.size(); centralID++) {
                Central cen = CentralsRepresentation.centrals.get(centralID);
                if (((CentralsRepresentation)actions.get(0)).representationCentrales[centralID] != 0) {
                    if (cen.getTipo() == Central.CENTRALA) {
                        A++;
                    }
                    else if (cen.getTipo() == Central.CENTRALB) {
                        B++;
                    }
                    else {
                        C++;
                    }
                }
            }
            System.out.println("A: " + A + ", B: " + B + ", C: " + C);
        }
        else {
            for (int i = 0; i < actions.size(); ++i) {
                String action = (String)actions.get(i);
                System.out.println(action);
            }
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