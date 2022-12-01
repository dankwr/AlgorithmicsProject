package CinemaShowing;

import java.util.List;

public class OverlappingConstraint {
	private final LinearProgramVariable firstVariable;
	private final LinearProgramVariable secondVariable;

	public OverlappingConstraint(LinearProgramVariable pFirstVariable, LinearProgramVariable pSecondVariable) {
		firstVariable = pFirstVariable;
		secondVariable = pSecondVariable;
	}

	public LinearProgramVariable getFirstVariable() {
		return firstVariable;
	}

	public LinearProgramVariable getSecondVariable() {
		return secondVariable;
	}
}
