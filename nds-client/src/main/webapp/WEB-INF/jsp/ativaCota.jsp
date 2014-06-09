<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Ativa Cota</title>
<style type="text/css">
	div.query-form {
		width: 430px;
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
	<c:url var="formAction" value="/ativaCota"/>
	<div class="query-form">
		<form action="${formAction}" method="post" >
			<label>Cota: </label>
			<input type="text" name="numeroCota" />
			<br>
			<label>Situação Cadastro: </label>
			<select name="situacaoCadastro">
	            <c:forEach items="${situacaoCadastro}" var="situacaoCadastro">
					<option value="<c:out value="${situacaoCadastro}"/>"><c:out value="${situacaoCadastro}"/></option>
				</c:forEach>
       		</select>
       		<br>
			<div style="float: right;">
				<input type="submit" value="Atualizar Cota"/>
			</div>
		</form>
	</div>
	<br>
	<br>
	<div class="query-form">${result}</div>
</body>
</html>