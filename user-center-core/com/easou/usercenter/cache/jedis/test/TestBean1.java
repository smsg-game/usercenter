package com.easou.usercenter.cache.jedis.test;

import java.util.List;
import java.util.Map;

public class TestBean1 {

	private String name;
	private Integer kk;
	private int sex;
	private boolean is;
	private List<String> l1;
	private List<TestBean2> l2;
	private Map<String, String> m1;
	private Map<String, TestBean2> m2;
	private TestBean2 bean2;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getKk() {
		return kk;
	}

	public void setKk(Integer kk) {
		this.kk = kk;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public boolean isIs() {
		return is;
	}

	public void setIs(boolean is) {
		this.is = is;
	}

	public List<String> getL1() {
		return l1;
	}

	public void setL1(List<String> l1) {
		this.l1 = l1;
	}

	public List<TestBean2> getL2() {
		return l2;
	}

	public void setL2(List<TestBean2> l2) {
		this.l2 = l2;
	}

	public Map<String, String> getM1() {
		return m1;
	}

	public void setM1(Map<String, String> m1) {
		this.m1 = m1;
	}

	public Map<String, TestBean2> getM2() {
		return m2;
	}

	public void setM2(Map<String, TestBean2> m2) {
		this.m2 = m2;
	}

	public TestBean2 getBean2() {
		return bean2;
	}

	public void setBean2(TestBean2 bean2) {
		this.bean2 = bean2;
	}

}
