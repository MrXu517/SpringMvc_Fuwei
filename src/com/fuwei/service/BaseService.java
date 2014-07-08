package com.fuwei.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.keepsoft.commons.annotation.Column;
import net.keepsoft.commons.annotation.IdentityId;
import net.keepsoft.commons.annotation.RegexValidate;
import net.keepsoft.commons.annotation.Table;
import net.keepsoft.commons.annotation.Temporary;
import net.keepsoft.commons.exception.AuthenticationException;
import net.keepsoft.commons.utils.Tools;
import net.keepsoft.dao.Dao;
import net.keepsoft.sql.SQLHelperFactory;

import org.apache.commons.lang.StringUtils;

import com.fuwei.commons.Pager;

/**
 * Service基类
 * 
 * @author Administrator
 * 
 */
public class BaseService {

	@Resource(name = "dao")
	protected Dao dao;

	/**
	 * 
	 * @param sql
	 *            语句中必须要有order by
	 * @param pager
	 * @return
	 */
	protected Pager findPager(String sql, Pager pager, Object... args) {
		String countSql = SQLHelperFactory.getMqSqlHelper().getCountSQL(sql);
		String pagedSql = SQLHelperFactory.getMqSqlHelper().getPageSQL(sql, pager.getPageNo(),
				pager.getPageSize());
		pager.setTotalCount(this.dao.queryForObject(countSql, Integer.class, args));
		pager.setResult(this.dao.queryForListMap(pagedSql, args));
		int totalPageNum = (pager.getTotalCount() + pager.getPageSize() - 1) / pager.getPageSize();
		pager.setTotalPage(totalPageNum);
		return pager;
	}

	/**
	 * 
	 * @param sql
	 *            语句中必须要有order by
	 * @param pager
	 * @return
	 */
	//实际并没有分页，错误的方法，已废弃
	protected Pager findPagerComplex(String sql, Pager pager, Object... args) {
		String countSql = SQLHelperFactory.getMqSqlHelper().getCountSQL(sql);
		pager.setTotalCount(this.dao.queryForObject(countSql, Integer.class, args));
		pager.setResult(this.dao.queryForListMap(sql, args));
		int totalPageNum = (pager.getTotalCount() + pager.getPageSize() - 1) / pager.getPageSize();
		pager.setTotalPage(totalPageNum);

		return pager;
	}

	private String getTableName(Class<?> cls) {
		return cls.isAnnotationPresent(Table.class) ? cls.getAnnotation(Table.class).value() : cls.getSimpleName();
	}

	public void fileVerification(Field field, Object val) {

		String err = null;
		boolean nullable = field.getAnnotation(Column.class).nullable();
		String length = field.getAnnotation(Column.class).length();
		String label = field.getAnnotation(Column.class).label();
		if ((!nullable) && val.toString().trim().length() == 0)
			throw new AuthenticationException(label + "不能为空");
		if (StringUtils.isNotBlank(length)) {
			String[] lengths = length.split(",");
			int lenX = val.toString().replaceAll("[^\\x00-\\xff]", "**").length();
			if (lengths.length == 1) {
				if (lenX > Integer.valueOf(lengths[0])) {
					err = label + "长度不能大于" + lengths[0] + "个字符(一个中文等于两个字符)";
					throw new AuthenticationException(err);
				}
			} else if (lengths.length == 2) {
				if (lenX > Integer.valueOf(lengths[1]) || lenX < Integer.valueOf(lengths[0])) {
					err = label + "长度不能少于" + lengths[0] + "个字符或大于" + lengths[1] + "个字符(一个中文等于两个字符)";
					throw new AuthenticationException(err);
				}
			}
		}
		if (field.isAnnotationPresent(RegexValidate.class)) {
			boolean is = field.getAnnotation(RegexValidate.class).is();
			boolean r = Tools.isExp(val, field.getAnnotation(RegexValidate.class).key());
			boolean b = is ? !r : r;
			if (b) {
				throw new AuthenticationException(field.getAnnotation(RegexValidate.class).value());
			}
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map getBeans(Object requiredType, String primaryKey) {
		Map map = new HashMap();
		Class<?> cls = requiredType.getClass();
		String tableName = getTableName(cls);
		String names = "";
		String params = "";
		String id = primaryKey;
		Object idv = null;
		Field[] fields = cls.getDeclaredFields();
		List values = new ArrayList();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Temporary.class))
				continue;
			String fileName = field.getName();
			boolean columnBool = field.isAnnotationPresent(Column.class);
			if (StringUtils.isNotBlank(primaryKey)) {
				if (primaryKey.equalsIgnoreCase(fileName)) {
					idv = getValue(cls, requiredType, fileName);
					if (idv == null) {
						throw new AuthenticationException(primaryKey + "不能为空");
					}
					if (columnBool) {
						String temp = field.getAnnotation(Column.class).name();
						if (StringUtils.isBlank(temp))
							id = temp;
					}
					continue;
				}

			} else if (field.isAnnotationPresent(IdentityId.class)) {
				continue;
			}
			Object val = getValue(cls, requiredType, fileName);
			if (val == null)
				continue;

			String name = fileName;
			if (columnBool) {
				fileVerification(field, val);
				String nameTemp = field.getAnnotation(Column.class).name();
				if (StringUtils.isNotBlank(nameTemp))
					name = nameTemp;
			}

			names += "," + name;
			if (StringUtils.isBlank(primaryKey)) {
				params += ",?";
			} else {
				names += "=?";
			}
			values.add(val);
		}
		String sql = null;
		if (names.length() > 0) {
			if (StringUtils.isBlank(primaryKey)) {
				sql = "insert into " + tableName + "(" + names.substring(1) + ") values (" + params.substring(1) + ")";

			} else {
				sql = "update " + tableName + " set " + names.substring(1) + " where " + id + "=?";
				values.add(idv);
			}
		}

		map.put("sql", sql);
		map.put("values", values);
		return map;
	}

	protected int saveEntity(Object requiredType) {
		return saveEntity(requiredType, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected int saveEntity(Object requiredType, String primaryKey) {
		Map map = getBeans(requiredType, primaryKey);
		String sql = map.get("sql").toString();
		if (StringUtils.isBlank(sql))
			return 0;
		List values = (List) map.get("values");
		int size = values.size();
		Object[] obj = values.toArray(new Object[size]);
		return this.dao.update(sql, obj);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object insertEntityId(Object requiredType, String primaryKey) {
		Map map = getBeans(requiredType, null);
		List values = (List) map.get("values");
		int size = values.size();
		Object[] obj = values.toArray(new Object[size]);
		return this.dao.updateAndReturnPrimaryKeyWithKeyColunmName(map.get("sql").toString(), primaryKey, obj);
	}

	/**
	 * 
	 * @param requiredType
	 * @param primaryKey
	 * @param notField
	 * @param nullget 是否不获取值为null的值
	 * @return map key值： names,params,values分别为字段名，参数（），值 如是修改则 primaryKey：值
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map getTypeMap(Object requiredType, String primaryKey, String notField,Boolean nullget) {
		Map map = new HashMap();
		Class<?> cls = requiredType.getClass();
		String tableName = cls.isAnnotationPresent(Table.class) ? cls.getAnnotation(Table.class).value() : cls
				.getSimpleName();
		Field[] fields = cls.getDeclaredFields();
		String names = "";
		String params = "";
		List values = new ArrayList();
		for (Field field : fields) {

			String fileName = field.getName();

			if (field.isAnnotationPresent(Temporary.class))
				continue;

			if (StringUtils.isNotBlank(primaryKey)) {
				if (primaryKey.equalsIgnoreCase(fileName))
					continue;
				if (StringUtils.isNotBlank(notField) && notField.indexOf("," + fileName + ",") != -1)
					continue;
			} else {
				if (field.isAnnotationPresent(IdentityId.class))
					continue;
			}

			Object val = getValue(cls, requiredType, fileName);
			if(nullget & val == null){
				continue;
			}
			if (field.isAnnotationPresent(Column.class)) {
				boolean nullable = field.getAnnotation(Column.class).nullable();
				String length = field.getAnnotation(Column.class).length();
				String label = field.getAnnotation(Column.class).label();
				if ((!nullable) && (val == null || val.toString().trim().length() == 0))
					throw new AuthenticationException((StringUtils.isBlank(label) ? fileName : label) + "不能为空");

				if (val != null && StringUtils.isNotBlank(length)) {
					String[] lengths = length.split(",");
					int lenX = val.toString().replaceAll("[^\\x00-\\xff]", "**").length();
					if (lengths.length == 1) {
						if (lenX > Integer.valueOf(lengths[0])) {
							throw new AuthenticationException((StringUtils.isBlank(label) ? fileName : label)
									+ "长度不能大于" + lengths[0] + "个字符(一个中文等于两个字符)");
						}
					} else if (lengths.length == 2) {
						if (lenX > Integer.valueOf(lengths[1]) || lenX < Integer.valueOf(lengths[0])) {
							throw new AuthenticationException((StringUtils.isBlank(label) ? fileName : label)
									+ "长度不能少于" + lengths[0] + "个字符或大于" + lengths[1] + "个字符(一个中文等于两个字符)");
						}
					}
				}

				String name = field.getAnnotation(Column.class).name();
				names += "," + (StringUtils.isBlank(name) ? fileName : name);
				if (StringUtils.isNotBlank(primaryKey)) {
					names += "=?";
				} else {
					params += ",?";
				}
				values.add(val);
				continue;
			}

			if (val != null && field.isAnnotationPresent(RegexValidate.class)) {
				boolean is = field.getAnnotation(RegexValidate.class).is();
				boolean r = Tools.isExp(val, field.getAnnotation(RegexValidate.class).key());
				boolean b = is ? !r : r;
				if (b) {
					throw new AuthenticationException(field.getAnnotation(RegexValidate.class).value());
				}
			}

			names += "," + fileName;

			if (StringUtils.isNotBlank(primaryKey)) {
				names += "=?";
			} else {
				params += ",?";
			}
			values.add(val);

		}
		map.put("tableName", tableName);
		map.put("names", names.substring(1, names.length()));
		if (StringUtils.isBlank(primaryKey))
			map.put("params", params.substring(1, params.length()));
		else {
			Object o = getValue(cls, requiredType, primaryKey);
			if (o == null) {
				throw new AuthenticationException(primaryKey + "不能为空");
			} else {
				values.add(o);
			}
		}

		int size = values.size();
		Object[] obj = values.toArray(new Object[size]);
		if (StringUtils.isBlank(primaryKey)) {
			map.put("params", params.substring(1, params.length()));
		}
		map.put("values", obj);
		return map;

	}

	private Object getValue(Class<?> cls, Object requiredType, String fileName) {
		try {
			Method m = cls.getMethod("get" + Tools.firstUpper(fileName));
			Object o = m.invoke(requiredType);
			return (o == null) ? null : o;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 插入数据通用类
	 * 
	 * @param requiredType
	 *            实体
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected int insert(Object requiredType) {
		Map map = getTypeMap(requiredType, null, null,false);
		Object[] obj = (Object[]) map.get("values");
		String sql = "insert into " + map.get("tableName") + "(" + map.get("names") + ") values (" + map.get("params")
				+ ")";
		return this.dao.updateAndReturnPrimaryKey(sql, obj);

	}

	/**
	 * 插入数据，返回自增值
	 * 
	 * @param requiredType
	 * @param keyColunmName
	 *            自增ID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected Object insert(Object requiredType, String keyColunmName) {
		Map map = getTypeMap(requiredType, null, null,false);
		Object[] obj = (Object[]) map.get("values");
		String sql = "insert into " + map.get("tableName") + "(" + map.get("names") + ") values (" + map.get("params")
				+ ")";
		return this.dao.updateAndReturnPrimaryKeyWithKeyColunmName(sql, keyColunmName, obj);
	}

	/**
	 * 通用更新方法
	 * 
	 * @param requiredType
	 *            实体
	 * @param primaryKey
	 *            主键
	 * @param notField
	 *            不更新字段 ,c1,c2,c3,
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected int update(Object requiredType, String primaryKey, String notField) {
		Map map = getTypeMap(requiredType, primaryKey, notField,false);
		String sql = "update " + map.get("tableName") + " set " + map.get("names") + " where  " + primaryKey + "=?";
		Object[] obj = (Object[]) map.get("values");
		return this.dao.update(sql, obj);
	}
	
	/**
	 * 通用更新方法
	 * 
	 * @param requiredType
	 *            实体
	 * @param primaryKey
	 *            主键
	 * @param notField
	 *            不更新字段 ,c1,c2,c3,
	 * @param nullget 是否更新值为null的字段，更新则nullget = false,不更新则true           
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected int update(Object requiredType, String primaryKey, String notField,Boolean nullget) {
		Map map = getTypeMap(requiredType, primaryKey, notField,true);
		String sql = "update " + map.get("tableName") + " set " + map.get("names") + " where  " + primaryKey + "=?";
		Object[] obj = (Object[]) map.get("values");
		return this.dao.update(sql, obj);
	}
	
	/**
	 * 判断数据是否存在
	 * 
	 * @param tname
	 * @param field
	 * @param val
	 * @return
	 */
	protected Boolean isExist(String tname, String field, Object val) {
		String sql = "select count(*) from " + tname + " where " + field + "=?";
		Integer v = this.dao.queryForObject(sql, Integer.class, val);
		return v == 0 ? false : true;
	}
	
	public Boolean isEmpty(String tname){
		String sql = "select count(*) from "+tname;
		int i = this.dao.queryForObject(sql, Integer.class);
		return i==0?true:false;
	}
}
