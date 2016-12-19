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
package gov.nist.mml.domain.nestedpod;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactPoint{
	@JsonProperty("@type")
	private String type;
	private String hasEmail;
	private String fn;
	private String[] postalAddress;
	private String phoneNumber;
	private String timezone;
	private String proxyFor;
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getHasEmail(){
		return hasEmail;
	}
	
	public void sethasEmail(String hasEmail){
		this.hasEmail = hasEmail;
	}

    public String getfn(){
      return fn;
    }
  
    public void setfn(String fn){
      this.fn = fn;
    }
  	
	public String[] getPostalAddress(){
		return postalAddress;
	}
	
	public void setPostalAddress(String[] postalAddress){
		this.postalAddress = postalAddress;
	}
	
	public String getPhoneNumber(){
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	
    public String getTimezone(){
      return timezone;
    }
  
    public void setTimezone(String timezone){
      this.timezone = timezone;
    }	
    
    public String getProxyFor(){
      return proxyFor;
    }
  
    public void setProxyFor(String proxyFor){
      this.proxyFor = proxyFor;
    }       
}
