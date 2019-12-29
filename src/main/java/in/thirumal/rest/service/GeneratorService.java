/**
 * 
 */
package in.thirumal.rest.service;

import java.util.List;

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
	public boolean generate(String databaseName, String schemaName) {
		System.out.println(targetConfig.toString());
		List<Entity> entities = genericDao.list(databaseName, schemaName);
		entities.stream().forEach(System.out::println);
		return true;
	}
	
}
