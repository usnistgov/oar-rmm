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

import com.fasterxml.jackson.annotation.JsonProperty;

public class Component{
	@JsonProperty("@type")
	private String[] type;
	private String description;
	private String conformsTo;
	private String downloadURL;
    private String mediaType;
    private String format;
    private String describedBy;
    private String describedByType;
    private String[] resType;
    private String proxyFor;
    private String title;
  
    
	public String[] getType(){
		return type;
	}
	
	public void setType(String[] type){
		this.type = type;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getconformsTo(){
		return conformsTo;
	}
	
	public void setConformsTo(String conformsTo){
		this.conformsTo = conformsTo;
	}
	
    public String getDownloadURL(){
      return downloadURL;
    }
  
    public void setDownloadURL(String downloadURL){
      this.downloadURL = downloadURL;
    }
  
    public String getMediaType(){
      return mediaType;
    }
  
    public void setMediaType(String mediaType){
      this.mediaType = mediaType;
    }
    
    public String getFormat(){
      return format;
    }
  
    public void setFormat(String format){
      this.format = format;
    }
    
    public String getDescribedBy(){
      return describedBy;
    }
  
    public void setDescribedBy(String describedBy){
      this.describedBy = describedBy;
    }
    
    public String getDescribedByType(){
      return describedByType;
    }
  
    public void setDescribedByType(String describedByType){
      this.describedByType = describedByType;
    }    

    public String getProxyFor(){
      return proxyFor;
    }
  
    public void setProxyFor(String proxyFor){
      this.proxyFor = proxyFor;
    }
    
    public String[] getResType(){
      return resType;
    }
  
    public void setResType(String[] resType){
      this.resType = resType;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }    

}
