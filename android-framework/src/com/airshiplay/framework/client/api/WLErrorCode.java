package com.airshiplay.framework.client.api;

public enum WLErrorCode {
	UNEXPECTED_ERROR("Unexpected errorCode occurred. Please try again."), REQUEST_TIMEOUT("Request timed out."), REQUEST_SERVICE_NOT_FOUND("Service not found"), UNRESPONSIVE_HOST(
			"The service is currently not available."), PROCEDURE_ERROR("Procedure invocation errorCode."), APP_VERSION_ACCESS_DENIAL("Application version denied."), APP_VERSION_ACCESS_NOTIFY(
			"Notify application version changed.");

	private final String description;

	private WLErrorCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}
