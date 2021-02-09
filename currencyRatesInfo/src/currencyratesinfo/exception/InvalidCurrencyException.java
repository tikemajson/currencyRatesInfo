package currencyratesinfo.exception;

public class InvalidCurrencyException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCurrencyException(String msg) {
		super(msg);
	}
}
