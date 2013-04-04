<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/geracaoNotaEnvio.js"></script>

<script>
	geracaoNotaEnvioController.init();
	bloquearItensEdicao(geracaoNotaEnvioController.workspace);
	
</script>

</head>

<body>
	<div class="areaBts">
		<div class="area">
			<div id="geracaoNotaEnvio-fileExport" style="display: none; float:right;">
				<span class="bt_arq" >
					<a id="geracaoNotaEnvio-btnImprimirXLS" href="javascript:;" title="Gerar Arquivo">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					</a>
				</span>
		
        		<span class="bt_arq" >
        			<a id="geracaoNotaEnvio-btnImprimirNE" isEdicao="true" href="#" title="Imprimir NE/NECA" >
        				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir NE/NECA" hspace="5" border="0" />
        			</a>
        		</span>

        		<span id="geracaoNotaEnvio-btnVisualizarNE" class="bt_arq">
        			<a href="${pageContext.request.contextPath}/expedicao/geracaoNotaEnvio/visualizarNE" title="Visualizar NE/NECA" target="_blank">
        				<img src="${pageContext.request.contextPath}/images/ico_detalhes.png" alt="Visualizar NE/NECA" hspace="5" border="0">
        			</a>
        		</span>
        	</div>
        </div>
    </div>
    <div class="linha_separa_fields">&nbsp;</div>
    
	<fieldset class="classFieldset">
   		
   		<legend> Geração de Nota de Envio</legend>
    	<form>
    	<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  			<tbody>
  				<tr>
    				<td>Fornecedor:</td>
    				<td>
    					<select id="geracaoNotaEnvio-filtro-selectFornecedores" multiple="multiple" style="width:200px">
							<c:forEach items="${fornecedores}" var="fornecedor">
								<option value="${fornecedor.key}">${fornecedor.value}</option>
							</c:forEach>
						</select>
    				</td>
    				
    				<td>Data Movimento:</td>
    				<td>
    					<input type="text" id="geracaoNotaEnvio-filtro-movimentoDe" name ="movimentoDe" style="width:76px;" class="input-date">
     						&nbsp;&nbsp;Até&nbsp;
      					<input type="text" id="geracaoNotaEnvio-filtro-movimentoAte" name ="movimentoAte" style="width:76px;" class="input-date">
      				</td>
    				
    				<td>Data Emissão:</td>
    				<td>
    					<input type="text" id="geracaoNotaEnvio-filtro-dataEmissao" style="width:80px;" class="input-date">
    				</td>
  				</tr>
 	      				  					
  				<tr>
    				<td width="91">Roteiro:</td>
   	 				<td width="215">
   	 					<select id="geracaoNotaEnvio-filtro-selectRoteiro" style="width:200px; font-size:11px!important">
      						<option value="">Selecione...</option>
      						<c:forEach items="${roteiros}" var="roteiro">
								<option value="${roteiro.key }">${roteiro.value }</option>
							</c:forEach>
    					</select>
    				</td>
    					
    				<td width="93">Rota:</td>
    				<td>
    					<select id="geracaoNotaEnvio-filtro-selectRota" style="width:150px; font-size:11px!important">
      						<option value="">Selecione...</option>
      						<c:forEach items="${rotas}" var="rota">
								<option value="${rota.key }">${rota.value }</option>
							</c:forEach>
    					</select>
    				</td>
    				
    				<td>Cota de:</td>
   					<td>
   						<input id="geracaoNotaEnvio-filtro-cotaDe" type="text" style="width:60px;" class="input-numeric">
							&nbsp;Até&nbsp;
						<input id="geracaoNotaEnvio-filtro-cotaAte" type="text" style="width:60px;" class="input-numeric">
					</td>
				</tr>
   				<tr>
   					<td>Intervalo Box:</td>
   					<td>
   						<input id="geracaoNotaEnvio-filtro-boxDe" type="text" style="width:76px;" class="input-numeric">
							&nbsp;Até &nbsp;
						<input id="geracaoNotaEnvio-filtro-boxAte" type="text" style="width:76px;" class="input-numeric">
					</td>
    					
   					<td>Exibir Notas: </td>
   					<td width="270">
   						<select id="geracaoNotaEnvio-filtro-exibirNotasEnvio" style="width:150px; font-size:11px!important">
      						<option value="AMBAS">Emitidas e a emitir</option>
      						<option value="EMITIDAS">Emitidas</option>
      						<option value="AEMITIR">A emitir</option>
    					</select>
    				</td>
 					<td width="78">&nbsp;</td>
   					<td width="180">
   						  						
   						<span class="bt_novos">
   							<a href="javascript:;" id="geracaoNotaEnvio-btnPesquisar">
   							<img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" />
   							</a>
   						</span>
   						
   					</td>
  				</tr>
					
			</tbody>
		</table>
		</form>
	</fieldset>
	
	 <div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset class="classFieldset">
		<legend>Geração de Nota de Envio</legend>
		
		<div id="geracaoNotaEnvio-pesquisa" style="display:none;" class="grids">
			<div id="geracaoNotaEnvio-flexigrid-pesquisa" />
		</div>
		
      </fieldset>
	
	  <div class="linha_separa_fields">&nbsp;</div>
	
	<div id="geracaoNotaEnvio-dialog-suplementar" style="display:none;">	
		<fieldset>
			<legend>Transferência para Suplementar</legend>
    		
    		<p>Reparte das Cotas selecionadas estão sendo transferido para Suplementar</p>
    		
		</fieldset>
	</div>
	
	<div id="geracaoNotaEnvio-dialog-cotasAusentes" style="display:none;" >	
		<fieldset>
			<legend>Cotas Suspensas</legend>
    		
    		<div id="geracaoNotaEnvio-flexigrid-cotasAusentes" />
    		
    		<span class="bt_sellAll" style="float:right;">
    			<label for="geracaoNotaEnvio-cotasAusentes-checkAll">Selecionar Todos</label>
    			<input type="checkbox" id="geracaoNotaEnvio-cotasAusentes-checkAll" style="float:left;">
    		</span>
		</fieldset>
	</div>
	
</body>
