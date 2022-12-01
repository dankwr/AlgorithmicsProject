package CsvImporter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import CinemaShowing.CinemaShowing;
import CinemaShowing.Weekday;

public class MapCsvToCinemaShowing {

	public static List<CinemaShowing> convertCsvFile(String pFilePath) throws FileNotFoundException, IOException {
		try (FileReader fileReader = new FileReader(pFilePath); BufferedReader br = new BufferedReader(fileReader)) {
			return br.lines().map(MapCsvToCinemaShowing::mapLineToCinemaShowing).collect(Collectors.toList());
		}
	}

	private static CinemaShowing mapLineToCinemaShowing(String pLine) {
		String[] splittedLine = pLine.split("[;]");
		assert splittedLine.length == 5;
		Weekday wd; 
		try {
			wd = Weekday.getWeekday(splittedLine[0]);
		} catch (IllegalStateException e) {
			throw new IllegalStateException("Exception in Line '" + pLine + "'", e);
		}
		int starttime = calculateStarttime(splittedLine[1]);
		int duration = Integer.parseInt(splittedLine[2]);
		String title = splittedLine[3];
		double score = Double.parseDouble(splittedLine[4]);
		return new CinemaShowing(pLine, wd, starttime, duration, title, score);
	}

	private static int calculateStarttime(String pTimeString) {
		String[] splittedTimeString = pTimeString.split(":");
		assert splittedTimeString.length == 2;
		int hour = Integer.parseInt(splittedTimeString[0]);
		int min = Integer.parseInt(splittedTimeString[1]);
		return hour * 60 + min;
	}
}
