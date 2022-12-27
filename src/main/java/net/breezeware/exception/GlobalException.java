package net.breezeware.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * GlobalException class is used to throw exception globally for All Controller
 * Exception.
 */

@RestControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {

    /**
     * @param  customException throw the Exception message and status
     * @return                 shown a list of Error messages.
     */
    @ExceptionHandler(CustomException.class)
    public ErrorResponse handleGlobalException(CustomException customException) {

        List<String> details = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(customException.getStatus().value());
        errorResponse.setMessage(customException.getStatus().name());
        details.add(customException.getMessage());
        errorResponse.setDetails(details);

        return errorResponse;
    }

    /**
     * @param  NullPointerException throw the Exception message and status
     * @return                      shown a list of Error messages.
     */
    @ExceptionHandler(NullPointerException.class)
    public ErrorResponse handleGlobalException(NullPointerException nullPointerException) {
        List<String> details = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(nullPointerException.getMessage());
        details.add(nullPointerException.getMessage());
        errorResponse.setDetails(details);
        return errorResponse;
    }

    /**
     * @param  NullPointerException throw the Exception message and status
     * @return                      shown a list of Error messages.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse handleGlobalException(NoSuchElementException noSuchElementException) {
        List<String> details = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(noSuchElementException.getMessage());
        details.add(noSuchElementException.getMessage());
        errorResponse.setDetails(details);
        return errorResponse;
    }

}
