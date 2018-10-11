package src;

import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import java.util.Vector;
import IA.Energia.*;

import static IA.Energia.Cliente.GARANTIZADO;

public class CentralsRepresentation {

    // Auxiliary Data Structures
    public enum TipoSolucionInicial {
        Random,         // Rellenar random (puede no ser una solución)
        Prioritarios,   // Rellenar solo los prioritarios
        Todos           // Rellenar prioritarios + opcionales
    }

    // Static members
    public static ArrayList<Central> centrals;
    public static ArrayList<Cliente> clients;

    // Representation
    public int[] representationClientes;
    public double[] representationCentrales;

    /* Constructor */
    public CentralsRepresentation(int numMaxCentrals, int numClients, TipoSolucionInicial tipSolInit) throws Exception {

        int [] propCentrals = new int[3];
        for (int i = 0; i < propCentrals.length; i++) {
            propCentrals[i] = IAUtils.random(1, numMaxCentrals / 3);
        }

        double [] propClients = new double[3];
        for (int i = 0; i < propClients.length - 1; i++) {
            propClients[i] = IAUtils.random(0.1, 0.5);
        }
        propClients[2] = 1.0 - propClients[0] - propClients[1];

        double propClientsGuaranteed = IAUtils.random();

        centrals = new Centrales(propCentrals, 0);
        clients = new Clientes(numClients, propClients, propClientsGuaranteed, 0);
        representationClientes = new int [clients.size()];
        representationCentrales = new double [centrals.size()];
        Arrays.fill(representationClientes, -1);
        Arrays.fill(representationCentrales, 0);

        fillInitialSolution(tipSolInit);

        System.out.println("Created");
    }

    /* Constructor per copy */
    public CentralsRepresentation(CentralsRepresentation copy) {
        this.representationClientes = copy.representationClientes;
        this.representationCentrales = copy.representationCentrales;
    }

    /* */
    private void fillInitialSolution(TipoSolucionInicial tipSolInit) {

        if (tipSolInit == TipoSolucionInicial.Random) {
            fillRandom();
        }
        else if (tipSolInit == TipoSolucionInicial.Prioritarios) {
            fillPrioritary();
        }
        else if (tipSolInit == TipoSolucionInicial.Todos) {
            fillAllClients();
        }
        else {
            System.out.println("Error parsing initial solution");
        }
    }

    public boolean canAssign(int centralID, int clientID) {

        Cliente cliente = clients.get(clientID);

        if (centralID == -1) {
            return cliente.getContrato() != GARANTIZADO;
        }
        else {
            Central central = centrals.get(centralID);

            double consumo = CentralsHeuristicFunction.getConsumo(central, cliente);
            return representationCentrales[centralID] + consumo <= central.getProduccion();
        }
    }

    public void assign(int centralID_old, int clientID, int centralID_new) {

        Central cenOld;
        Central cenNew;
        Cliente client;
        double consumoOld;
        double consumoNew;

        client = clients.get(clientID);

        if (centralID_old != -1) {
            cenOld = centrals.get(centralID_old);
            consumoOld = CentralsHeuristicFunction.getConsumo(cenOld, client);
            representationCentrales[centralID_old] -= consumoOld;
        }

        if (centralID_new != -1) {
            cenNew = centrals.get(centralID_new);
            consumoNew = CentralsHeuristicFunction.getConsumo(cenNew, client);
            representationCentrales[centralID_new] += consumoNew;
        }

        representationClientes[clientID] = centralID_new;
    }

    // Soluciones Iniciales

    private void fillRandom() {

        for (int clientID = 0; clientID < clients.size(); clientID++) {

            int centralID = IAUtils.random(0, centrals.size());
            if (canAssign(centralID, clientID)) {
                assign(-1, clientID, centralID);
            }
        }
    }

    private void fillPrioritary() {

        int currentClient = 0;
        int currentCentral = 0;

        while (currentClient < clients.size() && currentCentral < centrals.size()) {

            if (clients.get(currentClient).getContrato() == GARANTIZADO) {
                if (canAssign(currentCentral, currentClient)) {
                    assign(-1, currentClient, currentCentral);
                    currentClient++;
                }
                else {
                    currentCentral++;
                }
            }
        }
    }

    private void fillAllClients() {

        int currentClient = 0;
        int currentCentral = 0;

        while (currentClient < clients.size() && currentCentral < centrals.size()) {

            if (canAssign(currentCentral, currentClient)) {
                assign(-1, currentClient, currentCentral);
                currentClient++;
            }
            else {
                currentCentral++;
            }
        }
    }
}
