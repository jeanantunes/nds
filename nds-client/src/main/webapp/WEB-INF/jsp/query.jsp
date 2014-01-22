<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert query here</title>
<style type="text/css">
	div.query-form {
		width: 830px;
		margin: auto;
	}
	div.query-form table {
		border-collapse:collapse;
	}
	div.query-form table td {
		border: 1px solid;
		padding: 1px 4px;
	}
</style>
</head>
<body>
	<br>
	<c:url var="formAction" value="/query"/>
	<div class="query-form">
		<form action="${formAction}" method="post" >
			<textarea rows="15" cols="100" name="query">${query}</textarea>
			<br>
			<div style="float: right;">
				<label>Update: </label>
				<input type="checkbox" name="update" value="true" ${update ? 'checked="checked"' : ''}/>
				<input type="submit"/>
			</div>
		</form>
	</div>
	<br>
	<br>
	<div class="query-form">${result}</div>
</body>
</html>