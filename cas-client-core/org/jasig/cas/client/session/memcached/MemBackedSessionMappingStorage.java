package org.jasig.cas.client.session.memcached;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.jasig.cas.client.session.SessionMappingStorage;

import com.danga.MemCached.MemCachedClient;

public class MemBackedSessionMappingStorage implements SessionMappingStorage {

	private static MemCachedClient client = null;

	private static void initClient() {
		InputStream in = MemBackedSessionMappingStorage.class
				.getResourceAsStream("/sessionservice.properties");
		Properties props = new Properties();
		try {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String poolname = props.getProperty("poolname", "sesssock");
		client = new MemCachedClient(poolname);
	}

	// 失效时间
	private final int efficetTime = 24 * 60 * 60;

	private final static String PREFIX = "ID_TO_SESSION_KEY_";

	@Override
	public void addSessionById(String mappingId, HttpSession session) {
		if (null == client) {
			initClient();
		}
		client.add(PREFIX + mappingId, session.getId(), efficetTime);
	}

	@Override
	public void removeBySessionById(String sessionId) {
		// TODO Auto-generated method stub

	}

	@Override
	public HttpSession removeSessionByMappingId(String mappingId) {
		if (null == client) {
			initClient();
		}
		String sessionId = (String) client.get(PREFIX + mappingId);
		client.delete(PREFIX + mappingId);
		client.delete(sessionId);
		return null;
	}

}
