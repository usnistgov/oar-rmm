/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 * @author: Deoyani Nandrekar-Heinis
 */
package gov.nist.oar.rmm.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.CharArrayWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.mongodb.MongoCommandException;

import gov.nist.oar.rmm.exceptions.InternalServerException;
import gov.nist.oar.rmm.exceptions.KeyWordNotFoundException;
import gov.nist.oar.rmm.exceptions.ResourceNotFoundException;
import gov.nist.oar.rmm.utilities.entities.ErrorInfo;

@ControllerAdvice
/***
 * GlobalExceptionHandler class takes care of any exceptions thrown in the code
 * and returns appropriate messages
 * 
 * @author Deoyani Nandrekar-Heinis
 *
 */
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Autowired
    WebRequest request;

    private String formatStackTrace(Exception exc) {
        CharArrayWriter out = new CharArrayWriter(1024);
        PrintWriter pw = new PrintWriter(out);
        try {
            exc.printStackTrace(pw);
            return out.toString();
        }
        finally {
            pw.close();
        }
    }

    private String formatOrigin(Exception ex) {
        return ex.getStackTrace()[0].toString();
    }

    // TODO: consider changing return status to BAD_REQUEST
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(JsonMappingException.class)
    @ResponseBody
    /**
     * Handles JSON-to-object conversion errors
     * 
     * @param exception
     * @return ErrorInfo object with error details
     */
    public ErrorInfo jsonMappingError(JsonMappingException exception) {
	logger.error("JSON mapping error while responding to " + request.getDescription(false) + ": "
                     + exception.getMessage() + "; origin:\n" + formatOrigin(exception));
	return new ErrorInfo(request.getContextPath(), "JSON processing error",
                             HttpStatus.CONFLICT.toString());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MongoCommandException.class)
    @ResponseBody
    /**
     * Handles MongoDB interaction errors
     * 
     * @param exception
     * @return ErrorInfo object with error details
     */
    public ErrorInfo mongoError(MongoCommandException exception) {
	logger.error("MongoDB error while responding to " + request.getDescription(false) + ": " 
                     + exception.getMessage() + "\n" + formatStackTrace(exception));
	return new ErrorInfo(request.getContextPath(), "Internal database error",
                             HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    /**
     * Handles General Exception
     * 
     * @param exception
     * @return ErrorInfo object with error details
     */
    public ErrorInfo unexpectedError(Exception exception) {
	logger.error("Sending 500 for unexpected exception while responding to " +
                     request.getDescription(false) + ": " + exception.getMessage() + "\n" +
                     formatStackTrace(exception));
	return new ErrorInfo(request.getContextPath(), "Unexpected server error",
                             HttpStatus.CONFLICT.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    /**
     * Handles IllegalArgumentException raised by SearchController when query input errors 
     * are encountered.
     * 
     * @param IllegalArgumentException
     * @return ErrorInfo object with error details
     */
    public ErrorInfo illegalInputError(IllegalArgumentException exception) {
	logger.error("Sending 400 for illegal parameters encountered while responding to " +
                     request.getDescription(false) + ": " + exception.getMessage() + "\nOrigin: " +
                     formatOrigin(exception));
	return new ErrorInfo(request.getContextPath(), "Illegal input request",
                             HttpStatus.BAD_REQUEST.toString());
    }

    // Note: this exception is not raised in code
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(KeyWordNotFoundException.class)
    @ResponseBody
    /***
     * Handles KeywordNotFoundException
     * 
     * @param exception
     * @return ErrorInfo object with error details
     */
    public ErrorInfo keywordNotFound(KeyWordNotFoundException exception) {
	logger.error("Sending 404 for KeywordNotFoundException encountered while responding to " +
                     request.getDescription(false) + ": " + exception.getMessage() + "\n" +
                     formatStackTrace(exception));
	return new ErrorInfo(request.getContextPath(), "KeyWordNotFoundException",
                             HttpStatus.NOT_FOUND.toString());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    /***
     * Handles ResourceNotFoundException
     * 
     * @param ResourceNotFoundException
     * @return ErrorInfo object with error details
     */
    public ErrorInfo resourceNotFound(ResourceNotFoundException exception) {
	logger.error("Sending 404: Resource not found: " + request.getDescription(false) +
                     " (" + exception.getMessage() + ")");
	return new ErrorInfo(request.getContextPath(), "Request resource not found",
                             HttpStatus.NOT_FOUND.toString());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    @ResponseBody
    /***
     * Handles unexpected IOException
     * 
     * @param Exception
     * @return ErrorInfo object with error details
     */
    public ErrorInfo unexpectedIOError(IOException exception) {
	logger.error("Sending 500 for unexpected IOException while responding to " +
                     request.getDescription(false) + ": " + exception.getMessage() + "\n" +
                     formatStackTrace(exception));
	return new ErrorInfo(request.getContextPath(), "Unexpected IOException",
                             HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerException.class)
    @ResponseBody
    /***
     * Handles BadRequest
     * 
     * @param Exception
     * @return ErrorInfo object with error details
     */
    public ErrorInfo internalError(Exception exception) {
	logger.error("Sending 500 for unexpected exception while responding to " +
                     request.getDescription(false) + ": " + exception.getMessage() + "\n" +
                     formatStackTrace(exception));
	return new ErrorInfo(request.getContextPath(), "Internal server error",
		HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    /**
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    /***
     * Handles BadRequest
     * 
     * @param Exception
     * @return ErrorInfo object with error details
     * /
    public ErrorInfo runtimeError(RuntimeException exception) {
	logger.error("----Caught Runtime Exception----\n" + request.getDescription(false)
		+ "\n Detail BadRequestException:" + exception.getStackTrace());
	return new ErrorInfo(request.getContextPath(), "InternalServer Error",
		HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }
    */

}
