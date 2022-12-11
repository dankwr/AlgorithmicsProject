package main;

import java.io.IOException;
import java.util.List;

import CinemaShowing.CinemaShowing;
import CinemaShowing.MapCinemaShowingsToLinearProgram;
import CinemaShowing.MapCinemaShowingsToLinearProgram.Wrapper;
import CinemaShowing.SimplexAlgorithm.LinearProgramVariable;
import CsvImporter.MapCsvToCinemaShowing;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;

public class ScheduleCinemaShowingLpSolver {

	private static final String CSV_SUFFIX = ".csv";

	public static void main(String[] args) {
		assert args.length == 1;

		String filepath = args[0];
		assert filepath.endsWith(CSV_SUFFIX);

		try {
			List<CinemaShowing> cinemaShowings = MapCsvToCinemaShowing.convertCsvFile(filepath);
			Wrapper<LpSolve> linearProgram = MapCinemaShowingsToLinearProgram.convertLpSolve(cinemaShowings);
			linearProgram.getSolver().solve();
			printSchedulePlan(linearProgram);
			linearProgram.getSolver().deleteLp(); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LpSolveException e) { 
			e.printStackTrace();
		}
		System.out.println("Done!");
	}


	public static void printSchedulePlan(Wrapper<LpSolve> solver) throws LpSolveException {
		double[] result = solver.getSolver().getPtrVariables();
		System.out.println("Solver variables: " + result.length + ", variables: " + solver.getVariables().size());
		
		for (LinearProgramVariable var : solver.getVariables()) { 
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
