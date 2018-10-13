package src;

import java.util.Arrays;
import java.util.ArrayList;
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
    public CentralsRepresentation(int numCentrals, int numClients, TipoSolucionInicial tipSolInit) throws Exception {

        int [] propCentrals = new int[3];
        Arrays.fill(propCentrals, 0);
        for (int central = 0; central < numCentrals; central++) {
            int tipo = IAUtils.random(0, 3);
            propCentrals[tipo]++;
        }

        int [] propClients = new int[3];
        Arrays.fill(propClients, 0);
        for (int i = 0; i < numClients; i++) {
            int tipo = IAUtils.random(0, 3);
            propClients[tipo]++;
        }

        double propClientsGuaranteed = IAUtils.random();
        double [] proporcionClientes = new double[] {
                (double)propClients[0] / (double)numClients,
                (double)propClients[1] / (double)numClients,
                (double)propClients[2] / (double)numClients
        };
        if (proporcionClientes[0] + proporcionClientes[1] + proporcionClientes[2] != 1.0D) {
            proporcionClientes[2] = 1.0D - (proporcionClientes[0] + proporcionClientes[1]);
        }

        centrals = new Centrales(propCentrals, 0);
        clients = new Clientes(numClients, proporcionClientes, propClientsGuaranteed, 0);
        representationClientes = new int [clients.size()];
        representationCentrales = new double [centrals.size()];
        Arrays.fill(representationClientes, -1);
        Arrays.fill(representationCentrales, 0);

        fillInitialSolution(tipSolInit);

        System.out.println("Created");
    }

    /* Constructor per copy */
    public CentralsRepresentation(CentralsRepresentation copy) {
        this.representationClientes = copy.representationClientes.clone();
        this.representationCentrales = copy.representationCentrales.clone();
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

    // clientID: central_old -> central_new
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

    public boolean canSwap(int clientID_old, int clientID_new) {

        Cliente cliente_old = clients.get(clientID_old);
        Cliente cliente_new = clients.get(clientID_new);

        int centralID_old = representationClientes[clientID_old];
        int centralID_new = representationClientes[clientID_new];

        if (centralID_old == centralID_new) {
            return false;
        }

        Central central_old = centralID_old != -1 ? centrals.get(centralID_old) : null;
        Central central_new = centralID_new != -1 ? centrals.get(centralID_new) : null;

        double consumo_old_old = central_old != null ? CentralsHeuristicFunction.getConsumo(central_old, cliente_old) : 0.0d;
        double consumo_new_old = central_new != null ? CentralsHeuristicFunction.getConsumo(central_new, cliente_old) : 0.0d;
        double consumo_old_new = central_old != null ? CentralsHeuristicFunction.getConsumo(central_old, cliente_new) : 0.0d;
        double consumo_new_new = central_new != null ? CentralsHeuristicFunction.getConsumo(central_new, cliente_new) : 0.0d;

        if (central_old != null && (representationCentrales[centralID_old] - consumo_old_old + consumo_new_old) > central_old.getProduccion()) {
            return false;
        }

        if (central_new != null && (representationCentrales[centralID_new] - consumo_new_new + consumo_old_new) > central_new.getProduccion()) {
            return false;
        }

        return true;
    }

    // clientID_old: central_old -> central_new && clientID_new: central_new -> central_old
    public void swap(int clientID_old, int clientID_new) {

        int central_old = representationClientes[clientID_old];
        int central_new = representationClientes[clientID_new];

        if (canSwap(clientID_old, clientID_new)) {
            assign(central_old, clientID_old, central_new);
            assign(central_new, clientID_new, central_old);
        }
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
            else {
                currentClient++;
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
