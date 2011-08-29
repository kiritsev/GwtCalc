package ru.iteco.gwtcalc.server;

import java.util.List;

import ru.iteco.gwtcalc.client.GreetingService;
import ru.iteco.gwtcalc.client.GwtCalc;
import ru.iteco.gwtcalc.client.GwtCalc.CalcActionButtons;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public final static String ERROR_INPUT_DATA_MSG = "Error input data";

	private String calculate(List<Integer> numbers,
			List<CalcActionButtons> operations) {
		String result = "";
		int intResult = 0;
		if (numbers.size() < 2 || numbers.size() - 1 != operations.size()) {
			result = ERROR_INPUT_DATA_MSG;
		} else {
			intResult = numbers.get(0);
			for (int i = 1; i < numbers.size(); i++) {
				if (operations.get(i - 1).equals(CalcActionButtons.Plus)) {
					intResult += numbers.get(i);
				} else if (operations.get(i - 1)
						.equals(CalcActionButtons.Minus)) {
					intResult -= numbers.get(i);
				} else if (operations.get(i - 1).equals(CalcActionButtons.Mul)) {
					intResult *= numbers.get(i);
				} else if (operations.get(i - 1).equals(CalcActionButtons.Div)) {
					intResult /= numbers.get(i);
				}
			}
			result = String.valueOf(intResult);
		}
		return result;
	}

	@Override
	public String greetServer(List<Integer> numbers_input,
			List<GwtCalc.CalcActionButtons> actions_input) {

		return calculate(numbers_input, actions_input);
	}
}
