<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/alteracaoPrecoEdicaoExpedido.js"></script>
	
	<script type="text/javascript">
	
		var pesquisaProdutoPreco = new PesquisaProduto(manutencaoPublicacaoController.workspace);	
		$(function(){	
			manutencaoPublicacaoController.init();
		});
	</script>
	
	<style type="text/css">
		
		.fieldPrecoProduto{
			margin-top: 27px;
			width: 980px!important;
			margin-right: 0px!important}
	</style>
	
</head>

<body>
	<form action="financeiro/manutencaoPublicacao/pesquisarProduto" method="post">	
	
		<div id="dialog-confirmacao-alteracao-preco" title="Manutenção de Preço de Publicação Expedida" style="display: none;">
			<strong>O valor do consignado das cotas que receberam o produto será atualizado, Confirma?</strong>
		</div>
		
		<div class="areaBts">
			<div class="area">
				<span style="cursor: pointer;" class="bt_novos" rel="tipsy" title="Confirmar Alteração Preço de Publicação">
	            	<a isEdicao="true" href="javascript:;" onclick="manutencaoPublicacaoController.popupConfirmacao();" id="linkConfirmarAlteracao"><img src="${pageContext.request.contextPath}/images/ico_check.gif" border="0" /></a>
	            </span>
			</div>
		</div>
		<div class="linha_separa_fields">&nbsp;</div>
    	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
			<legend> Manutenção de Preço de Publicação Expedida</legend>
	        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	        	<tr>
	        		<td width="45" align="right">Código:</td>
	        		<td width="79">
	        			<input class="campoDePesquisa" id="manut-publicacao-codigoProduto" name="manut-publicacao-codigoProduto" style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
						   onchange="pesquisaProdutoPreco.pesquisarPorCodigoProdutoAutoCompleteEdicao('#manut-publicacao-codigoProduto', '#manut-publicacao-nomeProduto', '#manut-publicacao-edicaoProduto' , false);" />
	        		</td>
					<td width="64" align="right">Produto:</td>
					<td width="196">
						<input class="campoDePesquisa" id="manut-publicacao-nomeProduto" type="text" name="manut-publicacao-nomeProduto"  style="width: 150px;" maxlength="255"
					       onkeyup="pesquisaProdutoPreco.autoCompletarPorNomeProduto('#manut-publicacao-nomeProduto', false);"
					       onblur="pesquisaProdutoPreco.pesquisarPorNomeProduto('#manut-publicacao-codigoProduto', '#manut-publicacao-nomeProduto', null, false);"/>
					</td>
					<td width="50" align="right">Edição:</td>
					<td width="90">
						<input class="campoDePesquisa" id="manut-publicacao-edicaoProduto"  type="text" name="edicoes" style="width:80px;"/></td>
					<td width="258">
						<span style="cursor: pointer;" class="bt_novos" onclick="manutencaoPublicacaoController.pesquisar();">
							<a href="javascript:;" id="linkPesquisarAlteracaoPreco"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
						</span>
					</td>
	  			</tr>
	  		</table>
	  	</fieldset>
	  	
	  	<fieldset class="fieldPrecoProduto" style="display: none;" id="manut-publicacao-resultado">
	  		
	  		<legend><span id="manut-publicacao-txtLegenda">Publicação:</span></legend>
	  			
	  		<div style="margin-left: 1%;">	
	            <br />
	            <strong>Preço Atual</strong>: <span id="manut-publicacao-txtPrecoProduto"></span>
	            <br />
	            <br />
	            <p>
	            	<strong>Novo Preço:</strong>
	            	<input type="text" name="manut-publicacao-novoPrecoProduto" id="manut-publicacao-novoPrecoProduto" style="width:80px; margin-left:5px;" maxlength="10"/>
	            </p>
	            <br />
			</div>
		</fieldset>
	  	
	</form>
</body>