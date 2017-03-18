package com.blog.comm.handle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.blog.comm.annotation.Column;
import com.blog.comm.base.BaseModel;
import com.blog.comm.entity.BeanEntity;
import com.blog.comm.entity.Pager;
import com.blog.comm.entity.SQLEntity;
import com.blog.comm.entity.Where;
import com.blog.comm.util.JdbcUtil;
import com.blog.comm.util.PropertUtil;
import com.blog.comm.util.StringUtil;

/**
 * 数据库操作
 * @author DH
 *
 */
@Repository
public class JdbcHandle {
	

	/**
	 * 主库数据源
	 */
	@Resource
	private JdbcTemplate jdbcTemplate;
	

	Logger logger=Logger.getLogger(this.getClass());
	
	/**
	 * 执行SQL查询语句
	 * 
	 * @param sql
	 * @param paraMap
	 *            参数map容器
	 * @return 结果集
	 */
	private List<Map<String, Object>> baseQuery(String sql, Object... paras) {
		Long threadId = Thread.currentThread().getId();
		try {
			logger.debug("[线程ID："+threadId+"][执行语句:"+parseParams(sql, paras)+"]");
			return jdbcTemplate.queryForList(sql, paras);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 执行SQL更新语句
	 * 
	 * @param sql
	 *            语句
	 * @param paraMap
	 *            参数
	 * @return
	 */
	private Integer baseUpdate(final String sql,
			final Object...paras) {
		final Long threadId = Thread.currentThread().getId();
		try {
			logger.debug("[线程ID："+threadId+"][执行语句:"+parseParams(sql, paras)+"]");
			if (!sql.toLowerCase().trim().startsWith("insert")) {
				return jdbcTemplate.update(sql, paras);
			}
			KeyHolder keyHolder = new GeneratedKeyHolder();
			Integer code = jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql,
							PreparedStatement.RETURN_GENERATED_KEYS);
					for(int i=0;i<paras.length;i++){
						ps.setObject(i+1, paras[i]);
					}
					return ps;
				}
			}, keyHolder);
			List<Map<String, Object>> list=keyHolder.getKeyList();
			if (StringUtil.isNullOrEmpty(list)||list.size()>1) {
				return code;
			}
			if (StringUtil.isNullOrEmpty(list.get(0))||list.get(0).size()>1) {
				return code;
			}
			try {
				Integer obj=keyHolder.getKey().intValue();
				return obj;
			} catch (Exception e) {
				e.printStackTrace();
				return code;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	
	/**
	 * 查询功能区 -start
	 */

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 * @param paraMap
	 *            参数map容器
	 * @return 结果集
	 */
	public List<Map<String, Object>> query(String sql, Object... paras) {
		return baseQuery(sql, paras);
	}

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> query(String sql) {
		return baseQuery(sql, new Object[]{});
	}

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 * @return
	 */
	public Map<String, Object> queryFirst(String sql, Object... paras) {
		if(!sql.toLowerCase().contains("limit")){
			sql=sql+" limit 1";
		}
		List<Map<String, Object>> list = query(sql, paras);
		if (StringUtil.isNullOrEmpty(list)) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 * @return
	 */
	public Map<String, Object> queryFirst(String sql) {
		return queryFirst(sql, new Object[] {});
	}

	/**
	 * 执行SQL语句获得任意类型结果
	 * @param clazz 返回类型
	 * @param sql sql语句
	 * @param paras 参数列表
	 * @return
	 */
	public <T> T queryAuto(Class<?> clazz,String sql, Object... paras) {
		List<T> list=queryAutos(clazz, sql, paras);
		if(StringUtil.isNullOrEmpty(list)){
			return null;
		}
		return list.get(0);
	}
	/**
	 * 执行SQL语句获得任意类型结果
	 * @param clazz 返回类型
	 * @param sql sql语句
	 * @param paras 参数列表
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public <T> List<T> queryAutos(Class<?> clazz,String sql, Object... paras) {
		List<Map<String, Object>> records = query(sql, paras);
		if (StringUtil.isNullOrEmpty(records)) {
			return null;
		}
		Map<String, Object> rec = records.get(0);
		if(BaseModel.class.isAssignableFrom(clazz)){
			List<T> list=new ArrayList<T>();
			for(Map<String, Object> line:records){
				T t= PropertUtil.mapToModel(rec, clazz);
				if(!StringUtil.isNullOrEmpty(t)){
					list.add(t);
				}
			}
			return list;
		}
		sql=formatSql(sql);
		List list=new ArrayList();
		for(Map<String, Object> line:records){
			Object value= PropertUtil.parseValue(new ArrayList<Object>(line.values()).get(0), clazz);
			if(StringUtil.isNullOrEmpty(value)){
				if (sql.contains("select count(") || sql.contains("select sum(")|| sql.contains("select avg(")) {
					list.add(PropertUtil.parseValue(0, clazz));
				}
				continue;
			}
			list.add(value);
		}
		return list;
	}
	
	public List<?> queryField(Class<?> fieldType, String sql,Object... paras) {
		List<Map<String, Object>> recs= query(sql, paras);
		if(StringUtil.isNullOrEmpty(recs)){
			return null;
		}
		List<Object> list=new ArrayList<Object>();
		for(Map<String, Object> rec:recs){
			if(StringUtil.isNullOrEmpty(rec)){
				continue;
			}
			for(String key:rec.keySet()){
				Object value=rec.get(key);
				if(!StringUtil.isNullOrEmpty(value)){
					try {
						value=PropertUtil.parseValue(value, fieldType);
						list.add(value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			}
		}
		return list;
	}
	/**
	 * 根据多个字段查询对象
	 * 
	 * @param cla
	 *            类
	 * @param paraMap
	 *            条件集合
	 * @return
	 */
	public <T> List<T> findBean(Class<?> cla,
			Map<String, Object> paraMap) {
		List<Map<String, Object>> recs = findRecord(cla, paraMap, null, null);
		return JdbcUtil.parseBeans(cla, recs);
	}

	/**
	 * 根据多个字段查询对象
	 * 
	 * @param cla
	 *            类
	 * @param paraMap
	 *            条件集合
	 * @return
	 */
	public <T> List<T> findBean(Class<?> cla,
			Map<String, Object> paraMap, String orderField, Boolean isDesc) {
		List<Map<String, Object>> recs = findRecord(cla, paraMap, orderField, isDesc);
		return JdbcUtil.parseBeans(cla, recs);
	}
	/**
	 * 根据字段查询对象
	 * 
	 * @param cla
	 *            类
	 * @param fieldName
	 *            字段名
	 * @param fieldValue
	 *            字段值,可支持集合与数组IN查询
	 * @return
	 */
	public <T> List<T> findBean(Class<?> cla,
			String fieldName, Object fieldValue, String orderField,
			Boolean isDesc) {
		Map<String, Object> paraMap=new HashMap<String, Object>();
		paraMap.put(fieldName, fieldValue);
		List<Map<String, Object>> recs = findRecord(cla, paraMap,orderField,
				isDesc);
		return JdbcUtil.parseBeans(cla, recs);
	}

	/**
	 * 根据字段查询对象
	 * 
	 * @param cla
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public <T> List<T> findBean(Class<?> cla, String fieldName,
			Object fieldValue) {
		return findBean(cla, fieldName, fieldValue, null, null);
	}

	
	/**
	 * 根据对象查询对象集合
	 * 
	 * @param obj
	 *            对象
	 * @param where
	 *            where条件
	 * @param pager
	 *            分页对象
	 * @return
	 */
	public <T> List<T> findBean(Object obj, Where where, Pager pager) {
		List<Map<String, Object>> list = findRecord(obj, where, pager, null, null);
		return JdbcUtil.parseBeans(getObjectClass(obj), list);
	}

	/**
	 * 根据对象查询对象集合
	 * 
	 * @param obj
	 *            对象
	 * @param where
	 *            where条件
	 * @param pager
	 *            分页对象
	 * @return
	 */
	public <T> List<T> findBean(Object obj, Where where, Pager pager,
			String orderField, Boolean isDesc) {
		List<Map<String, Object>> list = findRecord(obj, where, pager, orderField, isDesc);
		return JdbcUtil.parseBeans(getObjectClass(obj), list);
	}

	/**
	 * 根据对象查询对象集合
	 * 
	 * @param obj
	 *            对象
	 * @param where
	 *            where条件
	 * @param pager
	 *            分页对象
	 * @return
	 */
	public <T> List<T> findBean(Object obj, Where where,
			String orderField, Boolean isDesc) {
		List<Map<String, Object>> list = findRecord(obj, where, null, orderField, isDesc);
		return JdbcUtil.parseBeans(getObjectClass(obj), list);
	}

	/**
	 * 根据对象查询对象集合
	 * 
	 * @param obj
	 *            对象
	 * @param where
	 *            where条件
	 * @param pager
	 *            分页对象
	 * @return
	 */
	public <T> List<T> findBean(Object obj, Pager pager) {
		List<Map<String, Object>> list = findRecord(obj, null, pager, null, null);
		return JdbcUtil.parseBeans(getObjectClass(obj), list);
	}

	/**
	 * 根据对象查询对象
	 * 
	 * @param obj
	 *            对象条件
	 * @param where
	 *            where条件
	 * @return
	 */
	public <T> List<T> findBean(Object obj, Where where) {
		List<Map<String, Object>> list = findRecord(obj, where, null, null, null);
		return JdbcUtil.parseBeans(getObjectClass(obj), list);
	}

	/**
	 * 根据obj内部字段名和值进行查询，默认条件为等于
	 * 
	 * @param obj
	 * @return
	 */
	public <T> List<T> findBean(Object obj) {
		List<Map<String, Object>> list = findRecord(obj, null, null, null, null);
		return JdbcUtil.parseBeans(getObjectClass(obj), list);
	}
	
	
	/**
	 * 根据字段查询对象
	 * 
	 * @param cla
	 * @param fieldName
	 * @param fieldValue
	 * @param orderField
	 * @param isDesc
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T findBeanFirst(Class<?> cla, String fieldName,
			Object fieldValue, String orderField, Boolean isDesc) {
		List<Object> list = (List<Object>) findBean(cla, fieldName,
				fieldValue, orderField, isDesc);
		if (StringUtil.isNullOrEmpty(list)) {
			return null;
		}
		return (T) list.get(0);
	}

	/**
	 * 根据字段查询对象
	 * 
	 * @param cla
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public <T> T findBeanFirst(Class<?> cla, String fieldName,
			Object fieldValue) {
		return findBeanFirst(cla, fieldName, fieldValue, null, null);
	}
	/**
	 * 根据对象条件进行查询
	 * 
	 * @param cla
	 * @param fieldName
	 * @param fieldValue
	 * @param orderField
	 * @param isDesc
	 * @return
	 */
	public <T> T findBeanFirst(Object obj, Where where,
			String orderField, Boolean isDesc) {
		List<Map<String, Object>> list = findRecord(obj, where, null, orderField, isDesc);
		if (StringUtil.isNullOrEmpty(list)) {
			return null;
		}
		return JdbcUtil.parseBean(getObjectClass(obj), list.get(0));
	}

	/**
	 * 根据对象条件进行查询
	 * 
	 * @param obj
	 * @param where
	 * @return
	 */
	public <T> T findBeanFirst(Object obj, Where where) {
		return findBeanFirst(obj, where, null, null);
	}

	/**
	 * 根据对象条件进行查询
	 * 
	 * @param obj
	 * @return
	 */
	public <T> T findBeanFirst(Object obj) {
		return findBeanFirst(obj, null, null, null);
	}
	/**
	 * 根据字段查询结果集
	 * 
	 * @param cla
	 * @param paraMap
	 * @return
	 */
	public <T> T findBeanFirst(Class<?> cla,
			Map<String, Object> paraMap) {
		Map<String, Object> record = findRecordFirst(cla, paraMap, null, null);
		return JdbcUtil.parseBean(cla, record);
	}

	/**
	 * 根据对象查询结果集
	 * 
	 * @param obj
	 *            对象条件
	 * @param where
	 *            where条件
	 * @param pager
	 *            分页信息
	 * @return
	 */
	public List<Map<String, Object>> findRecord(Object obj, Where where, Pager pager,
			String orderField, Boolean isDesc) {
		SQLEntity sqlEntity = JdbcUtil.parseSQL(obj, where, pager, orderField,
				isDesc);
		return baseQuery(sqlEntity.getSql(), sqlEntity.getParams());
	}

	

	
	/**
	 * 根据字段查询结果集
	 * 
	 * @param cla
	 *            类
	 * @param paraMap
	 *            多个字段
	 * @return
	 */
	public List<Map<String, Object>> findRecord(Class<?> cla,
			Map<String, Object> paraMap, String orderField, Boolean isDesc) {
		Where where = new Where();
		List<BeanEntity> entitys=PropertUtil.getBeanFields(cla);
		if (!StringUtil.isNullOrEmpty(paraMap)) {
			for (String key : paraMap.keySet()) {
				Object value=paraMap.get(key);
				BeanEntity entity=PropertUtil.getByList(entitys, "fieldName", key);
				Column column=(Column) entity.getAnnotation(Column.class);
				if(column==null){
					key=JdbcUtil.unParsParaName(key);
				}else{
					key=column.value();
				}
				if (StringUtil.isNullOrEmpty(value)) {
					where.set(key, "is null", new Object[] {  });
					continue;
				}
				if (value instanceof Collection<?>) {
					if (value instanceof Collection<?>) {
						where.set(key, "in",
								((Collection<?>) value).toArray());
					}
					continue;
				}
				if (value.getClass().isArray()) {
					if (value instanceof Object[]) {
						where.set(key, "in", (Object[]) value);
					}
					continue;
				}
				where.set(key, value);
			}
		}
		SQLEntity sqlEntity = JdbcUtil.parseSQL(cla, where, null, orderField,
				isDesc);
		return baseQuery(sqlEntity.getSql(), sqlEntity.getParams());
	}

	/**
	 * 根据字段查询结果集
	 * 
	 * @param cla
	 * @param paraMap
	 * @param orderField
	 * @param isDesc
	 * @return
	 */
	public Map<String, Object> findRecordFirst(Class<?> cla, Map<String, Object> paraMap,
			String orderField, Boolean isDesc) {
		List<Map<String, Object>> list = findRecord(cla, paraMap, orderField, isDesc);
		if (StringUtil.isNullOrEmpty(list)) {
			return null;
		}
		return list.get(0);
	}
	/**
	 * 分页查询
	 * 
	 * @param obj
	 *            对象条件
	 * @param pager
	 *            分页信息
	 * @return
	 */
	public Pager findPager(Object obj, Pager pager) {
		return findPager(obj, null, pager, null, null);
	}

	/**
	 * 分页查询
	 * 
	 * @param obj
	 *            对象条件
	 * @param pager
	 *            分页信息
	 * @return
	 */
	public Pager findPager(Object obj, Pager pager,
			String orderField, Boolean isDesc) {
		return findPager(obj, null, pager, orderField, isDesc);
	}

	/**
	 * 分页查询
	 * 
	 * @param obj
	 *            对象条件
	 * @param where
	 *            where条件
	 * @param pager
	 *            分页条件
	 * @return
	 */
	public Pager findPager(Object obj, Where where, Pager pager,
			String orderField, Boolean isDesc) {
		SQLEntity sqlEntity = JdbcUtil.parseSQL(obj, where, pager, orderField,
				isDesc);
		Integer totalRows = getCount(sqlEntity.getSql(), sqlEntity.getParams());
		pager.setTotalRows(totalRows);
		List<Map<String, Object>> list = baseQuery(sqlEntity.getSql(),
				sqlEntity.getParams());
		List<?> objList = JdbcUtil.parseBeans(getObjectClass(obj), list);
		pager.setData(objList);
		return pager;
	}

	/**
	 * 分页查询
	 * 
	 * @param obj
	 * @param where
	 * @return
	 */
	public Pager findPager(Object obj, Where where) {
		return findPager(obj, where, null, null, null);
	}

	/**
	 * 分页查询
	 * 
	 * @param obj
	 * @param orderField
	 * @param isDesc
	 * @return
	 */
	public Pager findPager(Object obj, String orderField,
			Boolean isDesc) {
		return findPager(obj, null, null, orderField, isDesc);
	}
	
	

	/**
	 * 根据语句和条件查询总记录数
	 * 
	 * @param sql
	 *            语句
	 * @param map
	 *            条件容器
	 * @return
	 */
	public Integer getCount(String sql, Object...params) {
		sql = parsCountSql(sql);
		Integer count = queryAuto(Integer.class,sql, params);
		return count;
	}

	/**
	 * 根据sql语句查询总记录数
	 * 
	 * @param sql
	 * @return
	 */
	public Integer getCount(String sql) {
		return getCount(sql, new Object[]{});
	}

	

	/**
	 * 查询功能区 -end
	 */

	/**
	 * 更新功能区 -start
	 */
	
	/**
	 * 更新操作
	 * 
	 * @param sql
	 * @param objs
	 * @return
	 */
	public Integer doUpdate(String sql, Object... objs) {
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		for (Object obj : objs) {
			map.put(map.size() + 1, obj);
		}
		return baseUpdate(sql, map);
	}

	/**
	 * 更新操作
	 * 
	 * @param sql
	 * @return
	 */
	public Integer doUpdate(String sql) {
		return baseUpdate(sql, new Object[]{});
	}



	/**
	 * 根据对象进行更新
	 * 
	 * @param obj
	 * @return
	 */
	public Integer update(Object obj) {
		return update(obj, "id");
	}

	/**
	 * 根据对象进行更新
	 * 
	 * @param obj
	 * @param priKeyNames
	 * @return
	 */
	public Integer update(Object obj, String... priKeyNames) {
		try {
			if (obj == null) {
				return -1;
			}
			// 获取表名
			String tableName = JdbcUtil.getTableName(obj);
			// 获取属性列表
			List<BeanEntity> prpres = PropertUtil.getBeanFields(obj);
			if (prpres == null || prpres.isEmpty()) {
				return -1;
			}
			List<String> priKeys = Arrays.<String> asList(priKeyNames);
			// 拼接SQL语句
			StringBuilder sql = new StringBuilder(MessageFormat.format(
					"update {0} set ", tableName));
			Map<Integer, Object> paraMap = new TreeMap<Integer, Object>();
			BeanEntity vo = null;
			String fieldName = null;
			for (int i = 0; i < prpres.size(); i++) {
				vo = prpres.get(i);
				if (vo != null) {
					fieldName = JdbcUtil.getFieldName(vo);
					if (fieldName == null || "".equals(fieldName)) {
						continue;
					}
					if (priKeys.contains(fieldName)) {
						continue;
					}
					if (vo.getFieldValue() == null) {
						continue;
					}
					sql.append(fieldName).append("=?");
					// 封装参数
					paraMap.put(paraMap.size() + 1, vo.getFieldValue());
					if (i < prpres.size() - 1) {
						sql.append(",");
					}
				}
			}
			if (sql.toString().endsWith(",")) {
				sql = new StringBuilder(sql.toString().substring(0,
						sql.toString().length() - 1));
			}
			sql.append(" where ");
			for (int i = 0; i < priKeyNames.length; i++) {
				Object fieldValue = PropertUtil.getFieldValue(obj,
						priKeyNames[i]);
				if (StringUtil.isNullOrEmpty(fieldValue)) {
					sql.append(MessageFormat.format(" {0} is null  ",
							priKeyNames[i]));
				} else {
					sql.append(MessageFormat.format(" {0}=? ", priKeyNames[i]));
					paraMap.put(paraMap.size() + 1, fieldValue);
				}
				if (i != priKeyNames.length - 1) {
					sql.append(" and ");
				}
			}
			return baseUpdate(sql.toString(), paraMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 更新功能区 -end
	 */

	/**
	 * 更新功能区 -end
	 */

	/**
	 * 插入功能区 -start
	 */
	/**
	 * 根据对象条件进行插入
	 * 
	 * @param obj
	 * @return
	 */
	public Integer insert(Object obj) {
		try {
			if (obj == null) {
				return -1;
			}
			// 获取表名
			String tableName =JdbcUtil.getTableName(obj);
			// 获取属性列表
			List<BeanEntity> prpres = PropertUtil.getBeanFields(obj);
			if (prpres == null || prpres.isEmpty()) {
				return -1;
			}
			// 拼接SQL语句
			StringBuilder sql = new StringBuilder(MessageFormat.format(
					"insert into {0} set ", tableName));
			Map<Integer, Object> paraMap = new TreeMap<Integer, Object>();
			BeanEntity vo = null;
			String fieldName = null;
			for (int i = 0; i < prpres.size(); i++) {
				vo = prpres.get(i);
				if (vo != null) {
					fieldName = JdbcUtil.getFieldName(vo);
					if (fieldName == null || "".equals(fieldName)) {
						continue;
					}
					if (vo.getFieldValue() == null) {
						continue;
					}
					sql.append(fieldName).append("=?");
					// 封装参数
					paraMap.put(paraMap.size() + 1, vo.getFieldValue());
					if (i < prpres.size() - 1) {
						sql.append(",");
					}
				}
			}
			if (sql.toString().endsWith(",")) {
				sql = new StringBuilder(sql.toString().substring(0,
						sql.toString().length() - 1));
			}
			return baseUpdate(sql.toString(), paraMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 根据对象进行插入或更新
	 * 
	 * @param obj
	 * @param priKeyName
	 * @return
	 */
	public Integer saveOrUpdateAuto(Object obj) {
		return saveOrUpdateAuto(obj, new String[]{});
	}
	/**
	 * 保存或更新
	 * @param obj 欲保存的对象
	 * @param addFields 当数据存在时累加的字段
	 * @return
	 */
	public Integer saveOrUpdateAuto(Object obj,String...addFields) {
		if (obj == null) {
			return -1;
		}
		// 获取表名
		String tableName = JdbcUtil.getTableName(obj);
		// 拼接SQL语句
		StringBuilder sql = new StringBuilder(MessageFormat.format(
				"insert into {0} set ", tableName));
		List<Object> paras=new ArrayList<Object>();
		String diySql=parseFieldSql(obj,paras);
		if(StringUtil.isNullOrEmpty(diySql)){
			return -1;
		}
		sql.append(diySql);
		sql.append(" ON DUPLICATE KEY UPDATE ");
		diySql=parseFieldSql(obj,paras,addFields);
		sql.append(diySql);
		return baseUpdate(sql.toString(),paras);
	}
	private String parseFieldSql(Object obj,List<Object> params,String...addFields){
		List<BeanEntity> prpres = PropertUtil.getBeanFields(obj);
		StringBuilder sql = new StringBuilder();
		BeanEntity vo = null;
		String fieldName = null;
		List<String> addFieldList=null;
		if(!StringUtil.isNullOrEmpty(addFields)){
			addFieldList=Arrays.asList(addFields);
		}
		if(StringUtil.isNullOrEmpty(addFieldList)){
			addFieldList=new ArrayList<String>();
		}
		for (int i = 0; i < prpres.size(); i++) {
			vo = prpres.get(i);
			if (vo != null) {
				fieldName = JdbcUtil.getFieldName(vo);
				if (fieldName == null || "".equals(fieldName)) {
					continue;
				}
				if (vo.getFieldValue() == null) {
					continue;
				}
				if(addFieldList.contains(fieldName)||addFieldList.contains(vo.getFieldName())){
					sql.append(fieldName).append("=").append(fieldName).append("+").append("?");
				}else{
					sql.append(fieldName).append("=?");
				}
				// 封装参数
				params.add(vo.getFieldValue());
				if (i < prpres.size() - 1) {
					sql.append(",");
				}
			}
		}
		if (sql.toString().endsWith(",")) {
			sql = new StringBuilder(sql.toString().substring(0,
					sql.toString().length() - 1));
		}
		return sql.toString();
	}

	private String parseFieldSql(Object obj,List<Object> params){
		return parseFieldSql(obj, params, new String[]{});
	}
	/**
	 * 插入功能区 -end
	 */

	/**
	 * 内部方法 -start
	 */
	private Class<?> getObjectClass(Object obj) {
		if (obj instanceof java.lang.Class) {
			return (Class<?>) obj;
		}
		return obj.getClass();
	}


	private String parsCountSql(String sql) {
		while (sql.indexOf("  ") > -1) {
			sql = sql.replace("  ", " ");
		}
		Integer formIndex = sql.toLowerCase().indexOf("from");
		if (formIndex > -1) {
			sql = sql.substring(formIndex, sql.length());
		}
		Integer orderIndex = sql.toLowerCase().indexOf("order by");
		if (orderIndex > -1) {
			sql = sql.substring(0, orderIndex);
		}
		Integer limitIndex = sql.toLowerCase().indexOf("limit");
		while (limitIndex > -1) {
			String firstSql = sql.substring(0, limitIndex);
			String lastSql = sql.substring(limitIndex);
			if (lastSql.indexOf(")") > -1) {
				lastSql = lastSql.substring(lastSql.indexOf(")"));
				firstSql = firstSql + lastSql;
			}
			sql = firstSql;
			limitIndex = sql.toLowerCase().indexOf("limit");
		}
		if (orderIndex > -1) {
			sql = sql.substring(0, orderIndex);
		}
		sql = "select count(*) " + sql;
		return sql;
	}

	private static String formatSql(String sql) {
		while (sql.contains("  ")) {
			sql = sql.replace("  ", " ");
		}
		return sql.toLowerCase();
	}

	private static String parseParams(String sql,Object... params){
		sql+=" ";
		String [] sqlRanks=sql.split("\\?");
		if(sqlRanks.length==1){
			return sql;
		}
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<sqlRanks.length;i++){
			sb.append(sqlRanks[i]);
			if(i!=sqlRanks.length-1){
				Object value=params[i];
				if(!StringUtil.isNullOrEmpty(value)){
					if(Date.class.isAssignableFrom(value.getClass())){
					value=dateToString((Date)value);
					}
					if(String.class.isAssignableFrom(value.getClass())){
						value="'"+value+"'";
					}
				}
				sb.append(value);
			}
		}
		return sb.toString();
	}
	/**
	 * 根据时间变量返回时间字符串
	 * 
	 * @return 返回时间字符串
	 * @param pattern
	 *            时间字符串样式
	 * @param date
	 *            时间变量
	 */
	public static String dateToString(Date date) {
		if (date == null) {
			return null;
		}
		try {
			SimpleDateFormat sfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sfDate.format(date);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void main(String[] args) {
		String sql="select * from table where uid=? and status=? and createTime>?";
		System.out.println(parseParams(sql, 1001,1,new Date()));
	}
}
