<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
	<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/furoProduto.js"></script>
	<script language="javascript" type="text/javascript">
		$(function(){
			furoProdutoController.init();
			bloquearItensEdicao(furoProdutoController.workspace);
		});
	</script>
</head>

<body>
	<form action="lancamento/furoProduto/pesquisar" method="post">
		<div id="dialog-confirmar-furo-produto" title="Furo de Produto" style="display:none;">
			<strong>Expedição Lógica já realizada, todos os movimentos serão extornados. Confirmar?</strong>
		</div>
		<div id="dialog-novo" title="Furo de Produto">
			<strong>Confirma o Furo de Produto?</strong>
		</div>
		<div class="areaBts">
			<div class="area">
				<span style="cursor: pointer;" class="bt_novos" rel="tipsy" title="Confirmar Furo de Produto">
	            	<a isEdicao="true" href="javascript:;" onclick="furoProdutoController.popup();" id="linkConfirmar"><img src="${pageContext.request.contextPath}/images/ico_check.gif" border="0" /></a>
	            </span>
			</div>
		</div>
		<div class="linha_separa_fields">&nbsp;</div>
    	<fieldset class="fieldFiltro">
			<legend> Furo de Produto</legend>
	        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	        	<tr>
	        		<td width="45" align="right">Código:</td>
	        		<td width="79">
	        			<input type="text" style="width:70px;" name="codigo" id="codigo" maxlength="255" onblur="furoProdutoController.buscarNomeProduto();"/>
	        		</td>
					<td width="64" align="right">Produto:</td>
					<td width="196">
						<input type="text" name="produto" id="produto" style="width:150px;" maxlength="255" onkeypress="furoProdutoController.pesquisarPorNomeProduto();"/>

					</td>
					<td width="50" align="right">Edição:</td>
					<td width="90">
						<input type="text" style="width:70px;" name="edicao" id="edicao" maxlength="20"/>
					</td>
					<td width="150" align="right">Data Lançamento:</td>
					<td width="146">
						<input type="text" name="dataLancamento" id="dataLancamento" style="width:70px;"/>
					</td>
					<td width="258">
						<span style="cursor: pointer;" class="bt_novos" onclick="furoProdutoController.pesquisar();">
							<a href="javascript:;" id="linkPesquisarFuro"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
						</span>
					</td>
	  			</tr>
	  		</table>
	  	</fieldset>
	  	
	  	<div class="linha_separa_fields">&nbsp;</div>
	  	
	  	<fieldset style="display: none;" id="resultado">
	  		<legend>Furo do Produto</legend>
	  			<div class="imgProduto">
	  				<img src="" alt="" id="imagem"/>
	  			</div>
	  			
	  		<div class="dadosProduto">	
	  			<strong id="txtProduto">Auto Motor Sport</strong>
	  			<br />
	  			<br />
	            <strong>Edição</strong>: <span id="txtEdicao">6556</span>
	            <br />
	            <br />
	            <strong>Qtde Exemplares</strong>: <span id="txtQtdExemplares">900</span>
	            <br />
	            <br />
	            <p>
	            	<strong>Nova Data:</strong>
	            	<input type="text" name="novaData" id="novaData" style="width:80px; margin-left:5px;" maxlength="10"/>
	            </p>
	            <br />
	            
	            
			</div>
			<input type="hidden" id="codigoProdutoHidden">
			<input type="hidden" id="produtoEdicaoHidden"/>
			<input type="hidden" id="lancamentoHidden"/>
		</fieldset>

	</form>
</body>
