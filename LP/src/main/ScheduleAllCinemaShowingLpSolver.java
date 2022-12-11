package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import CinemaShowing.CinemaShowing;
import CinemaShowing.MapCinemaShowingsToLinearProgram;
import CinemaShowing.MapCinemaShowingsToLinearProgram.Wrapper;
import CsvImporter.MapCsvToCinemaShowing;
import SimplexAlgorithm.LinearProgrammResult;
import lpsolve.LogListener;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;

public class ScheduleAllCinemaShowingLpSolver {

	private static final String CSV_SUFFIX = ".csv";

	public static void main(String[] args) {
		assert args.length == 1;

		String folderpath = args[0];
		List<String> files = new ArrayList<>();
		try (Stream<Path> paths = Files.walk(Paths.get(folderpath))) {
			files = paths.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String filepath : files) {
			if (!filepath.endsWith(CSV_SUFFIX))
				continue;
			try {
				System.out.println("Start with  " + filepath);
				List<CinemaShowing> cinemaShowings = MapCsvToCinemaShowing.convertCsvFile(filepath);
				Wrapper<LpSolve> linearProgram = MapCinemaShowingsToLinearProgram.convertLpSolve(cinemaShowings);
				linearProgram.getSolver().setOutputfile(""); 
				
				Instant start = Instant.now();
				linearProgram.getSolver().solve();
				Instant end = Instant.now();
				Duration interval = Duration.between(start, end); 
				System.out.println(filepath + ": Execution time: " + interval.getSeconds() + "."
						+ interval.getNano() / 1000000 + " sec, result is " + linearProgram.getSolver().getStatustext(linearProgram.getSolver().getStatus()));
				linearProgram.getSolver().deleteLp();
			} catch (Exception e) {
				System.out.println(filepath);
				e.printStackTrace();
			}
		}
		System.out.println("Done!");
	}
}
