
<head>

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/resumoExpedicao.js"></script>
	
	<script type="text/javascript">
		$(function() {
			resumoExpedicaoController.inicializar();
		});
	</script>
</head>

<body>
	
	<form action="/expedicao/resumo" id="resumo_expedicao_form">
	
		<fieldset class="classFieldset">
		  
		  <legend id="idFiledResumo"> Pesquisar Resumo  Expedição por Box </legend>
		  
		  	 <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			  <tr>
			    <td width="116">Data Lançamento:</td>
			    <td width="145">
			    	<input type="text" name="dataLancamento" id="dataLancamento" style="width:80px;"/>
			    </td>
			    <td width="91">Tipo Consulta:</td>
			    <td width="180">
			        <select name="tipoPesquisa" id="tipoPesquisa" style="width: 200px;" onchange="resumoExpedicaoController.mudarLegendaFielsSet('idFiledResumo','pesquisar')">
						<option selected="selected"></option>
						<c:forEach var="tipoResumo" items="${listaTipoResumo}">
							<option value="${tipoResumo.key}">${tipoResumo.value}</option>
						</c:forEach>
					</select>
			     </td>
			    <td width="375">
			    	<span class="bt_pesquisar">
			    		<a href="javascript:;" onclick="resumoExpedicaoController.pesquisar();">Pesquisar</a>
			    	</span>
			    </td>
			  </tr>
		  	</table>
		</fieldset>
	
		<div class="linha_separa_fields">&nbsp;</div>
	
		<div id="grid" style="display:none;">
		
			<fieldset class="classFieldset">
			    
			    <legend id="idFiledResultResumo">Resumo  Expedição por Box</legend>
			
				    <div id="gridProduto" style="display:none;">
			    	<table id="resumoExpedicaoGridProduto" class="resumoExpedicaoGridProduto"></table>
			    </div> 
			    
			    <div id="gridBox" style="display:none;">
			    	<table id="resumoExpedicaoGridBox" class="resumoExpedicaoGridBox"></table>
			    </div>
				
				<table width="950" border="0" cellspacing="1" cellpadding="1">
					  <tr>
					  	<td width="658">
						    <span class="bt_novos" title="Gerar Arquivo">
						    	<a href="javascript:;" onclick="resumoExpedicaoController.exportar('XLS');">
						    		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
						    		Arquivo
						    	</a>
						    </span>
						    <span class="bt_novos" title="Imprimir">
						    	<a href="javascript:;" onclick="resumoExpedicaoController.exportar('PDF');">
						    		<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
							    		Imprimir
							    	</a>
							    </span>
						    </td>
						    <td width="86"><strong>Total:</strong></td>
						    <td width="70" id="totalReparte"></td>
						    <td width="160"><strong>Total Valor Faturado R$:</strong></td>
						    <td width="89" id="totalValorFaturado"></td>
						  </tr>
					</table>
			</fieldset>
		</div>
	</form>
	
	<form action="/expedicao/resumo" id="venda_encalhe_resumo_expedicao_form">
	
		<div id="dialog-venda-encalhe" style="display:none;">
		
			<fieldset class="classFieldset">
			
				<legend id="idFiledResultResumo" style="color: #000000;">
					<strong>C&oacute;digo:</strong>
					<span  id="box-resumo-expedicao" ></span>
					- &nbsp;<strong>Box:</strong>
					<span id="nome-box-resumo-expedicao"></span>
				</legend>
			    
				<table id="venda-encalhe-grid"></table>
				
				<table width="100%" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td></td>
		    			<td>
		    				<span class="bt_novos" title="Gerar Arquivo">
		    					<a href="javascript:;" onclick="resumoExpedicaoController.exportarDetalhes('XLS');">
		    						<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo
		    					</a>
		    				</span>
		    				<span class="bt_novos" title="Imprimir">
		    					<a href="javascript:;" onclick="resumoExpedicaoController.exportarDetalhes('PDF');">
		    						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir
		    					</a>
		    				</span>
		    			</td>
		    			<td>
		    				<strong>Total R$:</strong>
		    			</td>
		    			<td id="valorTotal"></td>
		  			</tr>
				</table>
			</fieldset>
		</div>
	</form>
	
</body>