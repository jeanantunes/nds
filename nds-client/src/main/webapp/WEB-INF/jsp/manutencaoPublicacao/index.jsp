<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/alteracaoPrecoEdicaoExpedido.js"></script>
	
	<script type="text/javascript">
	
		var pesquisaProdutoPreco = new PesquisaProduto(manutencaoPublicacaoController.workspace);
        var pesquisaCota = new PesquisaCota(manutencaoPublicacaoController.workspace);

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
	
		<div id="dialog-confirmacao-alteracao-preco" title="Manutenção de Publicação Expedida" style="display: none;">
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

		<div style="padding-top:45px;">

			<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">

				<legend>Baixa Financeira</legend>


				<table width="575" border="0" cellspacing="1" cellpadding="2">
					<tr>
						<td width="20">
							<input type="radio" id="opcoesVisualizacao1" value="PRECO_CAPA" name="opcoesVisualizacao" onchange="$('#cotaDesconto,.descontoOpcoes').hide();$('#publicaoExpedida').show()"/>
						</td>
						<td width="99">
							<label for="opcoesVisualizacao1"><strong>Preço por capa</strong></label>
							</span>
						</td>
						<td width="20">
							<input type="radio" id="opcoesVisualizacao2" value="DESCONTO" name="opcoesVisualizacao" onchange="$('#cotaDesconto,#publicaoExpedida,#manut-publicacao-resultado').hide(); $('.descontoOpcoes').show()"/>
						</td>
						<td width="99">
							<label for="opcoesVisualizacao2"><strong>Desconto</strong></label>
						</td>

						<td width="20" >
							<input class="descontoOpcoes" style="display:none;" type="radio" id="descontoOpcoesProduto" value="PRODUTO" name="descontoOpcoes" onchange="$('#descontoCota,#cotaDesconto').hide();$('#publicaoExpedida').show()"/>
						</td>
						<td width="99" >
							<label class="descontoOpcoes" style="display:none;" for="descontoOpcoesProduto"><strong>Produto</strong></label>
						</td>

						<td width="20" >
							<input class="descontoOpcoes" style="display:none;" type="radio" id="descontoOpcoesCota" value="COTA" name="descontoOpcoes" onchange="$('#publicaoExpedida,#novoDescontoFields').hide();$('#cotaDesconto').show()"/>
						</td>
						<td width="99" >
							<label class="descontoOpcoes" style="display:none;" for="descontoOpcoesCota"><strong>Cota</strong></label>
						</td>
					</tr>
				</table>
			</fieldset>


		</div>



		<fieldset id="publicaoExpedida" class="fieldFiltro fieldFiltroItensNaoBloqueados" style="display:none">
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
							<a href="javascript:void(0);" id="linkPesquisarAlteracaoPreco"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
						</span>
					</td>
	  			</tr>
	  		</table>
	  	</fieldset>

		<!-- COTA -->
		<fieldset id="cotaDesconto" class="fieldFiltro fieldFiltroItensNaoBloqueados" style="display:none">
			<legend> Manutenção de Preço de Publicação Expedida</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="5%" align="right">Cota:</td>
					<td width="10%">
						<input class="campoDePesquisa" id="cota" name="manut-publicacao-codigoProduto" style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
							   onchange="pesquisaCota.pesquisarPorNumeroCota('#cota', '#nomeCota',false,function(result){ manutencaoPublicacaoController.cotaNumero = result.nome;  });"/>
					</td>
					<td width="6%" align="right">Nome:</td>
					<td width="196">
						<input class="campoDePesquisa" id="nomeCota" type="text" name="manut-publicacao-nomeProduto"  style="width: 150px;" maxlength="255"
							   onkeyup="pesquisaCota.autoCompletarPorNome('#nomeCota');"
							   onblur="pesquisaCota.pesquisarPorNomeCota('#cota', '#descricaoCota');"/>
					</td>
					<td width="258">
						<span style="cursor: pointer;" class="bt_novos" onclick="manutencaoPublicacaoController.pesquisar();">
							<a href="javascript:;" id=""><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
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

		<fieldset id="novoDescontoFields" class="fieldPrecoProduto" style="display: none;" >

			<legend><span id="novoDesconto-txtLegenda">Cota:</span></legend>

			<div style="margin-left: 1%;">
				<br />
				<strong>Desconto Atual</strong>: <select id="descontoAtualSel"></select>
				<br />
				<br />
				<p>
					<strong>Novo desconto:</strong>
					<input type="text" name="manut-publicacao-novoPrecoProduto" id="novoDescontoInput" style="width:80px; margin-left:5px;" maxlength="5"/>
				</p>
				<br />
			</div>
		</fieldset>
	  	
	</form>
</body>