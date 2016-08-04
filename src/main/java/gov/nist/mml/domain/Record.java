
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
package gov.nist.mml.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;


import com.fasterxml.jackson.annotation.JsonProperty;

import gov.nist.mml.domain.nestedpod.ContactPoint;
import gov.nist.mml.domain.nestedpod.Distribution;
import gov.nist.mml.domain.nestedpod.Publisher;


public class Record {

//@Id private String id;

	@JsonProperty("@type")
	private @TextIndexed String type;
    private @TextIndexed String[] keyword;
    private @TextIndexed String[] programCode;
	private @TextIndexed String landingPage;
	private @TextIndexed String accessLevel;
	private @TextIndexed Distribution[] distribution;
	private @TextIndexed String modified;
	private @TextIndexed Publisher publisher;
	private @TextIndexed String[] references;
	private @TextIndexed String[] bureauCode;
	private @TextIndexed(weight=2) String title;
	private @TextIndexed(weight=3)String description;
	private @TextIndexed ContactPoint contactPoint;
	private @TextIndexed String[] language;
	private @TextIndexed String license;
	@Id private @TextIndexed String identifier;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String[] getKeyword() {
		return keyword;
	}

	public void setKeyword(String[] keyword) {
		this.keyword = keyword;
	}
	
	
	public String[] getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String[] programCode) {
		this.programCode = programCode;
	}
	
	
	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}
	
	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevele(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	
	public Distribution[] getDistribution() {
		return distribution;
	}
	public void setDistribution(Distribution[] distribution) {
		this.distribution = distribution;
	}
	
	
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	
	
	public Publisher getPublisher() {
		return publisher;
	}
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	
	
	public String[] getReferences() {
		return references;
	}
	public void setReferences(String[] references) {
		this.references = references;
	}
	
	
	public String[] getBureauCode() {
		return bureauCode;
	}
	public void setBureauCode(String[] bureauCode) {
		this.bureauCode = bureauCode;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public ContactPoint getContactPoint() {
		return contactPoint;
	}
	public void setContactPoint(ContactPoint contactPoint) {
		this.contactPoint = contactPoint;
	}
	
	public String[] getLanguage() {
		return language;
	}
	public void setLanguage(String[] language) {
		this.language = language;
	}
	
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	
}

