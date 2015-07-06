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

<form id="pesquisar_certicado_form">
	<div class="areaBts">
		<div class="area">
			<span class="bt_novos">
				<a href="javascript:;" id="btnNovo" rel="tipsy" title="Incluir Novo Certificado">
					<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>
				</a>
			</span>
		</div>  		  		
	</div>
  	<div class="linha_separa_fields">&nbsp;</div>

	<form id="excluir_certificado_form">
		<div id="dialog-certificado-excluir" title="Excluir Certificado">		
			<p>Confirma a exclus&atilde;o deste Certificado?</p>
		</div>
	</form>

	<form action="/nfe/certificado/" id="novo_certificado_form">
		
		<input type="hidden" name="certificado.id" id="certificadoId" style="width:80px;"/>
		
		<div id="dialog-novo" title="Incluir Novo Certificado">  
		
		<div class="linha_separa_fields">&nbsp;</div>

		<jsp:include page="../messagesDialog.jsp" /> 
				
		<table width="600" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<div id="uploadedFileCertificadoDiv">
			<tbody>
				<div id="uploadedFileCertificado">
					<tr>
						<td width="80">Certificado:</td>
						<td width="200" colspan="3">
							<input type="file" name="certificado-upload" id="certificado-upload" size="50"/>
						</td>
					</tr>
					
					<tr>
						<td>&nbsp;</td>
						<td olspan="2">
							<span id="nomeCertificadoUpload"></span>
						</td>
					</tr>
				</div>
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
		</div>
	</form>
	
	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
   		<legend>Pesquisar Certificado</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              	<td width="80">Nome Certificado:</td>
              	<td width="152"><input type="text" name="nomeCertificado" id="nomeCertificado" style="width:130px;"/></td>
                
            	<td width="474"><span class="bt_novos">
            		<a isEdicao="true" name="btnPesquisar" id="btnPesquisar" onclick="certificadoNFEController.buscar();">
            			<img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" />
            		</a></span>
            	</td>
        	</tr>
    	</table>
	</fieldset>
    <div class="linha_separa_fields">&nbsp;</div>
	<div class="grids" style="display:none;">
	      <fieldset class="fieldGrid">
	       	  <legend>Certificados Cadastrados</legend>
	        	<table class="certificadoGrid"></table>	
	      </fieldset>
	</div>
</form>

</body>