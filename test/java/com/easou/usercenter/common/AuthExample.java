package com.easou.usercenter.common;

import org.apache.commons.logging.Log;

import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

public class AuthExample {
	/**
     * @param args
     */
    public static void main(String[] args) {
            String access_token = args[0];
            Weibo weibo = new Weibo();
            weibo.setToken(access_token);
            String uid = args[1];
            Users um = new Users();
            try {
                    User user = um.showUserById(uid);
                    System.out.println(user.toString());
            } catch (WeiboException e) {
                    e.printStackTrace();
            }
    }

}
