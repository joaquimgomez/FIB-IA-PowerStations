package src;

import java.util.Random;
import java.util.ArrayList;
import java.util.Vector;
import IA.Energia.*;

public class CentralsRepresentation {

    // Auxiliary Data Structures
    public enum TipoSolucionInicial {
        Random,         // Rellenar random (puede no ser una solución)
        Prioritarios,   // Rellenar solo los prioritarios
        Todos           // Rellenar prioritarios + opcionales
    }

    // Static members
    private static ArrayList<Central> centrals;
    private static ArrayList<Cliente> clients;

    // Representation
    private int[] representation;

    /* Constructor */
    public CentralsRepresentation(int numMaxCentrals, int numClients, TipoSolucionInicial tipSolInit) throws Exception {

        int [] propCentrals = new int[3];
        for (int i = 0; i < propCentrals.length; i++) {
            propCentrals[i] = IAUtils.random(1, 10);
        }

        double [] propClients = new double[3];
        for (int i = 0; i < propClients.length - 1; i++) {
            propClients[i] = IAUtils.random(0.1, 0.5);
        }
        propClients[2] = 1.0 - propClients[0] - propClients[1];

        double propClientsGuaranteed = IAUtils.random();

        centrals = new Centrales(propCentrals, 0);
        clients = new Clientes(numClients, propClients, propClientsGuaranteed, 0);
        representation = new int [clients.size()];

        fillInitialSolution(tipSolInit);
    }

    /* Constructor per copy */
    public CentralsRepresentation(CentralsRepresentation copy) {
        this.representation = copy.representation;
    }

    /* */
    private void fillInitialSolution(TipoSolucionInicial tipSolInit) {

        if (tipSolInit == TipoSolucionInicial.Random) {
            fillRandom();
        }
        else if (tipSolInit == TipoSolucionInicial.Prioritarios) {
            // fill____();
        }
        else if (tipSolInit == TipoSolucionInicial.Todos) {
            // fill____();
        }
        else {
            System.out.println("Error parsing initial solution");
        }
    }

    private void fillRandom() {
        for (int i = 0; i < clients.size(); i++) {
            int randCentral = IAUtils.random(0, centrals.size());
            representation[i] = randCentral;
        }
    }
}
