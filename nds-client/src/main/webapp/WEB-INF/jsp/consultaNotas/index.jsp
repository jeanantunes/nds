<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/consultaNotas.js"></script>
<title>Consulta de Notas</title>

<style type="text/css">
fieldset label 
{
	width: auto;
	margin-bottom: 0px !important;
}
</style>

</head>

<body>
	<form id="detalhe-nota-sem-fisico-form">
	<div id="dialog-novo" title="Detalhes da Nota">
	    <table id="notasSemFisicoDetalheGrid" class="notasSemFisicoDetalheGrid"></table>
		<br />

		<table width="569" border="0" cellspacing="2" cellpadding="2">
	      <tr style="font-size:11px;">
	        <td width="275" align="right"><strong>Total:</strong></td>
	        <td width="106" align="right">
	        	<span id="totalExemplares"></span>
	        </td>
	        <td width="168" align="right"> 
	        	<span id="totalSumarizado"></span>
	        </td>
	      </tr>
	    </table>
	</div>
	</form>
	
	<div class="container">

		<div id="effect" style="padding: 0 .7em;"
			class="ui-state-highlight ui-corner-all">
			<p>
				<span style="float: left; margin-right: .3em;"
					class="ui-icon ui-icon-info">
				</span> 
			</p>
		</div>

		<form name="formPesquisaNotas" id="formPesquisaNotas">
			
			<fieldset class="classFieldset">
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
							<input name="dataInicial" type="text" id="datepickerDe"
								   style="width: 80px; float: left; margin-right: 5px;" value="${dataAtual}" />
						</td>
						<td align="center">Até</td>
						<td>
							<input name="dataFinal" type="text" id="datepickerAte"
							 	   style="width: 80px; float: left; margin-right: 5px;" value="${dataAtual}" />
						</td>
					</tr>
					<tr>
						<td width="107">Tipo de Nota:</td>
						<td width="293">
						<select name="filtroConsultaNotaFiscal.idTipoNotaFiscal" id="selectTiposNotaFiscal" style="width: 250px;">
							<option selected="selected" value="-1"></option>
							<c:forEach items="${tiposNotaFiscal}" var="tipoNotaFiscal">
								<option value="${tipoNotaFiscal.id}">${tipoNotaFiscal.descricao}</option>
							</c:forEach>
							
						</select></td>
						<td width="95"><label for="notaRecebida" style="margin:0px">Nota Recebida</label></td>
						<td colspan="2">
						<select name="isNotaRecebida" id="selectNotaRecebida"
								style="width: 135px;">
								<option value="-1"></option>
								<option value="1">Sim</option>
								<option value="0">Não</option>
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
				<span class="bt_arquivo">
					<a href="${pageContext.request.contextPath}/estoque/consultaNotas/exportar?fileType=XLS">
						Arquivo
					</a>
				</span>
				<span class="bt_imprimir">
					<a href="${pageContext.request.contextPath}/estoque/consultaNotas/exportar?fileType=PDF">
						Imprimir
					</a>
				</span>
			</div>
		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>
	</div>
	
<script language="javascript" type="text/javascript">

$(function(){
	consultaNotasController.init();
});

</script>
	
</body>