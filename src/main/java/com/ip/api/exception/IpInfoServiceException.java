package com.ip.api.exception;

/**
 * @author cristian.espitia
 */
import java.io.Serializable;

public class IpInfoServiceException extends Exception implements Serializable {

    private static final String MESSAGE = "Ip service: ";

    public IpInfoServiceException(Throwable cause) {
        super(MESSAGE, cause);
    }

    public IpInfoServiceException(String cause) {
        super(String.format("%s %s", MESSAGE, cause));
    }

}