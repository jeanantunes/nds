<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/lancamentoNovo.js"></script>

<style type="text/css">
.btn_diferenca {
	width: 110px;
	line-height:25px;
	float: left;cursor:pointer;
	vertical-align: middle;
	background:url(./images/bt_add_novo.png) no-repeat left center;
	border: 1px outset buttonface;
}

.btn_diferenca:hover b  {
    text-decoration: underline;
    color:#00649F;
}

.btn_diferenca:focus b  {
    text-decoration: underline;
    color:#00649F;
}

	</style>

<input type="hidden" id="idProdutoEdicao"/>

 <input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">

<div id="dialogNovasDiferencas" 
	 title="Lançamento Faltas e Sobras - Produto"
	 style="display: none;">

	<jsp:include page="../messagesDialog.jsp" />
	
	<fieldset style="width:650px;">
   		<legend>Tipo de Diferença</legend>
		
		<table width="575" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td width="122">Tipo de Diferença:</td>
    			<td width="239">
					<select id="tipoDiferenca" style="width:200px;" onchange="lancamentoNovoController.tratarVisualizacaoOpcaoEstoque({tipoDiferenca:this.value, clearInputs:true});lancamentoNovoController.recalcularReparteAtual()">
						<c:forEach var="tipoDiferenca" items="${listaTiposDiferenca}">
							<option value="${tipoDiferenca.key}">${tipoDiferenca.value}</option>
						</c:forEach>
					</select>
    			</td>
    			<td width="20" class="lctoPorCota">
    				<input type="checkbox" name="checkbox" id="checkboxLancCota" onclick="lancamentoNovoController.lanctoPorCotaProduto();" />
    			</td>
    			<td width="181" class="lctoPorCota">Lançamento por Cota</td>
    			<td width="201" class="alteracaoReparte"></td>
  			</tr>
		</table>
	</fieldset>
	
	<div class="linha_separa_fields" style="width:650px!important;">&nbsp;</div>
	
	<div class="alteracaoReparte" style="display:none;">
		
		<fieldset style="width:650px!important;">
			<div style="overflow: auto; height: 50px;">
    			<table border="0" cellspacing="1" cellpadding="1" style="width:650px;" id="gridAlteracaoReparteCota">
					<tr>
						<td width="64" bgcolor="#F5F5F5">
							<strong>Cota</strong>
						</td>
						<td width="300" bgcolor="#F5F5F5">
							<strong>Nome</strong>
						</td>
					</tr>
					<tr id="trCota1">
						<td>
							<input type="text" name="cotaInput" id="cotaInputAlteracaoReparte" style="width:60px;" maxlength="10"
								onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNumeroCota(cotaInputAlteracaoReparte, nomeInputAlteracaoReparte, true, null,lancamentoNovoController.erroPesquisaCota,1);"/>
							
							<input type="hidden" name="rateioIDInputHidden"  id="rateioIDInputHiddenAlteracaoReparte"/>
							
						</td>
						<td>
							<input type="text" name="nomeInput" id="nomeInputAlteracaoReparte" style="width:300px;" maxlength="255"
								onkeyup="pesquisaCotaLancamentoFaltasSobras.autoCompletarPorNome(nomeInputAlteracaoReparte);" 
								onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNomeCota(cotaInputAlteracaoReparte, nomeInputAlteracaoReparte,true,null,lancamentoNovoController.erroPesquisaCota,1);"/>
						</td>
					</tr>
				</table>
			</div>
		</fieldset>
		
		<div class="linha_separa_fields" style="width:650px!important;">&nbsp;</div>
		
		<fieldset style="width:650px!important;">
    		<legend>Produto</legend>
    		<table class="lanctoFaltasSobras_2Grid" border="0" cellspacing="1" cellpadding="1" style="width:650px;">
	    		<tr>
					<td width="55" bgcolor="#F5F5F5">
						<strong>Código</strong>
					</td>
					<td width="130" bgcolor="#F5F5F5">
						<strong>Produto</strong>
					</td>
					<td width="96" align="center" bgcolor="#F5F5F5">
						<strong>Edição</strong>
					</td>
					<td bgcolor="#F5F5F5" align="center">
						<strong>Reparte</strong>
					</td>
					<td bgcolor="#F5F5F5" align="center">
						<strong>Reparte Devolvido</strong>
					</td>
					<td bgcolor="#F5F5F5" align="center">
						<strong>Saldo Consignado</strong>
					</td>
				</tr>
				<tr>
					<td>
						<input type="text" id="codigoProdutoInputAlteracaoReparte" style="width:75px;" maxlength="30"
							onchange="pesquisaProdutoLancamentoFaltasSobras.pesquisarPorCodigoProduto(codigoProdutoInputAlteracaoReparte, nomeProdutoInputAlteracaoReparte, edicaoProdutoInputAlteracaoReparte, true);"/>
					</td>
					<td>
						<input type="text" id="nomeProdutoInputAlteracaoReparte" style="width:130px;" maxlength="60"
							onkeyup="pesquisaProdutoLancamentoFaltasSobras.autoCompletarPorNomeProduto(nomeProdutoInputAlteracaoReparte, true);"
							onchange="pesquisaProdutoLancamentoFaltasSobras.pesquisarPorNomeProduto(codigoProdutoInputAlteracaoReparte, nomeProdutoInputAlteracaoReparte, edicaoProdutoInputAlteracaoReparte, true);"/>
					</td>
					<td align="center">
						<input type="text" id="edicaoProdutoInputAlteracaoReparte" onchange="lancamentoNovoController.buscarReparteCotaProduto();" style="width:80px;" maxlength="255" />
					</td>
					<td align="right" id="alteracaoReparteProduto"></td>
					<td align="center">
						<input id="diferencaProdutoInputAlteracaoReparte" onchange="lancamentoNovoController.atualizarSaldoConsignado();" style="width: 60px; text-align: center;" maxlength="10" />
					</td>
					<td align="center" id="saldoConsignado"></td>
				</tr>
    		</table>
    	</fieldset> 
	</div>
	
	<div class="linha_separa_fields" style="width:650px!important;">&nbsp;</div>
	
	<fieldset style="width:650px!important;">
   		<legend>Direcionar para:</legend>
       	
       	<table width="220" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td width="20" class="view-estouque">
					<input name="direcionar" type="radio" id="paraEstoque" onchange="lancamentoNovoController.paraEstoque(true);"/>
				</td>
				<td width="72" class="view-estouque">
					<span id="nomeEstoqueSpan">Estoque</span>
				</td>
				<td width="20" class="view-cota">
					<input name="direcionar" type="radio" id="paraCota" onchange="lancamentoNovoController.paraEstoque(false);" />
				</td>
				<td width="95" class="view-cota">Cota</td>
				<td width="72" class="alteracaoReparte">Estoque:</td>
				<td class="alteracaoReparte">
		    		 <select id="selectTipoEstoqueAlteracaoReparte" size="1" onchange="lancamentoNovoController.atualizarTipoEstoqueSelecionado();"></select>
		    	</td>
			</tr>
		</table>
    </fieldset>
	
	<div class="linha_separa_fields" style="width:650px!important;">&nbsp;</div>
	
	<div class="prodSemCota" style="display:block;">
    	
    	<fieldset style="width:650px!important;">
    		<legend>Produto</legend>
    		<table class="lanctoFaltasSobras_2Grid" border="0" cellspacing="1" cellpadding="1" style="width:650px;">
	    		<tr>
					<td width="55" bgcolor="#F5F5F5">
						<strong>Código</strong>
					</td>
					<td width="130" bgcolor="#F5F5F5">
						<strong>Produto</strong>
					</td>
					<td width="96" align="center" bgcolor="#F5F5F5">
						<strong>Edição</strong>
					</td>
					<td bgcolor="#F5F5F5" align="center">
						<strong>Preço Venda R$</strong>
					</td>
					<td bgcolor="#F5F5F5" align="center">
						<strong>Pacote Padrão</strong>
					</td>
					<td bgcolor="#F5F5F5" align="center">
						<strong>Saldo</strong>
					</td>
					<td bgcolor="#F5F5F5" align="center">
						<strong>Diferença</strong>
					</td>
				</tr>
				<tr>
					<td>
						<input type="text" id="codigoProdutoInput" style="width:75px;" maxlength="30" onchange="lancamentoNovoController.limparProduto()"
							onblur="pesquisaProdutoLancamentoFaltasSobras.pesquisarPorCodigoProduto(codigoProdutoInput, nomeProdutoInput, edicaoProdutoInput, true);"/>
					</td>
					<td>
						<input type="text" id="nomeProdutoInput" style="width:130px;" maxlength="60" onchange="lancamentoNovoController.limparProduto()"
							onkeyup="pesquisaProdutoLancamentoFaltasSobras.autoCompletarPorNomeProduto(nomeProdutoInput, true);"
							onblur="pesquisaProdutoLancamentoFaltasSobras.pesquisarPorNomeProduto(codigoProdutoInput, nomeProdutoInput, edicaoProdutoInput, true);"/>
					</td>
					<td align="center">
						<input type="text" id="edicaoProdutoInput" onchange="lancamentoNovoController.buscarPrecoProdutoEdicao();" style="width:80px;" maxlength="255" />
					</td>
					<td align="right" id="precoCapaProduto"></td>
					<td align="center" id="conferencia-pacotePadrao"></td>
					<td align="center" id="reparteProduto"></td>
					<td align="center">
						<input id="diferencaProdutoInput" style="width: 60px; text-align: center;" maxlength="10" />
					</td>
				</tr>
    		</table>
    	</fieldset> 
    	
		<div class="linha_separa_fields" style="width:650px!important;">&nbsp;</div>
		
		<div id="fieldCota" style="display:none;">
			
			<fieldset style="width:650px!important;">
				<legend>Cotas</legend>
				<div style="overflow: auto; height: 150px;">
	    			<table border="0" cellspacing="1" cellpadding="1" style="width:650px;" id="grid_1">
						<tr>
							<td width="64" bgcolor="#F5F5F5">
								<strong>Cota</strong>
							</td>
							<td width="300" bgcolor="#F5F5F5">
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
								<input type="text" name="cotaInput" id="cotaInput1" style="width:60px;" maxlength="10"
								   onchange="lancamentoNovoController.limparCota();"
									onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNumeroCota(cotaInput1, nomeInput1, true, function(data) { lancamentoNovoController.buscarReparteAtualCota(data,1);},lancamentoNovoController.erroPesquisaCota,1);"/>
								
								<input type="hidden" name="rateioIDInputHidden"  id="rateioIDInputHidden1"/>
								
							</td>
							<td>
								<input type="text" name="nomeInput" id="nomeInput1" style="width:300px;" maxlength="255"
									onkeyup="pesquisaCotaLancamentoFaltasSobras.autoCompletarPorNome(nomeInput1);" 
									onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNomeCota(cotaInput1, nomeInput1, true, function(data) { lancamentoNovoController.buscarReparteAtualCota(data,1)},lancamentoNovoController.erroPesquisaCota,1);"/>
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
			
			<div class="linha_separa_fields" style="width:650px!important;">&nbsp;</div>
			
		</div>

	</div>

	<div class="prodComCota" style="display:none;">
    	
    	<fieldset style="width:650px!important;">
   	  		<legend>Cota</legend>
   	  		
    		<table width="650" border="0" cellspacing="1" cellpadding="1">
				<tr>
					<td width="73">Data Lancto:</td>
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
						<img id="incluirNovosProduto" src="${pageContext.request.contextPath}/images/ico_pesquisar.png" width="16" height="16" alt="Incluir"
							onclick="lancamentoNovoController.pesquisarProdutosNota();"/>
					</td>
          		</tr>
        	</table>
		</fieldset>

	    <div class="linha_separa_fields" style="width:595px!important;">&nbsp;</div>
	    
	    <fieldset style="width:650px!important;">
			<legend>Produtos</legend>
			
			<div style="display: none;" id="divPesquisaProdutosNota">
	    		<table class="lanctoFaltasSobrasCota_3Grid"></table>
	    	</div>
		</fieldset>
		
		<div class="linha_separa_fields" style="width:650px!important;">&nbsp;</div>
		
	</div>
	
	<span id="viewIncluirNovaDiferenca">
		<button id="linkIncluirNovaDiferenca" class="btn_diferenca">
			<b style="margin-left: 20px;">Incluir Novo</b>
		</button>
	</span>

	<script language="javascript" type="text/javascript">
		$(function(){
			lancamentoNovoController.init();
		});
	</script>
</div>

<form id="form-selecionar-tipo-estoque">
	<div id="dialog-selecionar-tipo-estoque" title="Tipo de Estoque" style="display:none">
		<p>Nenhum estoque foi selecionado. Será utlilizado o estoque padrão [Lancamento].</p>
		<br></br>
		<p>Deseja continuar?</p>
	</div>
</form>

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