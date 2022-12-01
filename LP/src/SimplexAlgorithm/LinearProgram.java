package SimplexAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LinearProgram {

	int numberVariables;
	int numberConstrains;
	private double v;
	private double[] c;
	private double[][] A;
	private double[] b;
	private List<Integer> nonBasicValues;
	private List<Integer> basicValues;

	public LinearProgram(int pNumberVariables, int pNumberConstrains) {
		numberVariables = pNumberVariables;
		numberConstrains = pNumberConstrains;
		v = 0;
		c = new double[pNumberVariables];
		A = new double[pNumberVariables][pNumberConstrains];
		b = new double[pNumberConstrains];
		nonBasicValues = new ArrayList<>();
		basicValues = new ArrayList<>();
		for (int index = 0; index < pNumberVariables; ++index) {
			nonBasicValues.add(index);
		}
		for (int index = pNumberVariables; index < pNumberVariables + pNumberConstrains; ++index) {
			basicValues.add(index);
		}
	}

	public boolean hasAnyPositiveC() {
		for (double d : c) {
			if (d > 0) {
				return true;
			}
		}
		return false;
	}

	public Optional<Integer> determineLeavingVariable(int pEnteringVariable) {
		double[] coefficientsLeavingVariable = A[getNonBasicVariablesIndex(pEnteringVariable)];

		double min = Double.MAX_VALUE;
		Integer leavingVariable = null;
		for (int constrainIndex = 0; constrainIndex < numberConstrains; constrainIndex++) {
			double coefficient = coefficientsLeavingVariable[constrainIndex];
			if (coefficient < 0) {
				double value = b[constrainIndex] / (-coefficient);
				if (min > value) {
					min = value;
					leavingVariable = constrainIndex;
				}
			}
		}
		return Optional.ofNullable(getBasicVariables(leavingVariable));
	}

	private int getNonBasicVariablesIndex(int pVariableNumber) {
		for (int i = 0; i < nonBasicValues.size(); i++) {
			if (nonBasicValues.get(i) == pVariableNumber) {
				return i;
			}
		}
		throw new IllegalStateException("Variable isn't non basic variable");
	}

	private Integer getNonBasicVariables(Integer pVariableNumberIndex) {
		if (pVariableNumberIndex == null) {
			return null;
		}
		return nonBasicValues.get(pVariableNumberIndex);
	}

	private int getBasicVariablesIndex(int pVariableNumber) {
		for (int i = 0; i < basicValues.size(); i++) {
			if (basicValues.get(i) == pVariableNumber) {
				return i;
			}
		}
		throw new IllegalStateException("Variable isn't non basic variable");
	}

	private Integer getBasicVariables(Integer pVariableNumberIndex) {
		if (pVariableNumberIndex == null) {
			return null;
		}
		return basicValues.get(pVariableNumberIndex);
	}

	public void calculatePivot(int pEnteringVariable, int pLeavingVariable) {

		int oldEnteringIndex = getNonBasicVariablesIndex(pEnteringVariable);
		int oldLeavingIndex = getBasicVariablesIndex(pLeavingVariable);
		int newEnteringIndex = oldLeavingIndex;
		int newLeavingIndex = oldEnteringIndex;

		double[] newC = new double[numberVariables];
		double[][] newA = new double[numberVariables][numberConstrains];
		double[] newB = new double[numberConstrains];

		double a_l_e = A[oldLeavingIndex][oldEnteringIndex];
		newB[newEnteringIndex] = b[oldLeavingIndex] / -a_l_e;
		for (int index = 0; index < numberConstrains; index++) {
			if (index != newLeavingIndex) {
				newA[newEnteringIndex][index] = A[oldLeavingIndex][index] / -a_l_e;
			}
		}
		newA[newEnteringIndex][newLeavingIndex] = 1 / a_l_e;
		for (int index = 0; index < numberConstrains; index++) {
			if(index == oldLeavingIndex)
				continue;
			newB[index] = b[index] + A[index][oldEnteringIndex] * newB[newEnteringIndex];
			for (int secondIndex = 0; secondIndex < numberVariables; ++secondIndex) {
				if(secondIndex == oldEnteringIndex)
					continue;
				newA[index][secondIndex] = A[index][secondIndex] + A[index][oldEnteringIndex] * newA[newEnteringIndex][secondIndex];
			}
			newA[index][newLeavingIndex] = -A[index][oldEnteringIndex] * newA[newEnteringIndex][newLeavingIndex];
		}
		v = v * c[oldEnteringIndex] * newB[newEnteringIndex];
		for (int index = 0; index < numberVariables; ++index) {
			if(index == oldEnteringIndex)
				continue;
			newC[index] = c[index] + c[oldEnteringIndex] * newA[newEnteringIndex][index];
		}
		newC[newLeavingIndex] = c[oldEnteringIndex] * newA[newEnteringIndex][newLeavingIndex];
		c = newC;
		A = newA;
		b = newB;
		nonBasicValues.set(oldEnteringIndex, pLeavingVariable); 
		basicValues.set(oldLeavingIndex, pEnteringVariable);
		
	}

}
