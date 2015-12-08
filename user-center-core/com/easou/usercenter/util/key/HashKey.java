package com.easou.usercenter.util.key;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*******************************************************************************
 * @version 1.0
 * @author xu yi feng
 * 
 */
public class HashKey {

	private Map<Character, Upl> hashTable = new HashMap<Character, Upl>();
	private static HashKey hashKey = null;

	public static class TAG {
		public final static String B = "<b>xxx</b>";
		public final static String I = "<i>xxx</i>";
		public final static String U = "<u>xxx</u>";
		public final static String BIG = "<big>xxx</big>";
		public final static String SMALL = "<small>xxx</small>";
		public final static String FONT_RED="<font color='RED'>xxx</font>";
	}

	private HashKey() {
	}

	public static synchronized HashKey getInstance() {
		if (null == hashKey) {
			hashKey = new HashKey();
		}
		return hashKey;
	}

	/***************************************************************************
	 * 添加单个Key
	 * 
	 * @param word
	 * @param rplWord
	 * @param level
	 */
	public void addKey(String word, String rplWord, int level) {
		if (hashTable.get(charAtBegin(word, 0)) == null) {
			hashTable.put(charAtBegin(word, 0), new Upl(new UnionKeyPattern(
					new KeyPattern(word, rplWord, level))));
		} else {
			((Upl) hashTable.get(charAtBegin(word, 0))).getUnionKeyPattern()
					.append(new KeyPattern(word, rplWord, level));
		}
	}

	/***************************************************************************
	 * 载入所有Key
	 * 
	 * @param keyList
	 * @return
	 */
	public boolean loadKey(List<Key> keyList) {
		if (keyList == null || keyList.size() == 0) {
			return false;
		}
		for (Key k : keyList) {
			addKey(k.getWord(), k.getRplWord(), k.getLevel());
		}
		return true;
	}

	/**
	 * 字串首位
	 * 
	 * @param word
	 * @param index
	 * @return
	 */
	public char charAtBegin(String word, int index) {
		word=word.toLowerCase();
		if (word.length() > index) {
			return word.charAt(index);
		} else {
			return 0;
		}
	}

	/***************************************************************************
	 * 查找并行Key的各个WordCount
	 * 
	 * @param c
	 * @return
	 */
	public int[] findMatchWordCount(char c) {
		if (hashTable.get(c) == null) {
			return null;
		}
		return ((Upl) hashTable.get(c)).getUnionKeyPattern().getWordLen();
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
	public String findMatchByWord(String word, String rplStr, boolean flag,
			String level) {
		if (hashTable.get(charAtBegin(word, 0)) == null) {
			return "";
		}
		return ((Upl) hashTable.get(charAtBegin(word, 0))).getUnionKeyPattern()
				.findMatchInContent(word, rplStr, flag, level);
	}

	/***************************************************************************
	 * 是否存在敏感词
	 * 
	 * @param word
	 * @param level
	 * @return
	 */
	public boolean isExistByWord(String word, String level) {
		return "".equals(this.findMatchByWord(word, "", false, level)) ? (false)
				: (true);
	}

	/***************************************************************************
	 * 文章HASH查找
	 * 
	 * @param content
	 * @param rplStr
	 * @param rplFlag
	 * @param level
	 * @return
	 */
	public String findOrRplMatchByContent(String content, String rplStr,
			boolean rplFlag, String level) {
		if (null == content || content.length() < 1) {
			return content;
		}
		content = init(content);
		if (null == rplStr) {
			rplStr = "";
		}
		StringBuilder newContent = new StringBuilder();
		int[] tempWordLen = null;
		String tempStr = null;
		int step = 1;
		int len = content.length();
		//防间隔字符扰乱匹配
		boolean hitFlag=false;//命中标记
		int backI=0;//保存I值
		int js=1;//基数乘积
		// content遍历
		ii: for (int i = 0; i < len;) {
			tempWordLen = findMatchWordCount(content.charAt(i)); // Hash表中是否存在数据
			if (null == tempWordLen) {
				newContent.append(content.charAt(i));
				i++;
				continue;
			}
			hitFlag=false;
			backI=i;
			// 横向搜索
			for (int j = 0; j < tempWordLen.length; j++) {
				if ((tempWordLen[j] + ((tempWordLen[j]-1)*js) + i) < len) {
					step = tempWordLen[j] + ((tempWordLen[j]-1)*js) + i;
				} else {
					step = len;
				}

				tempStr = findMatchByWord(content.substring(i, step), rplStr,
						rplFlag, level);
				if (!"".equals(tempStr)) {
					hitFlag=true;
					newContent.append(tempStr);
					i = step;
					continue ii;
				}
			}
			if(!hitFlag && i==backI){
				js++;
				if(js<4){
					continue;
				}
				js=1;
			}
			newContent.append(content.charAt(i));
			i++;
		}
		content = null;
		return newContent.toString();
	}

	public boolean isExistKeyByContent(String content, String level) {
		if (null == content || content.length() < 1) {
			return false;
		}
		content = init(content);
		int[] tempWordLen = null;
		int step = 1;
		int len = content.length();
		//防间隔字符扰乱匹配
		int js=1;//基数乘积
		// content遍历
		for (int i = 0; i < len;) {
			tempWordLen = findMatchWordCount(content.charAt(i)); // Hash表中是否存在数据
			if (null == tempWordLen) {
				i++;
				continue;
			}
			// 横向搜索
			for (int j = 0; j < tempWordLen.length; j++) {
				if ((tempWordLen[j] + ((tempWordLen[j]-1)*js) + i) < len) {
					step = tempWordLen[j] + ((tempWordLen[j]-1)*js) + i;
				} else {
					step = len;
				}
				if (isExistByWord(content.substring(i, step), level)) {
					return true;
				}
			}
			js++;
			if(js<4){
				continue;
			}
			js=1;
			i++;
		}
		return false;
	}

	/***************************************************************************
	 * 预先过滤掉标记,特殊字符
	 * 
	 * @param content
	 * @return
	 */
	public String init(String content) {
		content = FilterUtil.strip_tags(content);
		content = FilterUtil.filterChar(content);
		return content;
	}

	/***************************************************************************
	 * 清除HashTable
	 */
	public void clear() {
		hashTable.clear();
	}

	public Map<Character, Upl> getHashTable() {
		return hashTable;
	}

}
