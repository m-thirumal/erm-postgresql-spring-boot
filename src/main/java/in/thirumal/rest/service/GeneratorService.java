/**
 * 
 */
package in.thirumal.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.thirumal.config.TargetConfig;
import in.thirumal.model.Entity;
import in.thirumal.persistence.GenericDao;

/**
 * @author Thirumal
 *
 */
@Service
public class GeneratorService {

	@Autowired
	TargetConfig targetConfig;
	
	@Autowired
	private GenericDao<Entity> genericDao;
	
	public boolean list() {
		System.out.println(targetConfig.toString());
		genericDao.list("j");
		return true;
	}
	
}
