/**
 * 
 */
package in.thirumal.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.thirumal.config.TargetConfig;
import in.thirumal.model.Attribute;
import in.thirumal.model.Entity;
import in.thirumal.persistence.GenericDao;
import in.thirumal.render.BaseClassRender;
import in.thirumal.render.DaoClassRender;
import in.thirumal.render.ModelClassRender;
import in.thirumal.rest.GenericService;
import in.thirumal.utility.ERM2BeansHelper;
import in.thirumal.utility.PrepareStatementBuilder;
import in.thirumal.utility.PrepareStatementBuilder.Action;

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
		LOGGER.info("---Start---");
		List<Entity> entities = genericDao.list(databaseName, schemaName);
		LOGGER.info("Totoal size: {}" + entities.size());
		try {
			ERM2BeansHelper.createDirectory(targetConfig.getRootFolder());
		} catch (Exception e) {
			LOGGER.info("Impossible to create the root folder: "+targetConfig.getRootFolder());
		}	
		LOGGER.info("----Directory created----");
		writeModel(entities);
		LOGGER.info("Model generated successfully");
		writeDao(entities);
		LOGGER.info("DAO generated successfully");
		writeQueries(entities);
		LOGGER.info("Queries generated successfully");
		return true;
	}

	private void writeQueries(List<Entity> entities) {
		ERM2BeansHelper.clearQueries(targetConfig.getRootFolder());
		String 	query =	null;
		String targetDirectory = targetConfig.getRootFolder();
		for(Entity entity : entities) {
			try {
				ERM2BeansHelper.writeFile("#"+entity.getName() + "Dao", targetDirectory, "queries.properties", true);
				ArrayList<Attribute> attributes 		=	entity.getAlAttr();
				int pkSize = 0;
				for (Attribute attr: attributes) {
					if (attr.isPrimaryKey()) {
						pkSize++;
					}
				}
				/* Query generation */
				query = PrepareStatementBuilder.create(entity, Action.KEY);
				ERM2BeansHelper.addQueryInProp(targetDirectory, entity, Action.KEY, query);
				
				query = PrepareStatementBuilder.create(entity, Action.LIST);
				ERM2BeansHelper.addQueryInProp(targetDirectory, entity, Action.LIST, query);		
				if (pkSize == 2) {
					query = PrepareStatementBuilder.create(entity, Action.GETCK);
					ERM2BeansHelper.addQueryInProp(targetDirectory,	entity, Action.GETCK, query);
				}
				query = PrepareStatementBuilder.create(entity, Action.GET);
				ERM2BeansHelper.addQueryInProp(targetDirectory,	entity, Action.GET, query);
			
				query = PrepareStatementBuilder.create(entity, Action.CREATE);
				ERM2BeansHelper.addQueryInProp(targetDirectory, entity,	Action.CREATE, query);
				
				query = PrepareStatementBuilder.create(entity, Action.UPDATE);
				ERM2BeansHelper.addQueryInProp(targetDirectory, entity,	Action.UPDATE, query);
				
				query = PrepareStatementBuilder.create(entity, Action.DELETE);
				ERM2BeansHelper.addQueryInProp(targetDirectory, entity,	Action.DELETE, query);
			} catch (Exception e) {
				LOGGER.severe("Impossible to create query for " + entity.getName() + ". Exception message: " + e.getMessage());
			}
		}
		
	}

	private void writeDao(List<Entity> entities) {
		String fileName = null;
		String className =	null;
		BaseClassRender classRender	= null;
		String 	classContent = null;
		for(Entity entity : entities) {
			//Setting package from the Configuration
			entity.setDaoPackage(targetConfig.getPackageDao());
			entity.removeInterfaces();
			entity.addInterface("GenericDaoImp <" + entity.getName() + ", Identifier, String>");
			className = entity.getName();
			fileName = className + "Dao.java";
			classRender	= new DaoClassRender(entity);		
			try {
				classContent = classRender.render();
			} catch (Exception ex) {
				LOGGER.severe("Impossible to render the DAO " + className + ". Exception message: " + ex.getMessage());
			}
			try {
				ERM2BeansHelper.writeFile(classContent, targetConfig.getDaoFolder(), fileName, false);
			} catch (Exception ex) {
				LOGGER.severe("Impossible to create the DAO " + className + ". Exception message: " + ex.getMessage());
			}
		}
	}

	private void writeModel(List<Entity> entities) {
		String fileName = null;
		String className =	null;
		BaseClassRender classRender	= null;
		String 	classContent = null;
		/* Writing model*/
		for(Entity entity : entities){			
			//Setting package from the Configuration
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
				ERM2BeansHelper.writeFile(classContent, targetConfig.getModelFolder(), fileName, false);
			} catch (Exception ex) {
				LOGGER.severe("Impossible to create the Model "+ className +". Exception message: " + ex.getMessage());
			}			
		}
	}
	
}
