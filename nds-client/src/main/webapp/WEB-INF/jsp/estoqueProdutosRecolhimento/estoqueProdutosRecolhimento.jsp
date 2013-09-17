<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/estoqueProdutosRecolhimento.js"></script>

</head>

<body>

	<div class="areaBts">
		<div class="area">
			<span class="bt_arq" style="display: none;">
				<!-- ARQUIVO EXCEL -->
				<a href="${pageContext.request.contextPath}/estoqueProdutosRecolhimento/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
				<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a>
			</span>
			<span class="bt_arq" style="display: none;">
				<!-- ARQUIVO PDF -->
				<a href="${pageContext.request.contextPath}/estoqueProdutosRecolhimento/exportar?fileType=PDF" rel="tipsy" title="Imprimir Arquivo">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /></a>
			</span>	
		</div>
	</div>
    
    <div class="linha_separa_fields">&nbsp;</div>
    	<fieldset class="fieldFiltro">
   	    	<legend>Pesquisar Produtos em Recolhimento</legend>
   	    	<table width="400" border="0" class="filtro">
   	    		<tr>
   	    			<td width="80" nowrap="nowrap">Data Encalhe:</td>
   	    			<td width="113">
						<input id="dataRecolhimento" value="${dataRecolhimento}" 
							name="dataRecolhimento" type="text" style="width:70px;"/>
					</td>
					<td>
						<span class="bt_pesquisar">
							<a href="javascript:;" onclick="EstoqueProdutosRecolhimentoController.pesquisar();"></a>
						</span>
					</td>
				</tr>
			</table>
		</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset class="fieldGrid" style="display:none;">
		<legend>Produtos</legend>
		<table id="gridResultado"></table>
	</fieldset>
</body>