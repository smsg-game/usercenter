package com.easou.usercenter.datasource;

import java.util.HashMap;
import java.util.Map;

public class DataSourceClusterTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Map<DataSourceCluster,String> map =new HashMap<DataSourceCluster,String>();
		map.put(DataSourceCluster.DEFAULT_DATA_SOURCE, "1");
		map.put(DataSourceCluster.WRITE_DATA_SOURCE, "2");
		System.out.println("1=="+map.get(DataSourceCluster.DEFAULT_DATA_SOURCE));
		System.out.println("2=="+map.get(DataSourceCluster.WRITE_DATA_SOURCE));

	}

}
