package com.easou.usercenter.util.key;


public class UnionKeyPattern {
	private int capacity = 2;
	private int count = 0;
	private KeyPattern[] keyPattern = new KeyPattern[capacity];

	public UnionKeyPattern(KeyPattern keyPattern) {
		this.keyPattern[count] = keyPattern;
		count++;
	}

	/***************************************************************************
	 * 确保容量
	 */
	private void ensureCapacity() {
		KeyPattern[] newKeyPattern = new KeyPattern[capacity];
		System.arraycopy(keyPattern, 0, newKeyPattern, 0,
				this.keyPattern.length);
		this.keyPattern = newKeyPattern;
	}

	/***************************************************************************
	 * 追加keyPattern
	 *
	 * @param keyPattern
	 * @return
	 */
	public UnionKeyPattern append(KeyPattern keyPattern) {
		// 去重
		for (int i = 0; i < count; i++) {
			if (this.keyPattern[i].getWord().equalsIgnoreCase(keyPattern.getWord())) {
				return this;
			}
		}
		if (count >= capacity) {
			capacity = count + 2;
			ensureCapacity();
		}
		this.keyPattern[count] = keyPattern;
		count++;
		return this;
	}

	/***************************************************************************
	 * 查找word是否是Key;如果flag为true返回rplWord,否则rplStr不为空返回rplStr
	 *
	 * @param word
	 * @param rplStr
	 * @param flag
	 * @param level
	 * @return
	 */
	public String findMatchInContent(String word, String rplStr, boolean flag,
			String level) {
		String matchStr="";
		for (int i = 0; i < count; i++) {
			if (level.indexOf('!') > -1) {
				if (level.indexOf(keyPattern[i].getLevel()) > -1) {
					continue;
				}
			} else {
				if(level.indexOf(keyPattern[i].getLevel())<0){
					continue;
				}
			}
			matchStr=FilterUtil.getMatcherString(word, getRegx(keyPattern[i].getWord()), 1);
			if (matchStr.length()>0) {
				if (flag && keyPattern[i].getRplWord()!=null) {
					return keyPattern[i].getRplWord();
				} else {
					if (null != rplStr && rplStr.length() > 0) {
						if (rplStr.indexOf(">xxx<") > 1) {
							return word.replaceAll(matchStr, rplStr.replaceAll("xxx", keyPattern[i]
									.getWord()));
						}
						return word.replaceAll(matchStr,rplStr);
					}
				}
				return word.replaceAll(matchStr,keyPattern[i].getWord());
			}
		}
		return "";
	}

	/***************************************************************************
	 * 拿到各个Key的长度
	 *
	 * @return
	 */
	public int[] getWordLen() {
		int[] wordLen = new int[count];
		for (int i = 0; i < count; i++) {
			wordLen[i] = this.keyPattern[i].getWord().length();
		}
		return wordLen;
	}

	/***************************************************************************
	 * 拿到KeyPattern数据
	 *
	 * @return
	 */
	public int getKeyPatternSize() {
		return this.count - 1;
	}

	public KeyPattern[] getKeyPattern() {
		return keyPattern;
	}

	public String getRegx(String word){
		char []words=word.toCharArray();
		StringBuilder sbd=new StringBuilder();
		for(char w:words){
			if(sbd.length()>0){
				sbd.append("[^\u4e00-\u9fa5]{0,3}?");
			}
			sbd.append(w);
		}
		return sbd.toString();
	}
}
