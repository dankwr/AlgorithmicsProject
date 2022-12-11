package CinemaShowing;

public class CinemaShowing {

	private final String lineInCsv;
	private final Weekday weekday;
	private final int starttime;
	private final int endtime;
	private final String title;
	private final double score;

	public CinemaShowing(String pLineInCsv, Weekday pWeekday, int pStarttime, int pDuration, String pTitle,
			double pScore) {
		weekday = pWeekday;
		starttime = pStarttime;
		endtime = pStarttime + pDuration;
		// TODO: kann wieder entfernt werden, wenn keine der Datensätze über Mitternacht
		// ausgestrahlt wird.
		assert endtime <= 1440;
		title = pTitle;
		score = pScore;
		lineInCsv = pLineInCsv;
	}

	public boolean isOverlappingWith(CinemaShowing pOtherCinemaShowing) {
		boolean isSameDay = weekday == pOtherCinemaShowing.weekday;
		boolean isOverlappingTime = starttime < pOtherCinemaShowing.endtime && pOtherCinemaShowing.starttime < endtime;
		return isSameDay && isOverlappingTime;
	}

	public boolean equalTitle(CinemaShowing pOtherCinemaShowing) {
		return title.equals(pOtherCinemaShowing.title);
	}

	public Weekday getWeekday() {
		return weekday;
	}

	public int getStarttime() {
		return starttime;
	}

	public int getEndtime() {
		return endtime;
	}

	public String getTitle() {
		return title;
	}

	public double getScore() {
		return score;
	}

	public String getLineInCsv() {
		return lineInCsv;
	}

	@Override
	public String toString() {
		return lineInCsv;
	}
}
