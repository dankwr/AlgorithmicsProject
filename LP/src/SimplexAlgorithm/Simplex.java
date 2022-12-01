package SimplexAlgorithm;

import java.util.Optional;

public class Simplex {

	public enum LinearProgrammResult {
		INFEASIBLE, UNBOUNDED, FEASIBLE;
	}

	public LinearProgrammResult doSimplex(LinearProgram pLP, ChoosingStrategy pChoosingStrategy) {
		LinearProgrammResult result = initializeLinearProgram(pLP, pChoosingStrategy);
		if(result != LinearProgrammResult.INFEASIBLE) {
			result = doSimplexLoop(pLP, pChoosingStrategy);
		}
		return result;
	}

	private LinearProgrammResult initializeLinearProgram(LinearProgram pLP, ChoosingStrategy pChoosingStrategy) {
		
		// Die Constrains aus dem ScheduleProblem sind alle in der korrekten Slack-Form, deswegen ist hier nicht mehr notwendig.
		return LinearProgrammResult.FEASIBLE; 
	}

	private LinearProgrammResult doSimplexLoop(LinearProgram pLP, ChoosingStrategy pChoosingStrategy) {

		while (pLP.hasAnyPositiveC()) {
			int enteringVariable = pChoosingStrategy.chooseEnteringVariable(pLP);
			Optional<Integer> leavingVariableOptional = pLP.determineLeavingVariable(enteringVariable);
			if (leavingVariableOptional.isPresent()) {
				pLP.calculatePivot(enteringVariable, leavingVariableOptional.get());
			} else {
				return LinearProgrammResult.UNBOUNDED;
			}
		}
		return LinearProgrammResult.FEASIBLE;
	}

}
