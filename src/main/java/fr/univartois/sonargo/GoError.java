package fr.univartois.sonargo;
public class GoError{
	private final String column;
	private final String line;
	private final String message;
	private final String severity;
	private final String filePath;
	
	public GoError(String column, String line, String message, String severity, String filePath) {
		super();
		this.column = column;
		this.line = line;
		this.message = message;
		this.severity = severity;
		this.filePath = filePath;
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
	public String getFilePath() {
		return filePath;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + ((line == null) ? 0 : line.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((severity == null) ? 0 : severity.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoError other = (GoError) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		if (line == null) {
			if (other.line != null)
				return false;
		} else if (!line.equals(other.line))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (severity == null) {
			if (other.severity != null)
				return false;
		} else if (!severity.equals(other.severity))
			return false;
		return true;
	}
	
}