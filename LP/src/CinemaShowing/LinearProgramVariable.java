package CinemaShowing;

public class LinearProgramVariable {

	private final int index;
	private final CinemaShowing cinemaShowing;

	public LinearProgramVariable(int pIndex, CinemaShowing pCinemaShowing) {
		index = pIndex;
		cinemaShowing = pCinemaShowing;
	}
	
	public int getIndex() {
		return index;
	} 
	
	public CinemaShowing getCinemaShowing() {
		return cinemaShowing;
	}

}
