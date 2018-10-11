package src;

import aima.search.framework.GoalTest;

public class CentralsGoalTest implements GoalTest {

	public boolean isGoalState(Object eState) {

		System.out.println("isGoal");

		// En búsqued local desconocemos el estado final, por lo tanto siempre devuelve false

		return false;

	}
	
}
