package currencyratesinfo.exception;

public class InvalidCurrencyValueException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCurrencyValueException(String msg) {
		super(msg);
	}
}
