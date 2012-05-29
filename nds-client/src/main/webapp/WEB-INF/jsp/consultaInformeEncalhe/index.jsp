<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jquery-upload/css/jquery.fileupload-ui.css">
<script
	src="${pageContext.request.contextPath}/scripts/jquery-upload/js/vendor/jquery.ui.widget.js"
	type="text/javascript"></script>
<script
	src="${pageContext.request.contextPath}/scripts/jquery-upload/js/jquery.iframe-transport.js"
	type="text/javascript"></script>
<script
	src="${pageContext.request.contextPath}/scripts/jquery-upload/js/jquery.fileupload.js"
	type="text/javascript"></script>	
<script
	src="${pageContext.request.contextPath}/scripts/informeEncalhe.js"
	type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	var informeEncalhe = new InformeEncalhe();
});
</script>
</head>
<body>	
	<fieldset class="classFieldset">
		<legend> Pesquisar Informe de Recolhimento</legend>
		<table width="950" border="0" cellpadding="2" cellspacing="1"
			class="filtro">
			<tr>
				<td width="68">Fornecedor:</td>
				<td width="210"><select name="select" id="idFornecdorSelect"
					style="width: 200px;">
						<option value="" selected="selected">Todos</option>
					<c:forEach items="${fornecedores}" var="fornecedor">					
						<option value="${fornecedor.key }">${fornecedor.value }</option>
					</c:forEach>
				</select></td>

				<td colspan="3">Semana:</td>

				<td width="89"><input type="text"
					id="semanaRecolhimentoBox" style="width: 70px;" /></td>

				<td colspan="3">Data Recolhimento:</td>

				<td width="272"><input type="text"
					id="dataRecolhimentoBox" style="width: 70px;" /></td>

				<td width="104"><span class="bt_pesquisar"><a
						href="javascript:;" id="btnPesquisar">Pesquisar</a></span></td>

			</tr>
		</table>
	</fieldset>

	<div class="linha_separa_fields">&nbsp;</div>
	<fieldset class="classFieldset">

		<legend>Informe de Recolhimentos Cadastrados</legend>

		<div class="grids" style="display: none;">

			<div id="consultaInformeEncalheGrid"></div>
			<span class="bt_novos" title="Imprimir"><a href="javascript:;" id="btnImprimir"><img
					src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" alt="" />Imprimir</a></span>
		</div>
	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>
	<div></div>
</body>
</html>