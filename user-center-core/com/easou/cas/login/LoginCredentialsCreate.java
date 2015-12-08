package com.easou.cas.login;

import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.easou.common.util.DESPlus;

/**
 * 用户登陆信息创建
 * 
 * @author river
 * 
 */
public class LoginCredentialsCreate extends AbstractAction {

	private DESPlus des = new DESPlus();

	public DESPlus getDes() {
		return des;
	}

	public void setDes(DESPlus des) {
		this.des = des;
	}

	@Override
	public Event doExecute(RequestContext context) throws Exception {
		String uInfo = context.getFlowScope().get("uinfo").toString();
		// 解密用户信息
		String uInfo_ = des.decrypt(uInfo);
		String[] infos = uInfo_.split("\\$");
		// 创建用户登陆对象
		UsernamePasswordCredentials cre = new UsernamePasswordCredentials();
		if(2 == infos.length) {
			cre.setPassword(infos[1]);
			cre.setUsername(infos[0]);
			cre.setCookieLogin(true);
		}
		context.getFlowScope().put("credentials", cre);
		return success();
	}

}