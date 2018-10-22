package src;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.*;

public class CentralsSuccessorFunctionSA implements SuccessorFunction {

    public CentralsSuccessorFunctionSA() {

    }

    public List getSuccessors(Object eState) {
        ArrayList retVal = new ArrayList ();
        CentralsRepresentation r = (CentralsRepresentation)eState;

        int cli1 = IAUtils.random(0, r.representationClientes.length);
        int cli2 = IAUtils.random(0, r.representationClientes.length);

        boolean assigned = false;

        int cent = IAUtils.random(0, r.representationCentrales.length);

        int op = IAUtils.random(0, CentralsRepresentation.centrals.size() * CentralsRepresentation.clients.size() +
                CentralsRepresentation.clients.size() * (CentralsRepresentation.clients.size() - (CentralsRepresentation.clients.size() / CentralsRepresentation.centrals.size())) + 1);

        if (op < CentralsRepresentation.centrals.size() * CentralsRepresentation.clients.size() + 1) {
            op = 0;
        }
        else {
            op = 1;
        }

        CentralsRepresentation succ = new CentralsRepresentation(r);
        String S = "";
        if (op == 0 && succ.canAssign(cent, cli1)){ // asign
            assigned = true;

            succ.assign(succ.representationClientes[cli1], cli1, cent);

            succ.hCentral_new = cent;
            succ.hCentral_old = r.representationClientes[cli1];
            succ.hCliente_old = cli1;
            succ.hCliente_new = -1;

            S = cli1 + ": " + r.representationClientes[cli1] + " -> " + cent;
        }
        else if (op == 1 && succ.canSwap(cli1, cli2)){ // swap
            assigned = true;

            succ.swap(cli1, cli2);

            succ.hCentral_new = r.representationClientes[cli2];
            succ.hCentral_old = r.representationClientes[cli1];
            succ.hCliente_old = cli1;
            succ.hCliente_new = cli2;

            S = cli1 + ": " + r.representationClientes[cli1] + " -> " + r.representationClientes[cli2]
                    + ", " + cli2 + ": " + r.representationClientes[cli2] + " -> " + r.representationClientes[cli1];
        }

        if (assigned){
            Successor s = new Successor(S, succ);
            retVal.add(s);
        }

        System.out.println("nSucc = " + retVal.size());
        return retVal;
    }

}
