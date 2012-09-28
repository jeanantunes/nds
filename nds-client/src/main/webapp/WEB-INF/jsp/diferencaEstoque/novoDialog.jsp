<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lancamentoNovo.js"></script>

<input type="hidden" id="idProdutoEdicao"/>

<div id="dialogNovasDiferencas" 
	 title="Lançamento Faltas e Sobras - Produto"
	 style="display: none;">

	<jsp:include page="../messagesDialog.jsp" />
	
	<fieldset style="width:585px;">
   		<legend>Tipo de Diferença</legend>
		
		<table width="575" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td width="122">Tipo de Diferença:</td>
    			<td width="239">
					<select id="tipoDiferenca" style="width:200px;" onchange="lancamentoNovoController.tratarVisualizacaoOpcaoEstoque(this.value);lancamentoNovoController.recalcularReparteAtualRateio()">
						<c:forEach var="tipoDiferenca" items="${listaTiposDiferenca}">
							<option value="${tipoDiferenca.key}">${tipoDiferenca.value}</option>
						</c:forEach>
					</select>
    			</td>
    			<td width="20">
    				<input type="checkbox" name="checkbox" id="checkboxLancCota" onclick="lancamentoNovoController.lanctoPorCotaProduto();" />
    			</td>
    			<td width="181">Lançamento por Cota</td>
  			</tr>
		</table>
	</fieldset>
	
	<div class="linha_separa_fields" style="width:580px!important;">&nbsp;</div>
	
	<div class="prodSemCota" style="display:block;">
    	
    	<fieldset style="width:585px!important;">
    		<legend>Produto</legend>
    		<table class="lanctoFaltasSobras_2Grid" border="0" cellspacing="1" cellpadding="1" style="width:585px;">
	    		<tr>
					<td width="64" bgcolor="#F5F5F5">
						<strong>Código</strong>
					</td>
					<td width="180" bgcolor="#F5F5F5">
						<strong>Produto</strong>
					</td>
					<td width="96" align="center" bgcolor="#F5F5F5">
						<strong>Edição</strong>
					</td>
					<td bgcolor="#F5F5F5" align="center">
						<strong>Preço Capa R$</strong>
					</td>
					<td bgcolor="#F5F5F5" align="center">
						<strong>Reparte</strong>
					</td>
					<td bgcolor="#F5F5F5" align="center">
						<strong>Diferença</strong>
					</td>
				</tr>
				<tr>
					<td>
						<input type="text" id="codigoProdutoInput" style="width:60px;" maxlength="30" onchange="lancamentoNovoController.limparProduto()"
							onblur="pesquisaProdutoLancamentoFaltasSobras.pesquisarPorCodigoProduto(codigoProdutoInput, nomeProdutoInput, edicaoProdutoInput, true);"/>
					</td>
					<td>
						<input type="text" id="nomeProdutoInput" style="width:180px;" maxlength="60" onchange="lancamentoNovoController.limparProduto()"
							onkeyup="pesquisaProdutoLancamentoFaltasSobras.autoCompletarPorNomeProduto(nomeProdutoInput, true);"
							onblur="pesquisaProdutoLancamentoFaltasSobras.pesquisarPorNomeProduto(codigoProdutoInput, nomeProdutoInput, edicaoProdutoInput, true);"/>
					</td>
					<td align="center">
						<input type="text" id="edicaoProdutoInput" onblur="lancamentoNovoController.buscarPrecoProdutoEdicao();" style="width:50px;" maxlength="255" />
					</td>
					<td align="right" id="precoCapaProduto"></td>
					<td align="center" id="reparteProduto"></td>
					<td>
						<input id="diferencaProdutoInput" style="width: 60px; text-align: center;" maxlength="255" />
					</td>
				</tr>
    		</table>
    	</fieldset> 
    	
    	<div class="linha_separa_fields" style="width:580px!important;">&nbsp;</div>
    	
     	<fieldset style="width:585px!important;">
    		<legend>Direcionar para:</legend>
        	
        	<table width="220" border="0" cellspacing="1" cellpadding="1">
				<tr>
					<td width="20" class="view-estouque"><input name="direcionar" type="radio" id="paraEstoque" onchange="lancamentoNovoController.paraEstoque(true);"/></td>
					<td width="72" class="view-estouque" >Estoque</td>
					<td width="20"><input name="direcionar" type="radio" id="paraCota" onchange="lancamentoNovoController.paraEstoque(false);" /></td>
					<td width="95">Cota</td>
				</tr>
			</table>
     	</fieldset>
     	
		<div class="linha_separa_fields" style="width:580px!important;">&nbsp;</div>
		
		<div id="fieldCota" style="display:none;">
			
			<fieldset style="width:585px!important;">
				<legend>Cotas</legend>
				<div style="overflow: auto; height: 150px;">
	    			<table border="0" cellspacing="1" cellpadding="1" style="width:565px;" id="grid_1">
						<tr>
							<td width="64" bgcolor="#F5F5F5">
								<strong>Cota</strong>
							</td>
							<td width="180" bgcolor="#F5F5F5">
								<strong>Nome</strong>
							</td>
							<td width="96" align="center" bgcolor="#F5F5F5">
								<strong>Reparte</strong>
							</td>
							<td bgcolor="#F5F5F5" align="center">
								<strong>Diferença</strong>
							</td>
							<td bgcolor="#F5F5F5" align="center">
								<strong>Reparte Atual</strong>
							</td>
						</tr>
						<tr id="trCota1">
							<td>
								<input type="text" name="cotaInput" id="cotaInput1" style="width:60px;" maxlength="255"
									onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNumeroCota(cotaInput1, nomeInput1, true, lancamentoNovoController.buscarReparteAtualCota(1),lancamentoNovoController.erroPesquisaCota(1));"/>
								
								<input type="hidden" name="rateioIDInputHidden"  id="rateioIDInputHidden1"/>
								
							</td>
							<td>
								<input type="text" name="nomeInput" id="nomeInput1" style="width:180px;" maxlength="255"
									onkeyup="pesquisaCotaLancamentoFaltasSobras.autoCompletarPorNome(nomeInput1);" 
									onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNomeCota(cotaInput1, nomeInput1, lancamentoNovoController.buscarReparteAtualCota(1),lancamentoNovoController.erroPesquisaCota(1));"/>
							</td>
							<td align="center" id="reparteText1"></td>
							<td align="center">
								<input type="text" name="diferencaInput" id="diferencaInput1" style="width:80px; text-align:center;" maxlength="255"
									onblur="lancamentoNovoController.adicionarLinhaCota(1);"
									onchange="lancamentoNovoController.calcularReparteAtual(1);"/>
							</td>
							<td id="reparteAtualText1" align="center"></td>
						</tr>
					</table>
				</div>
			</fieldset>
		</div>
	</div>

	<div class="prodComCota" style="display:none;">
    	
    	<fieldset style="width:585px!important;">
   	  		<legend>Cota</legend>
   	  		
    		<table width="579" border="0" cellspacing="1" cellpadding="1">
				<tr>
					<td width="73">Nota de Envio:</td>
					<td width="124">
						<div id="divDataNotaEnvio">
							<input name="dateNotaEnvio" type="text" style="width:80px;" class="viewNotaEnvio" id="dateNotaEnvio" />
						</div>
						<input type="hidden" name="rateioIDInputHiddenNota"  id="rateioIDInputHiddenNota"/>
					</td>
					<td width="27">Cota:</td>
					<td width="118">
						<input class="viewNotaEnvio" type="text" style="width:80px; float:left; margin-right:5px;" id="cotaInputNota" maxlength="255"
							onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNumeroCota(cotaInputNota, nomeCotaNota, true);" />
					</td>
					<td width="32">Nome:</td>
					<td width="167">
						<input class="viewNotaEnvio" type="text" style="width:160px;" id="nomeCotaNota" maxlength="255"
							onkeyup="pesquisaCotaLancamentoFaltasSobras.autoCompletarPorNome(nomeCotaNota);" 
							onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNomeCota(cotaInputNota, nomeCotaNota);" />
					</td>
					<td width="16">
						<img id="incluirNovosProduto" src="${pageContext.request.contextPath}/images/ico_add.gif" width="16" height="16" alt="Incluir"
							onclick="lancamentoNovoController.pesquisarProdutosNota();"/>
					</td>
          		</tr>
        	</table>
		</fieldset>

	    <div class="linha_separa_fields" style="width:595px!important;">&nbsp;</div>
	    
	    <fieldset style="width:585px!important;">
			<legend>Produtos</legend>
			
			<div style="display: none;" id="divPesquisaProdutosNota">
	    		<table class="lanctoFaltasSobrasCota_3Grid"></table>
	    	</div>
		</fieldset>
		
		<div class="linha_separa_fields" style="width:580px!important;">&nbsp;</div>
	</div>

	<br />
	
	<span id="viewIncluirNovaDiferenca" class="bt_add ">
		<a href="javascript:;" onclick="lancamentoNovoController.incluirNovo();">Incluir Novo</a>
	</span>
	
	<script language="javascript" type="text/javascript">
		$(function(){
			lancamentoNovoController.init();
		});
	</script>
</div>

<form id="idTipoEstoque">
		<div id="dialog-tipo-estoque" title="Tipo de Estoque" style="display:none">
			<fieldset>
				<legend>Selecione o Tipo de Estoque</legend>
			    
			    <table><tr>
			    	<td>
			    		  <select id="selectTipoEstoque" name="" size="1" onchange="lancamentoNovoController.atualizarQuantidade()" ></select>
			    	</td>
			    	<td width="20px">
			    		
			    	</td>
			    	<td>
			    		Quantidade:
			    	</td>
			  	  	<td>
				  	  	<input id="qtdeTipoDialog" type="text" style="width:30px;" disabled="disabled"/>
			    	</td>
			    </tr></table>
			  				
			</fieldset>
		</div>
	</form>