package src;

import aima.search.framework.HeuristicFunction;
import IA.Energia.Central;
import IA.Energia.Cliente;
import IA.Energia.VEnergia;
import java.lang.Exception;

import static IA.Energia.Cliente.GARANTIZADO;
import static IA.Energia.Cliente.NOGARANTIZADO;

public class CentralsHeuristicFunction implements HeuristicFunction {

	// Members
	public static VEnergia VEnergia = new VEnergia();

	// Constructor
	public CentralsHeuristicFunction() {

	}

	public double getHeuristicValue(Object s) {
		try {
			CentralsRepresentation state = (CentralsRepresentation) s;
			return HeuristicFunction1(state);
		}
		catch (Exception excepcion){
			System.out.println("Error: " + excepcion);
		}
		return 0.0;
	}

	public double HeuristicFunction1(CentralsRepresentation state) throws Exception {
		double beneficio = 0.0;

		for (int centralID = 0; centralID < state.representationCentrales.length; centralID++){
			int typeCentral = state.centrals.get(centralID).getTipo();

			if (state.representationCentrales[centralID] != 0){		// Coste central en marcha
				beneficio -= VEnergia.getCosteMarcha(typeCentral);
				beneficio -= VEnergia.getCosteProduccionMW(typeCentral);
			} else {	// Coste central en parada
				beneficio -= VEnergia.getCosteParada(typeCentral);
			}
		}

		for (int clientID = 0; clientID < state.representationClientes.length; clientID++){
			int centralAssigned = state.representationClientes[clientID];
			Cliente client = state.clients.get(clientID);
			int typeClient = client.getTipo();

			if (centralAssigned == -1 && typeClient == NOGARANTIZADO){
				beneficio -= VEnergia.getTarifaClientePenalizacion(client.getContrato())*client.getConsumo(); //???????????'
			} else if (typeClient == NOGARANTIZADO){
				beneficio += VEnergia.getTarifaClienteNoGarantizada(client.getContrato())*client.getConsumo();
			} else if (typeClient == GARANTIZADO){
				beneficio += VEnergia.getTarifaClienteGarantizada(client.getContrato())*client.getConsumo();

			}
		}

		// System.out.println(beneficio);

		return -beneficio;
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
