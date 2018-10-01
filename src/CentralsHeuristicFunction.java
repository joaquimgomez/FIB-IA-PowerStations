package src;

import aima.search.framework.HeuristicFunction;

public class CentralsHeuristicFunction implements HeuristicFunction {

	public CentralsHeuristicFunction() {

	}

	public double getHeuristicValue(Object state){
		CentralsRepresentation s = (CentralsRepresentation) state;
		double beneficio = 0.0;

		// Cálculo del beneficio con las conexiones del estado actual s

		return beneficio;
	}

}
