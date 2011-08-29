package ru.iteco.gwtcalc.client;

import java.util.ArrayList;
import java.util.List;

import ru.iteco.gwtcalc.client.GwtCalc.CalcActionButtons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class GwtCalcTest extends GWTTestCase {

	/**
	 * Must refer to a valid module that sources this class.
	 */
	@Override
	public String getModuleName() {
		return "ru.iteco.gwtcalc.GwtCalcJUnit";
	}

	/**
	 * Tests the FieldVerifier.
	 */
	public void testFieldVerifier() {
		// assertFalse(FieldVerifier.isValidName(null));
		// assertFalse(FieldVerifier.isValidName(""));
		// assertFalse(FieldVerifier.isValidName("a"));
		// assertFalse(FieldVerifier.isValidName("ab"));
		// assertFalse(FieldVerifier.isValidName("abc"));
		// assertTrue(FieldVerifier.isValidName("abcd"));
	}

	/**
	 * This test will send a request to the server using the greetServer method
	 * in GreetingService and verify the response.
	 */
	public void testGreetingService() {
		// Create the service that we will test.
		GreetingServiceAsync greetingService = GWT
				.create(GreetingService.class);
		ServiceDefTarget target = (ServiceDefTarget) greetingService;
		target.setServiceEntryPoint(GWT.getModuleBaseURL() + "gwtcalc/greet");

		// Since RPC calls are asynchronous, we will need to wait for a response
		// after this test method returns. This line tells the test runner to
		// wait
		// up to 5 seconds before timing out.
		delayTestFinish(5000);

		// Send a request to the server.

		List<Integer> numbers = new ArrayList<Integer>();
		List<GwtCalc.CalcActionButtons> operations = new ArrayList<GwtCalc.CalcActionButtons>();

		/**
		 * Test 1: "2+2=4"
		 */
		numbers.add(2);
		numbers.add(2);
		operations.add(CalcActionButtons.Plus);

		greetingService.greetServer(numbers, operations,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// The request resulted in an unexpected error.
						fail("Request failure: " + caught.getMessage());
					}

					@Override
					public void onSuccess(String result) {
						// Verify that the response is correct.
						assertFalse(result.contentEquals("5"));
						assertFalse(result.contentEquals("3"));
						assertTrue(result.contentEquals("4"));

						// Now that we have received a response, we need to tell
						// the
						// test runner
						// that the test is complete. You must call finishTest()
						// after
						// an
						// asynchronous test finishes successfully, or the test
						// will
						// time out.
						finishTest();
					}
				});

		/**
		 * Test 2: 100+1-1*3=300
		 * 
		 */
		numbers.clear();
		operations.clear();
		numbers.add(100);
		numbers.add(1);
		numbers.add(1);
		numbers.add(3);
		operations.add(CalcActionButtons.Plus);
		operations.add(CalcActionButtons.Minus);
		operations.add(CalcActionButtons.Mul);

		greetingService.greetServer(numbers, operations,
				new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						fail("Request failure: " + caught.getMessage());
					}

					@Override
					public void onSuccess(String result) {
						assertFalse(result.contentEquals("777"));
						assertTrue(result.contentEquals("300"));
						finishTest();
					}
				});

	}

	public void testGreetingServiceErrorMsg() {
		GreetingServiceAsync greetingService = GWT
				.create(GreetingService.class);
		ServiceDefTarget target = (ServiceDefTarget) greetingService;
		target.setServiceEntryPoint(GWT.getModuleBaseURL() + "gwtcalc/greet");
		delayTestFinish(5000);

		List<Integer> numbers = new ArrayList<Integer>();
		List<GwtCalc.CalcActionButtons> operations = new ArrayList<GwtCalc.CalcActionButtons>();

		/**
		 * Test 1: "2+2* = ERROR"
		 */
		numbers.add(2);
		numbers.add(2);
		operations.add(CalcActionButtons.Plus);
		operations.add(CalcActionButtons.Mul);

		greetingService.greetServer(numbers, operations,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// The request resulted in an unexpected error.
						fail("Request failure: " + caught.getMessage());
					}

					@Override
					public void onSuccess(String result) {
						assertTrue(result.contentEquals("Error input data"));
						// assertTrue(result
						// .equals(GreetingServiceImpl.ERROR_INPUT_DATA_MSG));
						finishTest();
					}
				});

	}
}
