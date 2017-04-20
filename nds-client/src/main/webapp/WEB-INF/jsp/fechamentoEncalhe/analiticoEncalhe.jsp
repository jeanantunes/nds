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

	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
    	<legend> Pesquisar Fornecedor</legend>
    	<form id="formAnaliticoEncalhe">
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="75">Data Encalhe:</td>
				<td width="100"><input name="filtro.dataEncalhe" type="text" id="analiticoEncalhe_dataEncalhe" style="width:80px;" value="" onchange="analiticoEncalheController.limpaGridPesquisa()" /></td>
				<td width="67">Fornecedor:</td>
				<td width="216">
					<select name="filtro.fornecedorId" id="selectFornecedor" style="width:200px;" onchange="analiticoEncalheController.limpaGridPesquisa()">
					<option value="">Selecione...</option>
					<c:forEach var="fornecedor" items="${listaFornecedores}">
						<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
					</c:forEach>
					</select>
				</td>
				<td></td>
			</tr>	
			<tr>	
				<td width="70">Box de Encalhe:</td>
				<td width="160">
					<select name="filtro.boxId" id="selectBoxEncalhe" style="width:160px;" onchange="analiticoEncalheController.limpaGridPesquisa()">
						<option value="">Selecione...</option>
						<c:forEach var="box" items="${listaBoxes}">
							<option value="${box.id}">${box.nome}</option>
						</c:forEach>
					</select>
				</td>
				
				<!-- 
				
				<td width="70">Vis&atilde;o:</td>
				<td width="100">
					<select name="filtro.visao" id="selectVisao" style="width:100px;">
					<c:forEach var="v" items="${listaVisao}">
						<option value="${v}">${v}</option>
					</c:forEach>
					</select>
				</td>
				-->
				<td width="70">&nbsp;</td>
				<td width="100">&nbsp;</td>
				<td width="106"><span class="bt_novos"><a href="javascript:;" onclick="analiticoEncalheController.pesquisar();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
			</tr>
		</table>
		
		</form>
    </fieldset>
    <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
    <legend> Historico de Encalhe</legend>
	    <form id="formHistoricoEncalhe">
			<div id="dialog-historico-detalhes" title="Historico de encalhe realizado" style="display:none;">
				<table width="600" border="0">
					<tr>
						<td width="40"><strong>Cota:</strong></td>
						<td width="40" id="histNumeroCota"></td>
						<td width="75"><strong>Nome:</strong></td>
						<td width="380" id="histNomeCota"></td>
					<tr>
				</table>
				<div class="linha_separa_fields">&nbsp;</div>
				<table class="dadosHistoricoEncalheGrid"></table>
			</div> 
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
		            <td width="50"><strong >Total R$:</strong></td>
		            <td width="81" id="valorTotalAnalitico" >0,00</td>
		            <td width="69"><strong>Qtde Cotas:</strong></td>
		            <td width="92" id="totalCotaAnalitico">0</td>
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