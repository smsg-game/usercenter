package com.easou.usercenter.dao.impl;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;

import com.easou.usercenter.cache.jedis.EJedis;
import com.easou.usercenter.datasource.ContextHolder;
import com.easou.usercenter.datasource.DataSourceCluster;
/**
 * 动态DAO基类，根据不同的情形，返回不同的数据源
 * 
 * @author damon
 * @since 2012.05.09
 *
 */
public class DynamicSqlSessionDaoSupport extends DaoSupport{
	
	Log log = LogFactory.getLog(DynamicSqlSessionDaoSupport.class);
	
	//@Resource
	private Map<DataSourceCluster, SqlSessionFactory> targetSqlSessionFactorys;  
	//@Resource  
    private SqlSessionFactory              defaultTargetSqlSessionFactory;  
	//@Resource
    private SqlSession                     sqlSession;  

    private boolean                        externalSqlSession;  
    
    protected EJedis cache;
    
	public EJedis getCache() {
		return cache;
	}

	public void setCache(EJedis cache) {
		this.cache = cache;
	}
  
    //@Autowired(required = false)  
    public final void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {  
        if (!this.externalSqlSession) {  
            this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);  
        }  
    }  
  
    //@Autowired(required = false)  
    public final void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {  
        this.sqlSession = sqlSessionTemplate;  
        this.externalSqlSession = true;  
    }  
  
    /** 
     * Users should use this method to get a SqlSession to call its statement 
     * methods This is SqlSession is managed by spring. Users should not 
     * commit/rollback/close it because it will be automatically done. 
     *  
     * @return Spring managed thread safe SqlSession 
     */  
    public final SqlSession getSqlSession() {  
        SqlSessionFactory targetSqlSessionFactory = null;
        if(ContextHolder  
                .getContext()!=null){
        	targetSqlSessionFactory = targetSqlSessionFactorys.get(ContextHolder  
                .getContext());  
            log.info("return the dataSource["+ContextHolder  
                    .getContext()+"] from ContextHolder");
        }
        if (targetSqlSessionFactory != null) {  
            setSqlSessionFactory(targetSqlSessionFactory);  
        } else if (defaultTargetSqlSessionFactory != null) {  
            setSqlSessionFactory(defaultTargetSqlSessionFactory);  
            log.info("get the default dataSource["+defaultTargetSqlSessionFactory  +"]");
        }  
        return this.sqlSession;  
    }  
  
    /** 
     * {@inheritDoc} 
     */  
    protected void checkDaoConfig() {  
        Assert.notNull(this.sqlSession,  
                "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");  
    }  
  
    public Map<DataSourceCluster, SqlSessionFactory> getTargetSqlSessionFactorys() {  
        return targetSqlSessionFactorys;  
    }  
  
    /** 
     * Specify the map of target SqlSessionFactory, with the lookup key as key. 
     * @param targetSqlSessionFactorys 
     */  
    public void setTargetSqlSessionFactorys(Map<DataSourceCluster, SqlSessionFactory> targetSqlSessionFactorys) {  
        this.targetSqlSessionFactorys = targetSqlSessionFactorys;  
    }  
  
    public SqlSessionFactory getDefaultTargetSqlSessionFactory() {  
        return defaultTargetSqlSessionFactory;  
    }  
  
    /** 
     * Specify the default target SqlSessionFactory, if any. 
     * @param defaultTargetSqlSessionFactory 
     */  
    public void setDefaultTargetSqlSessionFactory(SqlSessionFactory defaultTargetSqlSessionFactory) {  
        this.defaultTargetSqlSessionFactory = defaultTargetSqlSessionFactory;  
    }  

}
