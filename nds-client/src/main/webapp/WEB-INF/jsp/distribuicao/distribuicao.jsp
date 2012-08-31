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
        		<input type="checkbox" id="${param.tela}entregaReparteVenda" />
        		<label for="${param.tela}entregaReparteVenda">Entrega de Reparte de Venda</label>
				
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
								
				<jsp:include page="entregaEmBanca.jsp"/>
				
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
							<td align="center"><input type="checkbox" name="checkbox3" id="checkbox3" /></td>
							<td align="center"><input type="checkbox" name="checkbox6" id="checkbox6" /></td>
      					</tr>
    					<tr>
							<td>Boleto + Slip</td>
							<td align="center"><input type="checkbox" name="checkbox4" id="checkbox4" /></td>
							<td align="center"><input type="checkbox" name="checkbox" id="checkbox" /></td>
      					</tr>
    					<tr>
							<td>Recibo</td>
							<td align="center"><input type="checkbox" name="checkbox7" id="checkbox7" /></td>
							<td align="center"><input type="checkbox" name="checkbox12" id="checkbox12" /></td>
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