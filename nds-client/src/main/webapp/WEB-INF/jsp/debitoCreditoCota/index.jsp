<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>

	<title>NDS - Treelog</title>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/debitoCreditoCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/novoDialogDebitoCreditoCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
<script>

	$(function() {
		var pesquisaCotaDebitoCreditoCota = new PesquisaCota();
		debitoCreditoCotaController.init();
		novoDialogDebitoCreditoCotaController.init(pesquisaCotaDebitoCreditoCota);
		bloquearItensEdicao(debitoCreditoCotaController.workspace);
	});
	
</script>

</head>

<body>

<div id="form-dialog-excluir">
<div id="dialog-excluir" title="Excluir Tipo de Movimento">
<p><strong>Confirma a exclusão deste Tipo de Movimento?</strong></p>
</div>
</div>

<div id="form-dialog-editar">
<div id="dialog-editar" title="Editar Tipo de Movimento">

<jsp:include page="../messagesDialog.jsp" />

<form id="formEdicaoMovimentoFinanceiro">
<input type="hidden" name="debitoCredito.id" id="edicaoId" />
<table width="450" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="126">Tipo de Movimento:</td>
    <td width="310">
    <select name="debitoCredito.tipoMovimentoFinanceiro.id" id="edicaoTipoMovimento" style="width:300px;" onchange="debitoCreditoCotaController.configuraTelaEdicao(this.value); debitoCreditoCotaController.limparValor();">
		<c:forEach items="${tiposMovimentoFinanceiro}" var="tipoMovimento">
			<option value="${tipoMovimento.id}">${tipoMovimento.id}-${tipoMovimento.descricao}</option>
		</c:forEach>
    </select>
    </td>
  </tr>

  <tr>
    <td width="126">Cota:</td>
    <td width="310">
    <input maxlength="11" type="text" style="width:80px; float:left; margin-right:5px;" 
    	   name="debitoCredito.numeroCota" id="edicaoNumeroCota" onblur="debitoCreditoCotaController.pesquisarCota(true);"/>
    </td>
  </tr>
  <tr>
    <td width="126">Nome:</td>
    <td width="310">
    <input maxlength="80" type="text" style="width:300px;" name="debitoCredito.nomeCota" id="edicaoNomeCota" /></td>
  </tr>
  
  <tr>
    <td width="126">Data Lançamento:</td>
    <td width="310">
    	<input type="text" name="debitoCredito.dataLancamento" id="edicaoDataLancamento" readonly="readonly" style="width:80px;" />
    </td>
  </tr>
  
  <tr>
    <td>Data Vencimento:</td>
    <td>
    	<input type="text" name="debitoCredito.dataVencimento" id="edicaoDataVencimento" style="width:80px;" />
    </td>
  </tr>
  
  <tr>
    <td width="126" id="tituloEdicaoPercentual" >Percentual(%)</td>
    <td width="310">
    	<input  maxlength="3" type="text" style="width:80px; text-align:right;" name="edicaoPercentual" id="edicaoPercentual" onchange="debitoCreditoCotaController.obterInformacoesParaEdicao();" />
    </td>
  </tr>
  
  <tr>
    <td width="80" id="tituloEdicaoBaseCalculo">Base de Cálculo:</td>
    <td width="120">
		<select name="edicaoBaseCalculo" id="edicaoBaseCalculo" style="width:120px;" onchange="debitoCreditoCotaController.obterInformacoesParaEdicao();" >
		
	  		<option selected="selected"></option>
			<c:forEach items="${basesCalculo}" var="base">
				<option value="${base}">${base.value}</option>
			</c:forEach>
			
	    </select>
    </td>
  </tr>
  
  <tr>
    <td width="100" id="tituloEdicaoDataPeriodo">Período para Cálculo:</td>
    <td width="180" id="edicaoDataPeriodo">
		<input type="text" name="edicaoDataPeriodoDe" id="edicaoDataPeriodoDe" style="width:80px;" onchange="debitoCreditoCotaController.obterInformacoesParaEdicao();" />
        até
		<input type="text" name="edicaoDataPeriodoAte" id="edicaoDataPeriodoAte" style="width:80px;" onchange="debitoCreditoCotaController.obterInformacoesParaEdicao();" />
    </td>
  </tr>

  <tr>
    <td width="126">Valor R$:</td>
    <td width="310">
    	<input  maxlength="16" type="text" style="width:80px; text-align:right;" name="debitoCredito.valor" id="edicaoValor" />
    </td>
  </tr>
  <tr>
    <td width="126">Observação:</td>
    <td width="310">
    	<input  maxlength="150" type="text" style="width:300px;" name="debitoCredito.observacao" id="edicaoObservacao" />
    </td>
  </tr>
</table>
</form>
</div>
</div>

<div id="form-dialog-detalhe">
<div id="dialog-detalhe" title="Tipo de Movimento" style="display: none;">

<jsp:include page="../messagesDialog.jsp" />

<form id="formDetalheMovimentoFinanceiro">
<input readonly="readonly" type="hidden" name="debitoCredito.id" id="detalheId" />
<table width="450" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="126">Tipo de Movimento:</td>
    <td width="310">
    <input readonly="readonly" name="debitoCredito.tipoMovimentoFinanceiro.id" id="detalheTipoMovimento" style="width:300px;">
    </td>
  </tr>
  <tr>
    <td width="126">Cota:</td>
    <td width="310">
    <input readonly="readonly" type="text" style="width:80px; float:left; margin-right:5px;" 
    	   name="debitoCredito.numeroCota" id="detalheNumeroCota"/>
    </td>
  </tr>
  <tr>
    <td width="126">Nome:</td>
    <td width="310">
    <input readonly="readonly" type="text" style="width:300px;" name="debitoCredito.nomeCota" id="detalheNomeCota" /></td>
  </tr>
  <tr>
    <td width="126">Data Lançamento:</td>
    <td width="310">
    	<input readonly="readonly" type="text" name="debitoCredito.dataLancamento" id="detalheDataLancamento" style="width:80px;" />
    </td>
  </tr>
  <tr>
    <td>Data Vencimento:</td>
    <td>
    	<input readonly="readonly" type="text" name="debitoCredito.dataVencimento" id="detalheDataVencimento" style="width:80px;" />
    </td>
  </tr>
  <tr>
    <td width="126">Valor R$:</td>
    <td width="310">
    	<input readonly="readonly" type="text" style="width:80px; text-align:right;" name="debitoCredito.valor" id="detalheValor" />
    </td>
  </tr>
  <tr>
    <td width="126">Observação:</td>
    <td width="310">
    	<input readonly="readonly" type="text" style="width:300px;" name="debitoCredito.observacao" id="detalheObservacao" />
    </td>
  </tr>
</table>
</form>
</div>
</div>


	<form id="formPesquisaDebitosCreditos">
	
	<div class="areaBts">
		<div class="area">
			<span class="bt_novos">
				<a isEdicao="true" href="javascript:;" onclick="novoDialogDebitoCreditoCotaController.popupNovoDialog();" rel="tipsy" title="Incluir Novo Tipo de Movimento">
					<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" 
 	   				hspace="5" border="0"/>
 	   			</a>
 	   		</span>
			
			<span class="bt_arq">
			<a href="${pageContext.request.contextPath}/financeiro/debitoCreditoCota/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
				<img src="${pageContext.request.contextPath}/images/ico_excel.png"  hspace="5" border="0" />
			</a>
		</span>
		<span class="bt_arq">
			<a href="${pageContext.request.contextPath}/financeiro/debitoCreditoCota/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
			<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" border="0" />
			</a>
		</span>
		</div>
	</div>
	
	<div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
   	    <legend>Pesquisar Débitos / Créditos Cota</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		  <tr>
		
		    <td width="33">Cota:</td>
		         <td width="85">
		         <input name="filtroDebitoCredito.numeroCota" 
		       		    id="debito-credito-numeroCota" 
		       		    type="text"
		       		    maxlength="11"
		       		    style="width:80px; 
		       		    float:left; margin-right:5px;"
		       		    onchange="pesquisaCotaDebitoCreditoCota.pesquisarPorNumeroCota('#debito-credito-numeroCota', '#nomeCota');" />
			</td>
			<td width="41">Nome:</td>
			<td colspan="3">
			     <input name="filtroDebitoCredito.nomeCota" 
			      	    id="nomeCota" 
			      		type="text" 
			      		class="nomeCota" 
			      		maxlength="255"
			      		style="width:250px;"
			      		onkeyup="pesquisaCotaDebitoCreditoCota.autoCompletarPorNome('#nomeCota');" 
			      		onblur="pesquisaCotaDebitoCreditoCota.pesquisarPorNomeCota('#numeroCota', '#nomeCota');" />
			</td>
		    
		    <td width="114">Tipo de Lançamento:</td>
		    
		    <td colspan="4">
			    <select name="filtroDebitoCredito.idTipoMovimento" id="idTipoMovimento" style="width:250px;">
				  <option selected="selected"></option>
				  <c:forEach items="${tiposMovimentoFinanceiroMain}" var="tipoMovimento">
				  	<option value="${tipoMovimento.id}">${tipoMovimento.descricao}</option>
				  </c:forEach>
				</select>
			</td>
		
		  </tr>
		  <tr>
		    <td colspan="3">Data Lançamento:</td>
		    <td width="109"><input type="text" id="datepickerDeVenc" name="filtroDebitoCredito.dataLancamentoInicio" style="width:80px;" /></td>
		    <td width="32">Até:</td>
		    <td width="126"><input id="datepickerAteVenc" type="text" name="filtroDebitoCredito.dataLancamentoFim" style="width:80px;" /></td>
		    <td>Data Vencimento:</td>
		    <td width="105"><input type="text" id="datepickerDe" name="filtroDebitoCredito.dataVencimentoInicio" style="width:80px;" /></td>
		    <td width="31">Até:</td>
		    <td width="116"><input id="datepickerAte" type="text" name="filtroDebitoCredito.dataVencimentoFim" style="width:80px;" /></td>
		    <td width="102"><span class="bt_novos">
            	<a href="javascript:;" onclick="debitoCreditoCotaController.popularGridDebitosCreditos();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
  		</tr>
  		</table>
      </fieldset>
     </form>
          <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldGrid">
       	  <legend>Débitos / Créditos Cota Cadastrados</legend>
        <div class="grids" style="display:none;">
       	  <table class="debitosCreditosGrid"></table>
        
          <br />
          <span style="float:right; margin-right:297px" id="footerValorTotal"></span>
 		</div>
      </fieldset>
      


<jsp:include page="novoDialog.jsp" />

</body>
