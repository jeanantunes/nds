<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">

<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.maskmoney.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselects-0.3.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/alteracaoCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/utils.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
	<script	src="${pageContext.request.contextPath}/scripts/jquery-upload/js/jquery.iframe-transport.js"	type="text/javascript"></script>
	<script	src="${pageContext.request.contextPath}/scripts/jquery-upload/js/jquery.fileupload.js"	type="text/javascript"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script> --%>
	
	<script type="text/javascript">

		var pesquisaCotaAlteracaoCota = new PesquisaCota(alteracaoCotaController.workspace);
		
		$(function() {
			alteracaoCotaController.init(pesquisaCotaAlteracaoCota);
		});
		
	</script>
		
	<style>
		.diasFunc label, .finceiro label{ vertical-align:super;}
		#tabs-4 .especialidades fieldset{width:220px!important; margin-left: -16px; width: 258px !important;}
		#tabs-4 .bt_novos, #tabs-4 .bt_confirmar_novo{margin-left:-14px!important;}
		
		.associacao{width:818px!important; margin-left:-11px!important;}
		.semanal, .quinzenal, .mensal{display:none;}
		.linha_separa_fields{width:700px;}
		
		#dialog-novo label { 
			width:370px; margin-bottom:10px; float:left; font-weight:bold; line-height:26px; 
		}
				
	</style>	
</head>

<body>



<div id="dialog-confirm" title="Altera&ccedil;&atilde;o Cotas">
	<p>Confirma a altera&ccedil;&atilde;o dos dados de Distribui&ccedil;&atilde;o</p>
</div>


<form action="/administracao/alteracaoCota" id="alteracaoForm">

	<div id="dialog-novo" title="Alterar Cota">
		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="dialogMensagemNovo" name="messageDialog" />
		</jsp:include>

		<div id="tabs">
			<ul>
				<li><a href="#tabs-1">Fornecedor</a></li>
				<!--<li><a href="#tabs-2">Desconto</a></li>-->
				<li><a href="#tabs-3">Financeiro</a></li>
				<li><a href="#tabs-4">Distribui&ccedil;&atilde;o</a></li>
			</ul>
			<div id="tabs-1">
				<fieldset style="width: 790px;">
					<legend>Fornecedores</legend>
					<table width="597" border="0" align="center" cellpadding="2" cellspacing="2">
						<tr class="especialidades">
							<td width="278" valign="top">

								<fieldset style="width: 250px;">
									<legend>Selecione os Fornecedores</legend>
				              		<select name="idListFornecedores" multiple="multiple" id="idListFornecedores" style="height:270px; width:245px;">
										<%-- 
										<c:forEach items="${listFornecedores}" var="fornecedor">
											<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
										</c:forEach>
										--%>
									</select>									
								</fieldset>
							</td>
							<td width="39" align="center">
								<a href="javascript:associarFornecedor()"><img src="${pageContext.request.contextPath}/images/seta_vai_todos.png" width="39" height="30" /></a>
								<br /> <br />
								<a href="javascript:desAssociarFornecedor()"><img src="${pageContext.request.contextPath}/images/seta_volta_todos.png" width="39" height="30" /></a>
								<br />
							</td>
							<td width="285" valign="top">

								<fieldset style="width: 250px;">
									<legend>Fornecedores Selecionados</legend>
				              		<select name="idListaFornecedorAssociado" multiple="multiple" id="idListaFornecedorAssociado" style="height:270px; width:245px;">
				              		<%-- 
										<c:forEach items="${listaFornecedorAssociado}" var="fornecedor">
											<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
										</c:forEach>
									--%>
									</select>
									<br />
								</fieldset>
							</td>
						</tr>
					</table>

				</fieldset>
				<br clear="all" />
			</div>

			<div id="tabs-3">
				<fieldset style="width: 790px;">
					<legend>Desconto</legend>
					<table width="584" border="0" cellspacing="1" cellpadding="1">
						<tr>
							<td width="167">Fator Vencimento em D+:</td>
							<td width="60">
							
								<select name="filtroAlteracaoCotaDTO.filtroModalFinanceiro.idVencimento" id="idVencimentoModal" size="1" style="width: 70px; height: 19px;">
			              			<option selected="selected" value="-1"></option>	
									<c:forEach items="${listaVencimento}" var="vencimento">
										<option value="${vencimento}">D+ ${vencimento}</option>
									</c:forEach>
								</select> 
							</td>
							<td width="347">&nbsp;</td>
						</tr>
						<tr>
							<td>Valor M&iacute;nimo R$:</td>
							<td><input type="text" name="filtroAlteracaoCotaDTO.filtroModalFinanceiro.vrMinimo" id="idVrMinimoModal" style="width: 60px;" /></td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>Sugere Suspens&atilde;o:</td>
							<td><input name="filtroAlteracaoCotaDTO.filtroModalFinanceiro.isSugereSuspensao" type="checkbox" id="idIsSugereSuspensaoModal" /></td>
							<td>
								<table width="98%" border="0" cellspacing="0" cellpadding="0" class="suspensao" style="display: none;">
									<tr>
										<td width="51%">Qtde de dividas em aberto:</td>
										<td width="16%"><input type="text" name="filtroAlteracaoCotaDTO.filtroModalFinanceiro.qtdDividaEmAberto" id="idQtdDividaEmAbertoModal" style="width: 50px;" /></td>
										<td width="7%">ou</td>
										<td width="7%">R$:</td>
										<td width="19%"><input type="text" name="filtroAlteracaoCotaDTO.filtroModalFinanceiro.vrDividaEmAberto" id="idVrDividaEmAbertoModal" style="width: 50px;" /></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</fieldset>
				<br clear="all" />
			</div>



			<div id="tabs-4">
				<table width="840" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="400" valign="top">
							<table width="370" cellpadding="2" cellspacing="2" style="text-align: left;">
								<tr>
									<td width="157">Assist./Promotor Comercial:</td>
									<td width="269"><input type="text" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.nmAssitPromoComercial" id="idModalNmAssitPromoComercial" style="width: 220px" /></td>
								</tr>
								<tr>
									<td height="26">Gerente Comercial:</td>
									<td><input type="text" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.nmGerenteComercial" id="idModalNmGerenteComercial" style="width: 220px" /></td>
								</tr>
								<tr>
									<td valign="top">Observa&ccedil;&atilde;o:</td>
									<td><textarea rows="8" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.observacao" id="idModalObservacao" style="width: 220px"></textarea></td>
								</tr>
							</table> 
							
							<br /> 
							
							<input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.repartePontoVenda" id="idModalIsRepartePontoVenda" />
							<label for="repPtoVnda">Entrega de Reparte de Venda</label> 
							
							<br clear="all" /> 
							
							<input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.solicitacaoNumAtrasoInternet" id="idModalIsSolicitacaoNumAtrasoInternet" />
							<label for="solNumAtrs">Solicitação N&ordm;. Atrasados - Internet</label> 
							
							<br	clear="all" /> 
							
							<input type="checkbox"name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.recebeRecolheProdutosParciais" id="idModalIsRecebeRecolheProdutosParciais" />
							<label for="recebeRecolhe">Recebe / Recolhe produtos parciais</label>
							
							<br	clear="all" /> 

							<input type="checkbox"name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.recebeComplementar" id="idModalIsRecebeComplementar" />
							<label for="recebeComplementar">Cota Recebe Complementar</label>
							
						</td>
						<td width="12" style="width: 10px;">&nbsp;</td>
						<td width="390" valign="top">
							
							<fieldset style="width: 400px">
								<legend>Tipo de Entrega</legend>
								
								<table width="400" cellpadding="2" cellspacing="2" style="text-align: left;">
								<tr>
									<td width="106">Tipo de Entrega:</td>
									<td width="177">
										<select name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.descricaoTipoEntrega" id="idModalIdTipoEntrega" style="width:125px;" onchange="alteracaoCotaController.selectTipoEntregaDistribuicao()">
					              			<option selected="selected" value="-1"></option>
											<c:forEach items="${listTipoEntrega}" var="tipoEntrega">
												<option value="${tipoEntrega}">${tipoEntrega.value}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
							</table>

							<div id="entregaBancaPj" style="display: none;">
							
								<div class="divImpressaoTermoAdesao">
									<table width="399" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td width="115">Termo Ades&atilde;o:</td>
										<td width="20"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.termoAdesao" id="termoAdesao"  onclick="alteracaoCotaController.mostrarEsconderDivUtilizaArquivoTermo()"/></td>
									
										<td width="216">
										<div id="termoRecebidoDownload">
											<span class="bt_imprimir" style="display: block;">
												<a href="javaScript:alteracaoCotaController.downloadTermoAdesao()" target="_blank">Termo</a></span>
										</div>		
										</td>
										
									</tr>
							      	</table>
							     </div>
							     
									<div id="termoArquivoRecebido">
									<table width="399" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td width="130">Termo Ades&atilde;o Recebido?</td>
										<td colspan="2" width="20"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.termoAdesaoRecebido" id="termoAdesaoRecebido"   onclick="alteracaoCotaController.mostrarEsconderDivArquivoUpLoadTermo()"   /></td>
									</tr>
									</table>
									
									<div id="uploadedFileTermoDiv">
									<table width="399" border="0" cellspacing="1" cellpadding="1">
								
									<tr>
									
										<td>Arquivo:</td>
										<td colspan="2">
											<input name="uploadedFileTermo" type="file" id="uploadedFileTermo" size="30" />
										</td>
										
									</tr>
								
									<tr>
										<td>&nbsp;</td>
										<td olspan="2">
											<span id="nomeArquivoTermoAdesao"></span>
										</td>
									</tr>
									</table>	
									</div>	
									
									</div>
							</div>

							<div id="entregadorPj" style="display: none;">
								<table width="399" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td width="115">Utiliza Procura&ccedil;&atilde;o?</td>
										<td width="20"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.procuracao"  id="procuracao"   onclick="alteracaoCotaController.mostrarEsconderDivUtilizaArquivoProcuracao()"/></td>
									
										<td width="216">
										<div id="procuracaoRecebidoDownload">
											<span class="bt_imprimir" style="display: block;"><a
												href="javaScript:alteracaoCotaController.downloadProcuracao()" target="_blank">Procura&ccedil;&atilde;o</a></span>
										</div>		
										</td>
										
									</tr>
							      	</table>
									<div id="procuracaoArquivoRecebido">
									<table width="399" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td width="130">Procura&ccedil;&atilde;o Recebida?</td>
										<td colspan="2" width="20"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.procuracaoRecebida" id="procuracaoRecebida"   onclick="alteracaoCotaController.mostrarEsconderDivArquivoUpLoadProcuracao()"   /></td>
									</tr>
									</table>
									
									<div id="uploadedFileProcuracaoDiv">
									<table width="399" border="0" cellspacing="1" cellpadding="1">
								
									<tr>
									
										<td>Arquivo:</td>
										<td colspan="2">
											<input name="uploadedFileProcuracao" type="file" id="uploadedFileProcuracao" size="30" />
										</td>
										
									</tr>
								
									<tr>
										<td>&nbsp;</td>
										<td olspan="2">
											<span id="nomeArquivoProcuracao"></span>
										</td>
									</tr>
									</table>	
									</div>	
									
									</div>
							</div> 
							
							<div class="dadosCobrancaComuns"  style="display: none;">
								
								<table width="415" cellpadding="3" cellspacing="2">
		
									<tr>
										<td>Cobrança:</td>
										<td>
											<select style="width: 95px;" 
													id="modalidadeCobranca" onchange="alteracaoCotaController.mostrarOpcaoSelecionada()" 
													name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.modalidadeCobranca">
												<option onclick="alteracaoCotaController.mostrarOpcaoSelecionada();" value=""></option>	
												<option onclick="alteracaoCotaController.mostrarOpcaoTaxaFixa();" value="TAXA_FIXA">Taxa Fixa</option>
												<option onclick="alteracaoCotaController.mostrarOpcaoPercentual()" value="PERCENTUAL">Percentual</option>
											</select>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input type="checkbox" id="checkPorEntrega" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.porEntrega" /> Por Entrega
										</td>
							
									</tr>
									<tr>
										<td class="transpTaxaFixa">Valor R$:</td>
										<td class="transpTaxaFixa">
											<input type="text" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.taxaFixa" id="valorTaxaFixa" style="width: 70px;"/>
										</td>
										
										<td class="transpPercentual" style="display: none;">Percentual Faturamento:</td>
										<td class="transpPercentual" style="display: none;">
											<input type="text" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.percentualFaturamento" id="valorPercentualFaturamento" style="width: 70px;"/>
										</td>
										
									</tr>
									
									<tr>
										<td class="transpPercentual" style="display: none;">Base de Cálculo:</td>
										<td class="transpPercentual" style="display: none;">
											<select id="basesCalculo" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.baseCalculo"  style="width:128px"></select>
										</td>
									
									</tr>
							
								</table>
								
								<table width="415" cellspacing="3" cellpadding="2">
									<tr>
								        <td>Periodicidade:</td>
								        <td>
								        	<input name="radioPeriodicidade" type="radio" value="DIARIO" 
								        		onclick="alteracaoCotaController.alterarPeriodicidadeCobranca(this.value);" id="radioPeridioDiario" />
								        </td>
								        <td>Diário</td>
								        <td>
								        	<input name="radioPeriodicidade" type="radio" value="SEMANAL" 
								        		onclick="alteracaoCotaController.alterarPeriodicidadeCobranca(this.value);" />
								        </td>
								        <td>Semanal</td>
								        <td>
								        	<input name="radioPeriodicidade" type="radio" value="QUINZENAL" 
								        		onclick="alteracaoCotaController.alterarPeriodicidadeCobranca(this.value);" />
								        </td>
								        <td>Quinzenal</td>
								        <td>
								        	<input name="radioPeriodicidade" type="radio" value="MENSAL" 
								        		onclick="alteracaoCotaController.alterarPeriodicidadeCobranca(this.value);" />
								        </td>
								        <td>Mensal</td>
									</tr>
								</table>
								
								<table width="415" cellspacing="1" cellpadding="1" class="perCobrancaSemanal" style="display: none;">
									<tr class="checksDiasSemana">
										<td ><input type="radio"  value="SEGUNDA_FEIRA" name="diaSemanaCob" /></td>
										<td>Segunda</td>
										<td><input type="radio"   value="TERCA_FEIRA" name="diaSemanaCob" /></td>
										<td>Terça</td>
										<td><input type="radio"   value="QUARTA_FEIRA" name="diaSemanaCob" /></td>
										<td>Quarta</td>
										<td><input type="radio"   value="QUINTA_FEIRA" name="diaSemanaCob" /></td>
										<td>Quinta</td>
										<td><input type="radio"   value="SEXTA_FEIRA" name="diaSemanaCob" /></td>
										<td>Sexta</td>
										<td><input type="radio"   value="SABADO" name="diaSemanaCob" /></td>
										<td>Sábado</td>
										<td><input type="radio"   value="DOMINGO" name="diaSemanaCob" /></td>
										<td>Domingo</td>
								     </tr>
								</table>
								
								<table width="415" cellspacing="1" cellpadding="1" class="perCobrancaQuinzenal" style="display: none;">
							    	<tr>
										<td width="396" height="24" align="right">Todo dia:&nbsp;</td>
										<td width="69">
											<input type="text" id="inputQuinzenalDiaInicio" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.diaCobranca"
												onkeyup="alteracaoCotaController.calcularDiaFimCobQuinzenal();" style="width:60px;"/>
										</td>
										<td width="21" align="center">e</td>
										<td width="271">
											<input type="text" id="inputQuinzenalDiaFim" disabled="disabled" style="width:60px;"/>
										</td>
							  		</tr>
								</table>
								
								<table width="415" cellspacing="1" cellpadding="1" class="perCobrancaMensal" style="display: none;">
									<tr>
										<td width="495" align="right">Todo dia:&nbsp;</td>
										<td width="268">
											<input type="text" id="inputCobrancaMensal" style="width:60px;" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.diaCobranca"/>
										</td>
									</tr>
								</table>
								
								<table width="415" cellspacing="1" cellpadding="1">
								 <tr>
								    <td width="22%">Período Carência:</td>
								    <td>
								    	<table cellspacing="0" cellpadding="0">
									      <tr>
									        <td>
									        	<input id="inicioPeriodoCarencia"
													   name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.carenciaInicio" type="text" style="width: 70px" />		        	
									        </td>
									        <td>&nbsp;</td>
									        <td>Até</td>
									        <td>&nbsp;</td>
									        <td>
									        	<input id="fimPeriodoCarencia"
													   name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.carenciaFim" type="text" style="width: 70px" />
									        </td>
									      </tr>
								    	</table>
								    </td>
							  	</tr>
							  </table>
									
							</div>	
								
							</fieldset>
							
							
							<br />

							<fieldset style="width: 400px">
								<legend>Emiss&atilde;o de Documentos</legend>
								<table width="400" border="0" cellspacing="1" cellpadding="0">
									<tr>
										<td width="120" align="left">Utiliza</td>
										<td width="100" align="center">Impresso</td>
										<td width="110" align="center">E-mail</td>
									</tr>
									<tr>
										<td>Slip</td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isSlipImpresso" id="isSlipImpresso" /></td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isSlipEmail" id="isSlipEmail" /></td>
									</tr>
									<tr>
										<td>Boleto</td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoImpresso" id="isBoletoImpresso" /></td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoEmail" id="isBoletoEmail" /></td>
									</tr>
									<tr>
										<td>Boleto + Slip</td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoSlipImpresso" id="isBoletoSlipImpresso" /></td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoSlipEmail" id="isBoletoSlipEmail" /></td>
									</tr>
									<tr>
										<td>Recibo</td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isReciboImpresso" id="isReciboImpresso" /></td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isReciboEmail" id="isReciboEmail" /></td>
									</tr>
									<tr>
										<td>Nota de Envio</td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isNotaEnvioImpresso" id="isNotaEnvioImpresso" /></td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isNotaEnvioEmail" id="isNotaEnvioEmail" /></td>
									</tr>
									<tr>
										<td>Chamda de encalhe:</td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isChamdaEncalheImpresso" id="isChamdaEncalheImpresso" /></td>
									<td align="center">
										<input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isChamdaEncalheEmail" id="isChamdaEncalheEmail" />
									</td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
				</table>

				<br clear="all" />

			</div>
		</div>
	</div>
</form>
<form id="pesquisarForm">

		<div class="areaBts">
		<div class="area">
		
			<span class="bt_novos">
				<a href="javascript:;" onclick="alteracaoCotaController.carregarAlteracao();" rel="tipsy" title="Alterar">
					<img src="${pageContext.request.contextPath}/images/ico_editar.gif" hspace="5" border="0"/>
				</a>
			</span>
		</div>  		  		
	</div>
  	<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
   		<legend> Pesquisar         </legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
        	<tr>
            	<td>Cota:</td>
				<td width="120">
					<input name="filtroAlteracaoCotaDTO.numeroCota" class="campoDePesquisa"
						   type="text"
						   id="alteracao-cota-numeroCota"
						   maxlength="255"
						   style="width: 80px; margin-right: 5px; float: left;"
						   onchange="pesquisaCotaAlteracaoCota.pesquisarPorNumeroCota('#alteracao-cota-numeroCota', '#alteracao-cota-nomeCota', false, alteracaoCotaController.callBackSuccess, alteracaoCotaController.callBackErro);" />

				</td>
              	<td>Nome:</td>
				<td width="240">
					<input name="filtroAlteracaoCotaDTO.nomeCota" class="campoDePesquisa"
						   type="text"
						   id="alteracao-cota-nomeCota" 
						   maxlength="255" 
						   style="width: 200px;"
						   onkeyup="pesquisaCotaAlteracaoCota.autoCompletarPorNome('#alteracao-cota-nomeCota');" 
		      		 	   onblur="pesquisaCotaAlteracaoCota.pesquisarPorNomeCota('#alteracao-cota-numeroCota', '#alteracao-cota-nomeCota', false, alteracaoCotaController.callBackSuccess,alteracaoCotaController.callBackErro);" />
				</td>
                <td width="85">Municipio:</td>
                <td colspan="3">
					<select name="filtroAlteracaoCotaDTO.idMunicipio" id="idMunicipio" style="width:280px;" onchange="alteracaoCotaController.carregarBairros(this.value)">
              			<option selected="selected" value="-1"></option>
						<c:forEach items="${listMunicipios}" var="municipio">
							<option value="${municipio}">${municipio}</option>
						</c:forEach>
					</select>                 
              	</td>
              	<td>&nbsp;</td>
            </tr>
            <tr>
              	<td width="74">Fornecedor:</td>
              	<td>
              		<select name="filtroAlteracaoCotaDTO.idFornecedor" id="idFornecedor" style="width:100px;">
              			<option selected="selected" value="-1"></option>
						<c:forEach items="${listFornecedores}" var="fornecedor">
							<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
						</c:forEach>
					</select>
              	</td>
                <td width="73">Bairro:</td>
              	<td width="167">
              		<select name="filtroAlteracaoCotaDTO.idBairro" id="idBairro" style="width:100px;">
              			<option selected="selected" value="-1"></option>
						<c:forEach items="${listBairros}" var="bairro">
							<option value="${bairro}">${bairro}</option>
						</c:forEach>
					</select>              	
              	</td>
              	<td>Valor M&iacute;nimo:</td>
              	<td width="106">
              		<select name="filtroAlteracaoCotaDTO.idVrMinimo" id="idVrMinimo" style="width:80px;">
              			<option selected="selected" value="-1"></option>
						<c:forEach items="${listValoresMinimos}" var="valor">
							<option value="${valor}">${valor}</option>
						</c:forEach>
					</select> 
              	</td>
              	<td width="76" nowrap="nowrap">Tipo Entrega:</td>
              	<td width="102">
					<select name="filtroAlteracaoCotaDTO.descricaoTipoEntrega" id="idTpEntrega" style="width:100px;">
              			<option selected="selected" value="-1"></option>
						<c:forEach items="${listTipoEntrega}" var="tipoEntrega">
							<option value="${tipoEntrega}">${tipoEntrega.value}</option>
						</c:forEach>
					</select>
              	</td>
              	<td width="109">&nbsp;</td>
            </tr>
            <tr>
              	<td>Desconto:</td>
              	<td>
					<select name="filtroAlteracaoCotaDTO.tipoDesconto" id="idTpDesconto" style="width:100px;">
              			<option selected="selected" value="-1"></option>
						<c:forEach items="${listTipoDesconto}" var="tipoDesconto">
							<option value="${tipoDesconto}">${tipoDesconto.descricao}</option>
						</c:forEach>
					</select>
              	</td>
              	<td>Vencimento:</td>
              	<td>
					<select name="filtroAlteracaoCotaDTO.idVencimento" id="idVencimento" style="width:100px;">
              			<option selected="selected" value="-1"></option>
						<c:forEach items="${listaVencimento}" var="vencimento">
							<option value="${vencimento}">D+ ${vencimento}</option>
						</c:forEach>
					</select>              	
              	</td>
				<td colspan="2">Utiliza Parâm. de Cob. Distribuidor:</td>
				<td colspan="2">
					<select name="filtroAlteracaoCotaDTO.utilizaParametroCobrancaDistribuidor" id="utilizaParametroCobrancaDistribuidor" style="width:100px;">
						<option value="SIM">Sim</option>
						<option value="NAO">Não</option>
              			<option selected="selected" value="TODOS">Todos</option>
					</select> 
				</td>
              	<td><span class="bt_pesquisar"><a href="javascript:;" onclick="alteracaoCotaController.pesquisar();">Pesquisar</a></span></td>
            </tr>
		</table>
	</fieldset>
</form>    
   
  <form id="gridForm">
    <div class="linha_separa_fields">&nbsp;</div>
    <fieldset class="classFieldset">
       	<legend>Resultado da Pesquisa</legend>
        <div class="grids" style="display:none;">
       		<table class="alteracaoGrid"></table>
            <table width="950" border="0" cellspacing="0" cellpadding="0">
  				<tr>
    				<td width="502"></td>
    				<td width="168"><strong>Total de Cotas Selecionadas:</strong></td>
    				<td width="141"><span id="totalCotasSelecionadas"></span></td>
    				<td width="91">Selecionar Todos</td>
    				<td width="48"><input type="checkbox" name="checkAll" id="alteracaoCotaCheckAll" onclick="alteracaoCotaController.checkAll(this); alteracaoCotaController.verificarCheck();" /></td>
  				</tr>
			</table>
        </div>
    </fieldset>
</form>

</body>