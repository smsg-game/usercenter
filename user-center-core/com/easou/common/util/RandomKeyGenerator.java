package com.easou.common.util;

import java.util.Random;

public class RandomKeyGenerator {
    public static String generate(int len) {
        char[] temp = new char[len];
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            temp[i] = KEY_LIB[random.nextInt(KEY_LEN)];
        }
        return new String(temp);
    }
    
    public static String genNumber(int len) {
    	char[] temp = new char[len];
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            temp[i] = PASS_LIB[random.nextInt(PASS_LEN)];
        }
        return new String(temp);
    }
    
    /**
     * 必须有一个字母
     * @param len
     * @return
     */
    public static String generateMIX(int len) {
    	if(len<1) {
    		return "";
    	}
        char[] temp = new char[len];
        Random random = new Random();
        temp[0]=KEY_LIB[random.nextInt(KEY_LEN)];
        for (int i = 1; i < len; i++) {
            temp[i] = MIX_LIB[random.nextInt(MIX_LEN)];
        }
        return new String(temp);
    }

    private static final char[] KEY_LIB = new char[] { 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
    
    private static final char[] PASS_LIB = new char[] {'0','1','2','3','4','5','6','7','8','9'};
    
    private static final char[] MIX_LIB = new char[] { 'a', 'b', 'c', 'd', 'e',
        'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
        's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0','1','2','3','4','5','6','7','8','9' };

    private static final int KEY_LEN = 26;
    
    private static final int PASS_LEN = 10;
    
    private static final int MIX_LEN=36;

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomKeyGenerator.generateMIX(6));
        }
    }
}
