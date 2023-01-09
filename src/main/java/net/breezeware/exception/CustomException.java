package net.breezeware.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * CustomException class is used for throw the Exception.
 */

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomException extends Exception {



    /**
     * Error Message return variable
     */
    private String message;

    /**
     * Error Status return variable
     */
    private HttpStatus status;



}
