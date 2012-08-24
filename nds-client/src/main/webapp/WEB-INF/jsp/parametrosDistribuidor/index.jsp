<head>
<title>Parâmetros do Distribuidor</title>
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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/editor/jquery.wysiwyg.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/jquery.wysiwyg.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/wysiwyg.image.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/wysiwyg.link.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/editor/wysiwyg.table.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/parametrosDistribuidor.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/endereco.js"></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/scripts/jquery.numeric.js'></script>

<script type="text/javascript">
	
	parametrosDistribuidorController.init("${sessionScope.cadastroDistribuidorExisteLogotipo}");
	
	//var endereco = new Endereco("", "");
	
	//endereco.preencherComboUF("${parametrosDistribuidor.endereco.uf}");
</script>

</head>

<body>

<form id="salvarParametrosDistribuidor">
</form>

<form action="<c:url value='/administracao/parametrosDistribuidor/salvarLogo' />" id="formParamentrosDistribuidor"
	  method="post" enctype="multipart/form-data" >

	<input type="hidden" name="formUploadAjax" value="true" />
	
	<div class="corpo">
	    <div class="container">	
	    <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
					<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
					<b>Parâmetros do Distribuidor < evento > com < status >.</b></p>
		</div>
	    <fieldset class="classFieldset">
	   	    <legend>Parâmetros do Distribuidor</legend>
	        <div id="tabDistribuidor">
				<ul>
					<li><a href="#tabCadastroFiscal">Cadastro / Fiscal</a></li>
					<li><a href="#tabOperacao">Operação</a></li>
					<li><a href="#tabDiferenciada">Operação Diferenciada</a></li>
					<li><a href="#tabEmissao">Documentos</a></li>
					<li><a href="#tabContratos">Contratos e Garantias</a></li>
				    <li><a href="#tabNegociacao">Negociação</a></li>
				     <li><a href="#tabAprovacao">Aprovação</a></li>
				</ul>
				
				<!--  Aba Cadastro / Fiscal --> 
	            <jsp:include page="tabCadastroFiscal.jsp"/>
				
	            <!--  Aba Operação --> 
	            <jsp:include page="tabOperacao.jsp"/>
				
				<jsp:include page="tabOperacaoDiferenciada.jsp"/>
							
				<jsp:include page="tabEmissao.jsp"></jsp:include>
				
			    <!-- Aba Contratos e Garantias  -->	
	            <jsp:include page="tabContratosGarantias.jsp"></jsp:include>
	            			
				<jsp:include page="tabNegociacao.jsp"></jsp:include>
			   
			    <jsp:include page="tabAprovacao.jsp"></jsp:include>
			    
		   		<br clear="all" />
			</div>
		</fieldset>
	        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="parametrosDistribuidorController.popup_confirm();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Salvar</a></span>
	      <div class="linha_separa_fields">&nbsp;</div>
	    </div>
	</div>
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

<form id="confirmarParametrosDistribuidor">
	<div id="dialog-confirm" title="Salvar Parâmetro do Distribuidor">
		<p>Confirma os Parâmetros do Distribuidor?</p>
	</div>
</form>
</body>