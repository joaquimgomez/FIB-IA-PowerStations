import src.*;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import src.CentralsRepresentation.*;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class Main {

    public static void main(String[] args) throws Exception {

        CentralsRepresentation r = new CentralsRepresentation(10, 100, TipoSolucionInicial.Prioritarios);

        //CentralsHillClimbing(r);
        CentralsSimulatedAnnealing(r);
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
            SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(100,10,15,0.1);
            SearchAgent agent = new SearchAgent(problem, search);

            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            //String action = (String) actions.get(i);
            //System.out.println(action);
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