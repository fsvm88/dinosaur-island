package client.InputVerifiers;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class NumericInput extends InputVerifier {
	@Override
	public boolean verify(JComponent input) {
		if (input instanceof JTextField) {
			JTextField inputText = (JTextField) input;
			if (inputText.getText().matches("[\\d]{1,5}")) { return true; }
		}
		return false;
	}
}
