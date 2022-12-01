package CinemaShowing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TitleConstraint {
	private final List<LinearProgramVariable> variables;

	public TitleConstraint() {
		variables = new ArrayList<>();
	} 

	public void addLinearProgramVariable(LinearProgramVariable pVariable) {
		variables.add(pVariable);
	}
	
	public List<LinearProgramVariable> getVariables() {
		return Collections.unmodifiableList(variables); 
	}
	

}
