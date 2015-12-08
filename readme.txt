////////////////////////
[Meta]
Text encoding: UTF-8
////////////////////////

��Ŀ���л�������
===============

�ڵ���SSO��Ŀ��Eclipse�����ռ�֮���㻹��Ҫ����Ƿ���������й�����

1. ��װEclipse�����run-jetty-run(https://code.google.com/p/run-jetty-run/)

2. ���������ݼ��뵽����hosts�ļ��� 
183.232.129.27    mobile.redis.cache1
127.0.0.1         testsso.fantingame.com

3. ����ĿJava Build Path�е�JAR Library System��Ŀָ��ΪJDK��Ŀ¼����Ҫ��Eclipse��Installed JREs��Ŀ�м�����JDKĿ¼��
4. ����Run Configurations��ѡ��Jetty Webapp���½�һ����Ŀ���޸�PortΪ80��ContextΪ/��Ȼ��ѡShow Advanced Options�����ѡJNDI Support

5. ������������⣬��˶�hosts�Լ�WEB-INF��jetty-env.xml��IP����