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

public class DocumentReference{

    @JsonProperty("@type")
	private String type;
	private String location;
	private String label;
	private String citation;
	private String refid;
	private String refType;
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getLocation(){
		return location;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	public String getLabel(){
		return label;
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getCitation(){
		return citation;
	}
	
	public void setCitation(String citation){
		this.citation = citation;
	}
	
	public String getRefid(){
		return refid;
	}
	
	public void setRefid(String refid){
		this.refid = refid;
	}
	
	public String getRefType(){
		return refType;
	}
	
	public void setRefType(String refType){
		this.refType = refType;
	}
}
