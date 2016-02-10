<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Treelog</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/fechamentoCEIntegracao.js"></script>
<script language="javascript" type="text/javascript">

	$(function() {
		fechamentoCEIntegracaoController.init();		
		bloquearItensEdicao(fechamentoCEIntegracaoController.workspace);
	});	

</script>
<style type="text/css">
  .box_field{width:200px;}
</style>
</head>

<body>

	<iframe src="" id="download-iframe-fechamento" style="display:none;"></iframe>
	
	<form id="idFormConfirmacaoSalvar">
		<div id="dialog-ConfirmacaoSalvar" title="Fechamento CE - Integração" style="display:none;">
	  		<p>Deseja salvar as informações?</p>
		</div>
	</form>
	
	<form id="idFormConfirmacaoFechamento">
		<div id="dialog-Confirmacao-Fechamento" title="Fechamento CE - Integração" style="display:none;">
	  		<p>Deseja efetuar o fechamento da CE - Integração?</p>
		</div>
	</form>
	
	<form id="idFormConfirmacaoFechamentoSemCe">
		<div id="dialog-Confirmacao-Fechamento-SemCe" title="Fechamento CE - Integração" style="display:none;">
	  		<p>Deseja efetuar o fechamento </p>
		</div>
	</form>
	
	<form id="idFormPerdaGanhos">
		<div id="dialog-perdas-ganhos-Fechamento" title="Diferença(s) Fechamento CE - Integração" style="display:none;">
	    	<table class="perdaGanhoGrid"></table>
		</div>
	</form>
		
    <div class="areaBts">
    	<div class="area">
    		
    		<span class="bt_novos bt_acoes_grid_ce" id="btnSalvarCE" >
		      	<a isEdicao="true" href="javascript:;" title="Salvar" rel="tipsy" title="Salvar">
		      		<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" id="imagemSalvarCE" />
		      	</a>
		    </span>
		    
    		<span class="bt_novos bt_acoes_grid_ce" id="btnFechamento" >
		      	<a isEdicao="true" href="javascript:;" title="Fechamento" rel="tipsy" title="Fechamento">
		      		<img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" id="imagemFechamento" />
		      	</a>
		    </span>
		      
		    <span class="bt_novos bt_acoes_grid_ce" id="btnReabertura">
		      	<a isEdicao="true" href="javascript:;" title="Reabertura" rel="tipsy">
		      		<img src="${pageContext.request.contextPath}/images/ico_expedicao_box.gif" hspace="5" border="0" id="imagemReabertura" />
		      	</a>
		    </span>
		
		    <span class="bt_novos bt_acoes_grid_ce" id="btnImpBoleto">
    
		    	<c:if test="${BOLETO}">
		    	
			    	<a isEdicao="true"	href="javascript:;"
			    		rel="tipsy" title="Imprimir Boleto">
			    		<img src="${pageContext.request.contextPath}/images/ico_negociar.png" id="imagemImpressaoBoleto" hspace="5" border="0" />
			    	</a>
		    	
		    	</c:if>	
		    </span>
		       
	       <span class="bt_novos bt_acoes_grid_ce" id="btnImpBoletoEmBranco">

				
				<c:if test="${BOLETO_EM_BRANCO}">
			       	<a isEdicao="true" href="javascript:;"		       	
			       	rel="tipsy" title="Imprimir Boleto em Branco">
			       		<img src="${pageContext.request.contextPath}/images/ico_detalhes.png" id="imagemBoletoEmBranco" hspace="5" border="0" />
			       	</a>
				</c:if>					       
	       
	       </span>
		   
		   <span class="bt_novos bt_acoes_grid_ce" id="btnImpressaoCE" >
		      	<a isEdicao="true" href="javascript:;" title="Imprimir CE Devolução" rel="tipsy">
		      		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" id="imagemImprimirCE" />
		      	</a>
		    </span>
		    
		       
		    <span class="bt_arq bt_acoes_grid_ce">
				  	<a isEdicao="true" href="${pageContext.request.contextPath}/devolucao/fechamentoCEIntegracao/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
				  		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					</a>
			</span>
		    <span class="bt_arq bt_acoes_grid_ce">
    			<a href="${pageContext.request.contextPath}/devolucao/fechamentoCEIntegracao/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
    				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
    			</a>
    		</span>
    	</div>
    </div>
    <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
		<legend> Pesquisar Fechamento  CE</legend>			
 	    	<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="69">Fornecedor:</td>    
				  	<td width="223">
						<select id="idFornecedor" name="idFornecedor" style="width:170px;">
							<option  value="">Todos</option>
							<c:forEach items="${listaFornecedores}" var="fornecedor">
								<option value="${fornecedor.key}">${fornecedor.value}</option>	
							</c:forEach>
						</select>
					</td>
				  	<td width="48">
				  		Semana:
				  	</td>
				  	<td width="151">
				  		<input type="text" id="semana" name="semana" onchange="fechamentoCEIntegracaoController.buscarCESemana();"/>
				  	</td>
				  	<td width="48">
				  		CE:
				  	</td>
				  	<td style="width:100px;">
				  	
						<select id="comboCE-fechamentoCe-integracao" style="width:130px;">
							<option value="-1" >Selecione</option>
							
						</select>
					</td>
					
			  		<td style="width:100px;">
						<select id="combo-fechamentoCe-integracao" style="width:130px;">
							<option value="-1" >Selecione</option>
							<option value="COM" selected="selected" >Com CE</option>
							<option value="SEM">Sem CE</option>
						</select>
					</td>
					<td style="width:20px;">
						
					</td>
					<td width="333">
			  			<span id="btnPesquisar">
			  				<a href="javascript:;" class="botaoPesquisar" >
			  					<img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" />
			  				</a>
			  			</span>
			  		</td>
				</tr>
			</table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="fieldGrid">
       	  <legend> Fechamento CE</legend>
          
	        <div class="grids" style="display:none;">
	          	<table class="fechamentoCeGrid"></table>
	          	<table class="fechamentoSemCeGrid"></table>
	       		<div class="linha_separa_fields">&nbsp;</div>
				<br clear="all" />
				<div class="tabelaTotal" style="display:none;">
					<table name="total" id="total" >
						<td width="88" valign="top"><strong>Total Bruto R$:</strong></td>
						<td width="50" valign="top" id="totalBruto"></td>
						<td width="106" valign="top"><strong>Total Desconto R$:</strong></td>
						<td width="49" valign="top" id="totalDesconto"></td>
						<td width="93" valign="top"><strong>Total Líquido R$:</strong></td>
						<td width="70" valign="top" id="totalLiquido"></td>					
					</table>
				</div>
			</div>

		</fieldset>

</body>
