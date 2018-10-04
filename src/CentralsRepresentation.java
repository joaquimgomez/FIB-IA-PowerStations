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
    private static ArrayList<Central> centrals;
    private static ArrayList<Cliente> clients;

    // Representation
    public int[] representationClientes;
    public int[] representationCentrales;

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
        representationClientes = new int [clients.size()];
        representationCentrales = new int [centrals.size()];
        Arrays.fill(representationClientes, -1);
        Arrays.fill(representationCentrales, 0);

        fillInitialSolution(tipSolInit);
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

    private void fillRandom() {

        for (int i = 0; i < clients.size(); i++) {

            int randCentral = IAUtils.random(0, centrals.size());
            Central central = centrals.get(randCentral);
            Cliente cliente = clients.get(i);
            double coste = CentralsHeuristicFunction.getCoste(central, cliente);

            if (CentralsHeuristicFunction.canAssign(centrals.get(randCentral), coste)) {
                representationClientes[i] = randCentral;
                representationCentrales[randCentral] += coste;
            }
        }
    }

    private void fillPrioritary(){
        int currentCentral = 0;
        for (int i = 0; i < clients.size() && currentCentral <= centrals.size(); i++){
            if (clients.get(i).getTipo() == Cliente.GARANTIZADO) {
                if (canAssign(centrals.get(currentCentral))){
                    assignation(i, currentCentral);
                } else {
                    i++;
                    assignation(i, currentCentral);
                }
            }
        }
    }

    private void fillAllClients(){
        int currentCentral = 0;
        for (int i = 0; i < clients.size() && currentCentral <= centrals.size(); i++){
            if (canAssign(centrals.get(currentCentral))){
                assignation(i, currentCentral);
            } else {
                i++;
                assignation(i, currentCentral);
            }
        }
    }

    private void assignation(int client, int central){
        representationClientes[client] = central;
        representationCentrales[central] += clients.get(client).getConsumo();
    }
}
