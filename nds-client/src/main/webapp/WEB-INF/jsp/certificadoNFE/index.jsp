<input id="permissaoAlteracao" type="hidden"
	value="${permissaoAlteracao}">

<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/certificadoNFE.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery-upload/js/jquery.iframe-transport.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery-upload/js/jquery.fileupload.js" type="text/javascript"></script>

<title>Certificado NFE</title>

<style type="text/css">
fieldset label {
	width: auto;
	margin-bottom: 0px !important;
}

.gridLinhaDestacada {
	background: #BEBEBE;
	font-weight: bold;
	color: #fff;
}

.gridLinhaDestacada:hover {
	color: #000;
}

.gridLinhaDestacada a {
	color: #fff;
}

.gridLinhaDestacada a:hover {
	color: #000;
}
</style>
<style type="text/css">
#dialog-box {
	display: none;
}

#dialog-box fieldset {
	width: 570px !important;
}
</style>

<script language="javascript" type="text/javascript">
	$(function() {
		certificadoNFEController.init();
	});
</script>

</head>

<body>
	<div id="dialog-confirmar" title="Atualização do certificado NF-e?"
		style="display: none;">
		<p>Confirma a Atualiza&ccedil;&atilde;o do Certificado NF-e?</p>
	</div>

	<div class="areaBts">
		<div class="area">
			<span class="bt_novos">
				<a isEdicao="true" id="certificadoNFEConfirmar" onclick="certificadoNFEController.btnConfirmar();" rel="tipsy" title="Confirmar Upload do Arquivo">
					<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">
				</a>
			</span>
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>
	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">

		<legend> Certificado NF-e</legend>

		<table width="600" border="0" cellpadding="2" cellspacing="1"
			class="filtro">
			<div id="uploadedFileCertificadoDiv">
			<tbody>
				<tr>
					<td width="70">Certificado:</td>
					<td width="200" colspan="3"><input type="file" name="certificado-upload" /></td>
				</tr>
				
				<tr>
					<td width="50">Data Inicio:</td>
					<td width="200"><input type="text" id="certificado-data-inicio" name="certificado-data-inicio" class="input-date"/></td>

					<td width="50">Data Fim:</td>
					<td width="200"><input type="text" id="certificado-data-fim" name="certificado-data-fim" class="input-date"/></td>
				</tr>
				
				<tr>
					<td width="70">Senha:</td>
					<td width="250"><input type="password" id="certificado-senha" name="certificado-senha" /></td>

					<td width="70">Alias:</td>
					<td width="250"><input type="text" id="certificado-alias" name="certificado-alias" /></td>
				</tr>

			</tbody>
			</div>
		</table>

	</fieldset>

	<div class="linha_separa_fields">&nbsp;</div>
	
	

</body>