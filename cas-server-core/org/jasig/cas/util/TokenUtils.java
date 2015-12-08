/**
 * 
 */
package org.jasig.cas.util;

import com.easou.common.util.MD5Util;

/**
 * description：
 * @project sso
 * @author Seek
 * @time 上午10:11:34
 * @Date 2013-10-16
 */
public final class TokenUtils {
	
	/**
	 * description：根据用户信息获得一个token
	 * @author Seek
	 * @time 上午10:18:38
	 * @Date 2013-10-16
	 * @return String	- token
	 * @param userName	- a user name
	 * @param password	- a user's password
	 * @param isMd5Pwd	- 如果password参数是md5的，那么就传true，反之false
	 * @return
	 */
	public static final String getTokenByUserInfo(String userName, String password, boolean isMd5Pwd) {
		String md5Pwd = password;
		if(!isMd5Pwd){
			md5Pwd = MD5Util.md5(password).toLowerCase();
		}
		
		String ticketId = MD5Util.md5(userName + md5Pwd).toLowerCase();
		return ticketId;
	}
	
}
