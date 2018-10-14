package src;

import aima.search.framework.HeuristicFunction;
import IA.Energia.Central;
import IA.Energia.Cliente;
import IA.Energia.VEnergia;
import java.lang.Exception;


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

	public static double getDistacia(int centralID, int clienteID) {
		Cliente cliente = CentralsRepresentation.clients.get(clienteID);
		Central central = CentralsRepresentation.centrals.get(centralID);

		// Get distancia
		int x = central.getCoordX() - cliente.getCoordX();
		int y = central.getCoordY() - cliente.getCoordY();
		int d = x * x + y * y;
		return Math.sqrt(d);
	}

	/// @pre: distSquare es la distancia entre la central y el cliente, al cuadrado (es más fácil de computar)
	/// @post: devuelve el porcentaje de pérdida (0 = 0% , 1.0 = 100%)
	private static double getPorcentaje(int distSquared) {
		if (distSquared <= 100) {  // 10^2
			return 0;
		}
		else if (distSquared <= 625) {  // 25^2
			return 0.1;
		}
		else if (distSquared <= 2500) {  // 50^2
			return 0.2;
		}
		else if (distSquared <= 5625) {  // 75^2
			return 0.4;
		}
		else {
			return 0.6;
		}
	}

	/// @post: Devuelve los MW que consume un cliente para una central.
	public static double getConsumo(Central central, Cliente cliente) {

		// Get porcentaje
		int x = central.getCoordX() - cliente.getCoordX();
		int y = central.getCoordY() - cliente.getCoordY();
		int d = x * x + y * y;
		double porcentaje = getPorcentaje(d);

		// Calcular produccion para el cliente
		return cliente.getConsumo() * (porcentaje + 1.0D);
	}

	/// @pre: La central está en marcha.
	/// @post: Devuelve el coste "money" de encender una central.
	public static double getCosteEuros(Central central) {

		// Calcular producción de la central
		double produccion = central.getProduccion();

		// Calcular coste de la producción total
		switch (central.getTipo()) {
			case 0:
				return produccion * 5 + 2000;
			case 1:
				return produccion * 8 + 1000;
			case 2:
				return produccion * 15 + 500;
		}

		return Double.POSITIVE_INFINITY;
	}

	/// @post: Devuelve el beneficio de servir al cliente, en euros.
	private static double getBeneficio(Cliente cliente) {
		return (40 - cliente.getContrato() * (10) + cliente.getTipo() * 10) * cliente.getConsumo();
	}

	/// @post: Devuelve el coste de la indemnizacion por no servir al cliente, en euros
	private static double getIndemnizacion(Cliente cliente) {
		return cliente.getConsumo() * 5;
	}
}
