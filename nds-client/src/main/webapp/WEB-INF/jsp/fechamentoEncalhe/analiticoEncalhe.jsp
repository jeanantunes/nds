<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/analiticoEncalhe.js"></script>
</head>
<body>

	<div class="areaBts">
		<div class="area">
			<span class="bt_arq"><a href="javascript:;" onclick="analiticoEncalheController.imprimirArquivo('XLS');" rel="tipsy" title="Gerar Arquivo"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a></span>
			<span class="bt_arq"><a href="javascript:;" onclick="analiticoEncalheController.imprimirArquivo('PDF');" rel="tipsy" title="Imprimir"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /> </a></span>
		</div>
	</div>
    <div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="fieldFiltro">
    	<legend> Pesquisar Fornecedor</legend>
    	<form id="formAnaliticoEncalhe">
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="75">Data Encalhe:</td>
				<td width="114"><input name="filtro.dataEncalhe" type="text" id="datepickerDe" style="width:80px;" value="${dataOperacao}" onchange="analiticoEncalheController.limpaGridPesquisa()" /></td>
				<td width="67">Fornecedor:</td>
				<td width="216">
					<select name="filtro.fornecedorId" id="selectFornecedor" style="width:200px;" onchange="analiticoEncalheController.limpaGridPesquisa()">
					<option value="">Selecione...</option>
					<c:forEach var="fornecedor" items="${listaFornecedores}">
						<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
					</c:forEach>
					</select>
				</td>
				<td width="97">Box de Encalhe:</td>
				<td width="239">
					<select name="filtro.boxId" id="selectBoxEncalhe" style="width:100px;" onchange="analiticoEncalheController.limpaGridPesquisa()">
					<option value="">Selecione...</option>
					<c:forEach var="box" items="${listaBoxes}">
						<option value="${box.id}">${box.nome}</option>
					</c:forEach>
					</select>
				</td>
				<td width="106"><span class="bt_novos"><a href="javascript:;" onclick="analiticoEncalheController.pesquisar();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
			</tr>
		</table>
		</form>
    </fieldset>


	<div class="linha_separa_fields">&nbsp;</div>
      

	<fieldset class="classFieldset">
    	<legend> Resultado Pesquisa</legend>
        <div class="grids" id="divAnaliticoEncalheGrid" style="display:none;">
			<table class="fechamentoAnaliticoGrid"></table>
	        
	        <table width="950" border="0" cellspacing="2" cellpadding="2">
	        	<tr>
	            	<td width="626">&nbsp;</td>
		            <td width="50"><strong>Total R$:</strong></td>
		            <td width="81">12.690,00</td>
		            <td width="69"><strong>Qtde Cotas:</strong></td>
		            <td width="92">4</td>
	          	</tr>
	        </table>
		</div>
	</fieldset>



	<script language="javascript" type="text/javascript">
	$(function(){
		analiticoEncalheController.init();
	});
	</script>

</body>