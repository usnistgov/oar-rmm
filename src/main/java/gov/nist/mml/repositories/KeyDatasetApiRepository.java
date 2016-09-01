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

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import org.springframework.data.mongodb.core.query.TextCriteria;

import gov.nist.mml.domain.KeyDatasetApi;

import org.springframework.data.domain.Pageable;


@RepositoryRestResource
public interface KeyDatasetApiRepository extends MongoRepository<KeyDatasetApi, String> {
		
	//Generic Search
	List<KeyDatasetApi> findAllBy(TextCriteria criteria,Pageable pageable);
	
   List<KeyDatasetApi> findByNameContainingIgnoreCase(String name);
	
   
}

