package SimplexAlgorithm;

public class BlandRule implements ChoosingStrategy {

	@Override
	public int chooseEnteringVariable(LinearProgram pLP) {

		double[] coefficient = pLP.getC();
		double maxCoefficient = 0;
		Integer enteringVariableIndex = null;
		for (int index = 0; index < coefficient.length; index++) {
			double coeff = coefficient[index];
			if (maxCoefficient < coeff) {
				maxCoefficient = coeff;
				enteringVariableIndex = index;
			}
		} 
		return pLP.getNonBasicVariables(enteringVariableIndex);
	}

}
