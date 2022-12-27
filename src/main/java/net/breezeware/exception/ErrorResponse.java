package net.breezeware.exception;

import java.util.List;

import lombok.Data;

/**
 * ErrorResponse class is used for store the Error data.
 */
@Data
public class ErrorResponse {

    /**
     * Error Message
     */

    private String message;

    /**
     * Error Code
     */

    private int statusCode;

    /**
     * Error Details
     */

    private List<String> details;
}
