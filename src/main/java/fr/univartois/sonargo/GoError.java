package fr.univartois.sonargo;
public class GoError{
	private final String column;
	private final String line;
	private final String message;
	private final String severity;
	public GoError(final String column, final String line,final String message,final String severity) {
		super();
		this.column = column;
		this.line = line;
		this.message = message;
		this.severity = severity;
	}
	public String getColumn() {
		return column;
	}
	public String getLine() {
		return line;
	}
	public String getMessage() {
		return message;
	}
	public String getSeverity() {
		return severity;
	}
}