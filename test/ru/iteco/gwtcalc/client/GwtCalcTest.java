package ru.iteco.gwtcalc.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.iteco.gwtcalc.client.GwtCalc.CalcActionButtons;
import ru.iteco.gwtcalc.server.GreetingServiceImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class GwtCalcTest extends GWTTestCase {

	GreetingServiceAsync greetingService;

	/**
	 * Must refer to a valid module that sources this class.
	 */
	@Override
	public String getModuleName() {
		return "ru.iteco.gwtcalc.GwtCalcJUnit";
	}

	@Override
	protected void gwtSetUp() {
		this.greetingService = GWT.create(GreetingService.class);
		ServiceDefTarget target = (ServiceDefTarget) greetingService;
		target.setServiceEntryPoint(GWT.getModuleBaseURL() + "gwtcalc/greet");

		delayTestFinish(40000);
	}

	class MyAssert {
		private boolean condition;
		private String expectedResult;

		public MyAssert(boolean condition, String expectedResult) {
			this.condition = condition;
			this.expectedResult = expectedResult;
		}

	}

	private void sendRequest(final List<MyAssert> asserts,
			List<Integer> numbers, List<GwtCalc.CalcActionButtons> operations) {
		greetingService.greetServer(numbers, operations,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						fail("Request failure: " + caught.getMessage());
					}

					@Override
					public void onSuccess(String result) {
						Iterator<MyAssert> it = asserts.iterator();
						MyAssert item;

						while (it.hasNext()) {
							item = it.next();
							if (item.condition == true) {
								assertTrue(result
										.contentEquals(item.expectedResult));
							} else {
								assertFalse(result
										.contentEquals(item.expectedResult));
							}
						}

						finishTest();
					}
				});
	}

	// public void testCalculatorCalls() {
	// initialize();
	// subtestTwoPlusTwoIsFore();
	// subtestHundredPlusOneMinusOneMultiplyThree();
	// subtestTwoPlusTwoMultiplyNothingIsError();
	// }

	public void testTwoPlusTwoIsFore() {
		/**
		 * 2 + 2 = 4
		 */
		List<Integer> numbers = new ArrayList<Integer>();
		List<GwtCalc.CalcActionButtons> operations = new ArrayList<GwtCalc.CalcActionButtons>();
		List<MyAssert> assertions = new ArrayList<MyAssert>();

		numbers.add(2);
		operations.add(CalcActionButtons.Plus);
		numbers.add(2);

		assertions.add(new MyAssert(true, "4"));
		assertions.add(new MyAssert(false, "5"));
		assertions.add(new MyAssert(false, "6"));

		sendRequest(assertions, numbers, operations);
	}

	public void testHundredPlusOneMinusOneMultiplyThree() {
		/**
		 * 100 + 1 - 1 * 3 = 300
		 */
		List<Integer> numbers = new ArrayList<Integer>();
		List<GwtCalc.CalcActionButtons> operations = new ArrayList<GwtCalc.CalcActionButtons>();
		List<MyAssert> assertions = new ArrayList<MyAssert>();

		numbers.add(100);
		operations.add(CalcActionButtons.Plus);
		numbers.add(1);
		operations.add(CalcActionButtons.Minus);
		numbers.add(1);
		operations.add(CalcActionButtons.Mul);
		numbers.add(3);

		assertions.add(new MyAssert(false, "777"));
		assertions.add(new MyAssert(true, "300"));

		sendRequest(assertions, numbers, operations);
	}

	public void testTwoPlusTwoMultiplyNothingIsError() {
		/**
		 * Test 1: "2+2* = ERROR"
		 */
		List<Integer> numbers = new ArrayList<Integer>();
		List<GwtCalc.CalcActionButtons> operations = new ArrayList<GwtCalc.CalcActionButtons>();
		List<MyAssert> assertions = new ArrayList<MyAssert>();

		numbers.add(2);
		operations.add(CalcActionButtons.Plus);
		numbers.add(2);
		operations.add(CalcActionButtons.Mul);

		assertions.add(new MyAssert(true,
				GreetingServiceImpl.ERROR_INPUT_DATA_MSG));

		sendRequest(assertions, numbers, operations);
	}
}
