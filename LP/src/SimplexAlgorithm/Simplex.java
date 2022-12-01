package SimplexAlgorithm;

import java.util.Optional;

public class Simplex {

	public enum LinearProgrammResult {
		INFEASIBLE, UNBOUNDED, RESULT;
	}

	public LinearProgrammResult doSimplex(LinearProgram pLP, ChoosingStrategy pChoosingStrategy) {
		LinearProgrammResult result = initializeLinearProgram(pLP, pChoosingStrategy);
		if(result != LinearProgrammResult.INFEASIBLE) {
			result = doSimplexLoop(pLP, pChoosingStrategy);
		}
		return result;
	}

	private LinearProgrammResult initializeLinearProgram(LinearProgram pLP, ChoosingStrategy pChoosingStrategy) {
		
		return null;
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
		return LinearProgrammResult.RESULT;
	}

}
