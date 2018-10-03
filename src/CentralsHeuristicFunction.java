package src;

import aima.search.framework.HeuristicFunction;
import IA.Energia.Central;
import IA.Energia.Cliente;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;

public class CentralsHeuristicFunction implements HeuristicFunction {

	public CentralsHeuristicFunction() {

	}

	public double getHeuristicValue(Object state){
		CentralsRepresentation s = (CentralsRepresentation) state;
		double beneficio = 0.0;

		// Cálculo del beneficio con las conexiones del estado actual s

		return beneficio;
	}

	private double getPorcentaje(int distSquared) {
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

	/// @pre: La central está en marcha.
	/// @post: Devuelve el coste "money" de una central al producir para un cliente.
	private double getCoste(Central central, Cliente cliente) {

		double coste = 0.0;

		// Get porcentaje
		int x = central.getCoordX() - cliente.getCoordX();
		int y = central.getCoordY() - cliente.getCoordY();
		int d = x * x + y * y;
		double porcentaje = getPorcentaje(d);

		// Calcular produccion para el cliente
		double consumo = cliente.getConsumo() * (porcentaje + 1);

		// Calcular producción de la central
		double produccion = central.getProduccion() + consumo;

		// Calcular coste de la producción total
		switch (central.getTipo()) {
			case 'A':
				return produccion * 5 + 2000;
			case 'B':
				return produccion * 8 + 1000;
			case 'C':
				return produccion * 15 + 500;
		}

		return Double.POSITIVE_INFINITY;
	}
}
