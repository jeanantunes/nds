
<div id="form-dialog-novo">
<div id="dialog-novo" title="Incluir Novo Tipo de Movimento" style="display: none;">
	<fieldset style="width:815px!important;">
		<legend>Tipo de Movimento</legend>
		<jsp:include page="../messagesDialog.jsp" />
		
		<input type="hidden" id="grupoMovimentoHidden" />
		<!-- começa aqui  -->
		<div style="width:325px; line-height:26px;">Tipo de Lançamento:
			<select onchange="novoDialogDebitoCreditoCotaController.configuraTelaLancamento(this.value);" name="debitoCredito.tipoMovimentoFinanceiro.id" id="novoTipoMovimento" style="width:200px;float:right;">
				<option selected="selected"></option>
				<c:forEach items="${tiposMovimentoFinanceiro}" var="tipoMovimento">
					<option value="${tipoMovimento.id}">${tipoMovimento.descricao}</option>
				</c:forEach>
			</select>
		</div>

		
		<div name="tabelaFaturamento" id="tabelaFaturamento" >
		    <div style="width:181px; float:left; line-height:26px;">Percentual(%): <input type="text" maxlength="3" style="width:50px; text-align:right; float:right;" name="novoPercentual" id="novoPercentual2" /></div>
		    <div style="width:256px; float:left; line-height:26px; margin-left:10px;">
		    Base de Cálculo: <select name="debitoCredito.baseCalculo.id2" id="debitoCredito.baseCalculo.id" style="width:150px; float:right;">
		    <option selected="selected"></option>
		    <c:forEach items="${basesCalculo}" var="base">
		    	<option value="${base}">${base.value}</option>
		    </c:forEach>
		    </select></div>
		    <div style="width:222px; float:left; line-height:26px; margin-left:8px;">Período para Cálculo: 
		    <input type="text" name="novoDataPeriodoDe" id="novoDataPeriodoDe" style="width:70px;" /></div>
		    <div style="width:123px; float:left; line-height:26px;">Até:
		    <input type="text" name="novoDataPeriodoAte" id="novoDataPeriodoAte" style="width:70px;" />
		    </div>
		    <br clear="all"/>
		</div>
		
		
		<div style="width:235px; line-height:26px; float:left;">Box: <select name="debitoCredito.box.id" id="novoBox" onchange="novoDialogDebitoCreditoCotaController.carregarRoteiros(this.value,0);novoDialogDebitoCreditoCotaController.carregarRotas(0,0);" style="width:110px; float:right;">
		            <option value="0" selected="selected"></option>
		            <c:forEach items="${boxes}" var="box">
		              <option value="${box.id}">${box.nome}</option>
		            </c:forEach>
		        </select></div>
		<div style="width:57px; line-height:26px; float:left; margin-left:5px;">Roteiro: </div><div id="roteirosBox" style="float:left;"></div>
		<div style="width:40px; line-height:26px; float:left; margin-left:5px;">Rota: </div><div id="rotasRoteiro" style="float:left;"></div>
		
		<br clear="all"/>
		<div style="width:235px; line-height:26px; float:left;">Data Vencimento: <input type="text" name="debitoCredito.dataVencimento" id="novoDataVencimento" style="width:70px; margin-left:26px;" /></div>
		
		<div id="tituloNovoValor" style="width:150px; line-height:26px; float:left; margin-left:5px;">Valor(R$): <input maxlength="16" type="text" style="width:70px;" name="debitoCredito.valor" id="novoValor" /></div>
		<div style="width:321px; line-height:26px; float:left;">Observação: <input maxlength="150" type="text" style="width:240px;" name="debitoCredito.observacao" id="novoObservacao" /></div>
		<div style="width:40px; line-height:26px; float:left;"><a href="javascript:;" onclick="novoDialogDebitoCreditoCotaController.obterInformacoesParaLancamento();" style="width:20px;"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" hspace="5" border="0" /></a></div>
		<!-- termina aqui  -->
		<br clear="all" />
		
		
		<br />
		
		<form name="formularioListaLancamentos" id="formularioListaLancamentos">
	
			<table class="debitosCreditosGrid_1" id="debitosCreditosGrid_1"></table>
			
			<table  width="100%" border="0" cellspacing="2" cellpadding="2">
			    <tr>
				    <td width="65%"></td>    
				    
					<td width="35%">    
				        <span class="checar" style="float:right; margin-right:43px;">
				            <label for="textoSelTodos" id="textoSelTodos" style="float:left;">Marcar Todos</label>
				            <input title="Selecionar todos os lançamentos" type="checkbox" id="selTodos" name="selTodos" onclick="novoDialogDebitoCreditoCotaController.selecionarTodos(this.checked);" style="float:left; margin-top:8px;"/>
				        </span>
				    </td>
	            </tr> 
		    </table>
		
		</form>
	</fieldset>
	<br clear="all" />
</div>
</div>