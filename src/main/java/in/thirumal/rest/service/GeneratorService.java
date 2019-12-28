/**
 * 
 */
package in.thirumal.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.thirumal.config.TargetConfig;
import in.thirumal.model.Entity;
import in.thirumal.persistence.GenericDao;
import in.thirumal.rest.GenericService;

/**
 * @author Thirumal
 *
 */
@Service
public class GeneratorService implements GenericService {

	@Autowired
	TargetConfig targetConfig;
	
	@Autowired
	private GenericDao<Entity> genericDao;
	
	@Override
	public boolean generate(String schemaName) {
		System.out.println(targetConfig.toString());
		genericDao.list("j");
		return true;
	}
	
}
