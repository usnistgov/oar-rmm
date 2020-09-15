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
package gov.nist.oar.rmm.unit.repositories;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;


public interface CustomRepositoryTest {

	public List<Document> find(Map<String,String> param,Pageable p);
	public Document find(MultiValueMap<String,String> param);
	public List<Document> findtaxonomy(Map<String,String> param);
	public List<Document> findtaxonomy();
	public List<Document> findResourceApis();
	public Document findRecord(String id);
	public List<Document> findFieldnames();

}
