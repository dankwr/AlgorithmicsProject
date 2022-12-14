package main;

import java.io.IOException;
import java.util.List;

import CinemaShowing.CinemaShowing;
import CinemaShowing.MapCinemaShowingsToLinearProgram;
import CinemaShowing.MapCinemaShowingsToLinearProgram.Wrapper;
import CinemaShowing.SimplexAlgorithm.LinearProgramVariable;
import CsvImporter.MapCsvToCinemaShowing;
import SimplexAlgorithm.LinearProgram;
import SimplexAlgorithm.Simplex;
import SimplexAlgorithm.LinearProgrammResult;

public class ScheduleCinemaShowing {

	private static final String CSV_SUFFIX = ".csv";

	public static void main(String[] args) {
		assert args.length == 1;

		String filepath = args[0];
		assert filepath.endsWith(CSV_SUFFIX);

		try {
			List<CinemaShowing> cinemaShowings = MapCsvToCinemaShowing.convertCsvFile(filepath);
			Wrapper<LinearProgram> linearProgram = MapCinemaShowingsToLinearProgram.convertLinearProgram(cinemaShowings);
			LinearProgrammResult result = Simplex.doSimplex(linearProgram.getSolver(), Simplex.BLAND_RULE);
			if (result != LinearProgrammResult.FEASIBLE) {
				System.out.println("Result: " + result); 
			} else {
				printResult(linearProgram); 
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done!");
	}

	private static void printResult(Wrapper<LinearProgram> linearProgram) { 
		double[] result = linearProgram.getSolver().getResult();
		for (LinearProgramVariable var : linearProgram.getVariables()) {
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
