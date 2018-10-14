package src;

import IA.Energia.Central;
import IA.Energia.Cliente;
import aima.search.framework.HeuristicFunction;
import IA.Energia.VEnergia;

public class CentralsHeuristicFunction implements HeuristicFunction {

	// Constructor
	public CentralsHeuristicFunction() {

	}

	public double getHeuristicValue(Object s) {
		CentralsRepresentation state = (CentralsRepresentation) s;

		try {
			return HeuristicFunction1(state);
		}
        catch (Exception e) {
			System.out.println("Excepción: " + e);
			return 0.0;
		}
	}

	public double HeuristicFunction1(CentralsRepresentation state) throws Exception {

		// Coste centrales
		if (state.hCliente_old != -1) { // no es la solución inicial

			if (state.hCliente_new == -1) { // assign

				if (state.hCentral_old != -1 && state.representationCentrales[state.hCentral_old] == 0){ // se apaga una central old
					Central cent = CentralsRepresentation.centrals.get(state.hCentral_old);
                    state.beneficio += VEnergia.getCosteMarcha(cent.getTipo());
					state.beneficio += VEnergia.getCosteProduccionMW(cent.getTipo());
					state.beneficio -= VEnergia.getCosteParada(cent.getTipo());
				}
				if (state.hCentral_new != -1 && state.representationCentrales[state.hCentral_new] == 0) {  // se apaga una central new
                    Central cent = CentralsRepresentation.centrals.get(state.hCentral_new);
					state.beneficio += VEnergia.getCosteMarcha(cent.getTipo());
					state.beneficio += VEnergia.getCosteProduccionMW(cent.getTipo());
					state.beneficio -= VEnergia.getCosteParada(cent.getTipo());
				}
				if (state.hCentral_new != -1 && state.representationCentrales[state.hCentral_new]
						== CentralsRepresentation.clients.get(state.hCliente_old).getConsumo()) { // se enciende central
					Central cent = CentralsRepresentation.centrals.get(state.hCentral_new);
					state.beneficio -= VEnergia.getCosteMarcha(cent.getTipo());
					state.beneficio -= VEnergia.getCosteProduccionMW(cent.getTipo());
					state.beneficio += VEnergia.getCosteParada(cent.getTipo());
				}

			}
			else {	// swap
                // no cambia nada
			}

		}

		// Beneficio clientes
		if (state.hCliente_old != -1) { // no es la solución inicial

			if (state.hCliente_new == -1){ // assign
				Cliente c = state.clients.get(state.hCliente_old);

				if (state.hCentral_old == -1) { // fuera a dentro
					if (c.getContrato() == Cliente.NOGARANTIZADO) {
						state.beneficio += VEnergia.getTarifaClienteNoGarantizada(c.getTipo()) * c.getConsumo();
					}
					else {
						state.beneficio += VEnergia.getTarifaClienteGarantizada(c.getTipo()) * c.getConsumo();
					}
				}
				else {  // de una central a otra
					// solo cambia el beneficio si la central se apaga (se comprueba en el coste de centrales
				}
			}
			else {  // swap
				// en swap de clientes entre centrales no afecta al beneficio
			}
		}

		// Entropia solucion
		if (state.hCliente_old != -1) { // no es la solución inicial

			if (state.hCliente_new == -1) { // assign
				double dist_old = state.hCentral_old != -1 ? IAUtils.getDistacia(state.hCentral_old, state.hCliente_old) : 0;
				double dist_new = state.hCentral_new != -1 ? IAUtils.getDistacia(state.hCentral_new, state.hCliente_old) : 0;
				double perdida_old = VEnergia.getPerdida(dist_old);
				double perdida_new = VEnergia.getPerdida(dist_new);
				state.entropia = state.entropia - perdida_old + perdida_new;
			}
			else {  // swap
				double dist_old_old = state.hCentral_old != -1 ? IAUtils.getDistacia(state.hCentral_old, state.hCliente_old) : 0;
				double dist_new_old = state.hCentral_new != -1 ? IAUtils.getDistacia(state.hCentral_new, state.hCliente_old) : 0;
				double dist_old_new = state.hCentral_old != -1 ? IAUtils.getDistacia(state.hCentral_old, state.hCliente_new) : 0;
				double dist_new_new = state.hCentral_new != -1 ? IAUtils.getDistacia(state.hCentral_new, state.hCliente_new) : 0;
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
