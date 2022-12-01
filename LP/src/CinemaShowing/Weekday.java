package CinemaShowing;

import java.util.stream.Stream;

public enum Weekday {

	MONDAY("Mo"), 
	TUESDAY("Di"), 
	WEDNESDAY("Mi"), 
	THURSDAY("Do"), 
	FRIDAY("Fr"), 
	SATURDAY("Sa"), 
	SUNDAY("So");

	private final String abbreviation;

	private Weekday(String pAbbreviation) {
		abbreviation = pAbbreviation;
	}

	public static Weekday getWeekday(String pAbbreviation) {
		return Stream.of(values()).filter(weekday -> weekday.abbreviation.equals(pAbbreviation)).findAny()
				.orElseThrow(() -> new IllegalStateException("Unsupported abbreviation: " + pAbbreviation));
	}
}
