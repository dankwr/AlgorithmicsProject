package SimplexAlgorithm;

import java.util.Optional;


public class Simplex {

	public static final ChoosingStrategy BLAND_RULE = new BlandRule();  
	 
	public static LinearProgrammResult doSimplex(LinearProgram pLP, ChoosingStrategy pChoosingStrategy) {
		LinearProgrammResult result = initializeLinearProgram(pLP, pChoosingStrategy);
		if(result != LinearProgrammResult.INFEASIBLE) {
			result = doSimplexLoop(pLP, pChoosingStrategy);
		}
		return result;
	}

	private static LinearProgrammResult initializeLinearProgram(LinearProgram pLP, ChoosingStrategy pChoosingStrategy) {
		
		// Die Constrains aus dem ScheduleProblem sind alle in der korrekten Slack-Form, deswegen ist hier nicht mehr notwendig.
		return LinearProgrammResult.FEASIBLE; 
	}

	private static LinearProgrammResult doSimplexLoop(LinearProgram pLP, ChoosingStrategy pChoosingStrategy) {

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
