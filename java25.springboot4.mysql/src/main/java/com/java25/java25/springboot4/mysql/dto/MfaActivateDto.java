package com.java25.java25.springboot4.mysql.dto;

public class MfaActivateDto {

	private boolean TwoFactorEnabled;

	public boolean isTwoFactorEnabled() {
		return TwoFactorEnabled;
	}

	public void setTwoFactorEnabled(boolean twoFactorEnabled) {
		TwoFactorEnabled = twoFactorEnabled;
	}

	
	
}
