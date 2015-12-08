package com.easou.usercenter.util.key;

public class KeyPattern {
	private String word;
	private int level;
	private String rplWord;

	public KeyPattern(String word, String rplWord, int level) {
		this.word = word;
		this.rplWord = rplWord;
		this.level = level;
	}

	// 得到字串
	public String getWord() {
		return word.toString();
	}

	public int getLevel() {
		return level;
	}

	public String getRplWord() {
		return rplWord;
	}
}