NDS
===

pre requisites
--------------

	install git from http://git-scm.com/
	install maven from http://maven.apache.org/download.html
	install tomcat from http://linorg.usp.br/apache/tomcat/tomcat-7/v7.0.27/bin/apache-tomcat-7.0.27.zip
	install mysql from http://dev.mysql.com/downloads/

config
------

	create a nds-client database on mysql
	add datasource config in tomcat
	
	<TOMCAT_HOME>/config/context.xml
	<Resource name="jdbc/nds-client"              
	 type="javax.sql.DataSource" username="root" password="password"
	 driverClassName="com.mysql.jdbc.Driver" 
	 url="jdbc:mysql://localhost:3306/nds-client"
	 maxActive="8" maxIdle="4"
	 global="jdbc/nds-client" 
	/> 
	
to build
--------
	
	go to https://github.com/DGBti/NDS
	Fork!	
	git clone git@github.com:<yourgithubuser>/NDS.git
	cd NDS
	mvn clean install
	
to run 
------

	cp /nds-client/target/*.war <TOMCAT_HOME>/webapp
	localhost:8080/nds-client