/*******************************************************************************
 * Copyright 2017 - Université d'Artois
 *
 * This file is part of SonarQube Golang plugin (sonar-golang).
 *
 * Sonar-golang is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sonar-golang is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Sonar-golang.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *            Thibault Falque (thibault_falque@ens.univ-artois.fr)
 *******************************************************************************/
package fr.univartois.sonargo;
/**
 * GoError
 * Represents an Error from a gometalinter checkstyle report
 * 
 * This is an example of  gometalinter checkstyle report
 * This class have a field for all attribute of error tag except the attribute "source" 
 * {@code
 * 	 <?xml version="1.0" encoding="UTF-8"?>
<checkstyle version="5.0">
  <file name="serverimage.go">
    <error column="3" line="38" message="&#39;if errremove != nil { return errremove }; return nil&#39; can be simplified to &#39;return errremove&#39;" severity="warning" source="gosimple">
    </error>
    <error column="0" line="57" message="Binds to all network interfaces,MEDIUM,HIGH" severity="warning" source="gas">
    </error>
    <error column="2" line="9" message="could not import projectblackwhitego/encoding (can&#39;t find import: projectblackwhitego/encoding)" severity="error" source="gotype">
    </error>
    <error column="9" line="24" message="undeclared name: encoding" severity="error" source="gotype">
    </error>
    <error column="14" line="28" message="undeclared name: encoding" severity="error" source="gotype">
    </error>
    <error column="6" line="13" message="exported type ImageService should have comment or be unexported" severity="warning" source="golint">
    </error>
    <error column="6" line="17" message="exported type ImageServiceArgsRes should have comment or be unexported" severity="warning" source="golint">
    </error>
    <error column="1" line="22" message="exported method ImageService.ApplyFilter should have comment or be unexported" severity="warning" source="golint">
    </error>
  </file>
  </checkstyle>
 * }
 * 
 * @author thibault
 */
public class GoError{
	private final String column;
	private final int line;
	private final String message;
	private final String severity;
	private final String filePath;
	/**
	 * Create a GoError 
	 * @param column The column where there is the problem
	 * @param line The line where there is the problem
	 * @param message The message for the error
	 * @param severity The severity for the message, this is the severity by the linter it's not directly the severity of SonarQube
	 * @param filePath The file where there is the error 
	 */
	public GoError(String column, int line, String message, String severity, String filePath) {
		super();
		this.column = column;
		this.line = line;
		this.message = message;
		this.severity = severity;
		this.filePath = filePath;
	}
	/**
	 * Get the column where there is the problem
	 * @return The column attribute
	 */
	public String getColumn() {
		return column;
	}
	
	
	/**
	 * Get the line where there is the problem
	 * @return The line attribute
	 */
	public int getLine() {
		return line;
	}
	
	
	/**
	 * Get the message where there is the problem
	 * @return The message attribute
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * Get the severity where there is the problem
	 * @return The severity attribute
	 */
	public String getSeverity() {
		return severity;
	}
	
	/**
	 * Get the filePath of file where there is the problem
	 * @return The filePath attribute
	 */
	public String getFilePath() {
		return filePath;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + line;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return prime * result + ((severity == null) ? 0 : severity.hashCode());
		
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
		if (line != other.line)
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
	@Override
	public String toString() {
		return "GoError [column=" + column + ", line=" + line + ", message=" + message + ", severity=" + severity
				+ ", filePath=" + filePath + "]";
	}
	
	
}