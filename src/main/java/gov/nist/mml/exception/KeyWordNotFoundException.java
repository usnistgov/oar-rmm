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
package gov.nist.mml.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author dsn1
 *
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Resource Not Found") //404
public class KeyWordNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String requestUrl = "";
	public KeyWordNotFoundException(int id){
		super("EmployeeNotFoundException with id="+id);
	}
	public KeyWordNotFoundException(){
		super("Keywords you  are looking for are not available.");
	}
	public KeyWordNotFoundException(String requestUrl){
		
		super("Keywords not available for given Request."+requestUrl );
		this.setRequestUrl(requestUrl);
	}
	
	public String getRequestUrl(){
		return this.requestUrl;
	}
	public void setRequestUrl(String url){
		this.requestUrl = url;
	}
	
}
