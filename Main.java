import src.*;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class Main {

    public static void main(String[] args){

        CentralsRepresentation r = new CentralsRepresentation(/*FALTAN PARAMETROS*/);

        if (Integer.parseInt(args[1]) == 1)   CentralsHillClimbing(r);
        else    CentralsSimulatedAnnealing(r);

    }

    private static void CentralsHillClimbing(CentralsRepresentation r){
        try {
            Problem problem = new Problem(r,
                    new CentralsSuccessorFunction(),
                    new CentralsGoalTest(),
                    new CentralsHeuristicFunction());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem, search);

            /*
            Impresión de la solución
            */
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void CentralsSimulatedAnnealing(CentralsRepresentation r){
        try {
            Problem problem = new Problem(r,
                    new CentralsSuccessorFunctionSA(),
                    new CentralsGoalTest(),
                    new CentralsHeuristicFunction());
            SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(/* PARAMETROS */);
            SearchAgent agent = new SearchAgent(problem, search);

            /*
            Impresión de la solución
            */
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}