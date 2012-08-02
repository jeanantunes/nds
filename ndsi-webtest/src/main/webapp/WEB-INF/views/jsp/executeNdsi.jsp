<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Execução de interfaces EMS</title>
</head>
<body>
	<a href="${pageContext.request.contextPath}/executeNdsi.htm?emsRoute=br.com.abril.nds.integracao.ems0106.route.EMS0106Route&userName=webtest">Executar EMS0106</a>
	<br>
	<a href="${pageContext.request.contextPath}/executeNdsi.htm?emsRoute=br.com.abril.nds.integracao.ems0112.route.EMS0112Route&userName=webtest">Executar EMS0112</a>
	<br>
	<a href="${pageContext.request.contextPath}/executeEMS0118.htm">Executar EMS0118</a>
	<br>
	<a href="${pageContext.request.contextPath}/executeEMS0117.htm">Executar EMS0117</a>
	<br>
	<a href="${pageContext.request.contextPath}/executeEMS0116.htm">Executar EMS0116</a>
</body>
</html>