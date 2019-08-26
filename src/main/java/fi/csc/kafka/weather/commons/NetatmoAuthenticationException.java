package fi.csc.kafka.weather.commons;

public class NetatmoAuthenticationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public NetatmoAuthenticationException(String errorMessage) {
		super(errorMessage);
	}

}
