package com.easou.usercenter.cache;

/**
 * 过期时间影响范围
 * @author Jay
 * INDEXERRECT	只有索引会过期
 * ALLERRECT	索引和id都会过期
 *
 */
public enum ValueEffect {
	INDEXEFFECT, ALLEFFECT
}
