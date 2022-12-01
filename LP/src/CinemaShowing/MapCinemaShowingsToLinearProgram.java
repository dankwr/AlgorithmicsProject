package CinemaShowing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import SimplexAlgorithm.LinearProgram;

public class MapCinemaShowingsToLinearProgram {

	public static LinearProgram convert(List<CinemaShowing> pList) {
		int variableNumber = 0;
		final List<LinearProgramVariable> variables = new ArrayList<>(pList.size());
		for (CinemaShowing cinemaShowing : pList) {
			variables.add(new LinearProgramVariable(variableNumber++, cinemaShowing));
		}

		final Map<String, TitleConstraint> titleConstraints = new HashMap<>();
		for (LinearProgramVariable variable : variables) {
			TitleConstraint titleConstraint = titleConstraints.computeIfAbsent(variable.getCinemaShowing().getTitle(),
					x -> new TitleConstraint());
			titleConstraint.addLinearProgramVariable(variable);
		}

		final List<OverlappingConstraint> overlappingConstraints = new ArrayList<>();
		for (LinearProgramVariable variable : variables) {
			CinemaShowing cinemaShowing = variable.getCinemaShowing();
			for (LinearProgramVariable secondVariable : variables) {
				if (variable != secondVariable && cinemaShowing.isOverlappingWith(secondVariable.getCinemaShowing())) {
					overlappingConstraints.add(new OverlappingConstraint(variable, secondVariable));
				}
			}
		}

		return createLinearProgramm(variables, titleConstraints.values(), overlappingConstraints);
	}

	private static LinearProgram createLinearProgramm(List<LinearProgramVariable> variables,
			Collection<TitleConstraint> titleConstraints, List<OverlappingConstraint> overlappingConstraints) {
		int numberVariables = variables.size();
		int numberConstrains = titleConstraints.size() + overlappingConstraints.size();
		double[] c = new double[numberVariables];
		for (LinearProgramVariable variable : variables) {
			c[variable.getIndex()] = variable.getCinemaShowing().getScore();
		}
		double[][] A = new double[numberVariables][numberConstrains];
		double[] b = new double[numberConstrains];
		int constraintNumber = 0;
		for (TitleConstraint constraint : titleConstraints) {
			b[constraintNumber] = 1;
			Set<Integer> variablesInConstraint = constraint.getVariables().stream().map(LinearProgramVariable::getIndex)
					.collect(Collectors.toSet());
			for (int index = 0; index < numberVariables; ++index) {
				if (variablesInConstraint.contains(index)) {
					A[index][constraintNumber] = -1;
				} else {
					A[index][constraintNumber] = 0;
				}
			}
			++constraintNumber;
		}
		
		for (OverlappingConstraint constraint : overlappingConstraints) {
			b[constraintNumber] = 1;
			List<Integer> variablesInConstraint = Arrays.asList(constraint.getFirstVariable().getIndex(), constraint.getSecondVariable().getIndex()); 
			for (int index = 0; index < numberVariables; ++index) {
				if (variablesInConstraint.contains(index)) {
					A[index][constraintNumber] = -1;
				} else {
					A[index][constraintNumber] = 0;
				}
			}
			++constraintNumber;
		}

		return new LinearProgram(numberVariables, numberConstrains, c, A, b);
	}

}
