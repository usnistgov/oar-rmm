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

public class Person {
	@JsonProperty("@type")
	private String[] type;
	private String fn;
	private String givenName;
	private String familyName;
    private String middleName;
    private String orcid;
    private ResourceReference[] affiliation;
    private String proxyFor;

	public String[] getType(){
		return type;
	}
	
	public void setType(String[] type){
		this.type = type;
	}
	
	public String getfn(){
		return fn;
	}
	
	public void setfn(String fn){
		this.fn = fn;
	}
	
	public String getGivenName(){
		return givenName;
	}
	
	public void setGivenName(String givenName){
		this.givenName = givenName;
	}
	
    public String getFamilyName(){
      return familyName;
    }
  
    public void setFamilyName(String familyName){
      this.familyName = familyName;
    }	
    
    public String getMiddleName(){
      return middleName;
    }
  
    public void setMiddleName(String middleName){
      this.middleName = middleName;
    }    
    
    public String getOrcid(){
      return orcid;
    }
  
    public void setOrcid(String orcid){
      this.orcid = orcid;
    } 
    
    public ResourceReference[] getAffiliation(){
      return affiliation;
    }
  
    public void setAffiliation(ResourceReference[] affiliation){
      this.affiliation = affiliation;
    }  
    
    public String getProxyFor(){
      return proxyFor;
    }
  
    public void setProxyFor(String proxyFor){
      this.proxyFor = proxyFor;
    }      
}
