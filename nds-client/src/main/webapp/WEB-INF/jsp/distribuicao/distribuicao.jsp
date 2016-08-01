	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/distribuicao.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>

	<script type="text/javascript">

	var ${param.tela} = null;
	$(function() {
		${param.tela} = new Distribuicao('${param.tela}');
	});
		
	</script>
	
	<style>
		.divConteudoEntregador, .divConteudoEntregaBanca,
		.divUtilizaProcuracao, .divUtilizaTermoAdesao,
		.divProcuracaoRecebida, .divTermoAdesaoRecebido,
		#dialogMudancaTipoEntrega { display: none; }
	</style>

	<div id="dialogMudancaTipoEntrega" title="Mudança do Tipo de Entrega">
				
		<p>Ao mudar o Tipo de Entrega, informações do Tipo de Entrega anterior serão perdidas. Deseja continuar?</p>
				   
	</div>

	<fieldset style="width:880px!important; margin:5px!important;">
<legend>Distribuição</legend>
<table width="870" cellpadding="2" cellspacing="2" style="text-align:left;">
    	<tr>
    		<td width="390" valign="top">
   			  <table>
			    	<tr>
			        	<td>Cota:</td>
			            <td>

							<!-- Num Cota -->
							<input id="${param.tela}numCota" type="text" style="width:100px" />
						</td>
					</tr>
			        <tr>
			        	<td width="99">Box:</td>
			            <td width="308">

							<!-- Box -->
							<input id="${param.tela}box" type="text" style="width:100px" />
						</td>
					</tr>
			        <tr>
						<td>Qtde. PDV:</td>
			            <td>
							<!-- Qtde PDV -->
							<input id="${param.tela}qtdePDV" type="text" style="width:100px" />
						</td>
			        </tr>
			        <tr>
			        	<td width="99">Assist. Comercial:</td>
			            <td width="308">

							<!-- Assist. Comercial -->
							<input id="${param.tela}assistComercial" type="text" style="width:150px" />
						</td>
			        </tr>
			        <tr>
			        	<td width="99">Gerente Comercial:</td>
			            <td width="308">

							<!-- Gerente Comercial -->
							<input id="${param.tela}gerenteComercial" type="text" style="width:150px" />
						</td>
			        </tr>
			        <tr>
			            <td valign="top">Observação:</td>
			            <td colspan="3">
							<!-- Observacao  -->
							<textarea id="${param.tela}observacao" name="textarea" rows="8" style="width:220px"></textarea>
						</td>
			        </tr>
    			</table>
    			<!-- Entrega de Reparte de Venda -->
        		<input id="${param.tela}repPorPontoVenda" name="repPorPontoVenda" type="checkbox"  value="" style="margin-top:9px;" />
        		<label for="${param.tela}entregaReparteVenda" style="width:190px;float:left;">Entrega de Reparte de Venda</label>

         		<br clear="all" />

        		<!-- Solicitação Num. Atrasados -->
				<input id="${param.tela}solNumAtras" name="solNumAtrs" type="checkbox" value="" style="margin-top:9px;"/>
				<label for="${param.tela}solNumAtrs" style="width: 190px;">Solicitação N&ordm;. Atrasados - Internet</label>

         		<br clear="all" />

				<!-- Recebe / Recolhe produtos parciais -->
				<input id="${param.tela}recebeRecolhe"  name="recebeRecolhe" type="checkbox" value="" style="margin-top:9px;" />
				<label for="recebeRecolhe" style="width: 190px;">Recebe / Recolhe produtos parciais</label>
				
				<br clear="all" />
				
				<!-- Recebe Complementar -->
				<input id="${param.tela}recebeComplementar"  name="recebeComplementar" type="checkbox" style="margin-top:9px;" />
				<label for="${param.tela}recebeComplementar" style="width: 190px;">Cota Recebe Complementar</label> 
				
				<br clear="all" />
				
				<!-- Endere�o LED -->
				<input id="${param.tela}enderecoLED" maxlength="4" size="5" name="enderecoLED" type="text" style="margin-top:7px;" onkeypress="return event.charCode >= 48 && event.charCode <= 57"/>
				<label style="width: 75px;">Endere&ccedil;o LED:</label>
				
    		</td>
    		<td width="10" style="width:10px;">


    		</td>
    		<td width="440" valign="top">

		    		<fieldset style="width:415px;">
						<legend>Tipo de Entrega</legend>
		    			<table width="326">
			    			<tr>
					            <td width="26%">Tipo de Entrega:</td>
					            <td>
									
									<input type="hidden" id="${param.tela}tipoEntregaHidden" />
									
									<!-- Tipo de Entrega -->
									<select id="${param.tela}tipoEntrega" name="select4"  style="width:155px"
											onchange="DISTRIB_COTA.mostarPopUpAteracaoTipoEntrega(this.value);">
										<option selected="selected">...</option>
										<c:forEach items="${listTipoEntrega}" var="item">
											<option value="${item.key}">${item.value}</option>
										</c:forEach>
									</select>
		
								</td>
							</tr>
						</table>
						
						<div class="divConteudoEntregador">
							<div id="cotaTemEntregador" style="display: none;">
								<table width="399" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td width="130">Utiliza Procuração?</td>
										<td width="20">
											<input type="checkbox" id="${param.tela}utilizaProcuracao"
												   onclick="DISTRIB_COTA.mostrarEsconderDivUtilizaArquivo('divUtilizaProcuracao', 'divProcuracaoRecebida',
													   												      'utilizaProcuracao', 'procuracaoRecebida')" />
										</td>
													   												  
										<td width="245" height="39" class="procuracaoPf">
											<div class="divUtilizaProcuracao">
												<span class="bt_imprimir">
													<a href="javascript:;"
													   onclick="DISTRIB_COTA.downloadProcuracao();">Procuração</a>
												</span>
											</div>
										</td>
									</tr>
								</table>
									
								<div class="divUtilizaProcuracao">
									<table width="399" border="0" cellspacing="1" cellpadding="1">
										<tr>
											<td width="130">Procuração Recebida?</td>
											<td width="265">
												<input type="checkbox" id="${param.tela}procuracaoRecebida"
													   onclick="DISTRIB_COTA.mostrarEsconderDivArquivoRecebido('divProcuracaoRecebida', 'procuracaoRecebida')" />
											</td>
										</tr>
									</table>
									<div class="divProcuracaoRecebida">
										<table width="399" border="0" cellspacing="1" cellpadding="1">
											<tr>
												<td width="130">Arquivo:</td>
												<td width="265">
													
													<form action="<c:url value='/cadastro/cota/uploadProcuracao' />" id="formUploadProcuracao"
														  method="post" enctype="multipart/form-data" >		
																	
														<input type="hidden" name="formUploadAjax" value="true" />
														<input type="hidden" name="numCotaUpload" />
														   
													   	<div id="uploadProcuracao">
															<input name="uploadedFileProcuracao" type="file" id="uploadedFileProcuracao"
																   size="30" onchange="DISTRIB_COTA.submitForm('formUploadProcuracao')" />
														</div>
													</form>
												</td>
											</tr>
											<tr>
												<td width="130">&nbsp;</td>
												<td width="265">
													<span id="nomeArquivoProcuracao"></span>
												</td>
											</tr>
										</table>
									</div>
								</div>
						  </div>
						
					  </div>
					  
					<div class="dadosComuns">
						<table width="415" cellpadding="3" cellspacing="2">
		
							<tr>
								<td>Cobrança:</td>
								<td>
									<select style="width: 95px;" id="${param.tela}modalidadeCobranca" onchange="DISTRIB_COTA.mostrarOpcaoSelecionada()" >
										<option onclick="DISTRIB_COTA.mostrarOpcaoTaxaFixa();" value="TAXA_FIXA">Taxa Fixa</option>
										<option onclick="DISTRIB_COTA.mostrarOpcaoPercentual()" value="PERCENTUAL">Percentual</option>
									</select>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="checkbox" id="${param.tela}checkPorEntrega" /> Por Entrega
								</td>
					
							</tr>
							<tr>
								<td class="transpTaxaFixa">Valor R$:</td>
								<td class="transpTaxaFixa">
									<input type="text" id="${param.tela}valorTaxaFixa" style="width: 70px;"/>
								</td>
								
								<td class="transpPercentual" style="display: none;">Percentual Faturamento:</td>
								<td class="transpPercentual" style="display: none;">
									<input type="text" id="${param.tela}valorPercentualFaturamento" style="width: 70px;"/>
								</td>
								
							</tr>
							
							<tr>
								<td class="transpPercentual" style="display: none;">Base de Cálculo:</td>
								<td class="transpPercentual" style="display: none;">
									
									<!-- Base de Cálculo -->
									<select id="${param.tela}baseCalculo" name="baseCalculo"  style="width:128px">
										<c:forEach items="${listaBaseCalculo}" var="item">
											<option value="${item.key}">${item.value}</option>
										</c:forEach>
									</select>
								</td>
							
							</tr>
					
						</table>
					</div>  
						
					<jsp:include page="entregaEmBanca.jsp"/>
						
						
					<div class="dadosComuns">
							<table width="415" cellspacing="3" cellpadding="2">
								<tr>
							        <td>Periodicidade:</td>
							        <td>
							        	<input name="${param.tela}radioPeriodicidade" type="radio" value="DIARIO" 
							        		onclick="DISTRIB_COTA.alterarPeriodicidadeCobranca(this.value);" id="radioPeridioDiario" />
							        </td>
							        <td>Diário</td>
							        <td>
							        	<input name="${param.tela}radioPeriodicidade" type="radio" value="SEMANAL" 
							        		onclick="DISTRIB_COTA.alterarPeriodicidadeCobranca(this.value);" />
							        </td>
							        <td>Semanal</td>
							        <td>
							        	<input name="${param.tela}radioPeriodicidade" type="radio" value="QUINZENAL" 
							        		onclick="DISTRIB_COTA.alterarPeriodicidadeCobranca(this.value);" />
							        </td>
							        <td>Quinzenal</td>
							        <td>
							        	<input name="${param.tela}radioPeriodicidade" type="radio" value="MENSAL" 
							        		onclick="DISTRIB_COTA.alterarPeriodicidadeCobranca(this.value);" />
							        </td>
							        <td>Mensal</td>
								</tr>
							</table>
							
							<table width="415" cellspacing="1" cellpadding="1" class="perCobrancaSemanal" style="display: none;">
								<tr class="checksDiasSemana">
									<td ><input type="radio"  value="SEGUNDA_FEIRA" name="${param.tela}diaSemanaCob" /></td>
									<td>Segunda</td>
									<td><input type="radio"   value="TERCA_FEIRA" name="${param.tela}diaSemanaCob" /></td>
									<td>Terça</td>
									<td><input type="radio"   value="QUARTA_FEIRA" name="${param.tela}diaSemanaCob" /></td>
									<td>Quarta</td>
									<td><input type="radio"   value="QUINTA_FEIRA" name="${param.tela}diaSemanaCob" /></td>
									<td>Quinta</td>
									<td><input type="radio"   value="SEXTA_FEIRA" name="${param.tela}diaSemanaCob" /></td>
									<td>Sexta</td>
									<td><input type="radio"   value="SABADO" name="${param.tela}diaSemanaCob" /></td>
									<td>Sábado</td>
									<td><input type="radio"   value="DOMINGO" name="${param.tela}diaSemanaCob" /></td>
									<td>Domingo</td>
							     </tr>
							</table>
							
							<table width="415" cellspacing="1" cellpadding="1" class="perCobrancaQuinzenal" style="display: none;">
						    	<tr>
									<td width="396" height="24" align="right">Todo dia:&nbsp;</td>
									<td width="69">
										<input type="text" id="${param.tela}inputQuinzenalDiaInicio" 
											onkeyup="DISTRIB_COTA.calcularDiaFimCobQuinzenal();" style="width:60px;"/>
									</td>
									<td width="21" align="center">e</td>
									<td width="271">
										<input type="text" id="${param.tela}inputQuinzenalDiaFim" disabled="disabled" style="width:60px;"/>
									</td>
						  		</tr>
							</table>
							
							<table width="415" cellspacing="1" cellpadding="1" class="perCobrancaMensal" style="display: none;">
								<tr>
									<td width="495" align="right">Todo dia:&nbsp;</td>
									<td width="268"><input type="text" id="${param.tela}inputCobrancaMensal" style="width:60px;"/></td>
								</tr>
							</table>
							
							<table width="415" cellspacing="1" cellpadding="1">
							 <tr>
							    <td width="22%">Período Carência:</td>
							    <td>
							    	<table cellspacing="0" cellpadding="0">
								      <tr>
								        <td>
								        	<input id="${param.tela}inicioPeriodoCarencia"
												   name="inicioPeriodoCarencia" type="text" style="width: 70px" />		        	
								        </td>
								        <td>&nbsp;</td>
								        <td>Até</td>
								        <td>&nbsp;</td>
								        <td>
								        	<input id="${param.tela}fimPeriodoCarencia"
												   name="fimPeriodoCarencia" type="text" style="width: 70px" />
								        </td>
								      </tr>
							    	</table>
							    </td>
						  	</tr>
						  </table>
		
					</div>	
							
				</fieldset>
			
				<br />
				<fieldset style="width:415px;">
				<legend>Emissão de Documentos</legend>
				<table width="373" border="0" cellspacing="1" cellpadding="0">
					<tr>
						<td width="142" align="left">Utiliza</td>
						<td width="105" align="center">Impresso</td>
						<td width="106" align="center">E-mail</td>
     					</tr>
   					<tr>
						<td>Slip</td>
						<td align="center">
							<input id="${param.tela}slipImpresso" type="checkbox" name="checkbox4"/>
						</td>
						<td align="center">
							<input id="${param.tela}slipEmail" type="checkbox" name="checkbox"/>
						</td>
     					</tr>
   					<tr>
						<td>Boleto / Recibo</td>
						<td align="center">
							<input id="${param.tela}boletoImpresso" type="checkbox" />
						</td>
						<td align="center">
							<input id="${param.tela}boletoEmail" type="checkbox" />
						</td>
     					</tr>
   					<tr>
						<td>Boleto + Slip</td>
						<td align="center">
							<input id="${param.tela}boletoSlipImpresso" type="checkbox" />
						</td>
						<td align="center">
							<input id="${param.tela}boletoSlipEmail" type="checkbox" />
						</td>
     					</tr>
<!--     					<tr> -->
<!-- 							<td>Recibo</td> -->
<!-- 							<td align="center"> -->
<%-- 								<input id="${param.tela}reciboImpresso" type="checkbox" /> --%>
<!-- 							</td> -->
<!-- 							<td align="center"> -->
<%-- 								<input id="${param.tela}reciboEmail" type="checkbox" /> --%>
<!-- 							</td> -->
<!--       					</tr> -->
				    <tr>
				      	<td>Note de Envio</td>
					  	<td align="center">
							<input id="${param.tela}neImpresso" type="checkbox" name="checkbox2" />
						</td>
					    <td align="center">
					    	<input id="${param.tela}neEmail" type="checkbox" name="checkbox5"/>
					    </td>
				    </tr>
				    <tr>
						<td>Chamda de encalhe:</td>
						<td align="center">
							<input id="${param.tela}ceImpresso" type="checkbox" name="checkbox3"/>
						</td>
						<td align="center">
							<input id="${param.tela}ceEmail" type="checkbox" name="checkbox6" />
						</td>
     					</tr>
   				</table>
   				
   				<table width="373" border="0" cellspacing="1" cellpadding="0" style="padding-top: 30px;">
   					<tr>
						<td>Cota utiliza os parametros do distribuidor: 
							<input id="${param.tela}cotaUtilizaParametrosDistrib" type="checkbox" name="isCotaUtilizaParametrosDistrib" checked="checked" style="width: 50px;">
						</td>
   					</tr>
           		</table>
   				
			</fieldset>	

		
   		  </td>
    	</tr>
	</table>
</fieldset>