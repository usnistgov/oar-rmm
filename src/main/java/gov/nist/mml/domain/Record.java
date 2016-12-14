
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

//It is a dataset field in the Open data schema
public class Record {

	@Id private String id;

	@JsonProperty("@type")
	@TextIndexed private  String type;
	@TextIndexed(weight=2) private  String title;
	@TextIndexed(weight=3) private String description;
	@TextIndexed private  String[] keyword;
	@TextIndexed private  String modified;
	@TextIndexed private  Publisher publisher;
	@TextIndexed private  ContactPoint contactPoint;
	@TextIndexed private String identifier;
	@TextIndexed private String accessLevel;
	@TextIndexed private String[] bureauCode;
	@TextIndexed private String[] programCode;
	@TextIndexed private String license;
	@TextIndexed private String rights;
	@TextIndexed private String spatial;
	@TextIndexed private String temporal;
	@TextIndexed private Distribution[] distribution;
	@TextIndexed private String accuralPeriodicity;
	@TextIndexed private  String conformsTo;
	@TextIndexed private  boolean dataQuality;
	@TextIndexed  private String describedBy; //url
	@TextIndexed private  String describedByType;
	@TextIndexed private String isPartOf;
	@TextIndexed private String issued;
	@TextIndexed private String[] language;
	@TextIndexed private String landingPage;
	@TextIndexed private String primaryITInvestmentUII;
	@TextIndexed private String[] references;
	@TextIndexed private String systemOfRecords;
	@TextIndexed private String[] theme;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description ) {
		this.description = description;
	}
	
	public String[] getKeyword() {
		return keyword;
	}

	public void setKeyword(String[] keyword) {
		this.keyword = keyword;
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
	
	public ContactPoint getContactPoint() {
		return contactPoint;
	}
	public void setContactPoint(ContactPoint contactPoint) {
		this.contactPoint = contactPoint;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	public String[] getBureauCode() {
		return bureauCode;
	}
	public void setBureauCode(String[] bureauCode) {
		this.bureauCode = bureauCode;
	}
	public String[] getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String[] programCode) {
		this.programCode = programCode;
	}

	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	
	public String getRights() {
		return rights;
	}
	public void setRights(String rights) {
		this.rights = rights;
	}
	public String getSpatial() {
		return spatial;
	}
	public void setSpatial(String spatial) {
		this.spatial = spatial;
	}
	public String getTemporal() {
		return temporal;
	}
	public void setTemporal(String temporal) {
		this.temporal = temporal;
	}
	public Distribution[] getDistribution() {
		return distribution;
	}
	public void setDistribution(Distribution[] distribution) {
		this.distribution = distribution;
	}
	
	public String getAccuralPeriodicity() {
		return this.accuralPeriodicity;
	}
	public void setAccuralPeriodicity(String accuralPeriodicity) {
		this.accuralPeriodicity =  accuralPeriodicity;
	}
	
	public String getConformsTo() {
		return conformsTo;
	}

	public void setConformsTo(String conformsTo) {
		this.conformsTo = conformsTo;
	}
	
	public boolean getDataQuality() {
		return dataQuality;
	}

	public void setDataQuality(boolean dataQuality) {
		this.dataQuality = dataQuality;
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

	
	public String getIsPartOf() {
		return isPartOf;
	}

	public void setIsPartOf(String isPartOf) {
		this.isPartOf = isPartOf;
	}
	
	public String getIssued() {
		return issued;
	}

	public void setIssued(String issued) {
		this.issued = issued;
	}
	
	public String[] getLanguage() {
		return language;
	}
	public void setLanguage(String[] language) {
		this.language = language;
	}
	
	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}
	public String getPrimaryITInvestmentUII() {
		return primaryITInvestmentUII;
	}

	public void setPrimaryITInvestmentUII(String primaryITInvestmentUII) {
		this.primaryITInvestmentUII = primaryITInvestmentUII;
	}
	
	public String[] getReferences() {
		return references;
	}
	public void setReferences(String[] references) {
		this.references = references;
	}
	
	public String getSystemOfRecords() {
		return primaryITInvestmentUII;
	}

	public void setSystemOfRecords(String systemOfRecords) {
		this.systemOfRecords = systemOfRecords;
	}
	
	public String[] getTheme() {
		return theme;
	}

	public void setTheme(String[] theme) {
		this.theme = theme;
	}
}

