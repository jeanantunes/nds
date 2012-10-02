<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/distribuicao.js"></script>

	<script type="text/javascript">

		var ${param.tela} = new Distribuicao('${param.tela}');

	</script>
	
	<style>
		.divConteudoEntregador, .divConteudoEntregaBanca,
		.divUtilizaProcuracao, .divUtilizaTermoAdesao,
		.divProcuracaoRecebida, .divTermoAdesaoRecebido,
		#dialogMudancaTipoEntrega { display: none; }
	</style>
</head>

	<div id="dialogMudancaTipoEntrega" title="Mudança do Tipo de Entrega">
				
		<p>Ao mudar o Tipo de Entrega, informações do Tipo de Entrega anterior serão perdidas. Deseja continuar?</p>
				   
	</div>

	<fieldset style="width:880px!important; margin:5px!important;">
<legend>Distribuição</legend>
<table width="890" cellpadding="2" cellspacing="2" style="text-align:left;">
    	<tr>
    		<td width="418" valign="top">
   			  <table>
			    	<tr>
			        	<td>Cota:</td>
			            <td>

							<!-- Num Cota -->
							<input id="${param.tela}numCota" disabled="disabled" type="text" style="width:100px" />
						</td>
					</tr>
			        <tr>
			        	<td width="99">Box:</td>
			            <td width="308">

							<!-- Box -->
							<input id="${param.tela}box" disabled="disabled" type="text" style="width:100px" />
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
        		<input type="checkbox" id="${param.tela}entregaReparteVenda" style="margin-top:9px;" />
        		<label for="${param.tela}entregaReparteVenda" style="width:190px;float:left;">Entrega de Reparte de Venda</label>

         		<br clear="all" />

        		<!-- Solicitação Num. Atrasados -->
				<input id="${param.tela}solNumAtras" name="solNumAtrs" type="checkbox" value="" style="margin-top:9px;"/>
				<label for="${param.tela}solNumAtrs" style="width: 190px;">Solicitação Num. Atrasados - Internet</label>

         		<br clear="all" />

				<!-- Recebe / Recolhe produtos parciais -->
				<input id="${param.tela}recebeRecolhe"  name="recebeRecolhe" type="checkbox" value="" style="margin-top:9px;" />
				<label for="recebeRecolhe" style="width: 190px;">Recebe / Recolhe produtos parciais</label>
    		</td>
    		<td width="10" style="width:10px;">


    		</td>
    		<td width="440" valign="top">
    		<fieldset style="width:390px;">
				<legend>Tipo de Entrega</legend>
    			<table width="326">
	    			<tr>
			            <td width="86">Tipo de Entrega:</td>
			            <td width="228">
							
							<input type="hidden" id="${param.tela}tipoEntregaHidden" />
							
							<!-- Tipo de Entrega -->
							<select id="${param.tela}tipoEntrega" name="select4"  style="width:155px"
									onchange="DISTRIB_COTA.mostarPopUpAteracaoTipoEntrega(this.value);">
								<option selected="selected">...</option>

								<c:forEach items="${listaTipoEntrega}" var="item">
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
					<table width="399" border="0" cellspacing="1" cellpadding="1">
						<tr>
							<td width="124">Percentual Faturamento:</td>
							<td width="268">
								<input id="${param.tela}percentualFaturamentoEntregador" name="percentualFaturamento"
									   type="text" style="width: 70px; text-align: right;" />
							</td>
						</tr>
						<tr>
							<td width="124">Período Carência:</td>
							<td width="268">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="43%">
											<input id="${param.tela}inicioPeriodoCarenciaEntregador"
												   name="inicioPeriodoCarencia" type="text" style="width: 70px" />
										</td>
										<td width="14%">Até</td>
										<td width="43%">
											<input id="${param.tela}fimPeriodoCarenciaEntregador"
												   name="fimPeriodoCarencia" type="text" style="width: 70px" />
										</td>
									</tr>
								</table></td>
						</tr>
					</table>
			  </div>
				
				<jsp:include page="entregaEmBanca.jsp"/>
				</fieldset>

				<br />

				<fieldset style="width:390px;">
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
							<td>Boleto</td>
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
    					<tr>
							<td>Recibo</td>
							<td align="center">
								<input id="${param.tela}reciboImpresso" type="checkbox" />
							</td>
							<td align="center">
								<input id="${param.tela}reciboEmail" type="checkbox" />
							</td>
      					</tr>
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
				</fieldset>
   		  </td>
    	</tr>
	</table>
</fieldset>
