package ru.iteco.gwtcalc.client;

import java.util.List;

import ru.iteco.gwtcalc.client.GwtCalc.CalcActionButtons;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(List<Integer> numbers_input,
			List<CalcActionButtons> actions_input,
			AsyncCallback<String> callback) throws IllegalArgumentException;
}
