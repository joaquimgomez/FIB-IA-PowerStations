package src;

import java.util.Random;
import java.util.ArrayList;
import IA.Energia.*;


public class CentralsRepresentation {
    /*

     */

    private static int numMaxCentrals;
    private static int numClients;

    private Random r = new Random(System.currentTimeMillis());

    private static ArrayList<Central> centrals;
    private static ArrayList<Cliente> clients;

    private int [] connections;


    /* Constructor */
    public CentralsRepresentation(/*int numMaxCentrals,*/ int numClients, int tipSolInit) throws Exception {
        //this.numMaxCentrals = numMaxCentrals;
        this.numClients = numClients;

        int [] propCentrals = new int[3];
        for (int i = 0; i < propCentrals.length; i++)     propCentrals[i] = r.nextInt() + 1;

        double [] propClients = new double[3];
        for (int i = 0; i < propClients.length - 1; i++)    propClients[i] = r.nextDouble() + 0.1;
        propClients[2] = 1.0 - propClients[0] - propClients[1];

        double propClientsGuaranteed = r.nextDouble();

        centrals = new Centrales(propCentrals, 19);
        clients = new Clientes(numClients, propClients, propClientsGuaranteed, 10);

        connections = new int [clients.size()];

        connect(tipSolInit);
    }

    /* Constructor per copy */
    public CentralsRepresentation(){

    }

    /* */
    private void connect(int tipSolInit){
        if(tipSolInit == 1){

        } else {

        }

    }


}
