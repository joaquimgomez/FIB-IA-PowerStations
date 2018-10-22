package src;

import IA.Energia.Central;
import IA.Energia.Cliente;
import IA.Energia.VEnergia;

import aima.search.framework.HeuristicFunction;


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

	private double HeuristicFunction1(CentralsRepresentation state) throws Exception {


		// Coste centrales
		if (state.hCliente_old != -1) { // no es la solución inicial

			if (state.hCliente_new == -1) { // assign

				if (state.hCentral_old != -1 && state.representationCentrales[state.hCentral_old] == 0) { // se apaga una central old
					Central cent = CentralsRepresentation.centrals.get(state.hCentral_old);
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
		}

		// Beneficio clientes
		if (state.hCliente_old != -1) { // no es la solución inicial

			if (state.hCliente_new == -1) { // assign
				Cliente c = CentralsRepresentation.clients.get(state.hCliente_old);

				if (state.hCentral_old == -1) { // fuera a dentro
					if (c.getContrato() == Cliente.NOGARANTIZADO) {
						state.beneficio += VEnergia.getTarifaClienteNoGarantizada(c.getTipo()) * c.getConsumo();
						state.beneficio += VEnergia.getTarifaClientePenalizacion(c.getTipo()) * c.getConsumo();
					}
					else {
						state.beneficio += VEnergia.getTarifaClienteGarantizada(c.getTipo()) * c.getConsumo();
					}
				}
			}

		}



		// Entropia solucion
		if (state.hCliente_old != -1) { // no es la solución inicial

			if (state.hCliente_new == -1) { // assign
				int perdida_old = state.hCentral_old != -1 ?
						IAUtils.getPorcentajeInt(IAUtils.getDistanciaSq(state.hCentral_old, state.hCliente_old)) : 10;
				int perdida_new = state.hCentral_new != -1 ?
						IAUtils.getPorcentajeInt(IAUtils.getDistanciaSq(state.hCentral_new, state.hCliente_old)) : 10;
				state.perdida -= perdida_old;
				state.perdida += perdida_new;
			}
			else {  // swap
				int perdida_old_old = state.hCentral_old != -1 ?
						IAUtils.getPorcentajeInt(IAUtils.getDistanciaSq(state.hCentral_old, state.hCliente_old)) : 10;
				int perdida_new_old = state.hCentral_new != -1 ?
						IAUtils.getPorcentajeInt(IAUtils.getDistanciaSq(state.hCentral_new, state.hCliente_old)) : 10;
				int perdida_old_new = state.hCentral_old != -1 ?
						IAUtils.getPorcentajeInt(IAUtils.getDistanciaSq(state.hCentral_old, state.hCliente_new)) : 10;
				int perdida_new_new = state.hCentral_new != -1 ?
						IAUtils.getPorcentajeInt(IAUtils.getDistanciaSq(state.hCentral_new, state.hCliente_new)) : 10;
				state.perdida -= perdida_old_old;
				state.perdida += perdida_new_old;
				state.perdida -= perdida_new_new;
				state.perdida += perdida_old_new;
			}
		}

		double perdida = (double)state.perdida / (double)(state.representationClientes.length * 10 + state.perdida);
		double costePerdida = perdida * state.consumoTotal * 5;  // 5 € de penalización por cada MW de pérdida
		double heuristico = state.beneficio - costePerdida;
		/*System.out.println("p: "+perdida);
		System.out.println("cp: "+costePerdida);
		System.out.println("be:" + state.beneficio);*/
		return -heuristico;
	}

}
