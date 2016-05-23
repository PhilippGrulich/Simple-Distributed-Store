package de.tuberlin.aec.bg.sds.api;

public class APIResultMessage {
	public final String resultValue;
	public final Boolean resultCode;
	public APIResultMessage(String resultValue, Boolean resultCode) {
		super();
		this.resultValue = resultValue;
		this.resultCode = resultCode;
	}
	
}
