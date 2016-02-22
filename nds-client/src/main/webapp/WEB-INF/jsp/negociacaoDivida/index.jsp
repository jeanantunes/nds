<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/negociacaoDivida.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
</head>

<body>

	<div class="areaBts">
		<div class="area">
			
    		<span class="bt_novos" style="display: none;">
    			<a href="javascript:;" isEdicao="true" onclick="negociacaoDividaController.popup_formaPgto();" rel="tipsy"  title="Negociar" >
    				<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif" />
				</a>
			</span>
			
			<span class="bt_arq" style="display: none;">
				<a href="${pageContext.request.contextPath}/financeiro/negociacaoDivida/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
			</span>
			
			<span class="bt_arq" style="display: none;">
				<a href="${pageContext.request.contextPath}/financeiro/negociacaoDivida/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0"/>
				</a>
			</span>
	
		</div>
	</div>

	<div class="linha_separa_fields">&nbsp;</div>

	<form id="negociacaoDividaForm">
		<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
			<legend> Negociar D&iacute;vidas</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1"
				class="filtro">
				<tr>
					<td width="28">Cota:</td>
					<td width="60">
						<input type="text" name="filtro.numeroCota" id="negociacaoDivida_numCota" onchange="negociacaoDividaController.pesquisarCota(this.value);" style="width: 60px; float: left; margin-right: 5px;" />
					</td>
					<td width="39">Nome:</td>
					<td width="230">
						<span id="negociacaoDivida_nomeCota"></span>
					</td>
					<td width="41">Status:</td>
					<td width="100">
						<span id="negociacaoDivida_statusCota"></span>
					</td>
					
					<!-- 
					<td width="33" align="right">
						<input type="checkbox" name="filtro.lancamento" id="checkLancamentos" />
					</td>
					<td width="201">Lan&ccedil;amentos Futuros</td>					
					-->
					
					<td width="60">
						<span class="bt_pesquisar">
							<a href="javascript:;" onclick="negociacaoDividaController.pesquisar();"></a>
						</span>
					</td>
				</tr>
			</table>
		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="grids classFieldset" style="display: none;">
			<legend>
				D&iacute;vida Negociada - Cota: <span
					id="negociacaoDivida_numEnomeCota"></span>
			</legend>
			<br />
			<table class="negociacaoGrid"></table>
			<table width="100%" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="19%">
					</td>
					<td width="35%">
					</td>
					<td width="13%"><strong>Total Selecionado R$:</strong></td>
					<td width="6%"><span id="totalSelecionado">0,00</span></td>
					<td width="6%"><strong>Total R$:</strong></td>
					<td width="6%"><span id="total"></span></td>
					<td width="10%" align="right">
						<label for="selTodos">Selecionar Todos</label>
					</td>
					<td width="5%" align="left">
						<input isEdicao="true" type="checkbox"
							id="negociacaoCheckAll" name="Todos"
							onclick="negociacaoDividaController.checkAll(this);"
							style="float: left;" />
					</td>
				</tr>
			</table>
		</fieldset>
		</form>
		

		<%-- POPUPS --%>
		<form id="dialog-detalhe-form">
			<div id="dialog-detalhe" title="Detalhes da D&iacute;vida" style="display:none;">
				<fieldset>
					<legend>Dados da D&iacute;vida</legend>
					<table class="negociacaoDetalheGrid"></table>
					<br /> <table> <tr><td> <strong>Saldo R$: </strong> </td> <td> <div id="id_saldo"></div></td></tr> </table> <br />
				</fieldset>
		</div>
		</form>
	<form id="formaPgtoForm">
		<div id="dialog-NegociacaoformaPgto" title="Negociar D&iacute;vida" style="display:none;">
			
	
			<input type="hidden" name ="filtro.valorSelecionado" id="valorSelecionado"/>
			<input type="hidden" name="filtro.numeroCota" id="numeroCota"/>
			<fieldset style="width: 690px !important; margin-bottom: 5px;">
				<legend>Dados da Cota</legend>
				<p style="float: left;">
					<span id="formaPgto_numEnomeCota"></span>
				</p>
				<p style="float: right;">
					<strong>Divida Selecionada:</strong> R$ <span id="dividaSelecionada"></span>
				</p>
			</fieldset>
			<br clear="all" />

			<fieldset style="width: 690px !important;">
				<legend>Formas de Pagamento</legend>

				<table width="640" border="0" cellspacing="1" cellpadding="1">
					<tr id="negociacaoPorComissao-tr">
						<td width="20"><input name="tipoPgtos" type="radio" value=""
							id="negociacaoPorComissao" onclick="negociacaoDividaController.comissaoCota();" /></td>
						<td width="118">Comiss&atilde;o da Cota</td>
						<td width="502"></td>
					</tr>
				</table>
				<div class="comissaoAtual">
					<table width="100%" border="0" cellspacing="0" cellpadding="2">
						<tr>
							<td width="7%">Utilizar:</td>
							<td width="13%"><input name="" type="text" id="comissaoUtilizar"
								style="width: 80px;" /></td>
							<td width="57%" colspan="3">% para pagamento da
								d&iacute;vida</td>
						</tr>
					</table>
				</div>
				
				
				<table width="685" border="0" cellpadding="1" cellspacing="1">
					<tr>
						<td width="25"><input id="pagamentoEm" name="tipoPgtos" type="radio" value=""
							onclick="negociacaoDividaController.mostraPgto();" /></td>
						<td width="126">Pagamento em:</td>
						<td width="187"><select name="filtro.qntdParcelas" id="selectParcelas"
							style="width: 100px;" class="pgtos" onchange="negociacaoDividaController.calcularParcelas();" >
							<c:forEach items="${qntdParcelas}" var="parcela">
								<option value="${parcela}" >${parcela}x</option>
							</c:forEach>
						</select></td>
						<td width="133"><strong class="pgtos">Tipo de Pagamento:</strong></td>
						<td width="198"><select name="filtro.tipoPagamento" id="selectPagamento"
							onchange="negociacaoDividaController.opcaoFormasPagto(this.value); negociacaoDividaController.calcularParcelas();"  class="pgtos">
								<c:forEach items="${tipoPagamento}" var="pagamento">
									<option value="${pagamento}" >${pagamento.descricao}</option>
								</c:forEach>
						</select></td>
					</tr>
				</table>
				
				
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="pgtos">
					<!-- <tr>
						<strong class="pgtos">Cobran&ccedil;a</strong>
					</tr> -->
					<tr>
						<td width="3%"><input type="radio" name="filtro.periodicidade" id="radioDiario"
							value="DIARIA" checked="yes"
							onclick="negociacaoDividaController.mostraDiario(); negociacaoDividaController.calcularParcelas();" /></td>
							
						<td width="5%">Di&aacute;rio</td>
						
						<td width="3%"><input type="radio" name="filtro.periodicidade" id="radioSemanal"
							value="SEMANAL"
							onclick="negociacaoDividaController.mostraSemanal(); negociacaoDividaController.calcularParcelasSemanal();" /></td>
							
						<td width="7%">Semanal</td>
						<td width="3%"><input type="radio" name="filtro.periodicidade" id="radioQuinzenal"
							value="QUINZENAL"
							onclick="negociacaoDividaController.mostraQuinzenal(); negociacaoDividaController.calcularParcelasQuinzenal();" /></td>
							
						<td width="9%">Quinzenal</td>
						<td width="3%"><input type="radio" name="filtro.periodicidade" id="radioMensal"
							value="MENSAL"
							onclick="negociacaoDividaController.mostraMensal(); negociacaoDividaController.calcularParcelasMensal();" /></td>
							
						<td width="35%">Mensal</td>
						
						<td width="4%"><input type="checkbox" 
							id="checknegociacaoAvulsa" class="pgtos" /></td>
						<td width="28%">Negocia&ccedil;&atilde;o Avulsa</td>
					</tr>
				</table>
				
				
				<table width="100%" border="0" cellspacing="1" cellpadding="1"
					class="quinzenal">
					<tr>
						<td width="68">Todo dia:</td>
						<td width="66">
							<input type="text" name="filtro.quinzenalDia1" style="width: 60px;" 
								   id="diaInputQuinzenal1" maxlength="2"
								   onchange="negociacaoDividaController.calcularParcelasQuinzenal();" />
						</td>
						<td id="textoDiaInputQuinzenal" width="21">&nbsp; e:</td>
						<td width="522">
							<input type="text" name="filtro.quinzenalDia2"
								   id="diaInputQuinzenal2" style="width: 60px;"
								   readonly="readonly" />
						</td>
					</tr>
				</table>
				
				<table width="130px" border="0" cellspacing="1" cellpadding="1"
					class="mensal">
					<tr>
						<td width="68">Todo dia:</td>
						<td width="66">
							<input type="text" name="filtro.quinzenalDia1" style="width: 60px;" maxlength="2"
								   id="mensalDia" onchange="negociacaoDividaController.calcularParcelasMensal();" />
						</td>
						
					</tr>
				</table>
				
				
				<table width="100%" border="0" cellspacing="1" cellpadding="1"
					class="semanal">
					<tr>
						<td width="20"><input type="checkbox" name="semanalDias" onchange="negociacaoDividaController.opcaoFormasPagto($('#selectPagamento').val()); negociacaoDividaController.calcularParcelasSemanal()"
							value="SEGUNDA_FEIRA" id="checkbSegunda" /></td>
						<td width="86">Segunda-feira</td>
						<td width="20"><input type="checkbox" name="semanalDias" onchange="negociacaoDividaController.opcaoFormasPagto($('#selectPagamento').val()); negociacaoDividaController.calcularParcelasSemanal()"
							value="TERCA_FEIRA"id="checkTerca" /></td>
						<td width="70">Ter&ccedil;a-feira</td>
						<td width="20"><input type="checkbox" name="semanalDias" onchange="negociacaoDividaController.opcaoFormasPagto($('#selectPagamento').val()); negociacaoDividaController.calcularParcelasSemanal()"
							value="QUARTA_FEIRA"id="checkQuarta" /></td>
						<td width="78">Quarta-feira</td>
						<td width="20"><input type="checkbox" name="semanalDias" onchange="negociacaoDividaController.opcaoFormasPagto($('#selectPagamento').val()); negociacaoDividaController.calcularParcelasSemanal()"
							value="QUINTA_FEIRA" id="checkQuinta" /></td>
						<td width="78">Quinta-feira</td>
						<td width="20"><input type="checkbox" name="semanalDias" onchange="negociacaoDividaController.opcaoFormasPagto($('#selectPagamento').val()); negociacaoDividaController.calcularParcelasSemanal()"
							value="SEXTA_FEIRA" id="checkSexta" /></td>
						<td width="70">Sexta-feira</td>
						<td width="20"><input type="checkbox" name="semanalDias" onchange="negociacaoDividaController.opcaoFormasPagto($('#selectPagamento').val()); negociacaoDividaController.calcularParcelasSemanal()"
							value="SABADO"id="checkSabado" /></td>
						<td width="53">S&aacute;bado</td>
						<td width="20"><input type="checkbox" name="semanalDias" onchange="negociacaoDividaController.opcaoFormasPagto($('#selectPagamento').val()); negociacaoDividaController.calcularParcelasSemanal()"
							value="DOMINGO" id="checkDomingo" /></td>
						<td width="72">Domingo</td>
					</tr>
				</table>
				
				
				<br /> <br clear="all" />


				<table width="633" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="278" valign="top">
						
						
							<div id="gridCheque" style="display: none;">
								<strong>Dados do Cheque</strong> <br />
								<table id="tabelaCheque" width="347" border="0" cellspacing="1" cellpadding="1">
									<tr class="header_table">
										<td width="95" align="center">Vencimento</td>
										<td width="102" align="center">Valor R$</td>
										<td width="112" align="center">Num. Cheque</td>
										<td width="48" align="center">A&ccedil;&atilde;o</td>
									</tr>				
									
								</table>
							</div>

							<div id="gridVenctos" style="display: none;">
								<strong>Dados das Parcelas</strong> <br />
								<table id="tabelaParcelas" width="396" border="0" cellspacing="1" cellpadding="1">
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
										<td width="106" align="center" id="header_table_Ativar">Ativar ao pagar</td>
									</tr>
									
								</table>

							</div>


						</td>
						
						<td width="10" valign="top" style="width: 10px;">&nbsp;</td>
						<td width="335" valign="top">

							<div id="divBanco" style="display: none;">
								<table width="270" border="0" cellpadding="2" cellspacing="2">
									<tr>
										<td colspan="2"><b>Dados do Banco</b></td>
									</tr>
									<tr>
										<td width="53">Nome:</td>
										<td width="203"><select name="filtro.idBanco" id="selectBancosBoleto"
											style="width: 160px;"  onchange="negociacaoDividaController.calcularParcelas();">
											<c:forEach items="${bancos}" var="banco">
													<option value="${banco.id}" >${banco.nome}</option>
											</c:forEach>
										</select></td>
									</tr>
									<tr>
										<td align="right"><input type="checkbox" name="checkbox"
											id="checkReceberEmail" /></td>
										<td>Receber por E-mail?</td>
									</tr>
								</table>
							</div>

						</td>
					</tr>
				</table>

				<br clear="all" /> 
				
				<table id="encargos">
					<tr>
						<td>
							<input name="" type="checkbox" id="isentaEncargos" onclick="negociacaoDividaController.calcularParcelas();" style="float: left;" />
						</td>
						<td>
							<span style="float: left; margin-top: 8px;">Isenta Encargos</span>
						</td>
					</tr>
				</table>
			</fieldset>
			<span class="bt_novos" title="Imprimir" name="botoes" id="botaoImprimirNegociacao">
				<%-- a href="${pageContext.request.contextPath}/financeiro/negociacaoDivida/imprimirNegociacao?valorDividaSelecionada=asas" --%>

				<a href="javascript:;" onclick="negociacaoDividaController.imprimirNegociacao()">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						Imprimir Negocia&ccedil;&atilde;o
				</a>
			</span>
			<span class="bt_novos" title="Imprimir Boletos" name="botoes" id="botaoImprimirBoleto">
				<a href="${pageContext.request.contextPath}/financeiro/negociacaoDivida/imprimirBoletos">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						Imprimir Boletos
				</a>
			</span>
			<span class="bt_novos" title="Imprimir Recibo" name="botoes" id="botaoImprimirRecibo">
				<a href="${pageContext.request.contextPath}/financeiro/negociacaoDivida/imprimirRecibo">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						Imprimir Recibo
				</a>
			</span>
			
			</form> 
		</div>

		<div id="dialog-excluir" title="Baixa Bancária" style="display:none;">
			<p>Deseja confirmar Baixa Manual deste Boleto?</p>
		</div>

	
	<script type="text/javascript">
		$(function() {
			negociacaoDividaController.init();
		});
	</script>
</body>