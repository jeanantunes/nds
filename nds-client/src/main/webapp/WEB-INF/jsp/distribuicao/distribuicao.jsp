<head>	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/distribuicao.js"></script>
	
	<script type="text/javascript">		
		
		var ${param.tela} = new Distribuicao('${param.tela}');	
		
	</script>
</head>

	<table width="900" cellpadding="2" cellspacing="2" style="text-align:left;">
    	<tr>
    		<td width="442">
    			<table>
			    	<tr>
			        	<td>Cota:</td>
			            <td>
			            
							<!-- Num Cota -->
							<input id="${param.tela}numCota" disabled="disabled" type="text" style="width:100px" />
						</td>
					</tr>
			        <tr>
			        	<td width="120">Box:</td>
			            <td width="320">
			            
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
			        	<td width="116">Assist. Comercial:</td>
			            <td width="171">
			            
							<!-- Assist. Comercial -->
							<input id="${param.tela}assistComercial" type="text" style="width:150px" />
						</td>
			        </tr>
			        <tr>
			        	<td width="116">Gerente Comercial:</td>
			            <td width="171">
			            
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
        		<input type="checkbox" id="${param.tela}repPorPontoVenda" />
        		<label for="${param.tela}repPorPontoVenda">Entrega de Reparte de Venda</label>
				
         		<br clear="all" />
        		
        		<!-- Solicitação Num. Atrasados -->
				<input id="${param.tela}solNumAtras" name="solNumAtrs" type="checkbox" value=""/>
				<label for="${param.tela}solNumAtrs">Solicitação Num. Atrasados - Internet</label>
				
         		<br clear="all" />
         		
				<!-- Recebe / Recolhe produtos parciais -->        
				<input id="${param.tela}recebeRecolhe"  name="recebeRecolhe" type="checkbox" value="" />
				<label for="recebeRecolhe">Recebe / Recolhe produtos parciais</label>
    		</td>
    		<td width="12">
    		
    		
    		</td>
    		<td width="334" valign="top">
    			<table>
	    			<tr>
			            <td>Tipo de Entrega:</td>
			            <td>
			
							<!-- Tipo de Entrega -->
							<select id="${param.tela}tipoEntrega" name="select4"  style="width:155px">
								<option selected="selected">...</option>
								
								<c:forEach items="${listaTipoEntrega}" var="item">
									<option value="${item.key}">${item.value}</option>	          
								</c:forEach>                            
							</select>
						
						</td>
					</tr>
				</table>
				
				<div id="entregadorPf" style="display: none;">
					<table width="399" border="0" cellspacing="1" cellpadding="1">
						<tr>
							<td>Utiliza Procuração?</td>
							<td width="20">
								<input type="checkbox" name="checkbox15"
									   id="checkbox15" onclick="mostraProcuracaoPf();" />
								
							<td width="201" class="procuracaoPf">
								<span class="bt_imprimir" style="display: block;">
									<a href="../procuracao.htm" target="_blank">Procuração</a>
								</span>
							</td>
						</tr>
						<tr>
							<td>Procuração Recebida?</td>
							<td colspan="2">
								<input type="checkbox" name="checkbox2" id="checkbox2" />
							</td>
						</tr>
						<tr>
							<td>Arquivo:</td>
							<td colspan="2">
								<input name="fileField" type="file"
									   id="fileField" size="15" />
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td colspan="2">
								<a href="javascript:;">nome_do_arquivo</a>
								<a href="javascript:;">
									<img src="../images/ico_excluir.gif"
								   		 alt="Excluir arquivo" width="15" height="15" border="0" />
								</a>
							</td>
						</tr>
						<tr>
							<td width="145">Percentual Faturamento:</td>
							<td colspan="2">
								<input id="${param.tela}percentualFaturamento" type="text"
									   style="width: 70px; text-align: right;" />
							</td>
						</tr>
						<tr>
							<td>Período Carência:</td>
							<td colspan="2">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="43%">
											<input id="${param.tela}inicioPeriodoCarencia" 
												   name="inicioPeriodoCarencia" type="text" style="width: 70px" />
										</td>
										<td width="14%">Até</td>
										<td width="43%">
											<input id="${param.tela}fimPeriodoCarencia"
												   name="fimPeriodoCarencia" type="text" style="width: 70px" />
										</td>
									</tr>
								</table></td>
						</tr>
					</table>
				</div>
				
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
<br />
<br />
<br />