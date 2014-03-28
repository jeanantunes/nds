<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/NDS.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/menu_superior.css" />
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<style type="text/css">
body {
	font-size: 12px !important;
}

h1 {
	font-size: 20px;
}

h2 {
	font-size: 25px;
}

p {
	margin: 0px;
	padding: 0px;
	font-size: 11px;
}

.capas tr {
	border: 1px solid #000;
}

.box_rel {
	line-height: 15px !important;
	background: #fff;
}

.box_dados {
	line-height: 30px !important;
	font-size: 16px;
	font-weight: bold;
}

.class_sm {
	font-weight: bold;
	font-size: 13px;
}

.class_total {
	font-weight: bold;
	font-size: 16px;
	background: #C0C0C0;
}

.relatorios td {
	padding-left: 5px;
}

.class_total1 {
	font-weight: bold;
	font-size: 16px;
	background: #C0C0C0;
}
</style>
<script language="javascript" type="text/javascript">
	function imprimir() {
		$("#btImpressao", BaseController.workspace).hide();
		window.print();
	}
</script>
</head>

<body>
	<c:forEach items="${maps}" var="mapa">
		<table width="800" border="0" align="center" cellpadding="3"
			cellspacing="0" style="border: 1px solid #000; margin-bottom: 5px;">
			<tr>
				<td width="121" height="21" align="center"><span> <span
						class=""> <img
							src="${pageContext.request.contextPath}/administracao/parametrosDistribuidor/getLogo?number=${pageContext.request.requestedSessionId}"
							border="0" height="70" width="110" />
					</span>
				</span></td>
				<td width="301" align="center" valign="middle"><h3>${nomeDistribuidor}</h3></td>
				<td width="359" align="right" valign="middle"><h1>Mapa de Abastecimento de Produto por Cota</h1></td>
			</tr>
			<tr>
				<td colspan="3" align="center" valign="middle"></td>
			</tr>
		</table>
		<table width="800" border="0" align="center" cellpadding="0"
			cellspacing="0" style="margin-top: 5px;">
			<tr class="class_linha_3">
				<td width="660" class="relatorios"
					style="padding-left: 5px; border-left: 1px solid #000; border-top: 1px solid #000; border-bottom: 1px solid #000; border-right: 1px solid #000;">
					<table width="100%" border="0" cellspacing="1" cellpadding="1">
						<tr>
							<td width="8%"><strong>Produto</strong>:</td>
							<td width="46%"><strong>${mapa.nomeProduto}</strong></td>
							<td width="14%"><strong>Código de Barras:</strong></td>
							<td width="32%">${mapa.codigoDeBarras}</td>
						</tr>
						<tr>
							<td><strong>Edição:</strong></td>
							<td>${mapa.numeroEdicao}</td>
							<td><strong>Preço Capa R$:</strong></td>
							<td>${mapa.precoCapa}</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<table width="800" border="0" align="center" id="wrapper"
			cellpadding="0" cellspacing="0"
			style="border-spacing: 5px;">

			<tr>
				<td width="195" align="left" valign="top">

					<table width="195" border="0" cellpadding="0" cellspacing="0"
						style="margin-top: 5px;" class="relatorios">

						<tr class="class_linha_3">
							<td width="100"
								style="border-left: 1px solid #000; border-top: 1px solid #000; border-bottom: 1px solid #000;"><strong>Cota</strong></td>
							<td width="95" align="center"
								style="border-bottom: 1px solid #000; border-top: 1px solid #000; border-left: 1px solid #000; border-right: 1px solid #000;"><strong>Total</strong></td>
						</tr>

						<c:forEach items="${mapa.cotasQtdes}" var="cota"
							varStatus="statusCota">

							<tr class="class_linha_${statusCota.index%2==0?1:2}">
								<td
									style="border-left: 1px solid #000; border-bottom: 1px solid #000;">${cota.key}</td>
								<td align="center" class="class_total"
									style="border-right: 1px solid #000; border-left: 1px solid #000; border-bottom: 1px solid #000;">${cota.value}</td>
							</tr>
							<c:if test="${(((statusCota.index+1) % (qtdMaxRow)) == 0) }">
					</table>

				</td>
				<td width="195" valign="top">

					<table width="195" border="0" cellpadding="0" cellspacing="0"
						style="margin-top: 5px;" class="relatorios">

						<tr class="class_linha_3">
							<td width="100"
								style="border-left: 1px solid #000; border-top: 1px solid #000; border-bottom: 1px solid #000;"><strong>Cota</strong></td>
							<td width="95" align="center"
								style="border-bottom: 1px solid #000; border-top: 1px solid #000; border-left: 1px solid #000; border-right: 1px solid #000;"><strong>Total</strong></td>
						</tr>

						</c:if>

						</c:forEach>

					</table>

				</td>

			</tr>

			<tr>
				<td>&nbsp</td>
				<td>&nbsp</td>
				<td>&nbsp</td>
				<td>&nbsp</td>
			</tr>
		</table>
		
		<table width="800" border="0" align="center" cellpadding="0" cellspacing="0" style="page-break-after: always">
		<tr class="class_linha_3">
			<td width="660" class="relatorios"
				style="padding-left: 5px; border-left: 1px solid #000; border-top: 1px solid #000; border-bottom: 1px solid #000; border-right: 1px solid #000;"><table
					width="100%" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td style="width: 15%;"><strong>Total</strong>:</td>
						<td width="43%"><strong>Exemplares</strong></td>
					</tr>
					<c:forEach items="${mapa.boxQtdes}" var="mapBoxQtdes">
						<tr>
							<td><strong>${mapBoxQtdes.key}:</strong></td>
							<td>${mapBoxQtdes.value}</td>
						</tr>
					</c:forEach>
				</table></td>
		</tr>
	</table>
	
	</c:forEach>

</body>
</html>