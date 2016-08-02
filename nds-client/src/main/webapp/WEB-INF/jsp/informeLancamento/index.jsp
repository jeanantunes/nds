<html>
<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery.form.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.button.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery-upload/js/vendor/jquery.ui.widget.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery-upload/js/jquery.iframe-transport.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery-upload/js/jquery.fileupload.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/informeLancamento.js" type="text/javascript"></script>

<script type="text/javascript">
$(function(){
	informeLancamentoController.init();
});
</script>

</head>
<body>

<div class="linha_separa_fields">&nbsp;</div>
<div class="linha_separa_fields">&nbsp;</div>

	<div class="areaBts">
		<div class="area">
			<span class="bt_arq">
				<a href="javascript:;" id="btnImprimir_infoLanc" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" alt="" />
				</a>
			</span>
			<span class="bt_arq">
				<a href="javascript:;" id="btnExportar_infoLanc" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
			</span>
		</div>
	</div>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
		<legend> Pesquisar Informe de Lan&ccedil;amento</legend>
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="74">Fornecedor:</td>
				<td width="210">
					<select name="select" id="idFornecdorSelect_infoLanc" style="width: 200px;">
						<option value="" selected="selected">Todos</option>
						<c:forEach items="${fornecedores}" var="fornecedor">
							<option value="${fornecedor.key}">${fornecedor.value}</option>
						</c:forEach>
					</select>
				</td>

				<td colspan="3">Semana:</td>

				<td width="100">
					<input type="text" id="semanaLancamentoBox_infoLanc" style="width: 70px;" />
				</td>
				
				<td colspan="3">Sugerir Semana:</td>

				<td width="50">
					<input type="checkbox" id="sugerirSemana_infoLanc" />
				</td>
				
				<td colspan="3">Data Lan&ccedil;amento:</td>
				
				<td width="150">
					<input type="text" id="dataLancamentoBox_infoLanc" value="${data}" style="width: 70px;" />
				</td>

				<td width="105">
					<span class="bt_novos">
						<a href="javascript:;" id="btnPesquisar_infoLanc">
							<img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" />
						</a>
					</span>
				</td>

			</tr>
		</table>
	</fieldset>

	<div class="linha_separa_fields">&nbsp;</div>
	
	<div class="grids">
		<fieldset class="fieldInformeLancamentoGrid" style="display:none;">
			<legend>Informe de Lan&ccedil;amentos Cadastrados</legend>
			<table id="informeLancamentoGrid" ></table>		
		</fieldset>
	</div>
	
	<div id="dialog-ConfirmarUpdate" title="Atualizar dados do produto" style="display: none;">
			<p>Deseja atualizar os dados da edi&ccedil;&atilde;o?</p>
		</div>
	
</body>
</html>