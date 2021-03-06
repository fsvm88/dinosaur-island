package client.InputVerifiers;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class AlphanumericInput extends InputVerifier {
	@Override
	public boolean verify(JComponent input) {
		if (input instanceof JTextField) {
			JTextField inputText = (JTextField) input;
			if (inputText.getText().matches("[\\w]+")) { return true; }
		}
		return false;
	}
}
