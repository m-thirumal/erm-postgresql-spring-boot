# erm-postgresql-spring-boot

Generate Pojo & JdbcTemplate (DAO) for PostgreSQL

## To use swagger UI

<a href ="http://localhost:21991/swagger-ui/index.html">Swagger-UI</a>

## Configuration files

1. src/main/resources/application.yml
2. src/main/resources/application.properties
3. src/main/resources/application-erm-target.yml

## Other helper methods/classes

```
/**
 * 
 */
package com.thirumal.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import com.thirumal.exception.InternalServerException;
import com.thirumal.exception.ResourceNotFoundException;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

/**
 * Generic Data Access Object interface for repository
 * All ICMS DAO must implement this interface
 * @author Thirumal[திருமால்]
 * @since 14/04/2017
 * @version 1.0
 */
public interface GenericDao <T, I, S> {
   
    /** Persist the new instance (Model) object into database, I will hold localeCd value */
    Optional<T> create(T model, I identifier);
    
    /** Persist the new instance (Model) object into database, I will hold localeCd value */
    Long insert(T model, I identifier);
    
    /** Retrieve an Optional<object> that was previously persisted to the database using
     *  the indicated identifier (contains primary key & locale Cd)
     */
    Optional<T> get(@NotNull I identifier);
    
    /** Retrieve an object that was previously persisted to the database using
     *  the indicated identifier (contains primary key & locale Cd) and whereClause
     */

    /** Retrieve an Optional<object> that was previously persisted to the database using
     *  the indicated identifier (contains primary key & locale Cd) and whereClause
     */
    Optional<T> get(@NotNull I identifier, S whereClause);
   
    /**
     * Retrieve all instance using identifier ( id & LocaleCd)
     */
    List<T> list(I identifier);
    
    /**
     * Retrieve all instance using the indicated identifier (id
     *  & localeCd) and where clause
     */
    List<T> list(I identifier, S whereClause);
    
    /**
  	 * Count all instance using the indicated identifier (id
     *  & localeCd) and where clause
     */
    int count(I identifier, S whereClause);
    
    /** Save changes made to a persistent object. */
    Optional<T> update(T transientObject, I identifier);
    
    /** Heap Only Tuple to improve write performance in PostgreSQL
     *  Try not to include indexed column in the hot update method
     *  Refer for WHY: https://github.com/M-Thirumal/postgresql_reference/blob/master/hot_update/hot_update.md
     */
    Optional<T> hotUpdate(T transientObject, I identifier, S whereClause);
    
    /** Remove an object from persistent storage in the database */
    int delete(T persistentObject);
    
    /** Remove all objects from persistent storage in the database
     * using Identifier and where clause (mostly Foreign key)*/
    int delete(I identifier, S whereClause);
    
    default int findAndDelete(I identifier) {
    	return delete(get(identifier).orElseThrow(()->new ResourceNotFoundException("The requested object is not found in database")));
    }
	
    /**
     * @author Thirumal
     * Enum for PostgreSQL constant 
     */
	@Getter@ToString
	enum postgreSqlConstant {
		INFINITY("infinity"), //Should be used for end date
		INTEGER("INTEGER"),
		TAGS("tags"),
		ICMS_SERVICE("icms_service");
		private String constant;
		postgreSqlConstant(final String constant) {
			this.constant = constant;
		}
	}
	
	/**
	 * Replace the string with in-values with default delimiter "," or the requested delimiter
	 * @since 0.6
	 * @param <E extends Number>
	 * @param query
	 * @param replaceString
	 * @param inValues
	 * @param delimitter
	 * @return query with inValues
	 */
	default <E extends Number> String setInvalues(String query, String replaceString, Set<E> inValues, String... delimitter) {
		return query.replace(replaceString, inValues.stream().map(Object::toString)
				.collect(Collectors.joining(delimitter.length > 0 ? delimitter[0]:",")));
	}
	
	default String setInvalues(String query, String replaceString, Set<String> inValues) {
		if (inValues == null || inValues.isEmpty()) {
			throw new InternalServerException("SQL IN values cannot be empty");
		}
		return query.replace(replaceString, "'" + inValues.stream().map(Object::toString).collect(Collectors.joining("','")) + "'");
	}
	
	default String getIncrementDecrementQuery(String tableName, String columnName, int incrementDecrementCount) {
		if (tableName == null || columnName == null) {
			throw new ResourceNotFoundException("Table & column name can't be NULL");
		}
		return ("UPDATE indsolv.{TABLE_NAME} AS t SET {COLUMN_NAME} = (SELECT {COLUMN_NAME} + "
				+ incrementDecrementCount + " FROM indsolv.{TABLE_NAME} AS t1 WHERE t1.{TABLE_NAME}_uid = t.{TABLE_NAME}_uid) WHERE t.{TABLE_NAME}_uid = ?")
				.replace("{TABLE_NAME}", tableName).replace("{COLUMN_NAME}", columnName); 
	}
	
	/**
	 * Replace the STRING between 2 String (EXCLUSIVE)
	 * @param originalString
	 * @param startWord
	 * @param endWord
	 * @param replaceString
	 * @return STRING WITH REPLACED STRING
	 */
	static String replaceBetween2Words(String originalString, String startWord, String endWord, String replaceString) {
		return originalString.replaceAll("("+startWord + ")[^&]*("+endWord+")", "$1"+replaceString+"$2");
	}
	
	/**
	 * Covert list query to pagination query
	 * @param listQuery
	 * @return Pagination Query with count
	 */
	default String getPaginationQuery(String listQuery) {
		StringBuilder finalQuery = new StringBuilder();
		finalQuery.append("WITH r_cnt AS (");
		String countQuerySplit = replaceBetween2Words(listQuery, "SELECT", "FROM", " COUNT(*) cnt ");
		String[] countQuerySplitArray = countQuerySplit.split("(?i) LEFT JOIN");
		String firstPart = countQuerySplitArray[0];
		if (firstPart.contains("INNER JOIN")) {
			firstPart = firstPart.split("(?i) INNER JOIN")[0];
		}
		finalQuery.append(firstPart);
		String lastPart = countQuerySplitArray[countQuerySplitArray.length - 1].split("(?i) WHERE")[1];
		lastPart = lastPart.split("(?i)ORDER")[0].split("(?i)OFFSET")[0].split("(?i)LIMIT")[0];
		finalQuery.append(" WHERE " + lastPart + ")");
		finalQuery.append(listQuery.replaceFirst("SELECT", "SELECT (SELECT cnt FROM r_cnt), "));
		return finalQuery.toString();
	}
	
	default String getPaginationQueryForTable(String listQuery) {
		StringBuilder finalQuery = new StringBuilder();
		finalQuery.append("WITH r_cnt AS (");
		String countQuerySplit = replaceBetween2Words(listQuery, "SELECT", "FROM", " COUNT(*) cnt ");
		String[] countQuerySplitArray = countQuerySplit.split("(?i) ORDER");
		String firstPart = countQuerySplitArray[0];
		finalQuery.append(firstPart + ")");
		finalQuery.append(listQuery.replaceFirst("SELECT", "SELECT (SELECT cnt FROM r_cnt), "));
		return finalQuery.toString();
	}
	
	default void setValue(PreparedStatement ps, int index, Object value) throws SQLException {
	    switch (value) {
	        case null:
	            ps.setObject(index, null);
	            break;
	        case Integer intValue:
	            ps.setInt(index, intValue);
	            break;
	        case String stringValue:
	            ps.setString(index, stringValue);
	            break;
	        case Boolean booleanValue:
	            ps.setBoolean(index, booleanValue);
	            break;
	        case Double doubleValue:
	            ps.setDouble(index, doubleValue);
	            break;
	        case Float floatValue:
	            ps.setFloat(index, floatValue);
	            break;
	        case Long longValue:
	            ps.setLong(index, longValue);
	            break;
	        case Short shortValue:
	            ps.setShort(index, shortValue);
	            break;
	        case Byte byteValue:
	            ps.setByte(index, byteValue);
	            break;
	        case java.sql.Date dateValue:
	            ps.setDate(index, dateValue);
	            break;
	        case java.sql.Time timeValue:
	            ps.setTime(index, timeValue);
	            break;
	        case java.sql.Timestamp timestampValue:
	            ps.setTimestamp(index, timestampValue);
	            break;
	        default:
	            ps.setObject(index, value);
	            break;
	    }
	}

}
```

#### New Data type mapping can be done in DbHelper.java

### Contact Info

<a href="mailto:m.thirumal@hotmail.com?Subject=erm-postgresql-spring-boot" target="_top">Thirumal</a>

