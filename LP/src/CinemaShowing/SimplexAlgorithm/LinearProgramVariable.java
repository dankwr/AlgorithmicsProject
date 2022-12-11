package CinemaShowing.SimplexAlgorithm;

import CinemaShowing.CinemaShowing;

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
	
	public int getIncIndex() {
		return index + 1;
	} 
	
	public CinemaShowing getCinemaShowing() {
		return cinemaShowing;
	}

	@Override
	public String toString() {
		return cinemaShowing.toString();
	}
}
