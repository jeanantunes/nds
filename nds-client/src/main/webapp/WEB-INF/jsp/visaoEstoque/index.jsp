<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/visaoEstoque.js"></script>
	
	<style type="text/css">
		#dialog-lancamento, #dialog-suplementar, #dialog-encalhe, #dialog-transferencia, #dialog-inventario-confirm, #dialog-inventario, #dialog-juramentado{display:none;}
		#dialog-suplementar fieldset, #dialog-encalhe fieldset{width:600px!important;}
		#dialog-lancamento fieldset, #dialog-juramentado fieldset{width:800px!important;}
		#dialog-transferencia form{margin:0px!important; padding:0px!important; width:900px!important; overflow:hidden; }
	</style>
</head>

<body>

	<form action="/estoque/visaoEstoque" id="pesquisarVisaoEstoqueForm">
	
	<fieldset class="classFieldset">
		<legend> Pesquisar Estoque</legend>
	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	    	<tr>
	        	<td width="99">Data Movimento:</td>
	          	<td width="170"><input name="filtro.dataMovimentacao" type="text" value="${dataAtual}" style="width: 80px; float: left; margin-right: 5px;" /></td>
	          	<td width="71">Fornecedor:</td>
	          	<td width="480">
	          		<select name="filtro.idFornecedor" style="width: 250px;">
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
	
	</form>
	
	
	<div id="dialog-lancamento" title="Vis&atilde;o Estoque Lan&ccedil;amento">
		<fieldset>
	        <legend>Vis&atilde;o de Estoque / Lan&ccedil;amento</legend>
	        <table class="visaoEstoqueLanctoGrid"></table>
	        <span class="bt_arquivo"><a href="${pageContext.request.contextPath}/estoque/visaoEstoque/exportarDetalhe?fileType=XLS">Arquivo</a></span>
			<span class="bt_imprimir"><a href="${pageContext.request.contextPath}/estoque/visaoEstoque/exportarDetalhe?fileType=PDF">Imprimir</a></span>
		</fieldset>
	</div>

	
	
	<script type="text/javascript">
		$(function(){
			visaoEstoqueController.init();
		});
	</script>
</body>