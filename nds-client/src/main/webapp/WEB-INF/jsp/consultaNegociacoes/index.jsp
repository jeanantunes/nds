<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>

	<title>NDS - Treelog</title>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/consultaNegociacoes.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/negociacaoDivida.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
	
<script>

var pesquisaCota = new PesquisaCota();
	
	$(function() {
		consultaNegociacoesController.init();
		negociacaoDividaController.init();
	});
	
</script>

</head>

<body>


	<form id="formPesquisaDebitosCreditos">
	
	<div class="areaBts">
		<div class="area">
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
	
      <fieldset class="fieldFiltro">
   	    <legend>Pesquisar Negocia&ccedil;&otilde;es</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		  <tr>
		
		    <td width="40">Cota:</td>
		         <td width="85">
		         <input name="numeroCota" 
		       		    id="consultaNegociacaoNumeroCota" 
		       		    type="text"
		       		    maxlength="11"
		       		    style="width:80px; 
		       		    float:left; margin-right:5px;"/>
		       		    <!-- onchange="pesquisaCotaDebitoCreditoCota.pesquisarPorNumeroCota('#debito-credito-numeroCota', '#nomeCota');" -->
			</td>
			<td width="45" style="padding-left: 20px;">Nome:</td>
			<td width="250" >
			     <input name="nomeCota" 
			      	    id="consultaNegociacaoNomeCota" 
			      		type="text" 
			      		class="nomeCota" 
			      		maxlength="255"
			      		style="width:250px;"/>
			      		<!-- 
			      		onkeyup="pesquisaCotaDebitoCreditoCota.autoCompletarPorNome('#nomeCota');" 
			      		onblur="pesquisaCotaDebitoCreditoCota.pesquisarPorNomeCota('#numeroCota', '#nomeCota');"
			      		 -->
			</td>
		    
		    <td width="120" style="padding-left: 20px;">Situa&ccedil;&atilde;o da parcela:</td>
		    
		    <td>
			    <select name="situacaoParcela" id="consultaNegociacaoSituacaoParcela" style="width:150px;">
				  <option selected="selected"></option>
				  <option value="PAGA">Paga</option>
				  <option value="NAO_PAGA">N&atilde;o paga</option>
				  <option value="A_VENCER">A vencer</option>
				  <!-- 
				  <c:forEach items="${tiposMovimentoFinanceiroMain}" var="tipoMovimento">
				  	<option value="${tipoMovimento.id}">${tipoMovimento.descricao}</option>
				  </c:forEach>
				   -->
				</select>
			</td>
		  </tr>
		  </table>
		  
		  <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		  
		  <tr>
		    <td style="width:115px!important;">Data da negocia&ccedil;&atilde;o:</td>
		    <td width="109"><input type="text" id="consultaNegociacaoDataNegociacaoDe" name="dataNegociacaoDe" readonly="readonly" style="width:80px;" /></td>
		    <td width="32">Até:</td>
		    <td width="126"><input id="consultaNegociacaoDataNegociacaoAte" type="text" name="dataNegociacaoAte" readonly="readonly" style="width:80px;" /></td>
		    <td style="width:98px!important;padding-left: 95px;">Data Vencimento:</td>
		    <td width="105"><input type="text" id="consultaNegociacaoDataVencimentoDe" name="dataVencimentoDe" readonly="readonly" style="width:80px;" /></td>
		    <td width="31">Até:</td>
		    <td width="116"><input id="consultaNegociacaoDataVencimentoAte" type="text" name="dataVencimentoAte" readonly="readonly" style="width:80px;" /></td>
		    <td width="102"><span class="bt_novos">
            	<a href="javascript:;" onclick="consultaNegociacoesController.pesquisarNegociacoes();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
  		  </tr>
  		</table>
      </fieldset>
     </form>
          <div class="linha_separa_fields">&nbsp;</div>
      
      <fieldset class="fieldGrid">
       	  <legend>Negocia&ccedil;&otilde;es</legend>
        <div class="gridNegociacoesDiv" align="center" style="display:none;">
       	  <table id="negociacoesGrid"></table>
 		</div>
      </fieldset>

      <form id="cn_formaPgtoForm">
		<div id="cn_dialog-NegociacaoformaPgto" title="Negociar D&iacute;vida" style="display:none;">
			
	
			<input type="hidden" name ="filtro.valorSelecionado" id="cn_valorSelecionado"/>
			<input type="hidden" name="filtro.numeroCota" id="cn_numeroCota"/>
			<fieldset style="width: 690px !important; margin-bottom: 5px;">
				<legend>Dados da Cota</legend>
				<p style="float: left;">
					<span id="cn_formaPgto_numEnomeCota"></span>
				</p>
				<p style="float: right;">
					<strong>Divida Selecionada:</strong> R$ <span id="cn_dividaSelecionada"></span>
				</p>
			</fieldset>
			<br clear="all" />

			<fieldset style="width: 690px !important;">
				<legend>Formas de Pagamento</legend>

				<table width="640" border="0" cellspacing="1" cellpadding="1">
					<tr id="cn_negociacaoPorComissao-tr">
						<td width="20"><input name="tipoPgtos" type="radio" value=""
							id="cn_negociacaoPorComissao" onclick="consultaNegociacoesController.comissaoCota();" /></td>
						<td width="118">Comiss&atilde;o da Cota</td>
						<td width="502"></td>
					</tr>
				</table>
				<div class="cn_comissaoAtual">
					<table width="100%" border="0" cellspacing="0" cellpadding="2">
						<tr>
							<td width="7%">Utilizar:</td>
							<td width="13%"><input name="" type="text" id="cn_comissaoUtilizar"
								style="width: 80px;" /></td>
							<td width="57%" colspan="3">% para pagamento da
								d&iacute;vida</td>
						</tr>
					</table>
				</div>
				
				
				<table width="685" border="0" cellpadding="1" cellspacing="1">
					<tr>
						<td width="25"><input id="cn_pagamentoEm" name="tipoPgtos" type="radio" value=""
							onclick="consultaNegociacoesController.mostraPgto();" /></td>
						<td width="126">Pagamento em:</td>
						<td width="187"><select name="filtro.qntdParcelas" id="cn_selectParcelas"
							style="width: 100px;" class="cn_pgtos" onchange="consultaNegociacoesController.calcularParcelas();" >
							<c:forEach items="${qntdParcelas}" var="parcela">
								<option value="${parcela}" >${parcela}x</option>
							</c:forEach>
						</select></td>
						<td width="133"><strong class="cn_pgtos">Tipo de Pagamento:</strong></td>
						<td width="198"><select name="filtro.tipoPagamento" id="cn_selectPagamento"
							onchange="consultaNegociacoesController.opcaoFormasPagto(this.value); consultaNegociacoesController.calcularParcelas();"  class="cn_pgtos">
								<c:forEach items="${tipoPagamento}" var="pagamento">
									<option value="${pagamento}" >${pagamento.descricao}</option>
								</c:forEach>
						</select></td>
					</tr>
				</table>
				
				
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="cn_pgtos">
					<!-- <tr>
						<strong class="pgtos">Cobran&ccedil;a</strong>
					</tr> -->
					<tr>
						<td width="3%"><input type="radio" name="filtro.periodicidade" id="cn_radioDiario"
							value="DIARIA" checked="yes"
							onclick="consultaNegociacoesController.mostraDiario(); consultaNegociacoesController.calcularParcelas();" /></td>
							
						<td width="5%">Di&aacute;rio</td>
						
						<td width="3%"><input type="radio" name="filtro.periodicidade" id="cn_radioSemanal"
							value="SEMANAL"
							onclick="consultaNegociacoesController.mostraSemanal(); consultaNegociacoesController.calcularParcelasSemanal();" /></td>
							
						<td width="7%">Semanal</td>
						<td width="3%"><input type="radio" name="filtro.periodicidade" id="cn_radioQuinzenal"
							value="QUINZENAL"
							onclick="consultaNegociacoesController.mostraQuinzenal(); consultaNegociacoesController.calcularParcelasQuinzenal();" /></td>
							
						<td width="9%">Quinzenal</td>
						<td width="3%"><input type="radio" name="filtro.periodicidade" id="cn_radioMensal"
							value="MENSAL"
							onclick="consultaNegociacoesController.mostraMensal(); consultaNegociacoesController.calcularParcelasMensal();" /></td>
							
						<td width="35%">Mensal</td>
						
						<td width="4%"><input type="checkbox" 
							id="cn_checknegociacaoAvulsa" class="cn_pgtos" /></td>
						<td width="28%">Negocia&ccedil;&atilde;o Avulsa</td>
					</tr>
				</table>
				
				
				<table width="100%" border="0" cellspacing="1" cellpadding="1"
					class="cn_quinzenal">
					<tr>
						<td width="68">Todo dia:</td>
						<td width="66">
							<input type="text" name="filtro.quinzenalDia1" style="width: 60px;" 
								   id="cn_diaInputQuinzenal1" maxlength="2"
								   onchange="consultaNegociacoesController.calcularParcelasQuinzenal();" />
						</td>
						<td id="cn_textoDiaInputQuinzenal" width="21">&nbsp; e:</td>
						<td width="522">
							<input type="text" name="filtro.quinzenalDia2"
								   id="cn_diaInputQuinzenal2" style="width: 60px;"
								   readonly="readonly" />
						</td>
					</tr>
				</table>
				
				<table width="130px" border="0" cellspacing="1" cellpadding="1"
					class="cn_mensal">
					<tr>
						<td width="68">Todo dia:</td>
						<td width="66">
							<input type="text" name="filtro.quinzenalDia1" style="width: 60px;" maxlength="2"
								   id="cn_mensalDia" onchange="consultaNegociacoesController.calcularParcelasMensal();" />
						</td>
						
					</tr>
				</table>
				
				
				<table width="100%" border="0" cellspacing="1" cellpadding="1"
					class="cn_semanal">
					<tr>
						<td width="20"><input type="checkbox" name="cn_semanalDias" onchange="consultaNegociacoesController.opcaoFormasPagto($('#cn_selectPagamento').val()); consultaNegociacoesController.calcularParcelasSemanal()"
							value="SEGUNDA_FEIRA" id="cn_checkbSegunda" /></td>
						<td width="86">Segunda-feira</td>
						<td width="20"><input type="checkbox" name="cn_semanalDias" onchange="consultaNegociacoesController.opcaoFormasPagto($('#cn_selectPagamento').val()); consultaNegociacoesController.calcularParcelasSemanal()"
							value="TERCA_FEIRA"id="cn_checkTerca" /></td>
						<td width="70">Ter&ccedil;a-feira</td>
						<td width="20"><input type="checkbox" name="cn_semanalDias" onchange="consultaNegociacoesController.opcaoFormasPagto($('#cn_selectPagamento').val()); consultaNegociacoesController.calcularParcelasSemanal()"
							value="QUARTA_FEIRA"id="cn_checkQuarta" /></td>
						<td width="78">Quarta-feira</td>
						<td width="20"><input type="checkbox" name="cn_semanalDias" onchange="consultaNegociacoesController.opcaoFormasPagto($('#cn_selectPagamento').val()); consultaNegociacoesController.calcularParcelasSemanal()"
							value="QUINTA_FEIRA" id="cn_checkQuinta" /></td>
						<td width="78">Quinta-feira</td>
						<td width="20"><input type="checkbox" name="cn_semanalDias" onchange="consultaNegociacoesController.opcaoFormasPagto($('#cn_selectPagamento').val()); consultaNegociacoesController.calcularParcelasSemanal()"
							value="SEXTA_FEIRA" id="cn_checkSexta" /></td>
						<td width="70">Sexta-feira</td>
						<td width="20"><input type="checkbox" name="cn_semanalDias" onchange="consultaNegociacoesController.opcaoFormasPagto($('#cn_selectPagamento').val()); consultaNegociacoesController.calcularParcelasSemanal()"
							value="SABADO"id="cn_checkSabado" /></td>
						<td width="53">S&aacute;bado</td>
						<td width="20"><input type="checkbox" name="cn_semanalDias" onchange="consultaNegociacoesController.opcaoFormasPagto($('#cn_selectPagamento').val()); consultaNegociacoesController.calcularParcelasSemanal()"
							value="DOMINGO" id="cn_checkDomingo" /></td>
						<td width="72">Domingo</td>
					</tr>
				</table>
				
				
				<br /> <br clear="all" />


				<table width="633" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="278" valign="top">
						
						
							<div id="cn_gridCheque" style="display: none;">
								<strong>Dados do Cheque</strong> <br />
								<table id="cn_tabelaCheque" width="347" border="0" cellspacing="1" cellpadding="1">
									<tr class="header_table">
										<td width="95" align="center">Vencimento</td>
										<td width="102" align="center">Valor R$</td>
										<td width="112" align="center">Num. Cheque</td>
										<td width="48" align="center">A&ccedil;&atilde;o</td>
									</tr>				
									
								</table>
							</div>

							<div id="cn_gridVenctos" style="display: none;">
								<strong>Dados das Parcelas</strong> <br />
								<table id="cn_tabelaParcelas" width="396" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td height="21" align="center">&nbsp;</td>
										<td align="center">&nbsp;</td>
										<td colspan="3" align="center" bgcolor="#F2F2F2"><strong>Valor R$</strong></td>
										<td align="center">&nbsp;</td>
									</tr>
									<tr class="header_table">
										<td width="15" height="27" align="center">&nbsp;</td>
										<td width="72" align="center">Vencimento</td>
										<td width="60" align="center" class="header_table">Parcela</td>
										<td width="64" align="center" class="header_table">Encargos</td>
										<td width="60" align="center" class="header_table">Parc Total</td>
										<td width="106" align="center" id="cn_header_table_Ativar">Ativar ao pagar</td>
									</tr>
									
								</table>

							</div>


						</td>
						
						<td width="10" valign="top" style="width: 10px;">&nbsp;</td>
						<td width="335" valign="top">

							<div id="cn_divBanco" style="display: none;">
								<table width="270" border="0" cellpadding="2" cellspacing="2">
									<tr>
										<td colspan="2"><b>Dados do Banco</b></td>
									</tr>
									<tr>
										<td width="53">Nome:</td>
										<td width="203"><select name="filtro.idBanco" id="cn_selectBancosBoleto"
											style="width: 160px;"  onchange="consultaNegociacoesController.calcularParcelas();">
											<c:forEach items="${bancos}" var="banco">
													<option value="${banco.id}" >${banco.nome}</option>
											</c:forEach>
										</select></td>
									</tr>
									<tr>
										<td align="right"><input type="checkbox" name="checkbox"
											id="cn_checkReceberEmail" /></td>
										<td>Receber por E-mail?</td>
									</tr>
								</table>
							</div>

						</td>
					</tr>
				</table>

				<br clear="all" /> 
				
				<table id="cn_encargos">
					<tr>
						<td>
							<input name="" type="checkbox" id="cn_isentaEncargos" onclick="consultaNegociacoesController.calcularParcelas();" style="float: left;" />
						</td>
						<td>
							<span style="float: left; margin-top: 8px;">Isenta Encargos</span>
						</td>
					</tr>
				</table>
			</fieldset>
			<span class="bt_novos" title="Imprimir" name="botoes" id="cn_botaoImprimirNegociacao">

				<a href="javascript:;" onclick="consultaNegociacoesController.imprimirNegociacao()">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						Imprimir Negocia&ccedil;&atilde;o
				</a>
			</span>
			<span class="bt_novos" title="Imprimir Boletos" name="botoes" id="cn_botaoImprimirBoleto">
				<a href="${pageContext.request.contextPath}/financeiro/negociacaoDivida/imprimirBoletos">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						Imprimir Boletos
				</a>
			</span>
			<span class="bt_novos" title="Imprimir Recibo" name="botoes" id="cn_botaoImprimirRecibo">
				<a href="${pageContext.request.contextPath}/financeiro/negociacaoDivida/imprimirRecibo">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						Imprimir Recibo
				</a>
			</span>
		</div>
	</form> 
		
</body>
