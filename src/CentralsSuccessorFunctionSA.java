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
        CentralsHeuristicFunction hF = new CentralsHeuristicFunction();

        int cli1 = IAUtils.random(0, r.representationClientes.length);
        int cli2 = IAUtils.random(0, r.representationClientes.length);
        boolean assigned = false;

        while (!assigned){
            int cent = IAUtils.random(0, r.representationCentrales.length);

            int op = IAUtils.random(0, 2);
            CentralsRepresentation succ = new CentralsRepresentation(r);
            if (op == 0 && succ.canAssign(cent, cli1)){ // asign
                assigned = true;

                succ.assign(succ.representationClientes[cli1], cli1, cent);

                succ.hCentral_new = cent;
                succ.hCentral_old = r.representationClientes[cli1];
                succ.hCliente_old = cli1;
                succ.hCliente_new = -1;
            }
            else if (op == 1 && succ.canSwap(cli1, cli2)){ // swap
                assigned = true;

                succ.swap(cli1, cli2);

                succ.hCentral_new = r.representationClientes[cli2];
                succ.hCentral_old = r.representationClientes[cli1];
                succ.hCliente_old = cli1;
                succ.hCliente_new = cli2;
            }

            if (assigned){
                String S = "Algo";
                Successor s = new Successor(S, succ);
                retVal.add(s);
            }

        }

        System.out.println("nSucc = " + retVal.size());
        return retVal;
    }

}
