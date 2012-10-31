<head>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/negociacaoDivida.js"></script>



</head>

<body>
	<form id="negociacaoDividaForm">
		<fieldset class="classFieldset">
			<legend> Negociar D&iacute;vidas</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1"
				class="filtro">
				<tr>
					<td width="28">Cota:</td>
					<td colspan="3"><input type="text" name="filtro.numeroCota"
						id="negociacaoDivida_numCota"
						onblur="negociacaoDividaController.pesquisarCota(this.value);"
						style="width: 60px; float: left; margin-right: 5px;" /></td>
					<td width="39">Nome:</td>
					<td width="207"><span id="negociacaoDivida_nomeCota"></span></td>
					<td width="41">Status:</td>
					<td width="157"><span id="negociacaoDivida_statusCota"></span></td>
					<td width="33" align="right"><input type="checkbox"
						name="filtro.lancamento" id="checkLancamentos" /></td>
					<td width="201">Lan&ccedil;amentos Futuros</td>
					<td width="104"><span class="bt_pesquisar"><a
							href="javascript:;"
							onclick="negociacaoDividaController.pesquisar();"></a></span></td>
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
					<td width="19%"><span class="bt_arquivo"><a
							href="${pageContext.request.contextPath}/financeiro/negociacaoDivida/exportar?fileType=XLS">Arquivo</a></span>
						<span class="bt_imprimir"><a
							href="${pageContext.request.contextPath}/financeiro/negociacaoDivida/exportar?fileType=PDF">Imprimir</a></span>
					</td>
					<td width="35%"><span class="bt_confirmar_novo"
						title="Formas de Pagamento"><a href="javascript:;"
							onclick="negociacaoDividaController.popup_formaPgto();"><img
								border="0" hspace="5"
								src="${pageContext.request.contextPath}/images/ico_check.gif">Negociar</a></span>
					</td>
					<td width="13%"><strong>Total Selecionado R$:</strong></td>
					<td width="6%"><span id="totalSelecionado">0,00</span></td>
					<td width="6%"><strong>Total R$:</strong></td>
					<td width="6%"><span id="total"></span></td>
					<td width="15%"><span class="bt_sellAll"><label
							for="sel">Selecionar Todos</label><input type="checkbox"
							id="negociacaoCheckAll" name="Todos"
							onclick="negociacaoDividaController.checkAll(this);"
							style="float: left;" /></span></td>
				</tr>
			</table>
		</fieldset>
		</form>
		

		<%-- POPUPS --%>

		<div id="dialog-detalhe" title="Detalhes da D&iacute;vida" style="display:none;">
			<fieldset>
				<legend>Dados da D&iacute;vida</legend>
				<table class="negociacaoDetalheGrid"></table>
				<br /> <strong>Saldo R$: </strong> <br />
			</fieldset>
		</div>
	
	<form id="formaPgtoForm">
		<div id="dialog-formaPgto" title="Negociar D&iacute;vida" style="display:none;">
			
	
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
					<tr>
						<td width="20"><input name="tipoPgtos" type="radio" value=""
							id="negociacaoPorComissao" onclick="negociacaoDividaController.comissaoCota();" /></td>
						<td width="118">Comiss&atilde;o da Cota</td>
						<td width="502"></td>
					</tr>
				</table>
				<div class="comissaoAtual">
					<table width="100%" border="0" cellspacing="0" cellpadding="2">
						<tr>
							<td width="6%">Atual:</td>
							<td width="12%"><input name="" type="text" id="comissaoAtualCota"
								style="width: 80px;" /></td>
							<td width="5%">%</td>
							<td width="7%">Utilizar:</td>
							<td width="13%"><input name="" type="text" id="comissaoUtilizar"
								style="width: 80px;" /></td>
							<td width="57%" colspan="3">% para pagamento da
								d&iacute;vida</td>
						</tr>
					</table>
					<table width="100%" border="0" cellspacing="0" cellpadding="2">
						<tr>
							<td colspan="6" nowrap="nowrap">Comiss&atilde;o da Cota
								enquanto houver saldo de d&iacute;vida:</td>
							<td width="85"><input name="input2" type="text"
								style="width: 80px;" /></td>
							<td width="358">%</td>
						</tr>
					</table>

				</div>
				
				
				<table width="685" border="0" cellpadding="1" cellspacing="1">
					<tr>
						<td width="25"><input name="tipoPgtos" type="radio" value=""
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
								<option value="">Selecione</option>
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
							value="DIARIO" checked="yes"
							onclick="negociacaoDividaController.mostraDiario();" /></td>
							
						<td width="5%">Di&aacute;rio</td>
						
						<td width="3%"><input type="radio" name="filtro.periodicidade" id="radioSemanal"
							value="SEMANAL"
							onclick="negociacaoDividaController.mostraSemanal();" /></td>
							
						<td width="7%">Semanal</td>
						<td width="3%"><input type="radio" name="filtro.periodicidade" id="radioQuinzenal"
							value="QUINZENAL"
							onclick="negociacaoDividaController.mostraQuinzenal();" /></td>
							
						<td width="9%">Quinzenal</td>
						<td width="3%"><input type="radio" name="filtro.periodicidade" id="radioMensal"
							value="MENSAL"
							onclick="negociacaoDividaController.mostraMensal();" /></td>
							
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
						<td width="66"><input type="text" name="filtro.quinzenalDia1" style="width: 60px;" id="diaInputQuinzenal1" /></td>
						<td id="textoDiaInputQuinzenal" width="21">&nbsp; e:</td>
						<td width="522"><input type="text" name="filtro.quinzenalDia2"
							id="diaInputQuinzenal2" style="width: 60px;" /></td>
					</tr>
				</table>
				
				<table width="130px" border="0" cellspacing="1" cellpadding="1"
					class="mensal">
					<tr>
						<td width="68">Todo dia:</td>
						<td width="66"><input type="text" name="filtro.quinzenalDia1" style="width: 60px;" id="mensalDia" /></td>
						
					</tr>
				</table>
				
				
				<table width="100%" border="0" cellspacing="1" cellpadding="1"
					class="semanal">
					<tr>
						<td width="20"><input type="checkbox" name="semanalDias"
							value="2" id="checkbSegunda" /></td>
						<td width="86">Segunda-feira</td>
						<td width="20"><input type="checkbox" name="semanalDias"
							value="3"id="checkTerca" /></td>
						<td width="70">Ter&ccedil;a-feira</td>
						<td width="20"><input type="checkbox" name="semanalDias"
							value="4"id="checkQuarta" /></td>
						<td width="78">Quarta-feira</td>
						<td width="20"><input type="checkbox" name="semanalDias"
							value="5" id="checkQuinta" /></td>
						<td width="78">Quinta-feira</td>
						<td width="20"><input type="checkbox" name="semanalDias"
							value="6" id="checkSexta" /></td>
						<td width="70">Sexta-feira</td>
						<td width="20"><input type="checkbox" name="semanalDias"
							value="7"id="checkSabado" /></td>
						<td width="53">S&aacute;bado</td>
						<td width="20"><input type="checkbox" name="semanalDias"
							value="1" id="checkDomingo" /></td>
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
										<td width="106" align="center">Ativar ao pagar</td>
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

				<br clear="all" /> <input name="" type="checkbox" id="isentaEncargos"
					style="float: left;" /><span style="float: left; margin-top: 8px;">Isenta
					Encargos</span>
			</fieldset>
			<span class="bt_novos" title="Imprimir">
				<a href="${pageContext.request.contextPath}/financeiro/negociacaoDivida/imprimirNegociacao">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						Imprimir Negocia&ccedil;&atilde;o
				</a>
			</span>
			<span class="bt_novos" title="Imprimir Boletos">
				<a href="${pageContext.request.contextPath}/financeiro/negociacaoDivida/imprimirBoletos">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
						Imprimir Boletos
				</a>
			</span>
			</form> 
		</div>

		<div id="dialog-excluir" title="Baixa Banc�ria" style="display:none;">
			<p>Deseja confirmar Baixa Manual deste Boleto?</p>
		</div>

	
	<script type="text/javascript">
		$(function() {
			negociacaoDividaController.init();
		});
	</script>
</body>