/**
 * 
 */
package in.thirumal.rest.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.thirumal.config.TargetConfig;
import in.thirumal.model.Entity;
import in.thirumal.persistence.GenericDao;
import in.thirumal.render.ModelClassRender;
import in.thirumal.rest.GenericService;
import in.thirumal.utility.ERM2BeansHelper;

/**
 * @author Thirumal
 *
 */
@Service
public class GeneratorService implements GenericService {

	private	static	final	Logger	LOGGER		=	Logger.getLogger(GeneratorService.class.getName());
	
	@Autowired
	TargetConfig targetConfig;
	
	@Autowired
	private GenericDao<Entity> genericDao;
	
	@Override
	public boolean generate(String databaseName, String schemaName) {
		List<Entity> entities = genericDao.list(databaseName, schemaName);
		LOGGER.info("Totoal size: " + entities.size());
		try {
			ERM2BeansHelper.createDirectory(targetConfig.getRootFolder());
		} catch (Exception e) {
			LOGGER.info("Impossible to create the root folder: "+targetConfig.getRootFolder());
		}	
		writeModel(entities);
		return true;
	}

	private void writeModel(List<Entity> entities) {
		String fileName = null;
		String className =	null;
		in.thirumal.render.BaseClassRender classRender	= null;
		String 	classContent = null;
		/* Writing model*/
		for(Entity entity : entities){			
			//Setting pckg from the Configuration
			entity.setModelPackage(targetConfig.getPackageModel());			
			className =	entity.getName();
			fileName = className + ".java";
			classRender	= new ModelClassRender(entity);
			try {
				classContent = classRender.render();
			} catch (Exception ex) {
				LOGGER.severe(entity.getRawName() + "Model is not written: " + ex.getMessage());
			}
			try {
				//LOGGER.info("Create model " + className + ". Target path: " + targetDirectory + File.separator + fileName);
				ERM2BeansHelper.writeFile(classContent, targetConfig.getModelFolder(), fileName, false);
			} catch (Exception ex) {
				LOGGER.severe("Impossible to create the Model "+ className +". Exception message: " + ex.getMessage());
			}
			
		}
	}
	
}
