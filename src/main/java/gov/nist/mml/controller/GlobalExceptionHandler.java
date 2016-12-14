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
package gov.nist.mml.controller;


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

import gov.nist.mml.domain.ErrorInfo;
import gov.nist.mml.exception.KeyWordNotFoundException;
import gov.nist.mml.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	@Autowired
    WebRequest request;
	 
	  @ResponseStatus(HttpStatus.CONFLICT)
	  @ExceptionHandler(Exception.class)
	  @ResponseBody
	  public ErrorInfo  myError(Exception exception) {
	    logger.info("----Caught IOException----");
	    return new ErrorInfo(request.getDescription(false), exception,"General Exception",HttpStatus.CONFLICT.toString());
        
	  }
	  
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ExceptionHandler(IllegalArgumentException.class)
	  @ResponseBody
	  public ErrorInfo illegal(IllegalArgumentException exception) {
            logger.info("----This is a ilegal request----");
            return new ErrorInfo(request.getDescription(false), exception,"IllegalArgumentException",HttpStatus.BAD_REQUEST.toString());
            
	  }
	  
	  @ResponseStatus(HttpStatus.NOT_FOUND)
	  @ExceptionHandler(KeyWordNotFoundException.class)
	  @ResponseBody
	  public ErrorInfo notFound(Exception exception) {
            logger.info("----Caught KeywordNotFoundException----");
            return new ErrorInfo(request.getDescription(false), exception,"KeyWordNotFoundException",HttpStatus.NOT_FOUND.toString());
            
	  }
	  
	  
	  @ResponseStatus(HttpStatus.NOT_FOUND)
	  @ExceptionHandler(ResourceNotFoundException.class)
	  @ResponseBody
	  public ErrorInfo resourceNotFound(ResourceNotFoundException exception) {
            logger.info("----Caught KeywordNotFoundException----");
            return new ErrorInfo(request.getDescription(false), exception,"ResourceNotFoundException",HttpStatus.NOT_FOUND.toString());
            
	  }
	  
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ExceptionHandler(IOException.class)
	  @ResponseBody
	  public ErrorInfo badRequest(Exception exception) {
            logger.info("----Caught IOException----");
            return new ErrorInfo(request.getDescription(false), exception,"IOException",HttpStatus.BAD_REQUEST.toString());
	  }
} 