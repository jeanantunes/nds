<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/consultaNotas.js"></script>
<title>Consulta de Notas</title>

<style type="text/css">
fieldset label 
{
	width: auto;
	margin-bottom: 0px !important;
}

.gridLinhaDestacada {
  background:#BEBEBE; 
  font-weight:bold; 
  color:#fff;
}

.gridLinhaDestacada:hover {
   color:#000;
}

.gridLinhaDestacada a {
   color:#fff;
}

.gridLinhaDestacada a:hover {
   color:#000;
}
</style>

</head>

<body>
	
	
	<div class="areaBts" style="display: none">
		<div class="area">
		
			<span class="bt_arq">
				<a href="${pageContext.request.contextPath}/estoque/consultaNotas/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
			</span>
			
			<span class="bt_arq">
				<a href="${pageContext.request.contextPath}/estoque/consultaNotas/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
			</span>
		</div>
	</div>

	<div class="linha_separa_fields">&nbsp;</div>
		
	<form id="detalhe-nota-sem-fisico-form">
	<div id="dialog-novo" title="Detalhes da Nota">
	    <table id="notasSemFisicoDetalheGrid" class="notasSemFisicoDetalheGrid"></table>
		<br />

		<table width="830" border="0" cellspacing="2" cellpadding="2">
	      <tr style="font-size:11px;" align="right">
	        <td width="275" align="right"><strong>Total Exemplares:</strong></td>
	        <td width="106" align="right">
	        	<span id="totalExemplares"></span>
	        </td>
	        <td width="275" align="right"><strong>Valor Total:</strong></td>
	        <td width="168" align="right"> 
	        	<span id="totalSumarizado"></span>
	        </td>
	        <td width="275" align="right"><strong>Total C/Desc:</strong></td>
	        <td width="168" align="right"> 
	        	<span id="totalSumarizadoComDesconto"></span>
	        </td>
	      </tr>
	    </table>
	</div>
	</form>
	

		<div id="effect" style="padding: 0 .7em;"
			class="ui-state-highlight ui-corner-all">
			<p>
				<span style="float: left; margin-right: .3em;"
					class="ui-icon ui-icon-info">
				</span> 
			</p>
		</div>

		<form name="formPesquisaNotas" id="formPesquisaNotas">
			
			<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados" style="margin-top: 0px">
				<legend> Pesquisar Nota </legend>
				<table width="950" border="0" cellpadding="2" cellspacing="1"
					   class="filtro">
					<tr>
						<td>Fornecedor:</td>
						<td>
							<select name="filtroConsultaNotaFiscal.idFornecedor" id="selectFornecedores" style="width: 250px;">
								<option selected="selected" value="-1"></option>
								<c:forEach items="${fornecedores}" var="fornecedor">
									<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
								</c:forEach>
							</select>
						</td>
	
						<td>Período</td>
						<td width="46">de:</td>
						<td width="120">
							<input name="dataInicial" type="text" id="dataNFDe"
								   style="width: 80px; float: left; margin-right: 5px;" value="${dataAtual}" />
						</td>
						<td align="center">Até</td>
						<td>
							<input name="dataFinal" type="text" id="dataNFAte"
							 	   style="width: 80px; float: left; margin-right: 5px;" value="${dataAtual}" />
						</td>
					</tr>
					<tr>
					
						<td width="107"></td>
						<td width="293">
						</td>
						
						<td width="95"><label for="notaRecebida" style="margin:0px">Nota Recebida</label></td>
						<td colspan="2">
						<select name="filtroConsultaNotaFiscal.notaRecebida" id="selectNotaRecebida"
								style="width: 135px;">
								<option value="TODAS">Todas</option>
								<option value="SOMENTE_NOTAS_RECEBIDAS">Sim</option>
								<option value="SOMENTE_NOTAS_NAO_RECEBIDAS">Não</option>
								<option value="NOTAS_NAO_RECEBIDAS_COM_NOTA_DE_ENVIO">Apenas nota de envio</option>
						</select></td>
						<td width="31">&nbsp;</td>
						<td width="222">
							<span class="bt_pesquisar">
								<a href="javascript:;" onclick="consultaNotasController.pesquisarNotas()" id="btnPesquisar">Pesquisar</a>
							</span>
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
			<legend>Notas Cadastradas</legend>
			<div class="grids" style="display: none;">
				<table class="notasSemFisicoGrid" id="notasSemFisicoGrid"></table>
			</div>
		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>

	
<script language="javascript" type="text/javascript">

$(function(){
	consultaNotasController.init();
});

</script>
	
</body>