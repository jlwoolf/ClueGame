package clueGame;

@SuppressWarnings("serial")
public class BadConfigFormatException extends Exception{
	public BadConfigFormatException() {
		super("Bad Configuaration Formatting");
	}
	public BadConfigFormatException(char roomChar) {
		super("Configuration file contains invalid room");
	}
	public BadConfigFormatException(int col1, int col2) {
		super("Column values do not match for each row");
	}
}
