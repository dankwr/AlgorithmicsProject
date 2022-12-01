package CinemaShowing;

public class CinemaShowing {

	private final Weekday weekday;
	private final int starttime;
	private final int endtime;
	private final String title;
	private final float score;

	public CinemaShowing(Weekday pWeekday, int pStarttime, int pDuration, String pTitle, float pScore) {
		weekday = pWeekday;
		starttime = pStarttime;
		endtime = pStarttime + pDuration;
		// TODO: kann wieder entfernt werden, wenn keine der Datensätze über Mitternacht ausgestrahlt wird.
		assert endtime <= 1440;
		title = pTitle;
		score = pScore;
	}

	public boolean isOverlappingWith(CinemaShowing pOtherCinemaShowing) {
		boolean isSameDay = weekday == pOtherCinemaShowing.weekday;
		boolean isOverlappingTime = starttime < pOtherCinemaShowing.endtime && pOtherCinemaShowing.starttime < endtime;
		return isSameDay && isOverlappingTime;
	}

	public boolean equalTitle(CinemaShowing pOtherCinemaShowing) {
		return title.equals(pOtherCinemaShowing.title);
	}

	public String getTitle() {
		return title;
	}
	
	public float getScore() {
		return score; 
	}

}
