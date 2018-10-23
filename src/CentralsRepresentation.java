package src;

import java.util.Arrays;
import java.util.ArrayList;
import IA.Energia.*;

import static IA.Energia.Cliente.GARANTIZADO;
import static IA.Energia.Cliente.NOGARANTIZADO;

public class CentralsRepresentation {

    // Static members

    public static ArrayList<Central> centrals;
    public static ArrayList<Cliente> clients;


    // Representation members

    public int[] representationClientes;
    public double[] representationCentrales;


    // Heuristic members

    public double beneficio;
    public int perdida;
    public int consumoTotal;
    public int hCentral_old;
    public int hCentral_new;
    public int hCliente_old;
    public int hCliente_new;
    public double perdidaTotal;


    // Constructors

    public CentralsRepresentation(String typeSolInit) throws Exception {
        /*
         Random inital state
        */

        int numCentrals = IAUtils.random(5, 20);

        int[] propTypeCentrals = new int[3];
        Arrays.fill(propTypeCentrals, 0);
        for (int central = 0; central < numCentrals; central++) {
            int tipo = IAUtils.random(0, 3);
            propTypeCentrals[tipo]++;
        }


        int numClients = IAUtils.random(50, 200);

        int[] propTypeClients = new int[3];
        Arrays.fill(propTypeClients, 0);
        for (int i = 0; i < numClients; i++) {
            int tipo = IAUtils.random(0, 3);
            propTypeClients[tipo]++;
        }

        double [] proportionClients = new double[] {
                (double)propTypeClients[0] / (double)numClients,
                (double)propTypeClients[1] / (double)numClients,
                (double)propTypeClients[2] / (double)numClients
        };
        if (proportionClients[0] + proportionClients[1] + proportionClients[2] != 1.0D) {
            proportionClients[2] = 1.0D - (proportionClients[0] + proportionClients[1]);
        }


        double propClientsGuaranteed = IAUtils.random(0.1, 1.0);


        centrals = new Centrales(propTypeCentrals, 1234);
        clients = new Clientes(numClients, proportionClients, propClientsGuaranteed, 1234);
        representationClientes = new int [clients.size()];
        representationCentrales = new double [centrals.size()];
        Arrays.fill(representationClientes, -1);
        Arrays.fill(representationCentrales, 0);


        fillInitialSolution(typeSolInit);
        fillInitHeuristicParameters();
    }

    public CentralsRepresentation(String typeSolInit, int numClients, double[] propTypeClients, double propClientsGuaranteed, int[] propTypeCentrals, int seed) throws Exception {
        /*
         No-random inital state
        */

        centrals = new Centrales(propTypeCentrals, seed);
        clients = new Clientes(numClients, propTypeClients, propClientsGuaranteed, seed);
        representationClientes = new int [clients.size()];
        representationCentrales = new double [centrals.size()];
        Arrays.fill(representationClientes, -1);
        Arrays.fill(representationCentrales, 0);
        consumoTotal = 0;

        fillInitialSolution(typeSolInit);


        fillInitHeuristicParameters();
    }

    public CentralsRepresentation(CentralsRepresentation copy) {
        /*
         Constructor by copy
        */

        this.representationClientes = copy.representationClientes.clone();
        this.representationCentrales = copy.representationCentrales.clone();
        this.hCentral_old = copy.hCentral_old;
        this.hCentral_new = copy.hCentral_new;
        this.hCliente_old = copy.hCliente_old;
        this.hCliente_new = copy.hCliente_new;
        this.beneficio = copy.beneficio;
        this.perdida = copy.perdida;
        this.consumoTotal = copy.consumoTotal;
    }



    // Auxiliary methods

    private void fillInitialSolution(String typeSolInit) {

        if (typeSolInit.equals("p")) {
            fillPrioritary();
        }
        else if (typeSolInit.equals("t")) {
            fillAllClients();
        }
        else {
            System.out.println("Error parsing initial solution");
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

        fillPrioritary();

        int currentClient = 0;
        int currentCentral = 0;

        while (currentClient < clients.size() && currentCentral < centrals.size()) {

            if (representationClientes[currentClient] == -1) {
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

    private void fillInitHeuristicParameters() throws Exception {
        beneficio = setBeneficio();
        perdida = setEntropia();
        hCentral_old = -1;
        hCentral_new = -1;
        hCliente_old = -1;
        hCliente_new = -1;
        perdidaTotal = 0;
    }

    protected double setBeneficio() throws Exception {
        double beneficio = 0.0;

        // Coste centrales
        for (int centralID = 0; centralID < representationCentrales.length; centralID++) {
            int typeCentral = CentralsRepresentation.centrals.get(centralID).getTipo();

            if (representationCentrales[centralID] != 0){		// Coste central en marcha
                beneficio -= VEnergia.getCosteMarcha(typeCentral);
                beneficio -= VEnergia.getCosteProduccionMW(typeCentral);
            } else {	// Coste central en parada
                beneficio -= VEnergia.getCosteParada(typeCentral);
            }
        }


        // Beneficio de los clientes
        for (int clientID = 0; clientID < representationClientes.length; clientID++) {
            int centralAssigned = representationClientes[clientID];
            Cliente client = CentralsRepresentation.clients.get(clientID);
            int typeClient = client.getContrato();

            if (centralAssigned == -1 && typeClient == NOGARANTIZADO){
                beneficio -= VEnergia.getTarifaClientePenalizacion(client.getTipo()) * client.getConsumo();
            } else if (typeClient == NOGARANTIZADO){
                beneficio += VEnergia.getTarifaClienteNoGarantizada(client.getTipo()) * client.getConsumo();
            } else if (typeClient == GARANTIZADO){
                beneficio += VEnergia.getTarifaClienteGarantizada(client.getTipo()) * client.getConsumo();
            }
        }

        return beneficio;
    }

    protected int setEntropia() {
        int entropia = 0;

        for (int clienteID = 0; clienteID < representationClientes.length; clienteID++) {

            int centralID = representationClientes[clienteID];
            if (centralID != -1) {
                entropia += IAUtils.getPorcentajeInt(IAUtils.getDistanciaSq(centralID, clienteID));
            }
            else {
                entropia += 10;
            }
        }

        return entropia;
    }

    protected int setConsumoTotal() {
        int consumoTotal = 0;

        for (int centralID = 0; centralID < representationCentrales.length; centralID++) {
            consumoTotal += representationCentrales[centralID];
        }

        return consumoTotal;
    }

    // Operators and Auxiliary methods for operators

    public void assign(int centralID_old, int clientID, int centralID_new) {
        // clientID: central_old -> central_new

        Central cenOld;
        Central cenNew;
        Cliente client;
        double consumoOld;
        double consumoNew;

        client = clients.get(clientID);

        if (centralID_old != -1) {
            cenOld = centrals.get(centralID_old);
            consumoOld = IAUtils.getConsumo(cenOld, client);
            consumoTotal -= consumoOld;
            representationCentrales[centralID_old] -= consumoOld;
            perdidaTotal -= VEnergia.getPerdida(IAUtils.getDistacia(centralID_old, clientID)) * client.getConsumo();
        }

        if (centralID_new != -1) {
            cenNew = centrals.get(centralID_new);
            consumoNew = IAUtils.getConsumo(cenNew, client);
            consumoTotal += consumoNew;
            representationCentrales[centralID_new] += consumoNew;
            perdidaTotal += VEnergia.getPerdida(IAUtils.getDistacia(centralID_new, clientID)) * client.getConsumo();
        }

        representationClientes[clientID] = centralID_new;
    }

    public boolean canAssign(int centralID, int clientID) {

        Cliente cliente = clients.get(clientID);

        if (representationClientes[clientID] == centralID) {
            return false;
        }

        if (centralID == -1) {
            return cliente.getContrato() != GARANTIZADO;
        }
        else {
            Central central = centrals.get(centralID);

            double consumo = IAUtils.getConsumo(central, cliente);
            return representationCentrales[centralID] + consumo <= central.getProduccion();
        }
    }

    public void swap(int clientID_old, int clientID_new) {
        // clientID_old: central_old -> central_new && clientID_new: central_new -> central_old

        int central_old = representationClientes[clientID_old];
        int central_new = representationClientes[clientID_new];

        assign(central_old, clientID_old, central_new);
        assign(central_new, clientID_new, central_old);
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

        double consumo_old_old = central_old != null ? IAUtils.getConsumo(central_old, cliente_old) : 0.0d;
        double consumo_new_old = central_new != null ? IAUtils.getConsumo(central_new, cliente_old) : 0.0d;
        double consumo_old_new = central_old != null ? IAUtils.getConsumo(central_old, cliente_new) : 0.0d;
        double consumo_new_new = central_new != null ? IAUtils.getConsumo(central_new, cliente_new) : 0.0d;

        return !(central_old != null && (representationCentrales[centralID_old] - consumo_old_old + consumo_new_old) > central_old.getProduccion()) &&
                !(central_new != null && (representationCentrales[centralID_new] - consumo_new_new + consumo_old_new) > central_new.getProduccion());
    }

}
