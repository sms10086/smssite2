package com.note.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DbBean {
	private static final Log LOG = LogFactory.getLog(DbBean.class);
	
	private Connection connection = null;
	
	private boolean autoCommit = true;

	private static DataSource ds = null;
	
	public DbBean() {
	}
	
	private static void initDataSource() throws NamingException {
		if (ds == null) {
			synchronized(DbBean.class) {
				if (ds == null) {
					InitialContext ctx = new InitialContext();
					ds = (DataSource ) ctx.lookup("java:comp/env/jdbc/mfds");
				}
			}
		}
	}
	public Connection getConnection() throws SQLException {
		return getConnection(true);
	}

	public Connection getConnection(boolean autoCommit) throws SQLException {
		if (connection == null ) {
			try {
				initDataSource();
			}
			catch(NamingException ne) {
				throw new SQLException("can't initialize DataSource," + ne.getExplanation());
			}
			connection = ds.getConnection();
			this.autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(autoCommit);
			 
		 
		}
		return connection;
	}
	
	public void close() {
		setAutoCommit(this.connection, this.autoCommit);
		close(connection);
		connection = null;
	}
	
	public void rollback() {
		rollback(connection);
	}
	
	
	public static void setAutoCommit(Connection conn, boolean autoCommit) {
		if (conn != null)
		try {
			conn.setAutoCommit(autoCommit);
		}
		catch(Throwable thr) {
		}
	}
	
	public static void close(ResultSet rs) {
		if (rs != null)
		try {
			rs.close();
		}
		catch(Throwable thr) {
		}
	}
	
	public static void close(Connection conn) {
		if (conn != null)
		try {
			conn.close();
		}
		catch(Throwable thr) {
		}
	}
	
	public static void close(Statement stmt) {
		if (stmt != null)
		try {
			stmt.close();
		}
		catch(Throwable thr) {
		}
	}
	
	public static void close(ResultSet rs, Statement stmt) {
		close(rs);
		close(stmt);
	}

	public static void close(Statement stmt1, Statement stmt2) {
		close(stmt1);
		close(stmt2);
	}

	public static void close(ResultSet rs, Statement stmt1, Statement stmt2) {
		close(rs);
		close(stmt1);
		close(stmt2);
	}
	
	public static void close(Statement stmt, Connection conn) {
		close(stmt);
		close(conn);
	}
	
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		close(rs);
		close(stmt);
		close(conn);
	}
	
	public static void rollback(Connection conn) {
		if (conn != null) try {
			conn.rollback();
		}
		catch(Throwable thr) {}
	}
	
	
	public static int executeUpdate(String sql, Object[] params) {
		DbBean bean = new DbBean();
		Statement stmt = null;
		int result = -1;
		try {
			Connection conn = bean.getConnection();
			if (params == null || params.length == 0) {
				stmt = conn.createStatement();
				result = stmt.executeUpdate(sql);
			}
			else {
				PreparedStatement ptmt = conn.prepareStatement(sql);
				stmt = ptmt;
				bindVariables(ptmt, params);
				
				 result = ptmt.executeUpdate();
			}
		}
		catch(Throwable thr) {
			thr.printStackTrace();
			LOG.debug(sql, thr);
		}
		finally {
			DbBean.close(stmt);
			bean.close();
		}
		return result;
	}

	public static Object selectFirst(String sql, Object[] params, Class clazz) {
		DbBean bean = new DbBean();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Connection conn = bean.getConnection();
			if ( params != null && params.length > 0) {
				PreparedStatement ptmt = conn.prepareStatement(sql);
				stmt = ptmt;
				bindVariables(ptmt, params);
				rs = ptmt.executeQuery();
			}
			else {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
			}
			
			if ( ! rs.next() )
				return null;
			
			if (clazz == null)
				return fetchAsArray( rs );
			else if (clazz.equals(Map.class) || clazz.equals(HashMap.class) )
				return fetchAsHashMap( rs );
			else
			return fetchAsObject( rs, clazz);
		}
		catch(Throwable thr) {
			if (LOG.isErrorEnabled())
			LOG.error("paging \"" + sql + "\"", thr);
		}
		finally {
			DbBean.close(rs, stmt);
			bean.close();
		}
		return null;
	}

	public static Object single(String sql, Object[] params) {

		DbBean bean = new DbBean();

		Statement stmt = null;
		ResultSet rs = null;
		Object obj = null;
		try {
			Connection conn = bean.getConnection();

			if ( params != null && params.length > 0) {
				PreparedStatement ptmt = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				stmt = ptmt;
				bindVariables(ptmt, params);
				rs = ptmt.executeQuery();
			}
			else {
				stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				rs = stmt.executeQuery(sql);
			}
			if (rs.next()) {
				obj = rs.getObject(1);
			}
		}
		catch(Throwable thr) {
			if (LOG.isErrorEnabled())
			LOG.error("single \"" + sql + "\"", thr);
		}
		finally {
			DbBean.close(rs, stmt);
			bean.close();
		}
		return obj;
	}
	
	public static Object singleObject(String sql, Object[] params, Class clazz) {
		DbBean bean = new DbBean();
		Statement stmt = null;
		ResultSet rs = null;
		Object obj = null;
		try {
			Connection conn = bean.getConnection();
			if ( params != null && params.length > 0) {
				PreparedStatement ptmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				stmt = ptmt;
				bindVariables(ptmt, params);
				rs = ptmt.executeQuery();
			}
			else {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = stmt.executeQuery(sql);
			}
			if ( rs.next() ) {
				String[] names = getColumnNames(rs);
				Object[] objs = getValues(rs, names.length);
				Object inst = clazz.newInstance();
				populate(inst, names, objs);
				obj = inst;
			}
		}
		catch(Throwable thr) {
			if (LOG.isErrorEnabled())
			LOG.error("single \"" + sql + "\"", thr);
		}
		finally {
			DbBean.close(rs, stmt);
			bean.close();
		}
		return obj;
	}
	
	public static int getInt(String sql, Object[] params) {
		Object obj = single(sql, params);
		if (obj == null)
			return 0;
		if (obj instanceof Integer)
			return ((Integer)obj).intValue();
		try {
			String s = String.valueOf(obj);
			return Integer.parseInt(s);
		}
		catch(Throwable thr) {
			return 0;
		}
	}
	
	public static String getString(String sql, Object[] params) {
		Object obj = single(sql, params);
		if (obj == null)
			return null;
		if (obj instanceof String)
			return (String)obj;
		return String.valueOf(obj);
	}
	
	public static long getLong(String sql, Object[] params) {
		Object obj = single(sql, params);
		if (obj == null)
			return 0;
		if (obj instanceof Long)
			return ((Long)obj).longValue();
		try {
			String s = String.valueOf(obj);
			return Long.parseLong(s);
		}
		catch(Throwable thr) {
			return 0;
		}
	}
	
	public static Timestamp getTimestamp(String sql, Object[] params) {
		Object obj = single(sql, params);
		if (obj == null)
			return null;
		if (obj instanceof Timestamp)
			return (Timestamp)obj;
		if (obj instanceof java.sql.Date)
			return new Timestamp(((java.sql.Date)obj).getTime());
		return null;
	}
	
	public static ArrayList select(String sql, Object[] params, int startIndex, int total, Class clazz) {
		DbBean bean = new DbBean();
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList array = new ArrayList();
		try {
			Connection conn = bean.getConnection();
			if ( params != null && params.length > 0) {
				PreparedStatement ptmt = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
				stmt = ptmt;
				bindVariables(ptmt, params);
				rs = ptmt.executeQuery();
			}
			else {
				stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
				rs = stmt.executeQuery(sql);
			}
			if (startIndex >= 0) {
				 
				for(int i=0;i<=startIndex;i++){
				 
					if(!rs.next())return array;
				}
				 
			}
			else {
			
				if (! rs.first() )
				{
					return array;
				}
			}
			if (clazz == null)
				fetchAsArray(array, rs, total);
			else if (clazz.equals(Map.class) || clazz.equals(HashMap.class)){
				fetchAsHashMap(array, rs, total);
			}
			else {
				fetchAsObject(array, rs, total, clazz);
			}
		}
		catch(Throwable thr) {
			if (LOG.isErrorEnabled())
			LOG.error("paging \"" + sql + "\" " + startIndex + "," + total, thr);
		}
		finally {
			DbBean.close(rs, stmt);
			bean.close();
		}
		return array;
	}
	
	
	protected static int fetchAsArray(ArrayList array, ResultSet rs, int total) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int columnCount = meta.getColumnCount();
		int count = 0;
		do {
			Object obj = fetchResultSetObjects(rs, columnCount);
    		array.add( obj );
			count ++;
		} while ( (count < total || total <= 0) && rs.next());
		return count;
	}
	
	protected static Object fetchResultSetObjects(ResultSet rs, int columns) throws SQLException {
		if (columns <= 0)
			return null;
		if (columns == 1) {
			Object obj = rs.getObject( 1 );
			return  transform(obj) ;
		}
		else {
			String[] objs = new String[columns];
			for(int i = 0; i < columns; i ++) {
				Object obj = rs.getObject(i+1);
				objs[i] = transform( obj );
			}
			return objs;
		}
	}

	protected static Object fetchAsArray(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int columnCount = meta.getColumnCount();
		return fetchResultSetObjects(rs, columnCount);
	}
	
	protected static String[] getColumnNames(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int columnCount = meta.getColumnCount();
		String[] names = new String[columnCount];
		for(int i = 0; i < columnCount; i ++) {
			names[i] = meta.getColumnLabel(i+1);
		}
		return names;
	}
	
	protected static int fetchAsHashMap(ArrayList array, ResultSet rs, int total) throws SQLException {
		String[] names = getColumnNames(rs);
		int count = 0;
		do {
			HashMap map = new HashMap();
			for(int i = 0; i < names.length; i ++)
				map.put(names[i].toLowerCase(), rs.getObject(names[i]));
			array.add(map);
			count ++;
		} while ( (count < total || total <=0 ) && rs.next());
		return count;
	}
	
	protected static HashMap fetchAsHashMap(ResultSet rs) throws SQLException {
		String[] names = getColumnNames(rs);
		HashMap map = new HashMap();
		for(int i = 0; i < names.length; i ++)
			map.put(names[i].toLowerCase(), rs.getObject(names[i]));
		return map;
	}
	
	protected static int fetchAsObject(ArrayList array, ResultSet rs, int total, Class clazz) throws Exception {
 
		String[] names = getColumnNames(rs);
		int count = 0;
		do {
			Object[] objs = getValues(rs, names.length);
			Object inst = clazz.newInstance();
			populate(inst, names, objs);
			array.add(inst);
			count ++;
		} while ((count < total || total <= 0) && rs.next());
	
		return count;
	}
	
	protected static Object fetchAsObject(ResultSet rs, Class clazz) throws Exception {
		String[] names = getColumnNames(rs);
		Object[] objs = getValues(rs, names);
		Object inst = clazz.newInstance();
		populate(inst, names, objs);
		return inst;
	}
	
	private static Object[] getValues(ResultSet rs, String[] names) throws Exception {
		Object[] objs = new Object[names.length];
		for(int i = 0; i < names.length; i ++) {
			objs[i] = rs.getObject(names[i]);
		}
		return objs;
	}
	
	private static Object[] getValues(ResultSet rs, int count) throws Exception {
		Object[] objs = new Object[count];
		for(int i = 0; i < count; i ++) {
			objs[i] = rs.getObject(i+1);
		}
		return objs;
	}
	
	protected static void bindVariables(PreparedStatement ptmt, Object[] params) throws SQLException {
		for(int i = 0; i < params.length; i ++) {
			Object obj = params[i];
			if (obj instanceof Integer) {
				ptmt.setInt(i+1, ((Integer)obj).intValue());
			}
			else if (obj instanceof Long) {
				ptmt.setLong(i+1, ((Long)obj).longValue());
			}
			else if (obj instanceof String) {
				ptmt.setString(i+1, (String)obj);
			}
			else if (obj instanceof Timestamp) {
				ptmt.setTimestamp(i+1, (Timestamp)obj);
			}
			else if (obj instanceof Time) {
				ptmt.setTime(i+1, (Time)obj);
			}
			else if (obj instanceof java.sql.Date) {
				ptmt.setDate(i+1, (java.sql.Date)obj);
			}
			else if (obj instanceof java.util.Date) {
				ptmt.setDate(i+1, new java.sql.Date( ((java.util.Date)obj).getTime()));
			}
			else if (obj instanceof Byte) {
				ptmt.setByte(i+1, ((Byte)obj).byteValue());
			}
			else if (obj instanceof Short) {
				ptmt.setShort(i+1, ((Short)obj).shortValue());
			}
			else if (obj instanceof byte[]) {
				ptmt.setBytes(i+1, (byte[])obj);
			}
			else if (obj instanceof Blob) {
				ptmt.setBlob(i+1, (Blob)obj);
			}
			else if (obj instanceof Clob) {
				ptmt.setClob(i+1, (Clob)obj);
			}
			else {
				if ( obj == null )
					ptmt.setNull(i+1, Types.VARCHAR );
				else
				ptmt.setObject(i+1, obj);
			}
		}
	}
	
	protected static void populate(Object inst, String [] names, Object[] objs) throws Exception {
		Method[] m = inst.getClass().getMethods();
		for(int i = 0; i < m.length; i ++) {
			Class[] types = m[i].getParameterTypes();
			if (types != null && types.length == 1 && m[i].getName().startsWith("set")) {
				String name = m[i].getName().substring(3);
				Object value = findValue(name, names, objs);
				if (value != null) {
					try {
						value = mappingParameter(value, types[0]);
						m[i].invoke(inst, new Object[]{value});
					}
					catch(Throwable thr) {
						LOG.error(null, thr);
					}
				}
			}
		}
	}
	
	private static HashMap clazzes = new HashMap();
	static {
		clazzes.put(int.class, Integer.class);
		clazzes.put(double.class, Double.class);
		clazzes.put(float.class, Float.class);
		clazzes.put(boolean.class, Boolean.class);
		clazzes.put(long.class, Long.class);
	}
	
	private static Object mappingParameter(Object origin, Class clazz) throws Exception {
		Class originClazz = origin.getClass();
		if ( originClazz.equals(clazz) )
			return origin;
		if ( clazz.isAssignableFrom(originClazz ) )
			return origin;
		if ( java.util.Date.class.isAssignableFrom( originClazz ) ) {
			if (Timestamp.class.equals(clazz))
				return new Timestamp( ((java.util.Date) origin).getTime() );
		}
		String s = String.valueOf(origin);
		try {
			Class mapped = (Class) clazzes.get( clazz );
			if (mapped != null) clazz = mapped;
			Constructor c = clazz.getConstructor(new Class[]{ String.class } );
			return c.newInstance( new Object[]{s} );
		}
		catch(NoSuchMethodException nsme) {
			throw new RuntimeException(clazz.getName() + " no constructor for string");
		}
	}
	
	private static Object findValue(String name, String[] names, Object[] objs) {
		for(int i = 0; i < names.length; i ++) {
			if ( name.equalsIgnoreCase(names[i]) )
				return objs[i];
		}
		return null;
	}
	
	private static final SimpleDateFormat sdf_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");
	
	private static String transform(Object obj) {
		if (obj == null) return "";
		if ( obj instanceof java.sql.Timestamp ) {
			synchronized( sdf_timestamp ) {
				return sdf_timestamp.format(obj);
			}
		}
		if (obj instanceof java.sql.Date) {
			synchronized( sdf_date ) {
				return sdf_date.format( (java.util.Date) obj );
			}
		}
		return String.valueOf(obj);
	}
}
