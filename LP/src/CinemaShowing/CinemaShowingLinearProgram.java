package CinemaShowing;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import SimplexAlgorithm.LinearProgram;

public class CinemaShowingLinearProgram extends LinearProgram {

	private static final Comparator<LinearProgramVariable> COMPARATOR_ASC = Comparator.comparing(
			LinearProgramVariable::getCinemaShowing,
			Comparator.comparing(CinemaShowing::getWeekday).thenComparing(CinemaShowing::getStarttime)
					.thenComparing(CinemaShowing::getEndtime).thenComparing(CinemaShowing::getTitle));
	
	private final List<LinearProgramVariable> variables;
	private final Collection<TitleConstraint> titleConstraints;
	private final List<OverlappingConstraint> overlappingConstraints;

	public CinemaShowingLinearProgram(List<LinearProgramVariable> pVariables,
			Collection<TitleConstraint> pTitleConstraints, List<OverlappingConstraint> pOverlappingConstraints,
			int pNumberVariables, int pNumberConstrains, double[] pC, double[][] pA, double[] pB) {
		super(pNumberVariables, pNumberConstrains, pC, pA, pB);
		variables = pVariables;
		titleConstraints = pTitleConstraints;
		overlappingConstraints = pOverlappingConstraints;
	}

	public void printSchedulePlan() {
		double[] result = getResult();
		variables.sort(COMPARATOR_ASC);
		for (LinearProgramVariable var : variables) {
			double resultValue = result[var.getIndex()];
			String cinemaShowingDescription = var.getCinemaShowing().getLineInCsv() + ", result was: " + resultValue;
			if (resultValue > 0.9) {
				System.out.println("Visit: " + cinemaShowingDescription);
			} else {
				System.out.println("Dont visit: " + cinemaShowingDescription);
			}

		}
	}

}
