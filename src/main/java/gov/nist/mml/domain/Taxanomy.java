
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
import gov.nist.mml.domain.nestedpod.taxanomy.subCategory;


public class Taxanomy {

	@Id private String id;
	private @TextIndexed String keyIdentifier;
	private @TextIndexed String researchCategory;
	private @TextIndexed subCategory[] subCategories;

	
	public String getresearchCategory() {
		return researchCategory;
	}
	public void setresearchCategory(String researchCategory) {
		this.researchCategory = researchCategory;
	}
	
	
	public subCategory[] getsubCategories() {
		return this.subCategories;
	}
	public void setSubCategories(subCategory[] subcategories) {
		this.subCategories = subcategories;
	}
	
	
	public String getkeyIdentifier() {
		return keyIdentifier;
	}
	public void setkeyIdentifier(String keyIdentifier) {
		this.keyIdentifier = keyIdentifier;
	}
}

