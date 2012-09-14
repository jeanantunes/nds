NDS
===

build Status
------------

Geral: [![Build Status](http://177.71.255.76:8080/jenkins/job/build/badge/icon)](http://177.71.255.76:8080/jenkins/job/build/)

NDS-Parent: [![Build Status](http://177.71.255.76:8080/jenkins/job/build/br.com.abril$nds/badge/icon)](http://177.71.255.76:8080/jenkins/job/build/br.com.abril$nds/)

NDS-Model: [![Build Status](http://177.71.255.76:8080/jenkins/job/build/br.com.abril$nds-model/badge/icon)](http://177.71.255.76:8080/jenkins/job/build/br.com.abril$nds-model/)

NDS-Util: [![Build Status](http://177.71.255.76:8080/jenkins/job/build/br.com.abril$nds-util/badge/icon)](http://177.71.255.76:8080/jenkins/job/build/br.com.abril$nds-util/)

NDS-Client: [![Build Status](http://177.71.255.76:8080/jenkins/job/build/br.com.abril$nds-client/badge/icon)](http://177.71.255.76:8080/jenkins/job/build/br.com.abril$nds-client/)

NDSI-Canonical: [![Build Status](http://177.71.255.76:8080/jenkins/job/build/br.com.abril$ndsi-canonical/badge/icon)](http://177.71.255.76:8080/jenkins/job/build/br.com.abril$ndsi-canonical/)

NDSI-CouchdbInterface: [![Build Status](http://177.71.255.76:8080/jenkins/job/build/br.com.discover$ndsi-couchdbinterface/badge/icon)](http://177.71.255.76:8080/jenkins/job/build/br.com.discover$ndsi-couchdbinterface/)

NDSI-Engine: [![Build Status](http://177.71.255.76:8080/jenkins/job/build/br.com.abril$nsdi-engine/badge/icon)](http://177.71.255.76:8080/jenkins/job/build/br.com.abril$nsdi-engine/)

NDSI-WebTest: [![Build Status](http://177.71.255.76:8080/jenkins/job/build/br.com.abril.nds$ndsi-webtest/badge/icon)](http://177.71.255.76:8080/jenkins/job/build/br.com.abril.nds$ndsi-webtest/)


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
