package src;

import src.CentralsHeuristicFunction;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.*;

public class CentralsSuccessorFunction implements SuccessorFunction {

	public List getSuccessors(Object node) {

		System.out.println("successors");

		ArrayList ret = new ArrayList();

		CentralsRepresentation state = (CentralsRepresentation)node;

		for (int cliendID = 0; cliendID < state.representationClientes.length; cliendID++) {

			int centralAssigned = state.representationClientes[cliendID];

			for (int centralID = -1; centralID < state.representationCentrales.length; centralID++) {
				if (state.canAssign(centralID, cliendID)) {
					CentralsRepresentation succ = new CentralsRepresentation(state);
					succ.assign(centralAssigned, cliendID, centralID);
					Successor successor = new Successor(cliendID + ": " + centralAssigned + " -> " + centralID, succ);
					ret.add(successor);
				}
			}
		}

		System.out.println("nSucc = " + ret.size());

		return ret;
	}
}
