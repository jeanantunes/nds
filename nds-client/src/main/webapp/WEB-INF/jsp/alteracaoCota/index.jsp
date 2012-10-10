<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/alteracaoCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/utils.js"></script>
	
	<script type="text/javascript">

		var pesquisaCotaAlteracaoCota = new PesquisaCota();
		
		$(function(){
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


<form action="/administracao/alteracaoCota" id="pesquisarForm">

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
				              		<select name="filtroAlteracaoCotaDTO.listFornecedores" multiple="multiple" id="idListFornecedores" style="height:270px; width:245px;">
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
									<legend>Fornecedores selecionados</legend>
				              		<select name="filtroAlteracaoCotaDTO.filtroModalFornecedor.listaFornecedoresSelecionados" multiple="multiple" id="idListaFornecedorAssociado" style="height:270px; width:245px;">
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
							
								<select name="filtroAlteracaoCotaDTO.filtroModalFinanceiro.idVencimento" id="idVencimentoModal" size="1" style="width: 50px; height: 19px;">
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
				<table width="798" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="442" valign="top">
							<table width="442" cellpadding="2" cellspacing="2" style="text-align: left;">
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
							
							<input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.isRepartePontoVenda" id="idModalIsRepartePontoVenda"  />
							<label for="repPtoVnda">Reparte por	Ponto de Venda</label> 
							
							<br clear="all" /> 
							
							<input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.isSolicitacaoNumAtrasoInternet" id="idModalIsSolicitacaoNumAtrasoInternet"  />
							<label for="solNumAtrs">Solicita&ccedil;&atilde;o Num. Atrasados - Internet</label> 
							
							<br	clear="all" /> 
							
							<input type="checkbox"	name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.isRecebeRecolheProdutosParciais" id="idModalIsRecebeRecolheProdutosParciais"  />
							<label for="recebeRecolhe">Recebe / Recolhe produtos parciais</label>
						</td>
						<td width="12" style="width: 10px;">&nbsp;</td>
						<td width="334" valign="top">
							<table width="299" cellpadding="2" cellspacing="2"
								style="text-align: left;">
								<tr>
									<td width="106">Tipo de Entrega:</td>
									<td width="177">
										<select name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.idTipoEntrega" id="idModalIdTipoEntrega" style="width:100px;">
					              			<option selected="selected" value="-1"></option>
											<c:forEach items="${listTipoEntrega}" var="tipoEntrega">
												<option value="${tipoEntrega.id}">${tipoEntrega.descricaoTipoEntrega}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
							</table>

							<div id="entregaBancaPj" style="display: none;">
								<table width="399" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td width="153">Termo Ades&atilde;o:</td>
										<td width="20"><input type="checkbox" name="checkbox15" id="checkbox15" onclick="mostraTermoPf();" /></td>
										<td width="216"><span class="bt_imprimir" style="display: block;">
											<a href="../termo_adesao.html" target="_blank">Termo</a></span>
										</td>
									</tr>
									<tr>
										<td>Termo Ades&atilde;o Recebido?</td>
										<td colspan="2"><input type="checkbox" name="checkbox13" id="checkbox13" /></td>
									</tr>
									<tr>
										<td>Arquivo:</td>
										<td colspan="2"><input name="fileField" type="file"
											id="fileField" size="15" /></td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td colspan="2"><a href="javascript:;">nome_do_arquivo</a>
											<a href="javascript:;"><img
												src="${pageContext.request.contextPath}/images/ico_excluir.gif"
												alt="Excluir arquivo" width="15" height="15" border="0" /></a></td>
									</tr>
									<tr>
										<td>Percentual Faturamento:</td>
										<td colspan="2"><input type="text"
											style="width: 70px; text-align: right;" /></td>
									</tr>
									<tr>
										<td>Taxa Fixa R$</td>
										<td colspan="2"><input type="text"
											style="width: 70px; text-align: right;" /></td>
									</tr>
									<tr>
										<td>Base de Cálculo:</td>
										<td colspan="2"><select name="select" id="select3"
											style="width: 107px;">
										</select></td>
									</tr>
									<tr>
										<td>Per&iacute;odo Carência:</td>
										<td colspan="2"><table width="100%" border="0"
												cellspacing="0" cellpadding="0">
												<tr>
													<td width="27%"><input name="carenciaDe2" type="text"
														id="carenciaDe2" style="width: 70px" /></td>
													<td width="6%">Até</td>
													<td width="34%"><input name="carenciaAte2"
														type="text" id="carenciaAte2" style="width: 70px" /></td>
												</tr>
											</table></td>
									</tr>
								</table>
							</div>

							<div id="entregadorPj" style="display: none;">
								<table width="399" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td>Utiliza Procura&ccedil;&atilde;o?</td>
										<td width="20"><input type="checkbox" name="checkbox15"
											id="checkbox15" onclick="mostraProcuracaoPj();" /></td>
										<td width="201" class="procuracaoPj"><span
											class="bt_imprimir" style="display: block;"><a
												href="../procuracao.htm" target="_blank">Procura&ccedil;&atilde;o</a></span></td>
									</tr>
									<tr>
										<td>Procura&ccedil;&atilde;o Recebida?</td>
										<td colspan="2"><input type="checkbox" name="checkbox"
											id="checkbox" /></td>
									</tr>
									<tr>
										<td>Arquivo:</td>
										<td colspan="2"><input name="fileField" type="file"
											id="fileField" size="15" /></td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td colspan="2"><a href="javascript:;">nome_do_arquivo</a>
											<a href="javascript:;"><img
												src="${pageContext.request.contextPath}/images/ico_excluir.gif"
												alt="Excluir arquivo" width="15" height="15" border="0" /></a></td>
									</tr>
									<tr>
										<td width="145">Percentual Faturamento:</td>
										<td colspan="2"><input type="text"
											style="width: 70px; text-align: right;" /></td>
									</tr>
									<tr>
										<td>Per&iacute;odo Carência:</td>
										<td colspan="2"><table width="100%" border="0"
												cellspacing="0" cellpadding="0">
												<tr>
													<td width="43%"><input name="carenciaPjDe"
														type="text" id="carenciaPjDe" style="width: 70px" /></td>
													<td width="14%">Até</td>
													<td width="43%"><input name="carenciaPjAte"
														type="text" id="carenciaPjAte" style="width: 70px" /></td>
												</tr>
											</table></td>
									</tr>
								</table>
							</div> <br />

							<fieldset style="width: 335px;">
								<legend>Emiss&atilde;o de Documentos</legend>
								<table width="330" border="0" cellspacing="1" cellpadding="0">
									<tr>
										<td width="120" align="left">Utiliza</td>
										<td width="100" align="center">Impresso</td>
										<td width="110" align="center">E-mail</td>
									</tr>
									<tr>
										<td>Slip</td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isSkipImpresso" id="isSkipImpresso" /></td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isSkipEmail" id="isSkipEmail" /></td>
									</tr>
									<tr>
										<td>Boleto</td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoImpresso" id="isBoletoImpresso" /></td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoEmail" id="isBoletoEmail" /></td>
									</tr>
									<tr>
										<td>Boleto + Slip</td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoSkipImpresso" id="isBoletoSkipImpresso" /></td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isBoletoEmail" id="isBoletoEmail" /></td>
									</tr>
									<tr>
										<td>Recibo</td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isReciboImpresso" id="isReciboImpresso" /></td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isReciboEmail" id="isReciboEmail" /></td>
									</tr>
									<tr>
										<td>Note de Envio</td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isNoteEnvioImpresso" id="isNoteEnvioImpresso" /></td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isReciboEmail" id="isReciboEmail" /></td>
									</tr>
									<tr>
										<td>Chamda de encalhe:</td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isChamdaEncalheImpresso" id="isChamdaEncalheImpresso" /></td>
										<td align="center"><input type="checkbox" name="filtroAlteracaoCotaDTO.filtroModalDistribuicao.filtroCheckDistribEmisDoc.isChamdaEncalheEmail" id="isChamdaEncalheEmail" /></td>
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


		<fieldset class="classFieldset">
   		<legend> Pesquisar         </legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
        	<tr>
            	<td>Cota:</td>
				<td colspan="3">
					<input name="filtroAlteracaoCotaDTO.numeroCota"  class="campoDePesquisa"
						   type="text"
						   id="numeroCota"
						   maxlength="255"
						   style="width: 80px; margin-right: 5px; float: left;"
						   onchange="pesquisaCotaAlteracaoCota.pesquisarPorNumeroCota('#numeroCota', '#nomeCota', false, alteracaoCotaController.callBackSuccess,alteracaoCotaController.callBackErro);" />

				</td>
              	<td>Nome:</td>
				<td width="240">
					<input name="filtroAlteracaoCotaDTO.nomeCota" class="campoDePesquisa"
						   type="text"
						   id="nomeCota" 
						   maxlength="255" 
						   style="width: 200px;"
						   onkeyup="pesquisaCotaAlteracaoCota.autoCompletarPorNome('#nomeCota');" 
		      		 	   onblur="pesquisaCotaAlteracaoCota.pesquisarPorNomeCota('#numeroCota', '#nomeCota', false, alteracaoCotaController.callBackSuccess,alteracaoCotaController.callBackErro);" />
				</td>
                <td width="85">Municipio:</td>
                <td colspan="3">
					<select name="filtroAlteracaoCotaDTO.idMunicipio" id="idMunicipio" style="width:280px;">
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
              	<td colspan="3">
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
							<option value="${bairro.id}">${bairro.nome}</option>
						</c:forEach>
					</select>              	
              	</td>
              	<td>Valor M&iacute;nimo:</td>
              	<td width="106">
              		<select name="filtroAlteracaoCotaDTO.idVrMinimo" id="idVrMinimo" style="width:80px;">
              			<option selected="selected" value="-1"></option>
						<c:forEach items="${listHistoricoTitularidadeCotaFinanceiro}" var="historico">
							<option value="${historico.id}">${historico.valorMininoCobranca}</option>
						</c:forEach>
					</select> 
              	</td>
              	<td width="76" nowrap="nowrap">Tipo Entrega:</td>
              	<td width="102">
					<select name="filtroAlteracaoCotaDTO.idTpEntrega" id="idTpEntrega" style="width:100px;">
              			<option selected="selected" value="-1"></option>
						<c:forEach items="${listTipoEntrega}" var="tipoEntrega">
							<option value="${tipoEntrega.id}">${tipoEntrega.descricaoTipoEntrega}</option>
						</c:forEach>
					</select>
              	</td>
              	<td width="109">&nbsp;</td>
            </tr>
            <tr>
              	<td>Desconto:</td>
              	<td colspan="3">
					<select name="filtroAlteracaoCotaDTO.idTpDesconto" id="idTpDesconto" style="width:100px;">
              			<option selected="selected" value="-1"></option>
						<c:forEach items="${listTipoDesconto}" var="tipoDesconto">
							<option value="${tipoDesconto.descricao}">${tipoDesconto.descricao}</option>
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

              	<td><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();alteracaoCotaController.pesquisar()">Pesquisar</a></span></td>
            </tr>
		</table>
	</fieldset>
    
    <div class="linha_separa_fields">&nbsp;</div>
    <fieldset class="classFieldset">
       	<legend>Resultado da Pesquisa</legend>
        <div class="grids" style="display:none;">
       		<table class="alteracaoGrid"></table>
            <table width="950" border="0" cellspacing="0" cellpadding="0">
  				<tr>
    				<td width="502"><span class="bt_novos" title="Novo"><a href="javascript:;" onclick="alteracaoCotaController.carregarAlteracao();"><img src="${pageContext.request.contextPath}/images/ico_editar.gif" hspace="5" border="0"/>Alterar</a></span></td>
    				<td width="168"><strong>Total de Cotas Selecionadas:</strong></td>
    				<td width="141">4</td>
    				<td width="91">Selecionar Todos</td>
    				<td width="48"><input type="checkbox" name="checkbox3" id="acionador" /></td>
  				</tr>
			</table>
        </div>
    </fieldset>


	<script type="text/javascript">
		$(function(){
			alteracaoCotaController.init();
		});
	</script>
</form>
</body>