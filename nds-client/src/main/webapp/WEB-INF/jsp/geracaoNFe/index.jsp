<input id="permissaoAlteracao" type="hidden"
	value="${permissaoAlteracao}">

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/geracaoNFe.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/utils.js"></script>

<style type="text/css">
fieldset label {
	width: auto;
	margin-bottom: 0px !important;
}

#dialogCotasSuspensas fieldset {
	width: 570px !important;
}
</style>

<script type="text/javascript">
$(function(){
	geracaoNFeController.init();
	bloquearItensEdicao(geracaoNFeController.workspace);
});
</script>

<div id="geracaoNfe-dialog-cotasSuspensas"
	title="Gera&ccedil;&atilde;o NF-e">
	<form>
		<fieldset>
			<legend>Cotas Suspensas</legend>
			<table id="geracaoNfe-gridCotasSuspensas"></table>
			<span class="bt_sellAll" style="float: right;"> <label
				for="checkboxCheckAllCotasSuspensas">Selecionar Todos</label> <input
				type="checkbox" name="Todos"
				id="geracaoNfe-checkboxCheckAllCotasSuspensas" style="float: left;" />
			</span>
		</fieldset>
	</form>
</div>

<div id="geracaoNfe-dialog-cotasSuspensasConfirmar"
	title="Transferência para Suplementar">
	<form>
		<fieldset>
			<legend>Transferência para Suplementar</legend>
			<p>O reparte das cotas selecionadas serão transferidos para Suplementar</p>
		</fieldset>
	</form>
</div>
<div class="areaBts">
	<div class="area">
		<span class="bt_novos"><a isEdicao="true" href="javascript:;"
			id="geracaoNfe-btnGerar" rel="tipsy"
			title="Confirma  Gera&ccedil;&atilde;o de Nf-e?"><img
				src="${pageContext.request.contextPath}/images/ico_check.gif"
				width="16" height="16" border="0" hspace="5" /></a></span> <span
			class="bt_arq"><a href="javascript:;"
			id="geracaoNfe-btnImprimirXLS" rel="tipsy" title="Gerar Arquivo"><img
				src="${pageContext.request.contextPath}/images/ico_excel.png"
				hspace="5" border="0" /></a></span> <span class="bt_arq"><a
			href="javascript:;" id="geracaoNfe-btnImprimirPDF" rel="tipsy"
			title="Imprimir"><img
				src="${pageContext.request.contextPath}/images/ico_impressora.gif"
				alt="Imprimir" hspace="5" border="0" /></a></span>
	</div>
</div>
<div class="linha_separa_fields">&nbsp;</div>
<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
	<legend> Pesquisar NF-e</legend>
	<form>
		<table width="1000px" border="0" cellpadding="2" cellspacing="1"
			class="filtro">
			<tr>
				<td width="91">Destinat&aacute;rio:</td>
				<td width="204"><c:forEach items="${tiposDestinatarios}"
						var="tipoDestinatario" varStatus="status">
						<input type="radio" name="tipoDestinatario"
							id="tipoDestinatario${status.index}" value="${tipoDestinatario}"
							<c:if test="${status.index == 0}">checked="checked"</c:if>
							onchange="geracaoNFeController.verificarTipoDestinatario(this);" /> ${tipoDestinatario.descricao}
    					</c:forEach></td>
				<td colspan=2><select
					id="geracaoNfe-filtro-selectFornecedoresDestinatarios"
					name="selectFornecedores" multiple="multiple" style="width: 300px">
						<c:forEach items="${fornecedoresDestinatarios}" var="fornecedor">
							<option value="${fornecedor.key }">${fornecedor.value }</option>
						</c:forEach>
				</select></td>
				<td width="80"><span style="display:none;" class="emissaoRegimeEspecial">Emitir: </span></td>
				<td width="210">
					<div style="display:none;" class="emissaoRegimeEspecial">
					<select
						id="geracaoNfe-filtro-selectRegimeEspecialConsolidado"
						name="selectRegimeEspecialConsolidado" style="display:none; width: 200px;">
						<c:forEach items="${tiposEmissaoRegimeEspecial}" var="tipoEmissaoRegimeEspecial">
							<option value="${tipoEmissaoRegimeEspecial}">${tipoEmissaoRegimeEspecial.descricao }</option>
						</c:forEach>
					</select>
				</div>
				</td>
			</tr>
			<tr>
				<td width="100">Nat. de Opera&ccedil;&atilde;o:</td>
				<td width="195"><select id="geracaoNfe-filtro-naturezaOperacao"
					name="naturezaOperacao" onchange="geracaoNFeController.verificarRegimeEspecialNaturezaOperacao(this)"
					style="width: 250px; font-size: 11px !important">
						<option value="-1">Selecione...</option>

				</select></td>
				<td width="95">Data Movimento:</td>
				<td width="237">
					<input type="text" id="geracaoNfe-filtro-movimentoDe" name="movimentoDe" style="width: 76px;" class="input-date" />&nbsp;&nbsp;At&eacute;&nbsp;
					<input type="text" id="geracaoNfe-filtro-movimentoAte" name="movimentoAte" style="width: 76px;" class="input-date" />
				</td>
				<td width="80">Data Emiss&atilde;o:</td>
				<td width="212">
					<input name="geracaoNfe-filtro-dataEmissao" type="text" id="geracaoNfe-filtro-dataEmissao" style="width: 80px;" class="input-date" />
				</td>
			</tr>
			<tr>
				<td>Roteiro:</td>
				<td><select id="geracaoNfe-filtro-selectRoteiro"
					onchange="geracaoNFeController.changeRoteiro();"
					style="width: 200px; font-size: 11px !important">
						<option value="">Selecione...</option>
						<c:forEach items="${roteiros}" var="roteiro">
							<option value="${roteiro.key }">${roteiro.value }</option>
						</c:forEach>
				</select></td>
				<td>Rota:</td>
				<td><select id="geracaoNfe-filtro-selectRota"
					onchange="geracaoNFeController.changeRota();"
					style="width: 150px; font-size: 11px !important">
						<option value="">Selecione...</option>
						<c:forEach items="${rotas}" var="rota">
							<option value="${rota.key }">${rota.value }</option>
						</c:forEach>
				</select></td>
				<td>Cota de:</td>
				<td><input type="text"
					id="geracaoNfe-filtro-inputIntervaloCotaDe"
					name="inputIntervaloCotaDe" style="width: 80px;" />&nbsp;At&eacute;&nbsp;
					<input type="text" id="geracaoNfe-filtro-inputIntervaloCotaAte"
					name="inputIntervaloCotaAte" style="width: 80px;" /></td>
			</tr>
			<tr>
				<td>Intervalo Box:</td>
				<td><select name="geracaoNfe-filtro-boxDe"
					id="geracaoNfe-filtro-inputIntervaloBoxDe"
					onchange="geracaoNFeController.changeBox();" style="width: 100px;">
						<option value="" selected="selected">Selecione...</option>
						<c:forEach var="box" items="${listaBox}">
							<option value="${box.key}">${box.value}</option>
						</c:forEach>
				</select> &nbsp;Até &nbsp; <select name="geracaoNfe-filtro-boxAte"
					id="geracaoNfe-filtro-inputIntervaloBoxAte"
					onchange="geracaoNFeController.changeBox();" style="width: 100px;">
						<option value="" selected="selected">Selecione...</option>
						<c:forEach var="box" items="${listaBox}">
							<option value="${box.key}">${box.value}</option>
						</c:forEach>
				</select></td>
				<td>Fornecedor:</td>
				<td colspan="3"><select
					id="geracaoNfe-filtro-selectFornecedores" name="selectFornecedores"
					multiple="multiple" style="width: 400px">
						<c:forEach items="${fornecedores}" var="fornecedor">
							<option value="${fornecedor.key }">${fornecedor.value }</option>
						</c:forEach>
				</select></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>
					<span class="bt_pesquisar">
						<a href="javascript:;" id="geracaoNfe-filtro-btnPesquisar" onclick="geracaoNFeController.pesquisar();"></a>
					</span>
				</td>
			</tr>
		</table>
	</form>
</fieldset>
<div class="linha_separa_fields">&nbsp;</div>

<fieldset class="fieldGrid">
	<legend>Gera&ccedil;&atilde;o NF-e</legend>
	<!--
	<div class="grids" style="display: none;">
		<table id="geracaoNfe-gridNFe"></table>
	</div>
	-->

	<div id="geracaoNfe-gridNFe" style="display: none;" class="grids">
		<div id="geracaoNfe-flexigrid-pesquisa" />
	</div>
	<div id="geracaoNfe-gridNFe" style="display: none;" class="grids-forn">
		<div id="geracaoNfe-flexigrid-fornecedor-pesquisa" />
	</div>

</fieldset>