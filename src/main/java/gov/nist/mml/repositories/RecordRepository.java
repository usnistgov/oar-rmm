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
package gov.nist.mml.repositories;


import java.util.List;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import org.springframework.data.mongodb.core.query.TextCriteria;

import gov.nist.mml.domain.Record;
import gov.nist.mml.domain.nestedpod.ContactPoint;
import gov.nist.mml.domain.nestedpod.Distribution;
import gov.nist.mml.domain.nestedpod.Publisher;


@RepositoryRestResource
public interface RecordRepository extends MongoRepository<Record, String> {
	
	///These are HATEOS format serach api
	List<Record> findByTitle(@Param("title") String title);
	List<Record> findByType(@Param("type") String name);
	List<Record> findByKeyword(@Param("keyword") String[] keyword);
	List<Record> findByProgramCode(@Param("type") String[] programCode);
	List<Record> findByLandingPage(@Param("landingPage") String landingPage);
	List<Record> findByAccessLevel(@Param("accessLevel") String accessLevel);
	List<Record> findByDistribution(@Param("distribution") Distribution[] distribution);
	List<Record> findByModified(@Param("modified") String modified);
	List<Record> findByPublisher(@Param("publisher") Publisher publisher);
	List<Record> findByReferences(@Param("references") String[] references);
	List<Record> findByBureauCode(@Param("bureau") String[] bureau);
	List<Record> findByDescription(@Param("description") String description);
	List<Record> findByContactPoint(@Param("contactPoint") ContactPoint contactpoint);
	List<Record> findByLanguage(@Param("language") String[] language);
	List<Record> findByLicense(@Param("licence") String licence);
	
	//Generic Search
	List<Record> findAllBy(TextCriteria criteria);
    List<Record> findTitleTypeBy(TextCriteria criteria); // Need to do more research for multiple field search

    //Extra methods to create more advanced search
    //These are called from SearchController
	List<Record> findByTitleContaining(String title);
	
   
    List<Record> findByTypeContaining(String type);
    List<Record> findByKeywordContaining(String keyword);
	List<Record> findByProgramCodeContaining(String programCode);
	List<Record> findByLandingPageContaining(String landingpage);
	List<Record> findByAccessLevelContaining(String accessLevel);
	List<Record> findByDistributionContaining(String distribution);
	List<Record> findByModifiedContaining(String modified);
	List<Record> findByPublisherContaining(String publisher);
	List<Record> findByReferencesContaining(String references);
	List<Record> findByBureauCodeContaining(String bureau);
	List<Record> findByDescriptionContaining(String dscription);
	List<Record> findByContactPointContaining(String contactPoint);
	List<Record> findByLanguageContaining(String language);
	List<Record> findByLicenseContaining(String license);
   
}

