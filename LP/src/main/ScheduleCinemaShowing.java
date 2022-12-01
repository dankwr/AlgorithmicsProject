package main;

import java.io.IOException;

import CsvImporter.MapCsvToCinemaShowing;

public class ScheduleCinemaShowing {

	
	private static final String CSV_SUFFIX = ".csv";

	public static void main(String[] args) {
		assert args.length == 1;
		
		String filepath = args[0];
		assert filepath.endsWith(CSV_SUFFIX);
		
		try {
			MapCsvToCinemaShowing.convertCsvFile(filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done!"); 
	}
}
