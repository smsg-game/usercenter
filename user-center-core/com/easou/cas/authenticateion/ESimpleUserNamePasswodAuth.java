package com.easou.cas.authenticateion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.authentication.AuthenticationResult;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.BadUsernameOrPasswordAuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

import com.easou.common.constant.CachePrefixConstant;
import com.easou.common.constant.Server;
import com.easou.common.util.MobileUtil;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.cache.jedis.EJedis;
import com.easou.usercenter.dao.EucUserDao;
import com.easou.usercenter.datasource.DynamicCacheSource;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;

/**
 * 用户登陆验证
 * 
 * @author river
 * 
 */
public class ESimpleUserNamePasswodAuth extends
		AbstractUsernamePasswordAuthenticationHandler {
	Log log = LogFactory.getLog(ESimpleUserNamePasswodAuth.class);
	//@Resource(name = "eucUserDao")
	private EucUserDao dao;

	private EucUserService uService;

	//private EJedis cache;
	//缓存
	private DynamicCacheSource dynamicCacheSource;
	//服务器类型
	private String serverType;

	public DynamicCacheSource getDynamicCacheSource() {
		return dynamicCacheSource;
	}

	public void setDynamicCacheSource(DynamicCacheSource dynamicCacheSource) {
		this.dynamicCacheSource = dynamicCacheSource;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	@Override
	protected AuthenticationResult authenticateUsernamePasswordInternal(
			UsernamePasswordCredentials credentials)
			throws AuthenticationException {
	 
//			boolean isloginSucess = false;
			if(null==credentials) {
				log.error("Credentials为空");
				return new AuthenticationResult(false, null);
			}
			String name = "";
			try {
				name = credentials.getUsername().toLowerCase().trim();
			} catch (Exception e) {
				log.warn("用户名为空, 是否cookie=" + credentials.isCookieLogin() + " isCookie=" + credentials.getIsCookie());
			}
			String pwd = null;
			if (credentials.isCookieLogin()) {
				pwd = credentials.getPassword();
			} else {
				pwd = getPasswordEncoder().encode(credentials.getPassword());
			}
			EucUser user = dao.queryUserByIds(name, pwd);
			boolean isRegist = false;
			// 如果用户不存在
			if (user == null) {
				// 如果是手机号码
				if (StringUtil.isNumber(name)) {
					user = reMobileLogin(name, pwd);
					if(user!=null){
						isRegist = true;
					}
				}
			}
			if (user == null) {
				return new AuthenticationResult(false, null);
			}
//			isloginSucess = EasouUserEqualUtil.equalUserInfo(name, pwd, user);
			if (!"1".equals(String.valueOf(user.getStatus()))){
				throw new BadUsernameOrPasswordAuthenticationException("USER.VALIDATE");
			}
//			if (!isloginSucess)
//				return new AuthenticationResult(false, null);
//			else
			return new AuthenticationResult(true, user,isRegist);
	}

	/**
	 * 新注册手机号登陆
	 * 
	 * @param name
	 *            用户名称
	 * @param pwd
	 *            用户密码
	 * @return
	 */
	private EucUser reMobileLogin(String name, String pwd) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("根据key值获取密码信息key: "
						+ CachePrefixConstant.MOBREG_CACHE_PREFIX + name);
			}
			String pwd2 = getEJedis().get(CachePrefixConstant.MOBREG_CACHE_PREFIX
					+ name);
			if (pwd2 == null)
				pwd2 = "";
			if (log.isDebugEnabled()) {
				log.debug("获取的pwd为: " + pwd2+" 原pwd:"+pwd);
			}
			if (pwd.equals(pwd2)) {
				// 添手机号信息入库
				EucUser user = uService.insertUserForMobileRegist(name, pwd2, null);
				//删除数据
				getEJedis().del(CachePrefixConstant.MOBREG_CACHE_PREFIX
					+ name);
				return user;
			}
		} catch (Exception ex) {
			log.error(ex,ex);
		}
		return null;
	}
	
	/**
	 * 获取缓存
	 * 
	 * @return
	 */
	public EJedis getEJedis(){
		if(Server.getServerByType(serverType)==Server.DEFAULT){
			return dynamicCacheSource.getEJedisByServer(Server.DEFAULT);
		}else if(Server.getServerByType(serverType)==Server.DIANXIN){
			return dynamicCacheSource.getEJedisByServer(Server.DIANXIN);
		}else{
			try {
				throw new Exception("get the cache error:server type is null");
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
		
	}

	public void setDao(EucUserDao dao) {
		this.dao = dao;
	}

	public void setuService(EucUserService uService) {
		this.uService = uService;
	}

/*	public void setCache(EJedis cache) {
		this.cache = cache;
	}*/
}
