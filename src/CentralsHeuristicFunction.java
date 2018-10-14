package src;

import aima.search.framework.HeuristicFunction;


public class CentralsHeuristicFunction implements HeuristicFunction {

	// Constructor
	public CentralsHeuristicFunction() {

	}

	public double getHeuristicValue(Object s) {
		CentralsRepresentation state = (CentralsRepresentation) s;

		return HeuristicFunction1(state);
	}

	public double HeuristicFunction1(CentralsRepresentation state) {
		double heuristico = state.beneficio * (1.0D - (state.entropia / (double)CentralsRepresentation.clients.size()));

		return -heuristico;
	}
}
