package ru.iteco.gwtcalc.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtCalc implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	// private static final String SERVER_ERROR = "An error occurred while "
	// + "attempting to contact the server. Please check your network "
	// + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	enum CalcDigitButtons {
		D0("0", 0), D1("1", 1), D2("2", 2), D3("3", 3), D4("4", 4), D5("5", 5), D6(
				"6", 6), D7("7", 7), D8("8", 8), D9("9", 9);
		private String name;
		private int digit;

		private CalcDigitButtons(String value, int digit) {
			this.name = value;
			this.digit = digit;
		}
	};

	public static enum CalcActionButtons {
		Plus("+"), Minus("-"), Mul("*"), Div("/"), Reset("reset"), Del("del"), Result(
				"=");
		private String name;

		private CalcActionButtons(String value) {
			this.name = value;
		}
	}

	class DigitButton extends Button {
		private CalcDigitButtons info;

		public CalcDigitButtons getInfo() {
			return info;
		}

		public DigitButton(CalcDigitButtons value) {
			super(value.name);
			this.info = value;
		}
	}

	class ActionButton extends Button {
		private CalcActionButtons info;

		public CalcActionButtons getInfo() {
			return info;
		}

		public ActionButton(CalcActionButtons value) {
			super(value.name);
			this.info = value;
		}
	}

	class Calc {
		final Button calcDigit1Button = new DigitButton(CalcDigitButtons.D1);
		final Button calcDigit2Button = new DigitButton(CalcDigitButtons.D2);
		final Button calcDigit3Button = new DigitButton(CalcDigitButtons.D3);
		final Button calcDigit4Button = new DigitButton(CalcDigitButtons.D4);
		final Button calcDigit5Button = new DigitButton(CalcDigitButtons.D5);
		final Button calcDigit6Button = new DigitButton(CalcDigitButtons.D6);
		final Button calcDigit7Button = new DigitButton(CalcDigitButtons.D7);
		final Button calcDigit8Button = new DigitButton(CalcDigitButtons.D8);
		final Button calcDigit9Button = new DigitButton(CalcDigitButtons.D9);
		final Button calcDigit0Button = new DigitButton(CalcDigitButtons.D0);
		final Button calcResultButton = new ActionButton(
				CalcActionButtons.Result);
		final Button calcPlusButton = new ActionButton(CalcActionButtons.Plus);
		final Button calcMinusButton = new ActionButton(CalcActionButtons.Minus);
		final Button calcMulButton = new ActionButton(CalcActionButtons.Mul);
		final Button calcDivButton = new ActionButton(CalcActionButtons.Div);
		final Button calcDeleteButton = new ActionButton(CalcActionButtons.Del);
		final Button calcResetButton = new ActionButton(CalcActionButtons.Reset);

		final Label calcResultLabel = new Label();
		final Label calcErrorLabel = new Label();

		String inputBuf;

		List<Integer> numbers;

		List<CalcActionButtons> operations;

		private void resetCalcContainers() {
			this.inputBuf = null;
			this.numbers = new ArrayList<Integer>();
			this.operations = new ArrayList<CalcActionButtons>();
		}

		private void calculateOnServer(List<Integer> numbers,
				List<CalcActionButtons> operations) {
			greetingService.greetServer(numbers, operations,
					new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							calcErrorLabel.setText("RPC Failure: " + caught);
						}

						@Override
						public void onSuccess(String result) {
							calcResultLabel.setText(result);
						}
					});
		}

		class CalcButtonsHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {

				calcErrorLabel.setText("");

				if (event.getSource() instanceof DigitButton) {
					int digit = ((DigitButton) event.getSource()).getInfo().digit;
					if (inputBuf == null) {
						inputBuf = String.valueOf(digit);
					} else {
						inputBuf += String.valueOf(digit);
					}
					calcResultLabel.setText(inputBuf);
				} else if (event.getSource() instanceof ActionButton) {
					CalcActionButtons calcActionButton = ((ActionButton) event
							.getSource()).getInfo();

					switch (calcActionButton) {
					case Plus:
					case Minus:
					case Mul:
					case Div:
						if (inputBuf != null && inputBuf.length() != 0) {
							numbers.add(Integer.valueOf(inputBuf));
							inputBuf = null;
							if (numbers.size() > operations.size()) {
								operations.add(calcActionButton);
							} else {
								calcErrorLabel.setText("Calc internal error");
							}
						} else {
							calcErrorLabel.setText("Input number at first");
						}

						calcResultLabel.setText(calcActionButton.name);
						break;
					case Del:
						if (inputBuf != null && inputBuf.length() != 0) {
							inputBuf = inputBuf.substring(0,
									inputBuf.length() - 1);
							calcResultLabel.setText(inputBuf);
						}
						break;
					case Reset:
						resetCalcContainers();
						calcResultLabel.setText("");
						break;
					case Result:
						if (inputBuf != null) {
							numbers.add(Integer.valueOf(inputBuf));
							inputBuf = null;
						}
						if (numbers.size() - 1 == operations.size()) {
							calculateOnServer(numbers, operations);
							// calcResultLabel.setText("Result can be send:"
							// + calculateLocally(numbers, operations));
						} else {
							calcResultLabel.setText("Result error!");
						}
						resetCalcContainers();
						break;
					default:
						calcResultLabel.setText("ERROR BUTTON");
					}

				}

			}
		}

		public Calc() {
			initialize();
		}

		private void initialize() {
			calcResultLabel.setText("");

			addStylesToWidgets();
			addWidgetsToWindow();
			addCalcDigitsHandler();

			resetCalcContainers();
		}

		private void addStylesToWidgets() {
			calcDigit0Button.addStyleName("calcButton");

			calcDigit1Button.addStyleName("calcButton");
			calcDigit2Button.addStyleName("calcButton");
			calcDigit3Button.addStyleName("calcButton");

			calcDigit4Button.addStyleName("calcButton");
			calcDigit5Button.addStyleName("calcButton");
			calcDigit6Button.addStyleName("calcButton");

			calcDigit7Button.addStyleName("calcButton");
			calcDigit8Button.addStyleName("calcButton");
			calcDigit9Button.addStyleName("calcButton");

			calcPlusButton.addStyleName("calcOperationButton");
			calcMinusButton.addStyleName("calcOperationButton");
			calcMulButton.addStyleName("calcOperationButton");
			calcDivButton.addStyleName("calcOperationButton");

			calcDeleteButton.addStyleName("calcButton");
			calcResetButton.addStyleName("calcButton");
			calcResultButton.addStyleName("calcButton");

			calcErrorLabel.addStyleName("calcErrorLabel");
		}

		private void addWidgetsToWindow() {
			RootPanel.get("calcResultContainer").add(calcResultLabel);
			RootPanel.get("calcResultContainer").add(calcErrorLabel);

			RootPanel.get("calcButtonsRow1Container").add(calcDigit1Button);
			RootPanel.get("calcButtonsRow1Container").add(calcDigit2Button);
			RootPanel.get("calcButtonsRow1Container").add(calcDigit3Button);

			RootPanel.get("calcButtonsRow2Container").add(calcDigit4Button);
			RootPanel.get("calcButtonsRow2Container").add(calcDigit5Button);
			RootPanel.get("calcButtonsRow2Container").add(calcDigit6Button);

			RootPanel.get("calcButtonsRow3Container").add(calcDigit7Button);
			RootPanel.get("calcButtonsRow3Container").add(calcDigit8Button);
			RootPanel.get("calcButtonsRow3Container").add(calcDigit9Button);

			RootPanel.get("calcButtonsRow4Container").add(calcDigit0Button);

			RootPanel.get("calcButtonsRow5Container").add(calcPlusButton);
			RootPanel.get("calcButtonsRow5Container").add(calcMinusButton);
			RootPanel.get("calcButtonsRow5Container").add(calcMulButton);
			RootPanel.get("calcButtonsRow5Container").add(calcDivButton);

			RootPanel.get("calcButtonsRow6Container").add(calcResetButton);
			RootPanel.get("calcButtonsRow6Container").add(calcDeleteButton);
			RootPanel.get("calcButtonsRow6Container").add(calcResultButton);
		}

		private void addCalcDigitsHandler() {
			CalcButtonsHandler handler = new CalcButtonsHandler();

			calcDigit0Button.addClickHandler(handler);
			calcDigit1Button.addClickHandler(handler);
			calcDigit2Button.addClickHandler(handler);
			calcDigit3Button.addClickHandler(handler);
			calcDigit4Button.addClickHandler(handler);
			calcDigit5Button.addClickHandler(handler);
			calcDigit6Button.addClickHandler(handler);
			calcDigit7Button.addClickHandler(handler);
			calcDigit8Button.addClickHandler(handler);
			calcDigit9Button.addClickHandler(handler);
			calcPlusButton.addClickHandler(handler);
			calcMinusButton.addClickHandler(handler);
			calcMulButton.addClickHandler(handler);
			calcDivButton.addClickHandler(handler);
			calcResetButton.addClickHandler(handler);
			calcDeleteButton.addClickHandler(handler);
			calcResultButton.addClickHandler(handler);
		}

	}

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		new Calc();
	}
}
