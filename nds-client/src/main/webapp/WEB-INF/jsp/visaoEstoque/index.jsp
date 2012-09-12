<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/visaoEstoque.js"></script>
</head>

<body>
	<fieldset class="classFieldset">
		<legend> Pesquisar Estoque</legend>
	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	    	<tr>
	        	<td width="99">Data Movimento:</td>
	          	<td width="170"><input name="dataMovimento" type="text" id="dataMovimento" value="${dataAtual}" style="width: 80px; float: left; margin-right: 5px;" /></td>
	          	<td width="71">Fornecedor:</td>
	          	<td width="480">
	          		<select name="filtroConsultaNotaFiscal.idFornecedor" id="selectFornecedores" style="width: 250px;">
						<option selected="selected" value="-1"></option>
						<c:forEach items="${listFornecedores}" var="fornecedor">
							<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
						</c:forEach>
					</select>
	          	</td>
	          	<td width="104"><span class="bt_pesquisar"><a href="javascript:;" id="btnPesquisar"></a></span></td>
	        </tr>
	    </table>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>

	<div class="grids" style="display:none;">
		<fieldset class="classFieldset">
			<legend>Vis&atilde;o do Estoque</legend>
       	  	<table class="visaoEstoqueGrid"></table>
       	  	<span class="bt_arquivo"><a href="${pageContext.request.contextPath}/estoque/visaoEstoque/exportar?fileType=XLS">Arquivo</a></span>
			<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/estoque/visaoEstoque/exportar?fileType=PDF">Imprimir</a></span>
		</fieldset>
	</div>
	
	
	<script type="text/javascript">
		$(function(){
			visaoEstoqueController.init();
		});
	</script>
</body>