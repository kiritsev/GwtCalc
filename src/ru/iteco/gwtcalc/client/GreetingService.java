package ru.iteco.gwtcalc.client;

import java.util.List;

import ru.iteco.gwtcalc.client.GwtCalc.CalcActionButtons;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(List<Integer> numbers, List<CalcActionButtons> actions)
			throws IllegalArgumentException;
}
