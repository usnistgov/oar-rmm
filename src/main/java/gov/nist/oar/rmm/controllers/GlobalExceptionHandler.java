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

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(JsonMappingException.class)
    @ResponseBody
    /**
     * Handles General Exception
     * 
     * @param exception
     * @return ErrorInfo object with error details
     */
    public ErrorInfo myErrors(JsonMappingException exception) {
	logger.error("----Caught JsonMappingException----\n" + request.getDescription(false) + "\n Detail IOException:"
		+ exception.getStackTrace());
	return new ErrorInfo(request.getContextPath(), "General Exception", HttpStatus.CONFLICT.toString());

    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(MongoCommandException.class)
    @ResponseBody
    /**
     * Handles General Exception
     * 
     * @param exception
     * @return ErrorInfo object with error details
     */
    public ErrorInfo myErrorTest(com.mongodb.MongoCommandException exception) {
	logger.error("----Caught IOException----\n" + request.getDescription(false) + "\n Detail IOException:"
		+ exception.getStackTrace());
	return new ErrorInfo(request.getContextPath(), "General Exception", HttpStatus.CONFLICT.toString());

    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    /**
     * Handles General Exception
     * 
     * @param exception
     * @return ErrorInfo object with error details
     */
    public ErrorInfo myError(Exception exception) {
	logger.error("----Caught Exception----\n" + request.getDescription(false) + "\n Detail IOException:"
		+ exception.getStackTrace());
	return new ErrorInfo(request.getContextPath(), "General Exception", HttpStatus.CONFLICT.toString());

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    /**
     * Handles IllegalArgumentException
     * 
     * @param IllegalArgumentException
     * @return ErrorInfo object with error details
     */
    public ErrorInfo illegal(IllegalArgumentException exception) {
	logger.error("----This is a ilegal argument exception ----\n" + request.getDescription(false)
		+ "\n Detail ArgumentException:" + exception.getStackTrace());
	return new ErrorInfo(request.getContextPath(), "IllegalArgumentException", HttpStatus.BAD_REQUEST.toString());

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(KeyWordNotFoundException.class)
    @ResponseBody
    /***
     * Handles KeywordNotFoundException
     * 
     * @param exception
     * @return ErrorInfo object with error details
     */
    public ErrorInfo notFound(KeyWordNotFoundException exception) {
	logger.error("----Caught KeywordNotFoundException----\n" + request.getDescription(false)
		+ "\n Detail NotFoundException:" + exception.getStackTrace());
	return new ErrorInfo(request.getContextPath(), "KeyWordNotFoundException", HttpStatus.NOT_FOUND.toString());

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
	logger.error("----Caught ResourceNotFoundException----\n" + request.getDescription(false)
		+ "\n Detail ResourceNotFoundException:" + exception.getStackTrace());
	return new ErrorInfo(request.getContextPath(), "ResourceNotFoundException", HttpStatus.NOT_FOUND.toString());

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IOException.class)
    @ResponseBody
    /***
     * Handles BadRequest
     * 
     * @param Exception
     * @return ErrorInfo object with error details
     */
    public ErrorInfo badRequest(Exception exception) {
	logger.error("----Caught IOException----\n" + request.getDescription(false) + "\n Detail BadRequestException:"
		+ exception.getStackTrace());
	return new ErrorInfo(request.getContextPath(), "IOException", HttpStatus.BAD_REQUEST.toString());
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
	logger.error("----Caught Internal Error Exception----\n" + request.getDescription(false)
		+ "\n Detail BadRequestException:" + exception.getStackTrace());
	return new ErrorInfo(request.getContextPath(), "InternalServer Error",
		HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    /***
     * Handles BadRequest
     * 
     * @param Exception
     * @return ErrorInfo object with error details
     */
    public ErrorInfo runtimeError(RuntimeException exception) {
	logger.error("----Caught Runtime Exception----\n" + request.getDescription(false)
		+ "\n Detail BadRequestException:" + exception.getStackTrace());
	return new ErrorInfo(request.getContextPath(), "InternalServer Error",
		HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

}