package com.easou.cas.authenticateion;

import java.util.Map;

import org.jasig.cas.authentication.principal.SimplePrincipal;

public class ThirdPrincipal extends SimplePrincipal {
	
	public ThirdPrincipal(String id,Map<String, Object> attributes) {
		super(id,attributes);
	}

}
