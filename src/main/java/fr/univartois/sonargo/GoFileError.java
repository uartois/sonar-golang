package fr.univartois.sonargo;

import java.util.List;

public class GoFileError {
	private List<GoError> listError;
	private String filePath;

	
	public GoFileError(List<GoError> listError, String filePath) {
		super();
		this.listError = listError;
		this.filePath = filePath;
	}
	@Override
	public String toString() {
		return "GoFileError [listError=" + listError + ", filePath=" + filePath + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + ((listError == null) ? 0 : listError.hashCode());
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
		GoFileError other = (GoFileError) obj;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		if (listError == null) {
			if (other.listError != null)
				return false;
		} else if (!listError.equals(other.listError))
			return false;
		return true;
	}
	
	
}
