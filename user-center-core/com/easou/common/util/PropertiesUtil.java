/**
 * PropertiesUtil.java
 *
 * Copyright 2007 easou, Inc. All Rights Reserved.
 */
package com.easou.common.util;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * properties文件工具类
 * 
 * Revision History
 *
 * 2007-11-6,norbys,created it
 */
public class PropertiesUtil {
    private static final Log LOG = LogFactory.getLog(PropertiesUtil.class);

    /**
     * ��Properties��ȡ���ͣ�Ĭ��ֵΪ��
     */
    public static int getIntProperty(Properties props, String key) {
        return getIntProperty(props,key,0);
    }
    
    /**
     * ��Properties��ȡ����
     * @param defaultValue Ĭ��ֵ
     */
    public static int getIntProperty(Properties props, String key, int defaultValue) {
        int result = defaultValue;
        if (props != null) {
            String value = props.getProperty(key);
            try {
                result = Integer.parseInt(value);
            } catch (Exception ex) {
                LOG.error("get property fail:" + ex.getMessage());
            }
        }
        return result;
    }

    /**
     * ��Properties��ȡ�����ͣ�Ĭ��ֵΪ��
     */
    public static long getLongProperty(Properties props, String key) {
        return getLongProperty(props,key,0);
    }
    
    /**
     * ��Properties��ȡ������
     * @param defaultValue Ĭ��ֵ
     */
    public static long getLongProperty(Properties props, String key, long defaultValue) {
        long result = defaultValue;
        if (props != null) {
            String value = props.getProperty(key);
            try {
                result = Long.parseLong(value);
            } catch (Exception ex) {
                LOG.error("get property fail:" + ex.getMessage());
            }
        }
        return result;
    }

    /**
     * ��Properties��ȡboolean�ͣ�Ĭ��false
     */
    public static boolean getBooleanProperty(Properties props, String key) {
        return getBooleanProperty(props,key,false);
    }
    
    /**
     * ��Properties��ȡboolean��
     * @param defaultValue Ĭ��ֵ
     */
    public static boolean getBooleanProperty(Properties props, String key, boolean defaultValue) {
        boolean result = defaultValue;
        if (props != null) {
            String value = props.getProperty(key);
            try {
                result = Boolean.parseBoolean(value);
            } catch (Exception ex) {
                LOG.error("get property fail:" + ex.getMessage());
            }
        }
        return result;
    }

}
