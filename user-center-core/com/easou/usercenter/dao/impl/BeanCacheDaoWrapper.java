package com.easou.usercenter.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.usercenter.cache.AbstractCachedBeanProvide;
import com.easou.usercenter.cache.ValueEffect;

/**
 * 对指定单个bean进行数据操作类
 * 
 * @author river
 * 
 */
public abstract class BeanCacheDaoWrapper extends DynamicSqlSessionDaoSupport {

	/**
	 * 获取当前数据操作类所对应的表需要在缓存中建立索引的列名
	 * 
	 * @return
	 */
	protected abstract String[] getIndexColumnNames();

	/**
	 * 获取索引对应的key值
	 * 
	 * @return
	 */
	protected String getIndexKey(String tableName, String indexName,
			String indexValue) {
		return tableName + "@" + indexName + "@" + indexValue;
	}
	
	/**
	 * 获取实体数据所对应的key值
	 * 
	 * @param id
	 *            主键值
	 * @return
	 */
	protected String getIdKey(String tableName, String id) {
		return tableName + "@" + id;
	}

	Log log = LogFactory.getLog(BeanCacheDaoWrapper.class);

	/**
	 * 根据参数信息获取指定数据对象
	 * 
	 * @param <T>
	 * @param stateName
	 *            执行sql ID
	 * @param tableName
	 *            表名
	 * @param idName
	 *            主键名称
	 * @param indexName
	 *            索引名称
	 * @param indexValue
	 *            索引值
	 * @param reObjCla
	 *            返回数据类型
	 * @param effectTime
	 *            数据失效时间
	 * @return
	 * @throws Exception
	 */
	protected <T extends Object> T queryForObjectWithCache(
			final String stateName, String tableName, String idName,
			String indexName, final Object indexValue, Class<T> reObjCla,
			ValueEffect ef, Integer effectTime) throws Exception {
		AbstractCachedBeanProvide provide = new AbstractCachedBeanProvide(cache) {
			@Override
			public Object getDataFromSource() {
				Object obj = getSqlSession().selectOne(stateName, indexValue);
				return obj;
			}
		};
		provide.setCreateIndex(true);
		provide.setEfficTime(effectTime);
		provide.setIdName(idName);
		provide.setIndexName(indexName);
		if (ef != null)
			provide.setEf(ef);
		if (indexValue instanceof String)
			provide.setIndexValue(indexValue.toString());
		else if (indexValue instanceof Map) {
			StringBuffer buffer = new StringBuffer();
			Map maValue = (Map) indexValue;
			Set<Entry> ens = maValue.entrySet();
			for (Entry en : ens) {
				buffer.append(en.getValue() + "$");
			}
			provide.setIndexValue(buffer.toString());
		} else {
			new RuntimeException("indexValue 值类型只能为string与map");
		}
		provide.setTableName(tableName);
		provide.setRootDataType(reObjCla);
		return (T) provide.getData();
	}

	/**
	 * 根据参数信息获取指定数据对象
	 * 
	 * @param <T>
	 * @param stateName
	 *            执行sql ID
	 * @param tableName
	 *            表名
	 * @param idName
	 *            主键名称
	 * @param idValue
	 *            主键值
	 * @param reObjCla
	 *            返回数据类型
	 * @param effectTime
	 *            数据失效时间
	 * @return
	 * @throws Exception
	 */
	protected <T extends Object> T queryForObjectWithCacheById(
			final String stateName, String tableName, String idName,
			final String idValue, Class<T> reObjCla, Integer effectTime)
			throws Exception {
		AbstractCachedBeanProvide provide = new AbstractCachedBeanProvide(cache) {
			@Override
			public Object getDataFromSource() {
				Object obj = getSqlSession().selectOne(stateName, idValue);
				return obj;
			}
		};
		provide.setCreateIndex(false);
		provide.setEfficTime(effectTime);
		provide.setId(idValue);
		provide.setIdName(idName);
		provide.setTableName(tableName);
		provide.setRootDataType(reObjCla);
		return (T) provide.getData();
	}

	/**
	 * 根据主键更新数据信息
	 * 
	 * @param tableName
	 *            表名
	 * @param idValue
	 *            主键值
	 * @param statementName
	 *            执行sql ID
	 * @param obj
	 *            参数信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected boolean updateFiledValueWitchId(String tableName, String idValue,
			String statementName, Map obj, Class beanType, boolean delete)
			throws Exception {

		boolean bo = false;

		try {
			// 获取实体数据
			// Map<String, String> ob = cache.hGetAll(tableName + "@" +
			// idValue);
			Object ob = cache.hGetBean(tableName + "@" + idValue, beanType);
			List<String> indexValue = new ArrayList<String>();
			if (null != ob) {
				String[] indexColumn = this.getIndexColumnNames();
				Object newOb = null;
				for (String column : indexColumn) {
					newOb = obj.get(column);
					if (newOb != null) {
						Object value = PropertyUtils.getProperty(ob, column);
						if (log.isDebugEnabled()) {
							log.debug(column + "索引值" + value);
						}
						if (null != value) {
							indexValue.add(getIndexKey(tableName, column, value
									.toString()));
						}
					}
				}
			}

			if (log.isInfoEnabled()) {
				log.info("需要删除的索引: " + indexValue);
			}
			if (indexValue.size() > 0) {
				// 删除索引信息
				bo = cache.mdel(indexValue
						.toArray(new String[indexValue.size()]));
				if (!bo) {
					if (log.isInfoEnabled()) {
						log.info("删除索引信息失败");
					}
				}
			}
			if (delete) {
				bo = cache.del(getIdKey(tableName, idValue));
			} else {
				bo = cache.hmset(getIdKey(tableName, idValue), obj);
			}
			if (!bo) {
				throw new Exception("更新缓存数据失败!");
			}
		} catch (Exception e) {
			log.error("缓存更新失败", e);
		}
		// 更新数据库信息
		obj.put("id", idValue);
		int in = getSqlSession().update(statementName, obj);
		if (in < 0)
			return false;
		return bo;
	}

	/**
	 * 将指定的数据对像添加到数据源
	 * 
	 * @param tableName
	 *            表名称
	 * @param idValue
	 *            主键值
	 * @param statementName
	 *            执行sql id
	 * @param paramterBean
	 *            参数对象信息
	 * @return
	 */
	protected Object insertWitchCacheForId(String statementName, String tableName, String idValue, Object paramterBean) throws Exception {
		cache.setHBean(getIdKey(tableName, idValue), paramterBean);
		return getSqlSession().insert(statementName, paramterBean);
	}

	/**
	 * 将指定数据从数据源中删除
	 * 
	 * @param tableName
	 *            表名称
	 * @param idValue
	 *            ID值
	 * @param statementName
	 *            执行sql id
	 * @return
	 */
	protected int deleteWitchCache(String statementName, String tableName, Long idValue) throws Exception {
		cache.del(getIdKey(tableName, idValue+""));
		return getSqlSession().delete(statementName, idValue);
	}
	
	/**
	 * 按表名参数值返回列表
	 * @param stateName
	 * @param tableName
	 * @param parameter
	 * @param effectTime
	 * @return
	 * @throws Exception
	 */
	protected List queryForListWithCache(final String stateName, final String tableName, final Object parameter, Integer effectTime) throws Exception {
		String keyValue = "";
		if (parameter instanceof String || parameter instanceof Long
				|| parameter instanceof Integer)
			keyValue = this.getIdKey(tableName, parameter.toString());
		else if (parameter instanceof Map) {
			StringBuffer buffer = new StringBuffer();
			Map maValue = (Map) parameter;
			Set<Entry> ens = maValue.entrySet();
			for (Entry en : ens) {
				buffer.append(en.getValue() + "$");
			}
			keyValue = getIdKey(tableName, buffer.toString());
		} else {
			new RuntimeException("indexValue 值类型只能为string, long, integer与map");
		}
		List list = null;
		long start = System.currentTimeMillis();
		try {
			list = cache.get(keyValue, List.class);
			if(log.isInfoEnabled()) {
				log.info("从缓存中获取数据 " + keyValue + " 花费时间：" + (System.currentTimeMillis() - start) + "毫秒");
			}
		} catch (Exception e) {
			log.error(e,e);
		}
		if (null == list) {
			if (log.isInfoEnabled()) {
				log.info("缓存中不存在值,key:" + keyValue);
			}
			start = System.currentTimeMillis();
			list = getSqlSession().selectList(stateName, parameter);
			if (null != list && list.size()>0) {
				try {
					cache.set(keyValue, list, effectTime);
				} catch (Exception e) {
					log.error(e,e);
				}
				if (log.isInfoEnabled()) {
					log.info("从数据源获得查询结果,写入缓存,key:" + keyValue + " 耗时: " + (System.currentTimeMillis() - start) + "毫秒");
				}
			}
			return list;
		} 
		return list;
	}
	
	/**
	 * 按参数返回一个object
	 * @param <T>
	 * @param stateName
	 * @param tableName
	 * @param parameter
	 * @param reObjCla
	 * @param effectTime
	 * @return
	 * @throws Exception
	 */
	protected <T extends Object> T queryForObjectWithNoIndexCache(
			final String stateName, final String tableName, final Object parameter, Class<T> reObjCla,Integer effectTime) throws Exception {
		AbstractCachedBeanProvide provide = new AbstractCachedBeanProvide(cache) {
			@Override
			public Object getDataFromSource() {
				Object obj = getSqlSession().selectOne(stateName, parameter);
				return obj;
			}
		};
		provide.setCreateIndex(false);
		provide.setEfficTime(effectTime);
		provide.setTableName(tableName);
		if (parameter instanceof String)
			provide.setId(parameter.toString());
		else if (parameter instanceof Map) {
			StringBuffer buffer = new StringBuffer();
			Map maValue = (Map) parameter;
			Set<Entry> ens = maValue.entrySet();
			for (Entry en : ens) {
				buffer.append(en.getValue() + "$");
			}
			provide.setId(buffer.toString());
		}
		provide.setRootDataType(reObjCla);
		return (T) provide.getData();
	}
}
