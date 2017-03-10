
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
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty; 

import gov.nist.mml.domain.nestedpod.Component;
import gov.nist.mml.domain.nestedpod.ContactPoint;
import gov.nist.mml.domain.nestedpod.DocumentReference;
import gov.nist.mml.domain.nestedpod.Identifier;
import gov.nist.mml.domain.nestedpod.Person;
import gov.nist.mml.domain.nestedpod.Publisher;
import gov.nist.mml.domain.nestedpod.ResourceReference;

//It is a dataset field in the Open data schema
@Document
public class Record {

	@Id private String id;
	@JsonProperty("@type")
    @TextIndexed private String[] type;
	@TextIndexed private String resId;
    @TextIndexed(weight=2) private String title;
    @TextIndexed(weight=3) private String[] description;
    @TextIndexed private String[] keyword;
    @TextIndexed private String modified; 
    @TextIndexed private Publisher publisher;
    @TextIndexed private Component[] components;
    @TextIndexed private ContactPoint contactPoint;
    @TextIndexed private String accessLevel;
    @TextIndexed private String[] bureauCode;
    @TextIndexed private String[] programCode;
    @TextIndexed private String license;
    @TextIndexed private String rights;
    @TextIndexed private String spatial;
    @TextIndexed private String temporal;
    @TextIndexed private String accrualPeriodicity;
    @TextIndexed private String conformsTo;
    @TextIndexed private boolean dataQuality;
    @TextIndexed private String describedBy; 
    @TextIndexed private String describedByType;
    @TextIndexed private ResourceReference isPartOf;
    @TextIndexed private String issued;
    @TextIndexed private String[] resLanguage;
    @TextIndexed private String landingPage;  
    @TextIndexed private String primaryITInvestmentUII;
    @TextIndexed private DocumentReference[] references;
    @TextIndexed private String systemOfRecords;
    @TextIndexed private String[] theme;
    @TextIndexed private String doi;
    @TextIndexed private String[] abbrev; 
    @TextIndexed private String[] subtitle;
    @TextIndexed private String[] aka;
    @TextIndexed private Person[] authors;
    @TextIndexed private String ediid;
    @TextIndexed private Identifier identifier;
    @TextIndexed private String recommendedCitation;
    @TextIndexed private String orcidPath;

    public String getID() {
      return id;
    }

    public void setID(String id) {
      this.id = id;
    }
    
    public String getResId() {
      return resId;
    }

    public void setResId(String resId) {
      this.resId = resId;
    }    
    
    public String[] getType() {
		return type;
	}

	public void setType(String[] type) {
		this.type = type;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String[] getDescription() {
		return description;
	}

	public void setDescription(String[] description ) {
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
	
	public Component[] getComponents() {
		return components;
	}
	
	public void setComponents(Component[] components) {
		this.components = components;
	}
	
	public ContactPoint getContactPoint() {
		return contactPoint;
	}
	
	public void setContactPoint(ContactPoint contactPoint) {
		this.contactPoint = contactPoint;
	}
	
	public Identifier getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(Identifier identifier) {
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

	public String getAccrualPeriodicity() {
		return this.accrualPeriodicity;
	}
	
	public void setAccrualPeriodicity(String accrualPeriodicity) {
		this.accrualPeriodicity =  accrualPeriodicity;
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

	public ResourceReference getIsPartOf() {
		return isPartOf;
	}

	public void setIsPartOf(ResourceReference isPartOf) {
		this.isPartOf = isPartOf;
	}
	
	public String getIssued() {
		return issued;
	}

	public void setIssued(String issued) {
		this.issued = issued;
	}
	
	public String[] getResLanguage() {
		return resLanguage;
	}
	
	public void setResLanguage(String[] resLanguage) {
		this.resLanguage = resLanguage;
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
	
	public DocumentReference[] getReferences() {
		return references;
	}
	
	public void setReferences(DocumentReference[] references) {
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
	
	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}
	
	public String[] getAbbrev() {
		return abbrev;
	}

	public void setAbbrev(String[] abbrev) {
		this.abbrev = abbrev;
	}

    public String[] getSubtitle() {
      return subtitle;
    }

    public void setSubtitle(String[] subtitle) {
      this.subtitle = subtitle;
    }
  
    public String[] getAka() {
      return aka;
    }

    public void setAka(String[] aka) {
      this.aka = aka;
    }  

    public Person[] getAuthors() {
      return authors;
    }

    public void setAuthors(Person[] authors) {
      this.authors = authors;
    }     
    
    public String getEdiid() {
      return ediid;
    }

    public void setEdiid(String ediid) {
      this.ediid = ediid;
    }      

    public String getRecommendedCitation() {
      return recommendedCitation;
    }

    public void setRecommendedCitation(String recommendedCitation) {
      this.recommendedCitation = recommendedCitation;
    }    
}
 
