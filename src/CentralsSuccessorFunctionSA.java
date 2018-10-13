package src;

import src.CentralsHeuristicFunction;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.*;

public class CentralsSuccessorFunctionSA implements SuccessorFunction {

    public CentralsSuccessorFunctionSA() {

    }

    public List getSuccessors(Object eState) {
        ArrayList retVal = new ArrayList();
        CentralsRepresentation r = (CentralsRepresentation)eState;
        CentralsHeuristicFunction hF = new CentralsHeuristicFunction();

        int cli1 = IAUtils.random(0, r.representationClientes.length);
        int cli2 = IAUtils.random(0, r.representationClientes.length);
        boolean assigned = false;

        while (!assigned){
            int cent = IAUtils.random(0, r.representationCentrales.length);

            int op = IAUtils.random(0, 2);
            CentralsRepresentation newRepresentation = new CentralsRepresentation(r);
            if (op == 0 && newRepresentation.canAssign(cent, cli1)){ // asign
                assigned = !assigned;
                newRepresentation.assign(newRepresentation.representationClientes[cli1], cli1, cent);
            }
            else if (op == 1 && newRepresentation.canSwap(cli1, cli2)){ // swap
                assigned = !assigned;
                newRepresentation.swap(cli1, cli2);
            }

            if (assigned){
                double h = hF.getHeuristicValue(newRepresentation);
                retVal.add(new Successor("caca", newRepresentation));
            }

        }

        return retVal;
    }

}
