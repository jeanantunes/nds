<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}" />
<head>
<style type="text/css">
	#dialog-confirm{display:none;}
	label{width:auto!important;}
	#dialog-pesq-fornecedor fieldset {width:450px!important;}
	.forncedoresSel{display:none;}
	#dialog-pesq-fornecedor{display:none;}
	.forncedores ul{margin:0px; padding:0px;}
	.forncedores li{display:inline;}
	.forncedoresSel, .editorSel {
	    padding: 0px!important;
	}
	
	body{
	font-family: arial;
	font-size: 11px;
	color: #000;
	background-color: #FFF;
	margin: 0px;
}
</style>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/editor/jquery.wysiwyg.css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/jquery.wysiwyg.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/wysiwyg.image.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/wysiwyg.link.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/wysiwyg.table.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/parametrosDistribuidor.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/endereco.js"></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/scripts/jquery.numeric.js'></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/operacaoDiferenciada.js"></script>


<script type="text/javascript">
	
	parametrosDistribuidorController.init();
	
	var endereco = new Endereco("", "");
	
	endereco.preencherComboUF("${parametrosDistribuidor.endereco.uf}");
	bloquearItensEdicao(parametrosDistribuidorController.workspace);
	
	<c:if test="${!parametrosDistribuidor.possuiRegimeEspecialDispensaInterna}">
	$('.camposEspecificosRegimeEspecial').hide();
	</c:if>
	
</script>

</head>

<body>

<form id="salvarParametrosDistribuidor">
</form>

<form action="<c:url value='/administracao/parametrosDistribuidor/salvarLogo' />" id="formParamentrosDistribuidor"
	  method="post" enctype="multipart/form-data" >

	<input type="hidden" name="formUploadAjax" value="true" />
	
	<input type="hidden" id="tipoEnderecoHidden"
		   value="${parametrosDistribuidor.endereco.tipoEndereco}" />
					   		  
	<input type="hidden" id="relancamentoParciaisEmDiasHidden"
		   value="${parametrosDistribuidor.relancamentoParciaisEmDias}" />
		   
    <input type="hidden" id="impressaoNECADANFEHidden"
		   value="${parametrosDistribuidor.impressaoNECADANFE}" />
	
	<input type="hidden" id="impressaoCEHidden"
		   value="${parametrosDistribuidor.impressaoCE}" />
		   
	<input type="hidden" id="hasLogotipoHidden"
		   value="${hasLogotipo}" />
		   
	<div class="areaBts">
		<div class="area">
		    <span class="bt_novos"><a isEdicao="true" href="javascript:;" onclick="parametrosDistribuidorController.popup_confirm();" rel="tipsy" title="Salvar Parâmetros do Distribuidor"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/></a></span>
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>
    <fieldset class="fieldFiltro">
   	    <legend>Parâmetros do Distribuidor</legend>
        <div id="tabDistribuidor">
			<ul>
				<li><a href="#tabCadastro">Cadastro</a></li>
				<li><a href="#tabOperacao">Operação</a></li>
				<li><a href="#tabDiferenciada">Operação Diferenciada</a></li>
				<li><a href="#tabDistribuicao">Distribuição</a></li>
				<li><a href="#tabEmissao">Documentos</a></li>
				<li><a href="#tabContratos">Contratos e Garantias</a></li>
			    <li><a href="#tabNegociacao">Negociação</a></li>
			    <li><a href="#tabFiscal">Fiscal</a></li>
			    <li><a href="#tabAprovacao">Aprovação</a></li>
			</ul>
			
            <jsp:include page="tabCadastro.jsp"/>
			
            <!--  Aba Operação --> 
            <jsp:include page="tabOperacao.jsp"/>
			
			<jsp:include page="tabOperacaoDiferenciada.jsp"/>
			
			<!-- Aba Distribuição -->
			<jsp:include page="tabDistribuicao.jsp" />
			
			<jsp:include page="tabEmissao.jsp" />
			
			<jsp:include page="tabNegociacao.jsp" />

			<jsp:include page="tabFiscal.jsp" />
			
		    <!-- Aba Contratos e Garantias  	-->
            <jsp:include page="tabContratosGarantias.jsp"/>
             
            <%--
            <jsp:include page="tabAprovacao.jsp" />
            --%>
						
	   		<br clear="all" />
		</div>
	</fieldset>
</form>
	
<form id="pesquisaFornecedorParametrosDistribuidor">
	<div id="dialog-pesq-fornecedor" title="Selecione os Fornecedores">
		<fieldset>
			<legend>Selecione um ou mais Fornecedores</legend>
		    <select name="" size="1" multiple="multiple" style="width:440px; height:150px;" >
	          <c:forEach items="${fornecedores}" var="fornecedor">
	          	<option value="${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</option>
	     	  </c:forEach>
		    </select>
		</fieldset>
	</div>
</form>

<div id="confirmarParametrosDistribuidor" title="Salvar Parâmetro do Distribuidor">
	<p>Confirma os Parâmetros do Distribuidor?</p>
</div>

</body>