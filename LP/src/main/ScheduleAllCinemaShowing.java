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
import CinemaShowing.CinemaShowingLinearProgram;
import CinemaShowing.MapCinemaShowingsToLinearProgram;
import CsvImporter.MapCsvToCinemaShowing;
import SimplexAlgorithm.ChoosingStrategy;
import SimplexAlgorithm.LinearProgram;
import SimplexAlgorithm.Simplex;
import SimplexAlgorithm.Simplex.LinearProgrammResult;

public class ScheduleAllCinemaShowing {

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
			if(!filepath.endsWith(CSV_SUFFIX))
				continue;
			try {

				Instant start = Instant.now();
				List<CinemaShowing> cinemaShowings = MapCsvToCinemaShowing.convertCsvFile(filepath);
				CinemaShowingLinearProgram linearProgram = MapCinemaShowingsToLinearProgram.convert(cinemaShowings);
				LinearProgrammResult result = Simplex.doSimplex(linearProgram, Simplex.BLAND_RULE);
				Instant end = Instant.now();
				Duration interval = Duration.between(start, end);
				System.out.println(filepath + ": Execution time in milliseconds: " + interval.getNano() / 1000000 + " and result: " + result);

			} catch (Exception e) {
				System.out.println(filepath);
				e.printStackTrace();
			}
		}
		System.out.println("Done!");
	}
}
