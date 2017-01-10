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
 * @author: Som Kolli
 */
package gov.nist.mml.domain.nestedpod;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResourceReference{

    private String title;
	private String proxyFor;
	private String[] resourceType;

  	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getProxyFor(){
		return proxyFor;
	}
	
	public void setProxyFor(String proxyFor){
		this.proxyFor = proxyFor;
	}
	
	public String[] getResourceType(){
		return resourceType;
	}
	
	public void setResourceType(String[] resourceType){
		this.resourceType = resourceType;
	}
}
