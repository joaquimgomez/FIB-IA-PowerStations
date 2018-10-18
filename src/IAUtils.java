package src;

import IA.Energia.Central;
import IA.Energia.Cliente;

public class IAUtils {

    // if val < min -> min
    // if val > max -> max
    // return val
    public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
        if (val.compareTo(min) < 0) return min;
        else if (val.compareTo(max) > 0) return max;
        else return val;
    }


    // Random
    public static double random() {
        return Math.random();
    }

    public static int random(int min, int max) {
        return (int)(Math.random() * (max - min) + min);
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
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

    public static int getDistanciaSq(int centralID, int clienteID) {
        Cliente cliente = CentralsRepresentation.clients.get(clienteID);
        Central central = CentralsRepresentation.centrals.get(centralID);

        // Get distancia
        int x = central.getCoordX() - cliente.getCoordX();
        int y = central.getCoordY() - cliente.getCoordY();
        int d = x * x + y * y;
        return d;
    }

    /// @pre: distSquare es la distancia entre la central y el cliente, al cuadrado (es más fácil de computar)
    /// @post: devuelve el porcentaje de pérdida (0 = 0% , 1.0 = 100%)
    public static double getPorcentaje(int distSquared) {
        if (distSquared <= 100) {  // 10^2
            return 0.0D;
        }
        else if (distSquared <= 625) {  // 25^2
            return 0.1D;
        }
        else if (distSquared <= 2500) {  // 50^2
            return 0.2D;
        }
        else if (distSquared <= 5625) {  // 75^2
            return 0.4D;
        }
        else {
            return 0.6D;
        }
    }

    /// @pre: distSquare es la distancia entre la central y el cliente, al cuadrado (es más fácil de computar)
    /// @post: devuelve el porcentaje de pérdida (0 = 0% , 10 = 100%)
    public static int getPorcentajeInt(int distSquared) {
        if (distSquared <= 100) {  // 10^2
            return 0;
        }
        else if (distSquared <= 625) {  // 25^2
            return 1;
        }
        else if (distSquared <= 2500) {  // 50^2
            return 2;
        }
        else if (distSquared <= 5625) {  // 75^2
            return 4;
        }
        else {
            return 6;
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

}
