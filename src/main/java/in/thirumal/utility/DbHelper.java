package in.thirumal.utility;


import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.logging.Logger;
/**
 * @author Thirumal
 *
 */
public final class DbHelper {
	
	private	static	final	Logger	LOGGER		=	Logger.getLogger(DbHelper.class.getName());
	
	private static HashMap<String, String> simpleNamesCanonicalNames;
	private static HashMap<String, String> sqlTypesJavaTypes;
	private static HashMap<String, String> javaTypesPreparedStatementSet;
	private static HashMap<String, String> javaTypesResultSet;
	
	
	private static void mapJavatypesResultSet(){
		
		javaTypesResultSet = new HashMap<>();
		javaTypesResultSet.put("String", "%s.getString(%s)");
		javaTypesResultSet.put("BigDecimal", "%s.getBigDecimal(%s)");
		javaTypesResultSet.put("Boolean", "%s.getBoolean(%s)");
		javaTypesResultSet.put("Byte", "%s.getByte(%s)");
		javaTypesResultSet.put("Integer", "%s.getInt(%s)");
		javaTypesResultSet.put("Long", "%s.getLong(%s)");
		javaTypesResultSet.put("bigserial", "%s.getLong(%s)");
		javaTypesResultSet.put("Float", "%s.getFloat(%s)");
		javaTypesResultSet.put("Double", "%s.getDouble(%s)");
		javaTypesResultSet.put("byte[]", "%s.getBytes(%s)");
		javaTypesResultSet.put("Date", "%s.getDate(%s)");
		javaTypesResultSet.put("Time", "%s.getTime(%s)");
		javaTypesResultSet.put("Timestamp", "%s.getTimestamp(%s)");
		javaTypesResultSet.put("Short", "%s.getShort(%s)");
		// microsoft.sql.DateTimeOffset
		javaTypesResultSet.put("DateTimeOffset",
				"?.getObject(?, microsoft.sql.DateTimeOffset)");
		//PostgreSQL
		javaTypesResultSet.put("boolean", "%s.getBoolean(%s)");
		javaTypesResultSet.put("bool", "%s.getBoolean(%s)");
		javaTypesResultSet.put("bpchar", "%s.getString(%s)");
		javaTypesResultSet.put("inet", "%s.getString(%s)");
		javaTypesResultSet.put("UUID", "%s.getObject(%s)");
		javaTypesResultSet.put("citext", "%s.getString(%s)");
		javaTypesResultSet.put("jsonb", "%s.getString(%s)");
		javaTypesResultSet.put("PGInterval", "%s.getObject(%s)");
		javaTypesResultSet.put("OffsetDateTime", "%s.getObject(%s)");
		javaTypesResultSet.put("LocalDate", "%s.getObject(%s, LocalDate.class)");
		javaTypesResultSet.put("PGpoint", "%s.getObject(%s)");
		javaTypesResultSet.put("DateRange", "%s.getObject(%s)");
		javaTypesResultSet.put("DateTimeRange", "%s.getObject(%s)");
		javaTypesResultSet.put("bigint", "%s.getObject(%s)");
		javaTypesResultSet.put("tstzrange", "%s.getObject(%s)");
		javaTypesResultSet.put("_int8", "%s.getInt(%s)");
		
		//Enum
		javaTypesResultSet.put("nature_of_money", "%s.getString(%s)");
		javaTypesResultSet.put("nature_of_debt", "%s.getString(%s)");
		javaTypesResultSet.put("nature_of_claim", "%s.getString(%s)");
		javaTypesResultSet.put("auction_type", "%s.getString(%s)");
		javaTypesResultSet.put("city", "%s.getString(%s)");
		javaTypesResultSet.put("postal_code", "%s.getString(%s)");
		javaTypesResultSet.put("case_stage", "%s.getString(%s)");
		javaTypesResultSet.put("asset_class", "%s.getString(%s)");
		javaTypesResultSet.put("legal_provision", "%s.getString(%s)");
		javaTypesResultSet.put("_tags", "%s.getString(%s)");
		javaTypesResultSet.put("litigation_party_type", "%s.getString(%s)");
		javaTypesResultSet.put("news_edition", "%s.getString(%s)");
		javaTypesResultSet.put("mode_of_communication", "%s.getString(%s)");
		javaTypesResultSet.put("liquidation_reason", "%s.getString(%s)");
		javaTypesResultSet.put("withdrawal_reason", "%s.getString(%s)");
		javaTypesResultSet.put("icms_service", "%s.getString(%s)");
		javaTypesResultSet.put("units", "%s.getString(%s)");
		javaTypesResultSet.put("cirp_stage", "%s.getString(%s)");
		javaTypesResultSet.put("cirp_exclusion_reason", "%s.getString(%s)");
		javaTypesResultSet.put("period", "%s.getString(%s)");
		javaTypesResultSet.put("json", "%s.getString(%s)");
		javaTypesResultSet.put("_icms_service", "%s.getString(%s)");
		//
	}
	
	private static void mapJavaTypesPrepareStatementSet(){
		
		javaTypesPreparedStatementSet = new HashMap<>();
		javaTypesPreparedStatementSet
				.put("String", "%s.setString(%s, %s)");
		javaTypesPreparedStatementSet.put("BigDecimal",
				"%s.setBigDecimal(%s, %s)");
		javaTypesPreparedStatementSet.put("Boolean",
				"%s.setBoolean(%s, %s)");
		javaTypesPreparedStatementSet.put("Byte", "%s.setByte(%s, %s)");
		javaTypesPreparedStatementSet.put("Integer", "%s.setInt(%s, %s)");
		javaTypesPreparedStatementSet.put("Long", "%s.setLong(%s, %s)");
		javaTypesPreparedStatementSet.put("bigserial", "%s.setLong(%s, %s)");
		javaTypesPreparedStatementSet.put("Float", "%s.setFloat(%s, %s)");
		javaTypesPreparedStatementSet
				.put("Double", "%s.setDouble(%s, %s)");
		javaTypesPreparedStatementSet.put("byte[]", "%s.setBytes(%s, %s)");
		javaTypesPreparedStatementSet.put("Date", "%s.setObject(%s, %s)");
		javaTypesPreparedStatementSet.put("Time", "%s.setObject(%s, %s)");
		javaTypesPreparedStatementSet.put("Timestamp",
				"%s.setTimestamp(%s, %s)");
		javaTypesPreparedStatementSet.put("Short", "%s.setShort(%s, %s)");
		// microsoft.sql.DateTimeOffset
		javaTypesPreparedStatementSet.put("DateTimeOffset",
				"?.setObject(?, ?)");
		//PostgreSQL
		javaTypesPreparedStatementSet.put("boolean", "%s.setBoolean(%s, %s)");
		javaTypesPreparedStatementSet.put("bool", "%s.setBoolean(%s, %s)");
		javaTypesPreparedStatementSet.put("bpchar", "%s.setString(%s, %s)");
		javaTypesPreparedStatementSet.put("inet", "%s.setString(%s, %s)");
		javaTypesPreparedStatementSet.put("UUID", "%s.setObject(%s, %s)");
		javaTypesPreparedStatementSet.put("citext", "%s.setString(%s, %s)");
		javaTypesPreparedStatementSet.put("jsonb", "%s.setString(%s, %s)");
		javaTypesPreparedStatementSet.put("PGInterval", "%s.setObject(%s, %s)");
		javaTypesPreparedStatementSet.put("OffsetDateTime", "%s.setObject(%s, %s)");
		javaTypesPreparedStatementSet.put("LocalDate", "%s.setObject(%s, %s)");
		javaTypesPreparedStatementSet.put("PGpoint", "%s.setObject(%s, %s)");
		javaTypesPreparedStatementSet.put("DateRange", "%s.setObject(%s, %s)");
		javaTypesPreparedStatementSet.put("DateTimeRange", "%s.setObject(%s, %s)");
		javaTypesPreparedStatementSet.put("bigint", "%s.setObject(%s, %s)");
		javaTypesPreparedStatementSet.put("tstzrange", "%s.setObject(%s, %s)");
		javaTypesPreparedStatementSet.put("_int8", "%s.setObject(%s, %s)");
		//Enum
		javaTypesPreparedStatementSet.put("nature_of_money", "%s.setString(%s, %s)");
		javaTypesPreparedStatementSet.put("nature_of_debt", "%s.setString(%s, %s)");
		javaTypesPreparedStatementSet.put("nature_of_claim", "%s.setString(%s, %s)");
		javaTypesPreparedStatementSet.put("auction_type", "%s.setString(%s, %s)");
		javaTypesPreparedStatementSet.put("city", "%s.setString(%s, %s)");
		javaTypesPreparedStatementSet.put("postal_code", "%s.setString(%s, %s)");
		javaTypesPreparedStatementSet.put("case_stage", "%s.setString(%s, %s)");
		javaTypesPreparedStatementSet.put("asset_class", "%s.setString(%s, %s)");
		javaTypesPreparedStatementSet.put("legal_provision", "%s.getString(%s)");
		javaTypesPreparedStatementSet.put("_tags", "%s.getString(%s)");
		javaTypesPreparedStatementSet.put("litigation_party_type", "%s.getString(%s)");
		javaTypesPreparedStatementSet.put("news_edition", "%s.getString(%s)");
		javaTypesPreparedStatementSet.put("mode_of_communication", "%s.getString(%s)");
		javaTypesPreparedStatementSet.put("liquidation_reason", "%s.getString(%s)");
		javaTypesPreparedStatementSet.put("withdrawal_reason", "%s.getString(%s)");
		javaTypesPreparedStatementSet.put("icms_service", "%s.getString(%s)");		
		javaTypesPreparedStatementSet.put("units", "%s.getString(%s)");
		javaTypesPreparedStatementSet.put("cirp_stage", "%s.getString(%s)");
		javaTypesPreparedStatementSet.put("cirp_exclusion_reason", "%s.getString(%s)");
		javaTypesPreparedStatementSet.put("period", "%s.getString(%s)");
		javaTypesPreparedStatementSet.put("json", "%s.getString(%s)");
		javaTypesPreparedStatementSet.put("_icms_service", "%s.getString(%s)");
	}
	
	private static void	mapSQLTypesJavaTypes(){
		System.out.println("Mapping SQL types to Java type");
		sqlTypesJavaTypes	=	new HashMap<>();
		
		sqlTypesJavaTypes.put("char", "String");
		sqlTypesJavaTypes.put("varchar", "String");
		sqlTypesJavaTypes.put("longvarchar", "String");
		sqlTypesJavaTypes.put("nvarchar", "String");
		sqlTypesJavaTypes.put("nchar", "String");
		sqlTypesJavaTypes.put("ntext", "String");
		sqlTypesJavaTypes.put("sysname", "String");
		sqlTypesJavaTypes.put("tinytext", "String");
		sqlTypesJavaTypes.put("text", "String");
		sqlTypesJavaTypes.put("mediumtext", "String");
		sqlTypesJavaTypes.put("longtext", "String");
		sqlTypesJavaTypes.put("uniqueidentifier", "String");		
		sqlTypesJavaTypes.put("xml", "String");
		sqlTypesJavaTypes.put("longvarchar", "String");
		sqlTypesJavaTypes.put("sql_variant", "String");
		sqlTypesJavaTypes.put("sysname", "String");
		
		sqlTypesJavaTypes.put("numeric", "BigDecimal");
		sqlTypesJavaTypes.put("decimal", "BigDecimal");
		sqlTypesJavaTypes.put("bit", "Boolean");
		sqlTypesJavaTypes.put("tinyint", "Byte");
		sqlTypesJavaTypes.put("smallint", "Short");
		sqlTypesJavaTypes.put("int", "Integer");
		sqlTypesJavaTypes.put("integer", "Integer");
		sqlTypesJavaTypes.put("bigint", "Long");
		sqlTypesJavaTypes.put("bigserial", "Long");
		sqlTypesJavaTypes.put("real", "Float");
		sqlTypesJavaTypes.put("double", "Double");
		sqlTypesJavaTypes.put("money", "BigDecimal");
		sqlTypesJavaTypes.put("smallmoney", "BigDecimal");
		sqlTypesJavaTypes.put("real", "Float");
		sqlTypesJavaTypes.put("float", "Double");
		sqlTypesJavaTypes.put("unsigned smallint", "Integer");
		sqlTypesJavaTypes.put("unsigned int", "Integer");
		sqlTypesJavaTypes.put("unsigned bigint", "BigDecimal");
		sqlTypesJavaTypes.put("_int4", "Integer");
		sqlTypesJavaTypes.put("_int8", "Integer");
		
		sqlTypesJavaTypes.put("binary", "byte[]");
		sqlTypesJavaTypes.put("varbinary", "byte[]");
		sqlTypesJavaTypes.put("tinyblob", "byte[]");
		sqlTypesJavaTypes.put("blob", "byte[]");
		sqlTypesJavaTypes.put("mediumblob", "byte[]");
		sqlTypesJavaTypes.put("longblob", "byte[]");
		sqlTypesJavaTypes.put("image", "byte[]");
		sqlTypesJavaTypes.put("udt", "byte[]");
		
		sqlTypesJavaTypes.put("bit", "Boolean");
		
		sqlTypesJavaTypes.put("date", "LocalDate");
		
		/* BASE VERSION
		sqlTypesJavaTypes.put("time", "Time");
		sqlTypesJavaTypes.put("smalldatetime", "Timestamp");
		sqlTypesJavaTypes.put("datetime", "Timestamp");
		sqlTypesJavaTypes.put("datetime2", "Timestamp");
		sqlTypesJavaTypes.put("timestamp", "Timestamp");
		sqlTypesJavaTypes.put("datetimeoffset", "DateTimeOffset");*/
		
		sqlTypesJavaTypes.put("time", "LocalDate");
		sqlTypesJavaTypes.put("smalldatetime", "LocalDate");
		sqlTypesJavaTypes.put("datetime", "LocalDate");
		sqlTypesJavaTypes.put("datetime2", "LocalDate");
		sqlTypesJavaTypes.put("timestamp", "LocalDate");
		sqlTypesJavaTypes.put("timestamptz", "OffsetDateTime");
		sqlTypesJavaTypes.put("timestamp with time zone", "OffsetDateTime");
		sqlTypesJavaTypes.put("datetimeoffset", "LocalDate");
		sqlTypesJavaTypes.put("timestamp without time zone", "LocalDate");
		sqlTypesJavaTypes.put("tstzrange", "LocalDate");
		
		// PostgreSQL 
		sqlTypesJavaTypes.put("serial", "Long");
		sqlTypesJavaTypes.put("bool", "Boolean");
		sqlTypesJavaTypes.put("int2", "Integer");
		sqlTypesJavaTypes.put("int4", "Integer");
		sqlTypesJavaTypes.put("int8", "Integer");
		sqlTypesJavaTypes.put("bpchar", "String");
		sqlTypesJavaTypes.put("chkpass", "String");
		sqlTypesJavaTypes.put("inet", "String");
		sqlTypesJavaTypes.put("uuid", "UUID");
		sqlTypesJavaTypes.put("bytea", "byte[]");
		sqlTypesJavaTypes.put("citext", "String");
		sqlTypesJavaTypes.put("jsonb", "String");
		sqlTypesJavaTypes.put("interval", "PGInterval");
		sqlTypesJavaTypes.put("point", "PGpoint");
		sqlTypesJavaTypes.put("daterange", "DateRange");
		//tsVector
		sqlTypesJavaTypes.put("tsvector", "String");
		//ENUM
		sqlTypesJavaTypes.put("nature_of_money", "String");
		sqlTypesJavaTypes.put("nature_of_debt", "String");
		sqlTypesJavaTypes.put("nature_of_claim", "String");
		sqlTypesJavaTypes.put("auction_type", "String");
		sqlTypesJavaTypes.put("postal_code", "String");
		sqlTypesJavaTypes.put("city", "String");
		sqlTypesJavaTypes.put("case_stage", "String");
		sqlTypesJavaTypes.put("asset_class", "String");
		sqlTypesJavaTypes.put("legal_provision", "String");
		sqlTypesJavaTypes.put("_tags", "String");
		sqlTypesJavaTypes.put("litigation_party_type", "String");
		sqlTypesJavaTypes.put("news_edition", "String");
		sqlTypesJavaTypes.put("mode_of_communication", "String");
		sqlTypesJavaTypes.put("liquidation_reason", "String");
		sqlTypesJavaTypes.put("withdrawal_reason", "String");
		sqlTypesJavaTypes.put("icms_service", "String");
		sqlTypesJavaTypes.put("units", "String");
		sqlTypesJavaTypes.put("cirp_stage", "String");
		sqlTypesJavaTypes.put("cirp_exclusion_reason", "String");
		sqlTypesJavaTypes.put("period", "String");
		sqlTypesJavaTypes.put("json", "String");
		sqlTypesJavaTypes.put("_icms_service", "String");
	}
	
	private static void mapSimpleNameCanonicalName(){
		
		simpleNamesCanonicalNames	= new HashMap<>();
		
		simpleNamesCanonicalNames.put("String", "java.lang.String");
		simpleNamesCanonicalNames.put("BigDecimal", "java.math.BigDecimal");
		simpleNamesCanonicalNames.put("Boolean", "java.lang.Boolean");
		simpleNamesCanonicalNames.put("Byte", "java.lang.Byte");
		simpleNamesCanonicalNames.put("Integer", "java.lang.Integer");
		simpleNamesCanonicalNames.put("Long", "java.lang.Long");
		simpleNamesCanonicalNames.put("Float", "java.lang.Float");
		simpleNamesCanonicalNames.put("Double", "java.lang.Double");
		simpleNamesCanonicalNames.put("byte[]", "byte[]");
		simpleNamesCanonicalNames.put("Date", "java.util.Date");
		simpleNamesCanonicalNames.put("Time", "java.sql.Time");
		simpleNamesCanonicalNames.put("Timestamp", "java.sql.Timestamp");
		simpleNamesCanonicalNames.put("UUID", "java.util.UUID");
		simpleNamesCanonicalNames.put("Short", "java.lang.Short");
		simpleNamesCanonicalNames.put("DateTimeOffset", "microsoft.sql.DateTimeOffset");
		simpleNamesCanonicalNames.put("jsonb", "java.lang.String");
		simpleNamesCanonicalNames.put("PGInterval", "org.postgresql.util.PGInterval");
		simpleNamesCanonicalNames.put("OffsetDateTime", "java.time.OffsetDateTime");
		simpleNamesCanonicalNames.put("PGpoint", "org.postgresql.geometric.PGpoint");
		simpleNamesCanonicalNames.put("LocalDate", "java.time.LocalDate");
		simpleNamesCanonicalNames.put("DateRange", "com.enkindle.persistence.code.shared.DateRange");
		simpleNamesCanonicalNames.put("tsrange", "com.enkindle.persistence.code.shared.DateTimeRange");
		
		//Enum
		simpleNamesCanonicalNames.put("nature_of_money", "java.lang.String");
		simpleNamesCanonicalNames.put("nature_of_debt", "java.lang.String");
		simpleNamesCanonicalNames.put("nature_of_claim", "java.lang.String");
		simpleNamesCanonicalNames.put("auction_type", "java.lang.String");
		simpleNamesCanonicalNames.put("postal_code", "java.lang.String");
		simpleNamesCanonicalNames.put("city", "java.lang.String");
		simpleNamesCanonicalNames.put("case_stage", "java.lang.String");
		simpleNamesCanonicalNames.put("asset_class", "java.lang.String");
		simpleNamesCanonicalNames.put("legal_provision", "String");
		simpleNamesCanonicalNames.put("_tags", "String");
		simpleNamesCanonicalNames.put("litigation_party_type", "String");
		simpleNamesCanonicalNames.put("news_edition", "String");
		simpleNamesCanonicalNames.put("mode_of_communication", "String");
		simpleNamesCanonicalNames.put("liquidation_reason", "String");
		simpleNamesCanonicalNames.put("withdrawal_reason", "String");
		simpleNamesCanonicalNames.put("icms_service", "String");
	}
	
	
	public static String javaTypeToResultSetGet(String javaType) throws Exception {
		if(javaType == null || javaType.isEmpty()){
			throw new InvalidParameterException("the parameter is null or empty String");
		}
		if(javaTypesResultSet == null){
			mapJavatypesResultSet();
		}		
		if(javaTypesResultSet.containsKey(javaType)){
			return javaTypesResultSet.get(javaType);
		}		
		throw new Exception("Impossible to map the Java type " + javaType + " with a ResultSet get method.");
	}
	
	public static String javaTypeToPrepareStatementSet(String javaType) throws Exception {
		if(javaType == null || javaType.isEmpty()){
			throw new InvalidParameterException("the parameter is null or empty String");
		}		
		if(javaTypesPreparedStatementSet == null){
			mapJavaTypesPrepareStatementSet();
		}		
		if(javaTypesPreparedStatementSet.containsKey(javaType)){
			return javaTypesPreparedStatementSet.get(javaType);
		}		
		throw new Exception("Impossible to map the Java type "+javaType+" with a PreparedStatement set method.");
	}
	
	public static String sqlTypeToJavaType(String sqlType) throws Exception {		
		if(sqlType == null || sqlType.isEmpty()){
			throw new InvalidParameterException("the parameter is null or empty String");
		}		
		if(sqlTypesJavaTypes == null){
			mapSQLTypesJavaTypes();
		}		
		String sqlTypeStrLowerCase = sqlType.toLowerCase();
		if(sqlTypesJavaTypes.containsKey(sqlTypeStrLowerCase)){
			return sqlTypesJavaTypes.get(sqlTypeStrLowerCase);
		} else {
			if(sqlTypeStrLowerCase.matches("varbinary[(]([0-9]{1,})[)]")){
				return "byte[]";
			}			
			if(sqlTypeStrLowerCase.matches("varchar[(]([0-9]{1,})[)]")){
				return "String";
			}			
			if(sqlTypeStrLowerCase.matches("datetimeoffset[(]([0-9]{1,})[)]")){
				return "LocalDate";
			}			
		}		
		throw new Exception("Impossible to map the SQL type "+ sqlType +" with a Java Type.");		
	}
	
	public static String simpleNameToCanonicalName(String simpleName) throws Exception {
		if(simpleName == null || simpleName.isEmpty()){
			throw new InvalidParameterException("the parameter is null or empty String");
		}		
		if(simpleNamesCanonicalNames == null){
			mapSimpleNameCanonicalName();
		}		
		if(simpleNamesCanonicalNames.containsKey(simpleName)){
			return simpleNamesCanonicalNames.get(simpleName);
		}
		throw new Exception("Impossible to find the canonical name from the simpleName: "+simpleName);
	}	
	
	public static final String createPreparementSet(String ps, int index, String javaType, String sqlType,
			String objectSourceAsName, boolean canBeNull) {
		String result	= null;
		String psSet	= null;
		try {
			psSet = javaTypeToPrepareStatementSet(javaType);
		} catch (Exception ex) {
			LOGGER.severe(ex.getMessage());
		}
		boolean mappingExists = psSet != null && !ps.isEmpty();
		if (mappingExists) {
			String value = objectSourceAsName;
			/*if(sqlType != null) {
				if(sqlType.equalsIgnoreCase("time") || sqlType.equalsIgnoreCase("smalldatetime") || 
						   sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("datetime2") || 
						   sqlType.equalsIgnoreCase("timestamp") || sqlType.equalsIgnoreCase("datetimeoffset")) {
							objectSourceAsName = "new java.sql.Timestamp("+objectSourceAsName+".getTime())";							
							javaType = "Timestamp";
							
				} else if(sqlType.equalsIgnoreCase("date")) {					
					objectSourceAsName = "new java.sql.Date("+objectSourceAsName+".getTime())";
				}
			}*/
			result = String.format(psSet, ps, index, objectSourceAsName);
			if (canBeNull) {
				String result_tmp = "if(" + value
						+ " == null) {\r\n";
				result_tmp += "\t\t\t" + ps + ".setObject(" + index
						+ ", null);\r\n";
				result_tmp += "\t\t} else { \r\n";
				result_tmp += "\t\t\t" + result + ";\r\n";
				result_tmp += "\t\t}\r\n";
				result = result_tmp;
			} else {
				result += ";";
			}
		}
		return result;
	}

	public static String createResulSet(String rs, String javaType, String sqlType, String name) {
		String result	= null;
		String rsGet	= null;		
		try {
			rsGet = javaTypeToResultSetGet(javaType);
		} catch (Exception e) {
			System.out.println(sqlType + ", " + javaType);
			e.printStackTrace();
		}
		boolean mappingExists = rsGet != null && !rsGet.isEmpty();
		if (mappingExists) {
			if(sqlType != null){
				if(sqlType.equalsIgnoreCase("time") || sqlType.equalsIgnoreCase("smalldatetime") || 
						   sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("datetime2") || 
						   sqlType.equalsIgnoreCase("timestamp") || sqlType.equalsIgnoreCase("datetimeoffset")){
							javaType = "Timestamp";							
				} else if(sqlType.equalsIgnoreCase("date")){
					javaType = "Date";
				}
			}
			result = rs + ".getObject(\"" + name + "\") != null ? "	+ String.format(rsGet, rs, "\"" + name + "\"") + " : null";
			if (sqlType.equalsIgnoreCase("uuid")) {
				result = rs + ".getObject(\"" + name + "\") != null ? "	+ String.format(rsGet, ("(java.util.UUID) " + rs), "\"" + name + "\"") + " : null";
			}
			if (sqlType.equalsIgnoreCase("date")) {
				result = rs + ".getObject(\"" + name + "\") != null ? "	+ String.format(rsGet, rs, "\"" + name + "\"") + " : null";
			}
		}
		return result;
	}	
}
