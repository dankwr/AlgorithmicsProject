package main;

import java.io.IOException;
import java.util.List;

import CinemaShowing.CinemaShowing;
import CinemaShowing.MapCinemaShowingsToLinearProgram;
import CsvImporter.MapCsvToCinemaShowing;
import SimplexAlgorithm.ChoosingStrategy;
import SimplexAlgorithm.LinearProgram;
import SimplexAlgorithm.Simplex;
import SimplexAlgorithm.Simplex.LinearProgrammResult;

public class ScheduleCinemaShowing {

	private static final String CSV_SUFFIX = ".csv";

	public static void main(String[] args) {
		assert args.length == 1;

		String filepath = args[0];
		assert filepath.endsWith(CSV_SUFFIX);

		try {
			List<CinemaShowing> cinemaShowings = MapCsvToCinemaShowing.convertCsvFile(filepath);
			LinearProgram linearProgram = MapCinemaShowingsToLinearProgram.convert(cinemaShowings);
			LinearProgrammResult result = Simplex.doSimplex(linearProgram, Simplex.BLAND_RULE);
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

	private static void printResult(LinearProgram linearProgram) {
		// TODO Auto-generated method stub
		
	}
}
