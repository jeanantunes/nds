<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>STG - Sistema Treelog de Gestão</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/entradaNFETerceiros.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
	
<script language="javascript" type="text/javascript">
	$(function() {
		entradaNFETerceirosController.init();
	});
</script>
<style type="text/css">
#dialog-nfe, #dialog-dadosNotaFiscal {
	display: none;
}

.dados,.dadosFiltro,.nfes {
	display: none;
}

#dialog-novo,#dialog-alterar,#dialog-excluir,#dialog-rejeitar,#dialog-confirm
	{
	display: none;
	font-size: 12px;
}

fieldset label {
	width: auto;
	margin-bottom: 0px !important;
}

#dialog-dadosNotaFiscal fieldset {
	width: 810px !important;
}
</style>
</head>

<body>
	<form id="form-dadosNotaFiscal">
	<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
		<div id="dialog-dadosNotaFiscal" title="Dados da Nota Fiscal">
			<fieldset>
				<legend>Nota Fiscal</legend>
				<table width="670" border="0" cellspacing="1" cellpadding="1"
					style="color: #666;">
					<tr>
						<td width="133">Núm. Nota Fiscal:</td>
						<td width="307" id="numeroNotaFiscalPopUp"></td>
						<td width="106">Série:</td>
						<td width="111" id="serieNotaFiscalPopUp"></td>
					</tr>
					<tr>
						<td>Data:</td>
						<td id="dataNotaFiscalPopUp"></td>
						<td>Valor Total R$:</td>
						<td id="valorNotaFiscalPopUp"></td>
					</tr>
					<tr>
						<td>Chave de Acesso:</td>
						<td colspan="3" id="chaveAcessoNotaFiscalPopUp"></td>
					</tr>
				</table>
			</fieldset>
			<br clear="all" /> <br />

			<fieldset>
				<legend>Produtos Nota Fiscal</legend>
				<table class="pesquisarProdutosNotaGrid"></table>
			</fieldset>
		</div>
	</form>
	<form id="form-nfe">
		<div id="dialog-nfe" title="NF-e">
			<fieldset style="width: 310px !important;">
				<legend>Incluir NF-e</legend>
				<table width="280" border="0" cellspacing="1" cellpadding="0">
					<tr>
						<td width="84">Cota:</td>
						<td width="193">
							<input type="text" id="cotaCadastroNota" name="cotaCadastroNota" style="width: 80px; float: left; margin-right: 5px;" /> 
							<span class="classPesquisar">
								<a href="javascript:;" onclick="entradaNFETerceirosController.pesquisarCotaCadastroNota();">&nbsp;</a>
							</span>
						</td>
					</tr>
					<tr>
						<td>Nome:</td>
						<td>
							<input type="text" name="nomeCotaCadastroNota" id="nomeCotaCadastroNota" />
						</td>
					</tr>
					<tr>
						<td>NF-e:</td>
						<td>
							<input type="text" name="numeroNotaCadastroNota" id="numeroNotaCadastroNota" />
						</td>
					</tr>
					<tr>
						<td>Série:</td>
						<td>
							<input type="text" name="serieNotaCadastroNota" id="serieNotaCadastroNota" />
						</td>
					</tr>
					<tr>
						<td>Chave-Acesso:</td>
						<td>
							<input type="text" name="chaveAcessoCadastroNota" id="chaveAcessoCadastroNota" />
						</td>
					</tr>
					<tr>
						<td>Valor Nota R$:</td>
						<td>
							<input type="text" name="valorNotaCadastroNota" id="valorNotaCadastroNota" />
						</td>
					</tr>
				</table>
			</fieldset>
		</div>
	</form>

	<form id="form-confirmar-cancelamento">
		<div id="dialog-confirmar-cancelamento" title="Cancelamento de NF-e" style="display: none;">
			<p>Confirma o cancelamento da NF-e?</p>
		</div>
	</form>

	<form id="form-confirm">
		<div id="dialog-confirm" title="Aprovar Solicitação">
			<p>Você esta Aprovando uma Solicitação, tem certeza?</p>
		</div>
	</form>

	<form id="form-rejeitar">
		<div id="dialog-rejeitar" title="Rejeitar Solicitação">
			<p>Tem certeza que deseja Rejeitar esta Solicitação?</p>
		</div>
	</form>

	<form id="form-novo">
		<div id="dialog-novo" title="Geração arquivos Nf-e">
			<p>Confirma a Geração arquivos Nf-e?</p>
		</div>
	</form>

	<div class="areaBts">
		<div class="area">

			<span class="bt_novos" id="btnRegistrarNFe" style="display:none">
			<a href="javascript:;" onclick="entradaNFETerceirosController.popup_nfe('0','0');" rel="tipsy" title="Registrar NF-e">
				<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_add_novo.gif">
			</a>
			</span> 
			<span class="bt_arq"> 
			<a href="${pageContext.request.contextPath}/nfe/entradaNFETerceiros/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
				 <img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
			</a>
			</span>
			<span class="bt_arq"> 
			<a href="${pageContext.request.contextPath}/nfe/entradaNFETerceiros/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
			</a>
			</span>
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>

	<form id="form-pesquisa-nfe">
		<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
			<legend> Pesquisa NF-e</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="75">Fornecedor:</td>
					<td width="200">
						<select id="entrada-terceiros-selectFornecedoresDestinatarios"  name="filtro.listIdFornecedor.id" multiple="multiple" style="width:180px">
							<c:forEach items="${listFornecedores}" var="fornecedor">
								<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
							</c:forEach>
						</select>
					</td>
					<td width="50">Cota:</td>

					<td width="180"><input type="text" id="entrada-terceiro-numeroCota"
						name="filtro.cota.numeroCota"
						style="width: 80px; float: left; margin-right: 5px;"
						onchange="entradaNFETerceirosController.pesquisarCota();" /></td>
					<td colspan="3">Nome: <span id="nomeCota"></span></td>
				</tr>
				<tr>
					<td>Per&iacute;odo:</td>
					<td>
						<input name="filtro.dataInicial" type="text" id="entradaNFETerceiros-dataInicial" style="width: 70px; " /> 
						At&eacute;: 
						<input name="filtro.dataFinal" type="text" id="entradaNFETerceiros-dataFinal" style="width: 70px;" />
					</td>
					<td width="34">Status:</td>
					<td width="165">
						<select name="filtro.statusNotaFiscalEntrada" id="situacaoNfe" style="width: 160px;" onchange="mostra_status(this.value); entradaNFETerceirosController.pesquisarEncalhe();">
							<option value=""selected="selected">Selecione...</option>
							<c:forEach items="${comboStatusNota}" var="comboStatusNota">
								<option value="${comboStatusNota.key}">${comboStatusNota.value}</option>
							</c:forEach>
						</select>
					<td width="76">Tipo de Nota:</td>
					<td width="211">
						<select name="filtro.tipoNota" id="tipoNota" style="width: 160px;">
							<option value=""selected="selected">Selecione...</option>
							<c:forEach items="${tiposNotas}" var="tiposNotas">
								<option value="${tiposNotas.key}">${tiposNotas.value}</option>
							</c:forEach>
						</select>
					</td>
					<td width="131">
						<span class="bt_pesquisar">
							<a href="javascript:;" onclick="entradaNFETerceirosController.pesquisarEncalhe();" />
						</span>
					</td>
				</tr>
			</table>
		</fieldset>
	</form>
	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="fieldGrid">
		<legend>NF-e</legend>
		<div class="grids" style="display: none;">
			<div id="notaRecebida" style="display: none;">
				<table class="notaRecebidaGrid"></table>
			</div>

			<div id="pendenteRecEmissao" style="display: none;">
				<table class="encalheNfeGrid"></table>
			</div>
		</div>
	</fieldset>
</body>
</html>
