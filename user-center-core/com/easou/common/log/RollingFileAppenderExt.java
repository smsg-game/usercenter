package com.easou.common.log;

import java.io.File;

import org.apache.log4j.DailyRollingFileAppender;
 

public class RollingFileAppenderExt extends DailyRollingFileAppender {
	public void setFile(String file) {
		if (file != null) {
			String tmpfileName = file.trim().replace('/',
					File.separator.charAt(0));
			this.fileName=tmpfileName;
			String dirName = tmpfileName.substring(0, tmpfileName
					.lastIndexOf(File.separator.charAt(0)));
			File f = new File(dirName);
			if (!f.exists()) {
				f.mkdirs();
			}
		}
	}
}