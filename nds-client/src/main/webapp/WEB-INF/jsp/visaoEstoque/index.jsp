<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/visaoEstoque.js"></script>
	
	<style type="text/css">
		#dialog-visaoEstoque-detalhe, #dialog-visaoEstoque-detalhe-juramentado, #dialog-visaoEstoque-transferencia, 
		#dialog-visaoEstoque-inventario, #dialog-visaoEstoque-inventario-confirm {display:none;}
		#dialog-visaoEstoque-detalhe fieldset, #dialog-visaoEstoque-detalhe-juramentado fieldset{width:800px!important;}
		#dialog-visaoEstoque-transferencia form{margin:0px!important; padding:0px!important; width:900px!important; overflow:hidden; }
		
		.bt_imprimir_conferencia_cega{background:url(${pageContext.request.contextPath}/images/ico_impressora.gif) no-repeat left center; width:300px; line-height:39px; float:left;}
		.bt_imprimir_conferencia_cega a{padding-left:38px; color:#000; font-weight:bold; text-decoration:none;}
		.bt_imprimir_conferencia_cega a:hover{color:#00649F; text-decoration:underline;}
	</style>
</head>

<body>

	<form action="/estoque/visaoEstoque" id="pesquisarVisaoEstoqueForm">
	<input type="hidden" name="filtro.tipoEstoque" id="visaoEstoque_filtro_tipoEstoque"/>
	<input type="hidden" name="filtro.tipoEstoqueSelecionado" id="visaoEstoque_filtro_tipoEstoqueSelecionado"/>
	
	<fieldset class="classFieldset">
		<legend> Pesquisar Estoque</legend>
	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	    	<tr>
	        	<td width="99">Data Movimento:</td>
	          	<td width="170"><input name="filtro.dataMovimentacao" id="visaoEstoque_filtro_dataMovimentacao" type="text" value="${dataAtual}" style="width: 80px; float: left; margin-right: 5px;" /></td>
	          	<td width="71">Fornecedor:</td>
	          	<td width="480">
	          		<select name="filtro.idFornecedor" style="width: 250px;">
						<option selected="selected" value="-1"></option>
						<c:forEach items="${listFornecedores}" var="fornecedor">
							<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
						</c:forEach>
					</select>
	          	</td>
	          	<td width="104"><span class="bt_pesquisar"><a href="javascript:;" id="btnPesquisarVisaoEstoque"></a></span></td>
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
	
	</form>
	
	<%-- POPUPS --%>
	
	<div id="dialog-visaoEstoque-detalhe" title="Vis&atilde;o Estoque">
		<fieldset>
	        <legend>Vis&atilde;o de Estoque / <span id="visaoEstoque_detalhe_estoque"></span></legend>
	        <table class="visaoEstoqueDetalheGrid"></table>
	        <span class="bt_arquivo"><a href="${pageContext.request.contextPath}/estoque/visaoEstoque/exportarDetalhe?fileType=XLS">Arquivo</a></span>
			<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/estoque/visaoEstoque/exportarDetalhe?fileType=PDF">Imprimir</a></span>
		</fieldset>
	</div>
	
	<div id="dialog-visaoEstoque-detalhe-juramentado" title="Vis&atilde;o Estoque">
		<fieldset>
	        <legend>Vis&atilde;o de Estoque / Juramentado</legend>
	        <table class="visaoEstoqueDetalheJuramentadoGrid"></table>
	        <span class="bt_arquivo"><a href="${pageContext.request.contextPath}/estoque/visaoEstoque/exportarDetalheJuramentado?fileType=XLS">Arquivo</a></span>
			<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/estoque/visaoEstoque/exportarDetalheJuramentado?fileType=PDF">Imprimir</a></span>
		</fieldset>
	</div>

	<div id="dialog-visaoEstoque-transferencia" title="Transfer&ecirc;ncia entre Estoques">
		<fieldset style="width:870px!important;">
			<legend>Transferir</legend>
        	<table width="850" border="0" cellspacing="2" cellpadding="2">
          		<tr>
            		<td width="124">Data Movimenta&ccedil;&atilde;o:</td>
            		<td width="123"><span id="visaoEstoque_transferencia_dataMovimentacao" style="width:80px;"></span></td>
            		<td width="320"><strong>Estoque Selecionado</strong>: <span id="visaoEstoque_transferencia_estoqueSelecionado"></span></td>
            		<td width="101">Incluir no Estoque:</td>
            		<td width="150">
            			<select name="filtro.tipoEstoqueSelecionado" id="visaoEstoque_selectIncluirEstoque" style="width:150px;"></select>
            		</td>
          		</tr>
        	</table>
        </fieldset>
    
    	<div class="linha_separa_fields">&nbsp;</div>

		<fieldset style="width:870px!important;">
        	<legend>Transfer&ecirc;ncia entre Estoques</legend>
        	<table class="visaoEstoqueTransferenciaGrid"></table>
        	<span class="bt_sellAll" style="float:right;"><label for="sel">Selecionar Todos</label><input type="checkbox" id="visaoEstoqueCheckSelecionarTodos" name="Todos" onclick="visaoEstoqueController.checkAll(this);" style="float:left; margin-right:40px;"/></span>
		</fieldset>
	</div>
	
	<div id="dialog-visaoEstoque-inventario" title="Invent&aacute;rio do Estoque">
		<fieldset style="width:870px!important;">
      		<legend>Estoque Selecionado: <span id="visaoEstoque_inventario_estoqueSelecionado"></span> - Data: <span id="visaoEstoque_inventario_dataMovimentacao"></span></legend>
            <table class="visaoEstoqueInventarioGrid"></table>
      		<span class="bt_imprimir_conferencia_cega" title="Imprimir"><a href="javascript:visaoEstoqueController.imprimirConferenciaCega();">Imprimir Confer&ecirc;ncia Cega</a></span>
		</fieldset>
	</div>
	
	<div id="dialog-visaoEstoque-inventario-confirm" title="Confirmar Invent&aacute;rio do Estoque">
		<fieldset style="width:300px!important;">
      		<legend>Invent&aacute;rio</legend>
      		<p>Confirma a atualiza&ccedil;&aacute;o do Saldo?</p>
		</fieldset>
	</div>

	
	<script type="text/javascript">
		$(function(){
			visaoEstoqueController.init();
		});
	</script>
</body>