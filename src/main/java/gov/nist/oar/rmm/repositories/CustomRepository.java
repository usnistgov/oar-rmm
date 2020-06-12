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
package gov.nist.oar.rmm.repositories;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.data.domain.Pageable;

/**
 * CustomRepository is developed to identify functionality provided by search
 * API. These include differet requests to search database and extract data.
 * 
 * @author Deoyani Nandrekar-Heinis
 *
 */
public interface CustomRepository {

	/**
	 * Return the list of records found in database based on given criteria or
	 * request parameters. Pageable parameter accepts request for number of records
	 * per page and sorting order.
	 * 
	 * @param param request criteria/parameters
	 * @param p     Pageable request parameters
	 * @return List of Documents in JSON format
	 */
	public List<Document> find(Map<String, String> param, Pageable p);

	/**
	 * Search record with requested parameters without any paging requests.
	 * 
	 * @param param search criteria
	 * @return Document a JSON format with identified record/s
	 */
	public Document find(Map<String, String> param);

	/**
	 * Returns a list of Taxonomy terms used in the records in the form of flat JSON
	 * file
	 * 
	 * @param param requested parameters
	 * @return List of Documents/records in JSON format
	 */
	public List<Document> findtaxonomy(Map<String, String> param);

	/**
	 * Return the list of taxonomy document
	 * 
	 * @return
	 */
	public List<Document> findtaxonomy();

	/**
	 * Get list of resource APIs available
	 * 
	 * @return
	 */
	public List<Document> findResourceApis();

	/**
	 * Search record for given identifier
	 * 
	 * @param id
	 * @return Document in JSON format
	 */
	public Document findRecord(String id);

	/**
	 * Get the fields or keywords available in database
	 * 
	 * @return
	 */
	public List<Document> findFieldnames();

}
