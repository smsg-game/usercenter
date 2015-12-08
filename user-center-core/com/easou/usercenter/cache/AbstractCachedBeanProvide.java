package com.easou.usercenter.cache;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.usercenter.cache.jedis.EJedis;

/**
 * 对单个java bean数据对象进行缓存
 * 
 * @author river
 * 
 */
public abstract class AbstractCachedBeanProvide {
	protected EJedis cache;
	// 缓存key值
	protected String id;

	// 相对失效时间值(单位:秒)
	protected Integer efficTime;

	// 失效策略
	protected ValueEffect ef = ValueEffect.ALLEFFECT;

	protected Class rootDataType;

	// 需要建立索引的主键名称
	private String idName;
	// 索引名称
	private String indexName;

	private String indexValue;

	private boolean isCreateIndex = false;

	private String tableName;

	Log cacheLog = LogFactory.getLog(AbstractCachedBeanProvide.class);

	/**
	 * 根据索引信息从缓存中获取数据
	 * 
	 * @return
	 */
	private Object getDataFromCacheByIndex() {
		Object obj = null;
		String indexKey = getIndexKey();
		if (cacheLog.isInfoEnabled()) {
			cacheLog.info("需要创建索引信息,获取到的索引key值为: " + indexKey);
		}
		String id = cache.get(indexKey);
		if (cacheLog.isInfoEnabled()) {
			cacheLog.info("从索引中获取到的主键ID为:" + id);
		}
		if (id == null) {
			if (cacheLog.isInfoEnabled()) {
				cacheLog.info("索引中不存在当前数据的索引信息..");
			}
			obj = null;
		} else {
			obj = cache.hGetBean(getIdKey(id), rootDataType);
			if (cacheLog.isInfoEnabled()) {
				cacheLog.info("根据主键key:" + getIdKey(id) + "获取实体数据为:"+obj);
			}
			if (null == obj) {
				if (cacheLog.isInfoEnabled()) {
					cacheLog.info("实体数据为空，删除索引 :" + indexKey);
				}
				cache.del(indexKey);
			}
		}
		return obj;
	}

	/**
	 * 获取实体数据所对应的key值
	 * 
	 * @param id
	 *            主键值
	 * @return
	 */
	private String getIdKey(String id) {
		return tableName + "@" + id;
	}

	/**
	 * 获取索引对应的key值
	 * 
	 * @return
	 */
	private String getIndexKey() {
		return tableName + "@" + indexName + "@" + indexValue;
	}

	/**
	 * 直接根据主键信息从缓存中获取数据信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object getDataFromCacheWitchNoIndex() {
		String idKey = getIdKey(id);
		if (cacheLog.isInfoEnabled()) {
			cacheLog.info("不需要创建索引信息,根据key: " + idKey + " 获取数据");
		}
		return cache.hGetBean(idKey, rootDataType);
	}

	/**
	 * 从缓存中获取数据信息
	 * 
	 * @return
	 */
	private Object getDataFromCache() {
		long start = System.currentTimeMillis();
		Object obj = null;
		// 如果是有索引的
		if (this.isCreateIndex) {
			obj = getDataFromCacheByIndex();
		} else {
			obj = getDataFromCacheWitchNoIndex();
		}
		if (cacheLog.isInfoEnabled()) {
			cacheLog.info("从缓存中获取数据花费时间为: "
					+ (System.currentTimeMillis() - start) + "毫秒");
		}
		return obj;
	}

	/**
	 * 将数据添加到缓存,同时创建指定字段的索引信息
	 * 
	 * @param obj
	 *            数据对象
	 * @throws Exception
	 */
	private boolean addDataToCacheWitchIndex(Object obj) throws Exception {

		boolean result = false;
		// 主键值
		Object id = PropertyUtils.getProperty(obj, this.idName);
		if (id == null)
			throw new Exception(this.idName + "对应的主键值不存在");

		String idKey = getIdKey(id.toString());
		// 如果没有设置失效时间则数据永不失效
		if (efficTime == null) {
			// 保存实体数据
			boolean re1 = cache.setHBean(idKey, obj);

			// 如果实体数据保存失败
			if (!re1) {
				return result;
			}
			// 建立索引(表名@索引名@索引值->id)
			boolean re2 = cache.set(getIndexKey(), id.toString());
			result = re1 && re2;
		} else {
			// 保存实体数据
			boolean re1 = cache.setHBean(idKey, obj);
			// 如果实体数据保存失败
			if (!re1) {
				return result;
			}
			// 如果需要值及索引都会失效
			if (ef == ValueEffect.ALLEFFECT)
				// 设置失效时间
				cache.expire(idKey, efficTime);
			// 建立索引设置失效时间
			boolean re2 = cache.set(getIndexKey(),id, efficTime);
			result = re1 && re2;
		}
		return result;
	}

	/**
	 * 将数据添加到缓存,并不建立索引信息
	 * 
	 * @param obj
	 *            数据对象
	 * @return
	 * @throws Exception
	 */
	private boolean addDataToCacheWitchNoIndex(Object obj) throws Exception {
		boolean result = false;
		if (cacheLog.isInfoEnabled()) {
			cacheLog.info("缓存数据不需要建立索引,将数据直接添加到缓存");
		}
		// 如果没有设置失效时间则数据永不失效
		String idKey = getIdKey(id);
		if (efficTime == null) {
			result = cache.setHBean(idKey, obj);
		} else if (efficTime != null) {
			// 如果设置为相对失效时间
			result = cache.setHBean(idKey, obj);
			cache.expire(idKey, efficTime);
		}
		return result;
	}

	/**
	 * 将数据添加到缓存
	 */
	public void addDataToCache(Object obj) {
		long start = System.currentTimeMillis();
		boolean result = false;
		if (obj == null) {
			if (cacheLog.isInfoEnabled()) {
				cacheLog.info("数据不存在,不进行数据缓存");
			}
			return;
		}
		if (cacheLog.isInfoEnabled()) {
			cacheLog.info("开始将数据添加到缓存");
		}
		try {
			// 如果需要建立索引
			if (this.isCreateIndex) {
				result = addDataToCacheWitchIndex(obj);
			} else {
				result = addDataToCacheWitchNoIndex(obj);
			}
			if (cacheLog.isInfoEnabled()) {
				cacheLog.info("将数据添加到缓存花费时间为: " + (System.currentTimeMillis() - start) + "毫秒");
				if (result) {
					cacheLog.info("缓存数据成功");
				} else {
					cacheLog.info("缓存数据失败");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Object getData() {
		Object obj = null;
//		long start = System.currentTimeMillis();
		try {
			// 从缓存中获取数据
			obj = getDataFromCache();
		} catch (Exception e) {
			cacheLog.error(e,e);
		}
		if (obj == null) {
			if (cacheLog.isInfoEnabled()) {
				cacheLog.info("缓存中不存在数据,开始从数据源中获取数据");
			}
			long start = System.currentTimeMillis();
			obj = getDataFromSource();
			if (cacheLog.isInfoEnabled()) {
				cacheLog.info("从数据源获取数据为: " + obj + "花费时间为: "
						+ (System.currentTimeMillis() - start) + "毫秒");
			}
			try {
			// 将数据存入缓存中
			addDataToCache(obj);
			} catch (Exception e) {
				cacheLog.error(e,e);
			}
		}
		return obj;
	}

	/**
	 * 从数据源中获取数据信息
	 * 
	 * @return
	 */
	public abstract Object getDataFromSource();

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setRootDataType(Class rootDataType) {
		this.rootDataType = rootDataType;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public void setCreateIndex(boolean isCreateIndex) {
		this.isCreateIndex = isCreateIndex;
	}

	/**
	 * 设置失效时间
	 * 
	 * @param efficTime失效时间
	 *            (单位:毫秒)
	 */
	public void setEfficTime(Integer efficTime) {
		this.efficTime = efficTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setEf(ValueEffect ef) {
		this.ef = ef;
	}

	public String getIndexValue() {
		return indexValue;
	}

	public void setIndexValue(String indexValue) {
		this.indexValue = indexValue;
	}

	public AbstractCachedBeanProvide(EJedis cache) {
		this.cache = (EJedis) cache;
	}
}
