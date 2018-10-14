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

		// Coste centrales

		// Beneficio clientes

		// Entropia solucion
		if (state.hCliente_old != -1) { // no es la solución inicial

			if (state.hCliente_new == -1) { // assign
				double dist_old = getDistacia(state.hCentral_old, state.hCliente_old);
				double dist_new = getDistacia(state.hCentral_new, state.hCliente_old);
				double perdida_old = VEnergia.getPerdida(dist_old);
				double perdida_new = VEnergia.getPerdida(dist_new);
				state.entropia = state.entropia - perdida_old + perdida_new;
			}
			else {  // swap
				double dist_old_old = getDistacia(state.hCentral_old, state.hCliente_old);
				double dist_new_old = getDistacia(state.hCentral_new, state.hCliente_old);
				double dist_old_new = getDistacia(state.hCentral_old, state.hCliente_new);
				double dist_new_new = getDistacia(state.hCentral_new, state.hCliente_new);
				double perdida_old_old = VEnergia.getPerdida(dist_old_old);
				double perdida_new_old = VEnergia.getPerdida(dist_new_old);
				double perdida_old_new = VEnergia.getPerdida(dist_old_new);
				double perdida_new_new = VEnergia.getPerdida(dist_new_new);
				state.entropia = state.entropia - perdida_old_old + perdida_new_old;
				state.entropia = state.entropia - perdida_new_new + perdida_old_new;
			}

		}

		double heuristico = state.beneficio * (1.0D - (state.entropia / (double)CentralsRepresentation.clients.size()));

		return -heuristico;
	}
}
