package com.educationalappstore.licensing;

public interface LicenseCheckerCallback {
	public void allow(int code, String message);
	public void dontAllow(int code, String message);
	public void error(int code, String message);
}
