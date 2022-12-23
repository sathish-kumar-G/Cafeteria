package net.breezewere.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * CustomException class is used for throw the Exception.
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class CustomException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Error Message return variable
     */
    private String message;

    /**
     * Error Status return variable
     */
    private HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

}
