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

		// Por cada cliente
		

		System.out.println("nSucc = " + ret.size());

		return ret;
	}
}
