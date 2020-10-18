package clueGame;

@SuppressWarnings("serial")
public class BadConfigFormatException extends Exception{
	public BadConfigFormatException() {
		super("Bad Configuaration Formatting");
	}
	public BadConfigFormatException(char roomChar) {
		super("Configuration file contains invalid room");
	}
}
