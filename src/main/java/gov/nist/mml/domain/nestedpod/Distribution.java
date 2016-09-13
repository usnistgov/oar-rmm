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
import org.springframework.data.mongodb.core.index.TextIndexed;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Distribution{
	@Id private String id;
	@JsonProperty("@type")
	private @TextIndexed String type;
	private @TextIndexed String accessURL;
	private @TextIndexed String conformsTo;
	private @TextIndexed String describedBy; //url
	private @TextIndexed String describedByType;
	private @TextIndexed String description;
	private @TextIndexed String downloadURL;
	private @TextIndexed String mediaType;
	private @TextIndexed String format;
	private @TextIndexed String title;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getAccessURL(){
		return accessURL;
	}
	
	public void setAccessURL(String durl){
		accessURL = durl;
	}
	
	public String getConformsTo() {
		return conformsTo;
	}

	public void setConformsTo(String conformsTo) {
		this.conformsTo = conformsTo;
	}
	
	public String getDescribedBy() {
		return describedBy;
	}

	public void setDescribedBy(String describedBy) {
		this.describedBy = describedBy;
	}
	

	public String getDescribedByType() {
		return describedByType;
	}

	public void setDescribedByType(String describedByType) {
		this.describedByType = describedByType;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String desc){
		description = desc;
	}
	
	public String getdownloadURL(){
		return downloadURL;
	}
	
	public void setDownloadURL(String durl){
		downloadURL = durl;
	}
	
	public String getmediaType(){
		return mediaType;
	}
	
	public void setMediaType(String mtype){
		this.mediaType = mtype;
	}
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}