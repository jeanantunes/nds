/**
 * 
 */
package br.com.abril.nds.service.exception;

/**
 * @author Diego Fernandes
 *
 */
public class RelationshipRestrictionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1657053959682975228L;

	/**
	 * 
	 */
	public RelationshipRestrictionException() {
	}

	/**
	 * @param message
	 */
	public RelationshipRestrictionException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RelationshipRestrictionException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RelationshipRestrictionException(String message, Throwable cause) {
		super(message, cause);
	}

}
