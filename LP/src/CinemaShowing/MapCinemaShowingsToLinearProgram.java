package CinemaShowing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import CinemaShowing.SimplexAlgorithm.CinemaShowingLinearProgram;
import CinemaShowing.SimplexAlgorithm.LinearProgramVariable;
import CinemaShowing.SimplexAlgorithm.OverlappingConstraint;
import CinemaShowing.SimplexAlgorithm.TitleConstraint;
import SimplexAlgorithm.LinearProgram;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;

public class MapCinemaShowingsToLinearProgram {

	private static final Comparator<LinearProgramVariable> COMPARATOR_ASC = Comparator.comparing(
			LinearProgramVariable::getCinemaShowing,
			Comparator.comparing(CinemaShowing::getWeekday).thenComparing(CinemaShowing::getStarttime)
					.thenComparing(CinemaShowing::getEndtime).thenComparing(CinemaShowing::getTitle));
	
	public static class Wrapper<T> {
		private final T solver;
		private final List<LinearProgramVariable> variables;

		Wrapper(List<LinearProgramVariable> pVariables, T pSolver) {
			solver = pSolver;
			variables = pVariables;
		}

		public T getSolver() {
			return solver;
		}

		public List<LinearProgramVariable> getVariables() {
			return variables;
		}

	}

	public static Wrapper<LinearProgram> convertLinearProgram(List<CinemaShowing> pList) {
		final List<LinearProgramVariable> variables = createVariables(pList);
		variables.sort(COMPARATOR_ASC);

		final List<TitleConstraint> titleConstraints = createTitleConstraints(variables);

		final List<OverlappingConstraint> overlappingConstraints = createOverlappingConstraints(variables);

		return createLinearProgramm(variables, titleConstraints, overlappingConstraints);
	}

	public static Wrapper<LpSolve> convertLpSolve(List<CinemaShowing> pList) throws LpSolveException {
		final List<LinearProgramVariable> variables = createVariables(pList);
		variables.sort(COMPARATOR_ASC);

		final List<TitleConstraint> titleConstraints = createTitleConstraints(variables);

		final List<OverlappingConstraint> overlappingConstraints = createOverlappingConstraints(variables);

		return createtLpSolve(variables, titleConstraints, overlappingConstraints);
	}

	private static Wrapper<LpSolve> createtLpSolve(List<LinearProgramVariable> variables,
			List<TitleConstraint> titleConstraints, List<OverlappingConstraint> overlappingConstraints)
			throws LpSolveException {
		LpSolve solver = LpSolve.makeLp(0, variables.size());
		int numberVariables = variables.size();
		double[] c = new double[numberVariables + 1];
		c[0] = 0;
		for (LinearProgramVariable variable : variables) {
			c[variable.getIncIndex()] = variable.getCinemaShowing().getScore();
			solver.setInt(variable.getIncIndex(), true); 

			double[] coefficents = createCoefficients(numberVariables + 1, Arrays.asList(variable.getIncIndex()), 1); 
			solver.addConstraint(coefficents, LpSolve.LE, 1); 
		}
		solver.setObjFn(c);
		solver.setMaxim();

		for (TitleConstraint constraint : titleConstraints) {
			Set<Integer> variablesInConstraint = constraint.getVariables().stream().map(LinearProgramVariable::getIncIndex) 
					.collect(Collectors.toSet()); 
			double[] coefficents = createCoefficients(numberVariables + 1, variablesInConstraint, 1);
			solver.addConstraint(coefficents, LpSolve.LE, 1);
		}

		for (OverlappingConstraint constraint : overlappingConstraints) {
			List<Integer> variablesInConstraint = Arrays.asList(constraint.getFirstVariable().getIncIndex(),
					constraint.getSecondVariable().getIncIndex());
			double[] coefficent = createCoefficients(numberVariables + 1, variablesInConstraint, 1);
			solver.addConstraint(coefficent, LpSolve.LE, 1);
		}
		
		solver.setTimeout(360); 
		return new Wrapper<>(variables, solver);
	} 

	private static double[] createCoefficients(int pNumberVariables, Collection<Integer> pVariablesInConstraint, double pValue) {
		double[] coefficent = new double[pNumberVariables];
		setValues(pNumberVariables, pVariablesInConstraint, pValue, coefficent);
		return coefficent;
	} 

	private static void setValues(int pNumberVariables, Collection<Integer> pVariablesInConstraint, double pValue,
			double[] coefficent) { 
		for (int index = 0; index < pNumberVariables; ++index) {
			if (pVariablesInConstraint.contains(index)) {
				coefficent[index] = pValue;
			} else { 
				coefficent[index] = 0;
			}
		}
	}

	private static List<LinearProgramVariable> createVariables(List<CinemaShowing> pList) {
		int variableNumber = 0;
		final List<LinearProgramVariable> variables = new ArrayList<>(pList.size());
		for (CinemaShowing cinemaShowing : pList) {
			variables.add(new LinearProgramVariable(variableNumber++, cinemaShowing));
		}
		return variables;
	}

	private static List<TitleConstraint> createTitleConstraints(final List<LinearProgramVariable> variables) {
		final Map<String, TitleConstraint> titleConstraints = new HashMap<>();
		for (LinearProgramVariable variable : variables) {
			TitleConstraint titleConstraint = titleConstraints.computeIfAbsent(variable.getCinemaShowing().getTitle(),
					x -> new TitleConstraint());
			titleConstraint.addLinearProgramVariable(variable);
		}

		return titleConstraints.values().stream().filter(TitleConstraint::isNecessary).collect(Collectors.toList());
	}

	private static List<OverlappingConstraint> createOverlappingConstraints(
			final List<LinearProgramVariable> variables) {
		final List<OverlappingConstraint> overlappingConstraints = new ArrayList<>();
		for (int firstIndex = 0; firstIndex < variables.size(); ++firstIndex) {
			LinearProgramVariable variable = variables.get(firstIndex);
			CinemaShowing cinemaShowing = variable.getCinemaShowing();
			for (int secondIndex = firstIndex + 1; secondIndex < variables.size(); secondIndex++) {
				LinearProgramVariable secondVariable = variables.get(secondIndex);
				if (cinemaShowing.isOverlappingWith(secondVariable.getCinemaShowing())) { 
					overlappingConstraints.add(new OverlappingConstraint(variable, secondVariable));
				}
			}
		}
		return overlappingConstraints;
	}

	private static Wrapper<LinearProgram> createLinearProgramm(List<LinearProgramVariable> variables,
			List<TitleConstraint> titleConstraints, List<OverlappingConstraint> overlappingConstraints) {
		int numberVariables = variables.size();
		int numberConstrains = titleConstraints.size() + overlappingConstraints.size();
		double[] c = new double[numberVariables];
		for (LinearProgramVariable variable : variables) {
			c[variable.getIndex()] = variable.getCinemaShowing().getScore();
		}
		double[][] A = new double[numberConstrains][numberVariables];
		double[] b = new double[numberConstrains];
		int constraintNumber = 0;
		for (TitleConstraint constraint : titleConstraints) {
			b[constraintNumber] = 1;
			Set<Integer> variablesInConstraint = constraint.getVariables().stream().map(LinearProgramVariable::getIndex)
					.collect(Collectors.toSet());
			for (int index = 0; index < numberVariables; ++index) {
				if (variablesInConstraint.contains(index)) {
					A[constraintNumber][index] = -1;
				} else {
					A[constraintNumber][index] = 0;
				}
			}
			++constraintNumber;
		}

		for (OverlappingConstraint constraint : overlappingConstraints) {
			b[constraintNumber] = 1;
			List<Integer> variablesInConstraint = Arrays.asList(constraint.getFirstVariable().getIndex(),
					constraint.getSecondVariable().getIndex());
			for (int index = 0; index < numberVariables; ++index) {
				if (variablesInConstraint.contains(index)) {
					A[constraintNumber][index] = -1;
				} else {
					A[constraintNumber][index] = 0;
				}
			}
			++constraintNumber;
		}
 
		return new Wrapper<>(variables, new LinearProgram(numberVariables,
				numberConstrains, c, A, b)); 
	}

}
