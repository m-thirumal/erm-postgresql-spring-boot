package in.thirumal.render;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import in.thirumal.model.Attribute;
import in.thirumal.model.Entity;
import in.thirumal.utility.DbHelper;
import in.thirumal.utility.ERM2BeansHelper.StringHelper;
/**
 * 
 * @author thirumal
 *
 */
public class DaoClassRender extends BaseClassRender {
	
	private ArrayList<String> mandatoryPckgs; 
	
	public DaoClassRender(Entity entity) {
		super(entity);
		mandatoryPckgs = new ArrayList<>(); 		
		init();		
	}	
	
	private void init(){
		addMandatoryPackage("java.sql.Connection");
		addMandatoryPackage("java.sql.PreparedStatement");
		addMandatoryPackage("java.sql.ResultSet");		
		addMandatoryPackage("java.sql.SQLException");
		addMandatoryPackage("java.util.List");
		addMandatoryPackage("java.util.Optional");
		addMandatoryPackage("org.springframework.beans.factory.annotation.Autowired");
		addMandatoryPackage("org.springframework.core.env.Environment");
		addMandatoryPackage("org.springframework.jdbc.core.JdbcTemplate");
		addMandatoryPackage("org.springframework.jdbc.core.PreparedStatementCreator");
		addMandatoryPackage("org.springframework.jdbc.core.RowMapper");
		addMandatoryPackage("org.springframework.dao.EmptyResultDataAccessException");
		addMandatoryPackage("org.springframework.jdbc.support.GeneratedKeyHolder");
		addMandatoryPackage("org.springframework.jdbc.support.KeyHolder");
		addMandatoryPackage("org.springframework.stereotype.Repository");
		addMandatoryPackage("com.enkindle.persistence.GenericDao");
	}
	
	public void addMandatoryPackage(String classCanonicalName){
		mandatoryPckgs.add("import " + classCanonicalName + ";");
	}
	

	@Override
	public String render() throws Exception {
		StringBuffer 		output				=	new StringBuffer();
		Attribute 			attribut			=	null;
		ArrayList<Attribute> attributes 		=	getEntity().getAlAttr();
		String 				classNameLowerCase	=	getEntity().getName().toLowerCase();
		String 				methodName 			= 	null;
		String 				className 			= 	getEntity().getName() + "Dao";
		String 				preparementSet 		= 	null;
		String 				interfacesToOuput 	= 	null;
		String 				modelFileName 		= 	getEntity().getName();
		String				lineSeparator		=	StringHelper.lineSeparator;
		String				tabulation			=	StringHelper.tabulation;
		String				pkInJavaType	    =	null;
		String				pkInRaw	    =	null;
		String				ignoreRowCreationDate =   "rowCreationDate";
		String              ignoreRowCreatedBy = "rowCreatedBy";
		       		
		for (Attribute attr : attributes){
			if(attr.isPrimaryKey()){
				pkInJavaType = attr.getName();
				pkInRaw = attr.getRawName();
				break;
			}
		}
		
		int pkSize = 0;
		List<String> pkAttributes = new ArrayList<>();
		for (Attribute attr: attributes) {
			if (attr.isPrimaryKey()) {
				pkSize++;
				pkAttributes.add(attr.getRawName());
			}
		}
		output.append("package "+getEntity().getDaoPackage()+";"+lineSeparator);
		output.append(lineSeparator);
		output.append("import com.enkindle.persistence.model." + modelFileName + ";" + lineSeparator);
		output.append("import com.enkindle.persistence.model.shared.Identifier;" + lineSeparator);
		if (pkSize == 2) {
			output.append("import java.sql.Statement;" + lineSeparator);
		}
		for(String pckg : mandatoryPckgs){
			output.append(pckg+lineSeparator);
		}
		output.append(lineSeparator);
		if (getEntity().hasInterface()) {
			interfacesToOuput = " implements ";
			String interfaceToOuput = null;
			ArrayList<String> interFaces = getEntity().getInterfaces();
			for (int i = 0, interfaceCanonicalNamesLenght = interFaces.size(); i < interfaceCanonicalNamesLenght; i++) {
				interfaceToOuput = interFaces.get(i);
				interfacesToOuput += interfaceToOuput + (i == (interfaceCanonicalNamesLenght - 1) ? " ": ", ");
			}
		}

		output.append("/**\r\n" + 
     			" * Generated using erm-postgresql-spring-boot project\r\n"+
     			" * @see <a href=\"https://github.com/M-Thirumal/erm-postgresql-spring-boot\">erm-postgresql-spring-boot</a>\r\n" +
     			" * @author erm-postgresql-spring-boot\r\n" + 
     			" * @since "+ LocalDate.now() +"\r\n" + 
     			" * @version 1.0\r\n" + 
     			" */" + lineSeparator);
		output.append("@Repository" + lineSeparator);
		output.append("public class " + className
				+ (getEntity().hasParent() ? " extends " + getEntity().getParentClass() : "")
				+ (interfacesToOuput != null ? interfacesToOuput : "") + " {"
				+ lineSeparator + lineSeparator);
		output.append(tabulation + "private final JdbcTemplate jdbcTemplate;" + lineSeparator);
		output.append(tabulation + "private Environment environment;" + lineSeparator + lineSeparator + tabulation + "@Autowired" +
				lineSeparator);
		// Constructor
		output.append(tabulation+"public " + className + "(JdbcTemplate jdbcTemplate, Environment environment) {" + lineSeparator);
		output.append(tabulation+tabulation+"this.jdbcTemplate = jdbcTemplate;" + lineSeparator);
		output.append(tabulation+tabulation+"this.environment = environment;" + lineSeparator);
		output.append(tabulation+"}" + lineSeparator + lineSeparator);
		
		/* Create */
		output.append(tabulation+"@Override" + lineSeparator);
		output.append(tabulation+"public " + modelFileName + " create(" + modelFileName  + " " + classNameLowerCase + ", Identifier identifier) { "+ lineSeparator);
		/*if (pkSize == 2) {
			output.append(tabulation + tabulation + "return get(Integer.parseInt(holder.getKeys().get(\"" + pkAttributes.get(0) +
				"\").toString()), Integer.parseInt(holder.getKeys().get(\""+ pkAttributes.get(1) + "\").toString()));" +  lineSeparator);
			output.append(tabulation + "}" + lineSeparator + lineSeparator );
			//output.append(tabulation + "public " + modelFileName + " get(Integer " + pkAttributes.get(0) + ", Integer " + pkAttributes.get(1) + ") {" + lineSeparator);
			//	output.append(tabulation + tabulation + "return jdbcTemplate.queryForObject(environment.getProperty(\"" + modelFileName +
				//		".getck\"), new Object[] { " + pkAttributes.get(0) + ", " + pkAttributes.get(1) + 
					//	" }, new " + modelFileName + "RowMapper());" + lineSeparator + tabulation + "}" + lineSeparator + lineSeparator);
		} else {*/
		output.append(tabulation + tabulation + "return createV1(" + modelFileName + " " + classNameLowerCase + ", identifier).orElse(null);" + lineSeparator);
		output.append(tabulation + "}" + lineSeparator + lineSeparator );
		/* Create V1*/
		output.append(tabulation+"@Override" + lineSeparator);
		output.append(tabulation+"public Optional<" + modelFileName + "> createV1(" + modelFileName  + " " + classNameLowerCase + ", Identifier identifier) { "+ lineSeparator);
		output.append(tabulation + tabulation + "return getV1(new Identifier(insert(" + classNameLowerCase + ", identifier)" + ", identifier.getLocaleCd()));" + lineSeparator);
		output.append(tabulation + "}" + lineSeparator + lineSeparator );
		/*Insert */
		output.append(tabulation+"@Override" + lineSeparator);
		output.append(tabulation+"public Long " + "insert(" + modelFileName  + " " + classNameLowerCase + ", Identifier identifier) {"+ lineSeparator);
		output.append(tabulation + tabulation + "KeyHolder holder = new GeneratedKeyHolder();" + lineSeparator +
				tabulation + tabulation + "jdbcTemplate.update(new PreparedStatementCreator()  {" + lineSeparator +
				tabulation + tabulation + tabulation + "@Override" + lineSeparator +
				tabulation + tabulation + tabulation + "public PreparedStatement createPreparedStatement(Connection con) throws SQLException {" + lineSeparator +
				tabulation + tabulation + tabulation + "PreparedStatement ps = con.prepareStatement(environment.getProperty(\"" + 
				modelFileName + ".create\"), "); 
		if (pkSize == 2) {
			output.append("Statement.RETURN_GENERATED_KEYS);;" + lineSeparator);
		} else {
			output.append("new String[] { \"" + pkInRaw + "\" });" + lineSeparator);
		}
		int psIndex = 0;
		for (int i = 0, attributesLenght = attributes.size(); i < attributesLenght; i++) {
			attribut = attributes.get(i);
			if (attribut.getName().equalsIgnoreCase(ignoreRowCreationDate) || attribut.getSqlType().equalsIgnoreCase("uuid")) {
				continue;
			}
			if (pkSize == 2 || !attribut.isPrimaryKey() || !attribut.isAutoincrement()) {
				psIndex++;
				if (!attribut.getJavaType().equalsIgnoreCase("Boolean")) {
					methodName = StringHelper.saniziteForClassName(attribut.getName());
					methodName = "get" + methodName;
				} else {
					methodName = StringHelper.getMethodNameForBoolean(StringHelper.sanitizeForAttributName(attribut.getName()));
				}
				methodName += "()";
				preparementSet = DbHelper.createPreparementSet("ps", (psIndex),
						attribut.getJavaType(), attribut.getSqlType(), classNameLowerCase + "."	+ methodName, true);
				output.append(tabulation+tabulation+ tabulation + tabulation + preparementSet);
			}

		}
		output.append(tabulation+tabulation+ tabulation + tabulation +"return ps;" + lineSeparator);
		output.append(tabulation+tabulation+ tabulation + "}" + lineSeparator);
		output.append(tabulation+tabulation+  "}, holder);" + lineSeparator);
		output.append(tabulation + tabulation + "return holder.getKey().longValue();" + lineSeparator);
		output.append(tabulation + "}" + lineSeparator + lineSeparator );
			
		/* Get Method */
		output.append(tabulation + "@Override" + lineSeparator);
		output.append(tabulation+"public "+ modelFileName + " get(Identifier identifier) {" +  lineSeparator);
		output.append(tabulation + tabulation + "return getV1(identifier).orElse(null);" + lineSeparator);
		output.append(tabulation + "}" + lineSeparator + lineSeparator);
		
		/*Get Optional */
		output.append(tabulation + "@Override" + lineSeparator);
		output.append(tabulation+"public Optional<"+ modelFileName + "> getV1(Identifier identifier) {" +  lineSeparator);
		output.append(tabulation + tabulation + "try {" + lineSeparator);
		
		output.append(tabulation + tabulation + tabulation + "return Optional.of(jdbcTemplate.queryForObject(environment.getProperty(\"" + modelFileName + ".get\"), new Object[] {"
				+ lineSeparator + tabulation + tabulation + tabulation + tabulation + "identifier.getLocaleCd()," 
				+ lineSeparator + tabulation + tabulation + tabulation + tabulation + "identifier.getId()"
				+ lineSeparator + tabulation + tabulation + tabulation + "}, " +
				classNameLowerCase + "RowMapper()));" + lineSeparator );
		output.append(tabulation + tabulation + "} catch (EmptyResultDataAccessException e) {" + lineSeparator + tabulation + tabulation + 
				tabulation + "return Optional.empty();" + lineSeparator + tabulation + tabulation + "}" + lineSeparator);
		output.append(tabulation + "}" + lineSeparator + lineSeparator);
		/* Get where method */
		output.append(tabulation + "@Override" + lineSeparator);
		output.append(tabulation+"public "+ modelFileName + " get(Identifier identifier, String whereClause) {" +  lineSeparator);
		output.append(tabulation  + tabulation + "return return getV1(identifier, whereClause).orElse(null);" + lineSeparator);
		output.append(tabulation + "}" + lineSeparator + lineSeparator);
		/* GetV1 where Optional method */
		output.append(tabulation + "@Override" + lineSeparator);
		output.append(tabulation+"public Optional<"+ modelFileName + "> getV1(Identifier identifier, String whereClause) {" +  lineSeparator);
		output.append(tabulation + tabulation + "try {" + lineSeparator);
		output.append(tabulation + tabulation + tabulation + "return jdbcTemplate.queryForObject(environment.getProperty(\"" + modelFileName + ".getBy\" + whereClause" +
				"), new Object[] { "
				+ lineSeparator + tabulation + tabulation + tabulation + tabulation + "identifier.getLocaleCd()," 
				+ lineSeparator + tabulation + tabulation + tabulation + tabulation + "identifier.getId()"
				+ lineSeparator + tabulation + tabulation + tabulation + "}, " + classNameLowerCase + "RowMapper);" + lineSeparator );
		output.append(tabulation + tabulation + "} catch (EmptyResultDataAccessException e) {" + lineSeparator + tabulation + tabulation + 
				tabulation + "return Optional.empty();" + lineSeparator + tabulation + tabulation + "}" + lineSeparator);
		output.append(tabulation + "}" + lineSeparator + lineSeparator);

		/* List method*/
		output.append(tabulation + "@Override" + lineSeparator);
		output.append(tabulation+"public List<"+ modelFileName + "> list(Identifier identifier) {" +  lineSeparator);
		output.append(tabulation + tabulation + "try {" + lineSeparator);
		output.append(tabulation + tabulation + tabulation + "return jdbcTemplate.query(environment.getProperty(\"" + modelFileName + ".list\""+ 
				"), new Object[] { "
				+ lineSeparator + tabulation + tabulation + tabulation + tabulation + "identifier.getLocaleCd()," 
				+ lineSeparator + tabulation + tabulation + tabulation + tabulation + "identifier.getId()"
				+ lineSeparator + tabulation + tabulation + tabulation + " }, " + classNameLowerCase + "RowMapper);" + lineSeparator );
		output.append(tabulation + tabulation + "} catch (Exception e) {" + lineSeparator + tabulation + tabulation + 
				tabulation + "throw new IcmsException(ErrorFactory.DATABASE_EXCEPTION, e.getMessage());" + lineSeparator + tabulation + tabulation + "}" + lineSeparator);
		output.append(tabulation + "}" + lineSeparator + lineSeparator);
		/* LIST METHOD*/
		output.append(tabulation + "@Override" + lineSeparator);
		output.append(tabulation+"public List<"+ modelFileName + "> list(Identifier identifier, String whereClause) {" +  lineSeparator);
		output.append(tabulation + tabulation + "try {" + lineSeparator);
		output.append(tabulation + tabulation + tabulation + "return jdbcTemplate.query(environment.getProperty(\"" + modelFileName + ".listBy\" + whereClause" +
				"), new Object[] { "
				+ lineSeparator + tabulation + tabulation + tabulation + tabulation + "identifier.getLocaleCd()," 
				+ lineSeparator + tabulation + tabulation + tabulation + tabulation + "identifier.getId()"
				+ lineSeparator + tabulation + tabulation + tabulation + " }, " + classNameLowerCase + "RowMapper);" + lineSeparator );
		output.append(tabulation + tabulation + "} catch (Exception e) {" + lineSeparator + tabulation + tabulation + 
				tabulation + "throw new IcmsException(ErrorFactory.DATABASE_EXCEPTION, e.getMessage());" + lineSeparator + tabulation + tabulation + "}" + lineSeparator);
		output.append(tabulation + "}" + lineSeparator + lineSeparator);

		/* Update Method */
		output.append(tabulation + "@Override" + lineSeparator);
		output.append(tabulation+"public "+ modelFileName + " update(" + modelFileName + " " + classNameLowerCase + ", Identifier identifier) {" +  lineSeparator);
		output.append(tabulation + tabulation + "return updateV1(loginusernote, identifier).orElse(null);" + lineSeparator 
				+ tabulation + "}" + lineSeparator + lineSeparator);
		/* updateV1 method*/
		output.append(tabulation + "@Override" + lineSeparator);
		output.append(tabulation+"public Optional<"+ modelFileName + "> updateV1(" + modelFileName + " " + classNameLowerCase + ", Identifier identifier) {" +  lineSeparator);
		output.append(tabulation + tabulation + "jdbcTemplate.update(environment.getProperty(\"" + modelFileName + ".update\"), " + lineSeparator);
		for (int i = 0, attributesLenght = attributes.size(); i < attributesLenght; i++) {
			attribut = attributes.get(i);
			if (attribut.getName().equalsIgnoreCase(ignoreRowCreationDate) || attribut.getName().equalsIgnoreCase(ignoreRowCreatedBy)) {
				continue;
			}
			if (!attribut.isPrimaryKey() || !attribut.isAutoincrement()) {
				if (!attribut.getJavaType().equalsIgnoreCase("Boolean")) {
					methodName = StringHelper.saniziteForClassName(attribut.getName());
					methodName = "get" + methodName;
				} else {
					methodName = StringHelper.getMethodNameForBoolean(StringHelper.sanitizeForAttributName(attribut.getName()));
				}
				methodName += "()";
				if ((i + 1) < attributesLenght) {
					output.append(tabulation + tabulation + tabulation + classNameLowerCase + "." + methodName + "," + lineSeparator);
				} else {
					if (pkSize == 1) {
						output.append(tabulation + tabulation + tabulation + classNameLowerCase + "." + methodName +"," + lineSeparator + tabulation +
								tabulation + tabulation + classNameLowerCase + ".get" + StringHelper.saniziteForClassName(pkAttributes.get(0)) +"());" + lineSeparator);
					} else if(pkSize ==2) { 
						output.append(tabulation + tabulation + tabulation + classNameLowerCase + "." + methodName +"," + lineSeparator + tabulation +
								tabulation + tabulation + classNameLowerCase + ".get" + StringHelper.saniziteForClassName(pkAttributes.get(0)) +"()," +
								lineSeparator + tabulation + tabulation + tabulation + classNameLowerCase + ".get" +
								StringHelper.saniziteForClassName(pkAttributes.get(1))+"());" 
								+ lineSeparator);
					} else { //TODO for composite key morethan two
						output.append(tabulation + tabulation + tabulation + classNameLowerCase + "." + methodName + ");" + lineSeparator);
					}
				}
			}
		}
		
		String pkUppercase = pkInJavaType.substring(0, 1).toUpperCase() + pkInJavaType.substring(1);
		output.append(tabulation + tabulation + "return getV1(new Identifier(" + classNameLowerCase + ".get" + pkUppercase
				 + "(), identifier.getLocaleCd()));" + lineSeparator + tabulation + "}" + lineSeparator + lineSeparator);
		/* Delete method */
		output.append(tabulation + "@Override" + lineSeparator);
		output.append(tabulation+"public int delete("+ modelFileName + " " + classNameLowerCase + ") {" +  lineSeparator);
		output.append(tabulation + tabulation + "return jdbcTemplate.update(environment.getProperty(\"" + 
				modelFileName + ".delete\"), " + classNameLowerCase +  ".get" + pkUppercase +  "());" + lineSeparator + tabulation + "}" + lineSeparator
				+ lineSeparator);
		/* DeleteV1 method */
		output.append(tabulation + "@Override" + lineSeparator);
		output.append(tabulation+"public int deleteV1(Optional<"+ modelFileName + "> " + classNameLowerCase + ") {" +  lineSeparator);
		output.append(tabulation + tabulation + "return delete(" + classNameLowerCase +".orElseThrow(()->new IcmsException(ErrorFactory.RESOURCE_NOT_FOUND, \"Note is not available\")));"
				+ lineSeparator +  tabulation + "}" + lineSeparator + lineSeparator);
		/* RowMapper Class */
		output.append(tabulation + "RowMapper<" + modelFileName + "> " + classNameLowerCase + "RowMapper = (rs, rowNum) -> {" + lineSeparator + lineSeparator);
		output.append(tabulation + tabulation +tabulation + modelFileName + " " + classNameLowerCase + " = new " + modelFileName + "();" + lineSeparator);
/*
		for (int i = 0, attributesLenght = attributes.size(); i < attributesLenght; i++) {
			attribut = attributes.get(i);
			String canonicalName;
			try {
				canonicalName = DbHelper.simpleNameToCanonicalName(attribut.getJavaType());
			} catch (Exception ex) {
				throw new Exception(ex.getMessage());
			}
			output.append(tabulation + tabulation + tabulation + canonicalName + " " + attribut.getName() + " = null;"+ lineSeparator);
			if (attribut.isForeignKey() && attribut.getRawName().toLowerCase().endsWith("_cd")) {
				output.append(tabulation + tabulation + tabulation +  "java.lang.String " + attribut.getName().substring(0, attribut.getName().length() - 2) 
						+ "Locale" + " = null;" + lineSeparator);
			}
		}*/
		for (int i = 0, attributesLenght = attributes.size(); i < attributesLenght; i++) {
			attribut = attributes.get(i);
			output.append(StringHelper.lineSeparator);
			String rsCreated = DbHelper.createResulSet("rs", attribut.getJavaType(), attribut.getSqlType(),
					attribut.getRawName());
			if (!attribut.getJavaType().equalsIgnoreCase("Boolean")) {
				methodName = StringHelper.saniziteForClassName(attribut
						.getName());
				methodName = "set" + methodName;
			} else {
				methodName = "set" + StringHelper.getMethodNameForBoolean(StringHelper.sanitizeForAttributName(attribut.getName()));
			}
			String setObj = classNameLowerCase + "." + methodName + "("	+ rsCreated + ")";
			output.append(tabulation + tabulation + tabulation + setObj + ";" + lineSeparator);
			if (attribut.isForeignKey() && attribut.getRawName().toLowerCase().endsWith("_cd")) {
				output.append(StringHelper.lineSeparator);
				String rawLocale = attribut.getRawName().substring(0, attribut.getRawName().length() - 2) + "locale";
				methodName = methodName.substring(0, methodName.length() - 2) + "Locale";
				String inner =  "rs.getObject(\"" +	rawLocale + "\") != null ? rs.getString(\"" + rawLocale + "\") : null;";
				output.append(tabulation + tabulation + tabulation + classNameLowerCase + "." + methodName + "(" + inner +  ")" + lineSeparator);
			}
		}
		output.append(lineSeparator);
		output.append(tabulation + tabulation + tabulation + "return "+classNameLowerCase+";"+lineSeparator);
		output.append(tabulation + tabulation + "};" + lineSeparator);
		//output.append(tabulation + "}" + lineSeparator);
		output.append(lineSeparator);
		output.append("}" + lineSeparator);
		output.append(lineSeparator);
		//output.append((tabulation + tabulation + tabulation + )
		
		return output.toString();		
	}

}
