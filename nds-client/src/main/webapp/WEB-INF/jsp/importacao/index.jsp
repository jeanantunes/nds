<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
<script type="text/javascript">

$(function() {	
	
	var options = {
		success: tratarRespostaImportacao,
    };
	
	$('#formImportacao').ajaxForm(options);
	
});

function tratarRespostaImportacao(data) {
	
	data = replaceAll(data, "<pre>", "");
	data = replaceAll(data, "</pre>", "");
	
	data = replaceAll(data, "<PRE>", "");
	data = replaceAll(data, "</PRE>", "");

	var responseJson = jQuery.parseJSON(data);
	
	if (responseJson.mensagens) {

		exibirMensagensImportacao(
			responseJson.mensagens.tipoMensagem, 
			responseJson.mensagens.listaMensagens
		);
	}
}

function exibirMensagensImportacao(tipoMensagem, mensagens) {

	var divError= $("#errorImportacao");

	var textError = $("#idTextErrorImportacao");
	
	$(divError).hide();

	if (tipoMensagem != "ERROR") {
		
		exibirMensagem(tipoMensagem, mensagens);
		
		return;
	}
	
	clearMessageTimeout();

	montarExibicaoMensagem(false, tipoMensagem, mensagens,
						   null, null,
						   null, null,
						   divError, textError);

	clearMessageTimeout();
}

</script>
</head>
<body>

<form id="formImportacao" action='<c:url value="/importacao/upload" />' enctype="multipart/form-data" method="post">

	<b> Arquivo: </b> 

	<input type="file" id="arquivoImportacao" name="arquivoImportacao" style="position:relative" size="25" />

	<input type="hidden" name="formUploadAjax" value="true" />

	<br clear="all" />
	<br clear="all" />

	<b> Tipo de Importação: </b>
	
	<select name="tipoImportacaoArquivo">
		<option></option>
		<c:forEach var="tipoImportacao" items="${tiposImportacao}">
			<option value="${tipoImportacao}"> ${tipoImportacao} </option>
		</c:forEach>
	</select>
	
	<input type="submit" value="Realizar importação" />

</form>

<br clear="all" />

<div id="errorImportacao" class="ui-state-error ui-corner-all" 
	 style="display: none; position: absolute; width: 980px; z-index: 10;">
	<p>
		<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
		<b id="idTextErrorImportacao"></b>
	</p>
</div>

</body>