package com.ip.api.exception;

/**
 * @author cristian.espitia
 */
import java.io.Serializable;

public class IpValidationServiceException extends Exception implements Serializable {

    private static final String MESSAGE = "Ip Validation service: ";

    public IpValidationServiceException(Throwable cause) {
        super(MESSAGE, cause);
    }

    public IpValidationServiceException(String cause) {
        super(String.format("%s %s", MESSAGE, cause));
    }

}