package com.easou.usercenter.cache.jedis.test;

import java.util.List;
import java.util.Map;

public class TestBean2 {
	private String t2name;
	private int mk;
	private Integer s;
	private List<String> t2l;
	private Map<String, String> m2;
	private TestBean1 bean1;

	public String getT2name() {
		return t2name;
	}

	public void setT2name(String t2name) {
		this.t2name = t2name;
	}

	public int getMk() {
		return mk;
	}

	public void setMk(int mk) {
		this.mk = mk;
	}

	public Integer getS() {
		return s;
	}

	public void setS(Integer s) {
		this.s = s;
	}

	public List<String> getT2l() {
		return t2l;
	}

	public void setT2l(List<String> t2l) {
		this.t2l = t2l;
	}

	public Map<String, String> getM2() {
		return m2;
	}

	public void setM2(Map<String, String> m2) {
		this.m2 = m2;
	}

	public TestBean1 getBean1() {
		return bean1;
	}

	public void setBean1(TestBean1 bean1) {
		this.bean1 = bean1;
	}

}
