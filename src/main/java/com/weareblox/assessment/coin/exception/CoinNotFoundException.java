package com.weareblox.assessment.coin.exception;

/**
 * The exception thrown in case of no coin found
 */
public class CoinNotFoundException  extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message = "Coin could not be found. Please priovide a valid id";
	
	public CoinNotFoundException() {
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
