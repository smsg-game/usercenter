package com.easou.usercenter.web.api;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easou.common.api.CodeConstant;
import com.easou.common.api.ExpJUser;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.JDesc;
import com.easou.common.api.JUser;
import com.easou.common.util.MD5Util;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.web.apibeans.JSonBeanFactory;
import com.easou.usercenter.web.validation.FormUserValidator;

@Controller
@RequestMapping("/api/")
public class ApiUserController extends AbstractAPIController {

	@Resource(name = "eucUserServiceImpl")
	private EucUserService eucUserService;

	/**
	 * 根据用户id获取用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getUserById")
	@ResponseBody
	public String getUserById(final HttpServletRequest request, final HttpServletResponse response) {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				JDesc jdesc = new JDesc(); // 错误描述
				Long id = getRequestBean().getBody().getLong("id");
				if (null != id) {
					EucUser eucUser = eucUserService.queryUserInfoById(id + "");
					if (null != eucUser) {
						JBody jbody = new JBody();
						jbody.putContent("user", transJUser(eucUser, new ExpJUser()));
						jbean.setBody(jbody); // 查询到用户，加入返回体
					} else {
						jdesc.addReason("2", "没找到用户");
					}
				} else {
					jdesc.addReason("1", "id参数错误");
				}
				if (jdesc.size() > 0) {
					jbean.getHead().setRet(CodeConstant.BUSI_ERROR);
					jbean.setDesc(jdesc);
				}
				return jbean;
			}

		};
		return abc.genReturnJson();
	}

	/**
	 * 根据用户注册的手机号获取用户信息，手机号在用户系统中是唯一的
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getUserByMobile")
	@ResponseBody
	public String getUserByMobile(final HttpServletRequest request, final HttpServletResponse response) {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				JDesc jdesc = new JDesc(); // 错误描述
				String mobile = getRequestBean().getBody().getString("mobile");
				if (null != mobile && StringUtil.isNumber(mobile)) {
					EucUser eucUser = eucUserService.queryUserInfoByMobile(mobile);
					if (null != eucUser) {
						JBody jbody = new JBody();
						jbody.putContent("user", transJUser(eucUser, new ExpJUser()));
						jbean.setBody(jbody); // 查询到用户，加入返回体
					} else {
						jdesc.addReason("2", "没找到用户");
					}
				} else {
					jdesc.addReason("1", "mobile参数错误");
				}
				if (jdesc.size() > 0) {
					jbean.getHead().setRet(CodeConstant.BUSI_ERROR);
					jbean.setDesc(jdesc);
				}
				return jbean;
			}

		};
		return abc.genReturnJson();
	}

	@RequestMapping("updateUser")
	@ResponseBody
	public String updateUser(final HttpServletRequest request, final HttpServletResponse response) {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() throws IllegalAccessException, InvocationTargetException {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				JDesc jdesc = new JDesc(); // 错误描述

				Long userId = null;
				if (this.getCheckMode() == CHECK_BY_TOKEN) {// TOKEN校验方式
					userId = this.getAuthToken().getId();
				} else {
					userId = getRequestBean().getBody().getLong("id");
				}
				JUser juser = getRequestBean().getBody().getObject("user", JUser.class);
				if (null == userId || 0 == userId.longValue()) {
					jdesc.addReason("1", "id参数错误");
				} else if (null == juser) {
					jdesc.addReason("2", "user参数错误");
				} else {
					EucUser eucUser = eucUserService.queryUserInfoById(userId + ""); // 要修改的用户
					if (null == eucUser) {
						jdesc.addReason("3", "修改的用户不存在");
					} else {
						boolean hasChange = false;
						EucUser updateUser = new EucUser(); // 要更新的类
						// String name = null == juser.getName() ? null : juser
						// .getName().toLowerCase().trim();
						String nickName = null == juser.getNickName() ? null : juser.getNickName().trim();
						Integer birthday = juser.getBirthday();
						Integer sex = juser.getSex();
						String city = null == juser.getCity() ? null : juser.getCity().trim();
						Integer occuId = juser.getOccuId();
						Integer status = juser.getStatus();

						// // 用户名
						// if (checkName(name, eucUser, jdesc)) {
						// updateUser.setName(name);
						// eucUser.setName(name);
						// hasChange = true;
						// }

						// 昵称
						if (checkNickName(nickName, eucUser, jdesc)) {
							updateUser.setNickName(nickName);
							eucUser.setNickName(nickName);
							hasChange = true;
						}

						// 出生年份
						if (checkBirthday(birthday, eucUser, jdesc)) {
							updateUser.setBirthday(birthday);
							eucUser.setBirthday(birthday);
							hasChange = true;
						}

						// 性别
						if (checkSex(sex, eucUser, jdesc)) {
							updateUser.setSex(sex);
							eucUser.setSex(sex);
							hasChange = true;
						}

						// 城市
						if (checkCity(city, eucUser, jdesc)) {
							updateUser.setCity(city);
							eucUser.setCity(city);
							hasChange = true;
						}

						// 职业
						if (checkOccuId(occuId, eucUser, jdesc)) {
							updateUser.setOccuId(occuId);
							eucUser.setOccuId(occuId);
							hasChange = true;
						}

						// 用户状态
						if (null != status) {
							updateUser.setStatus(status);
							eucUser.setStatus(status);
							hasChange = true;
						}

						if (hasChange) {
							// BeanUtils.copyProperties(updateUser, juser);
							// if (log.isDebugEnabled()) {
							// log.debug("status=" + updateUser.getStatus()
							// + " sex=" + updateUser.getSex()
							// + " occuId=" + updateUser.getOccuId());
							// }
							eucUserService.updateUserById(userId + "", updateUser);
							JBody jbody = new JBody();
							jbody.putContent("user", transJUser(eucUser, new ExpJUser()));
							jbean.setBody(jbody);
						} else {
							// 什么动作都没产生
							if (0 == jdesc.size())
								jdesc.addReason("9", "无信息更新");
						}
					}
				}
				if (jdesc.size() > 0) {
					jbean.getHead().setRet(CodeConstant.BUSI_ERROR);
					jbean.setDesc(jdesc);
				}
				return jbean;
			}

		};
		return abc.genReturnJson();
	}

	/**
	 * 通过用户凭证(token,id)更新密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updatePasswd")
	@ResponseBody
	public String updatePasswd(final HttpServletRequest request, final HttpServletResponse response) {
		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				JBean jbean = JSonBeanFactory.getDefaultBean();
				JDesc jdesc = new JDesc(); // 错误描述

				Long id = null;
				if (this.getCheckMode() == CHECK_BY_TOKEN) {// TOKEN校验方式
					id = this.getAuthToken().getId();
				} else {
					id = getRequestBean().getBody().getLong("id");
				}

				String oldPass = getRequestBean().getBody().getString("oldPass");
				String newPass = getRequestBean().getBody().getString("newPass");

				if (null != id && 0 != id.longValue()) {
					EucUser eucUser = eucUserService.queryUserInfoById(id + "");
					if (null != eucUser) {
						if (MD5Util.md5(oldPass).equals(eucUser.getPasswd())) {
							String s = FormUserValidator.checkPassword(newPass);
							if (!"".equals(s)) {
								jdesc.addReason("4", s);
							}
						} else {
							jdesc.addReason("3", "旧密码错误");
						}
					} else {
						jdesc.addReason("2", "没找到用户");
					}
				} else {
					jdesc.addReason("1", "id参数错误");
				}
				if (jdesc.size() > 0) {
					jbean.getHead().setRet(CodeConstant.BUSI_ERROR);
					jbean.setDesc(jdesc);
				} else {
					eucUserService.updatePasswd(id + "", newPass);
				}
				return jbean;
			}

		};
		return abc.genReturnJson();
	}

	/**
	 * 重设用户密码为固定密码，该接口只提供给管理后台使用
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("resetPasswd")
	@ResponseBody
	public String resetPasswd(final HttpServletRequest request, final HttpServletResponse response) {

		AbstractJBeanCreator abc = new AbstractJBeanCreator(request, response) {

			@Override
			public JBean getJBean() {
				// JBean jbean = JSonBeanFactory.getDefaultBean();
				// JDesc jdesc = new JDesc(); // 错误描述
				//
				// Long id = getRequestBean().getBody().getLong("id");
				// if (null == id || 0 == id.longValue()) {
				// return busiErrorBean(jbean,jdesc,"1", "id参数错误");
				// }
				// EucUser eucUser = eucUserService.queryUserInfoById(id + "");
				// if(null==eucUser) {
				// return busiErrorBean(jbean,jdesc,"2", "用户不存在");
				// }
				// String newPass = SSOConfig.getProperty("passwd.init");
				// eucUserService.updatePasswd(id + "", newPass);
				// return jbean;
				return JSonBeanFactory.getDefaultErrorBean();
			}

		};

		return abc.genReturnJson();
	}

	// private boolean checkName(String name, EucUser eucUser, JDesc jdesc) {
	// if (null == name || name.equals(eucUser.getName())) {
	// return false;
	// }
	// if (!eucUser.getName().equalsIgnoreCase("es" + eucUser.getId())) {
	// // 必须符合es+id号的登录名才能修改
	// jdesc.addReason("4", "不能修改登录名");
	// return false;
	// }
	// String s = FormUserValidator.checkUserName(name);
	// if (!"".equals(s)) {
	// jdesc.addReason("4", s);
	// return false;
	// } else {
	// EucUser temp = eucUserService.queryUserInfoByName(name);
	// if (null != temp && !temp.getId().equals(eucUser.getId())) {
	// // 登录名相同并且不为当前用户ID的用户
	// jdesc.addReason("4", "该登录名已被注册");
	// return false;
	// }
	// return true;
	// }
	// }

	private boolean checkNickName(String nickName, EucUser eucUser, JDesc jdesc) {
		if (null == nickName || nickName.equals(eucUser.getNickName())) {
			return false;
		}
		String s = FormUserValidator.checkNickName(nickName);
		if (!"".equals(s)) {
			jdesc.addReason("5", s);
			return false;
		} else {
			EucUser temp = eucUserService.queryUserInfoByNickName(nickName);
			if (null != temp && !temp.getId().equals(eucUser.getId())) {
				// 昵称相同并且不为当前用户ID的用户
				jdesc.addReason("5", "该昵称已被注册");
				return false;
			}
			return true;
		}
	}
}