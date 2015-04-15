<input id="permissaoAlteracao" type="hidden"
	value="${permissaoAlteracao}" />
<head>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/pesquisaEditor.js"></script>	
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
<script language="javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery.multiselects-0.3.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery.justPercent.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/cadastroTipoDesconto.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/cadastroTipoDescontoDistribuidor.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/cadastroTipoDescontoCota.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/cadastroTipoDescontoEditor.js"></script>	
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/cadastroTipoDescontoProduto.js"></script>

<script language="javascript" type="text/javascript">
	var pesquisaCotaTipoDescontoCota = new PesquisaCota(tipoDescontoController.workspace);
	
	var pesquisaEditorTipoDescontoCota = new PesquisaEditor(tipoDescontoController.workspace);

	var pesquisaProdutoTipoDescontoCota = new PesquisaProduto(tipoDescontoController.workspace);

	$(function() {
		tipoDescontoController.init();
		descontoDistribuidorController.init();
		descontoCotaController.init();
		descontoEditorController.init(pesquisaCotaTipoDescontoCota);
		descontoProdutoController.init(pesquisaCotaTipoDescontoCota);
		bloquearItensEdicao(tipoDescontoController.workspace);
	});
</script>

</head>

<body>

	<form action="/administracao/naturezaOperacao"
		id="dialog-excluirCota_form">
		<div id="dialog-excluirCota" title="Atenção" style="display: none">
			<p>Confirmar exclusão Desconto ?</p>
		</div>
	</form>

	<!-- Modal de inclusão de novo desconto Geral  -->
	<form action="/administracao/naturezaOperacao" id="dialog-geral_form">
		<jsp:include page="novoDescontoGeral.jsp" />
	</form>

	<!-- Modal de inclusão de novo desconto Especifico  -->
	<form action="/administracao/naturezaOperacao"
		id="dialog-especifico_form">
		<jsp:include page="novoDescontoEspecifico.jsp" />
	</form>
	
	<!-- Modal de inclusão de novo desconto Especifico  -->
	<form action="/administracao/cadastroTipoNota"
		id="dialog-editor_form">
		<jsp:include page="novoDescontoEditor.jsp" />
	</form>

	<!-- Modal de inclusão de novo desconto Produto  -->
	<jsp:include page="novoDescontoProduto.jsp" />


	<form action="/administracao/naturezaOperacao"
		id="dialog-fornecedores_form">
		<div id="dialog-fornecedores" title="Fornecedores"
			style="display: none;">
			<fieldset style="width: 350px !important;">
				<legend>Fornecedores</legend>
				<table class="lstFornecedoresGrid"></table>
			</fieldset>
		</div>
	</form>
	
	<form action="" id="dialog-cotas-editor_form">
		<div id="dialog-cotas-editor" title="Cotas" style="display: none;">
			<fieldset style="width: 350px !important;">
				<legend>Cotas</legend>
				<table class="lstCotasEditorGrid"></table>
			</fieldset>
		</div>
	</form>

	<form action="/administracao/naturezaOperacao"
		id="dialog_consulta_tipo_desconto_form">
		<div class="areaBts">
			<div class="area">
				<div id="panelBtsGERAL" style="display: none;">
					<span class="bt_novos"> <a isEdicao="true"
						href="javascript:;"
						onclick="descontoDistribuidorController.popup_geral();"
						rel="tipsy" title="Incluir Novo"> <img
							src="${pageContext.request.contextPath}/images/ico_salvar.gif"
							hspace="5" border="0" />
					</a>
					</span>
				</div>
				<div id="panelBtsESPECIFICO" style="display: none;">
					<span class="bt_novos"> <a isEdicao="true"
						href="javascript:;"
						onclick="descontoCotaController.popup_especifico();"
						rel="tipsy" title="Incluir Novo"> <img
							src="${pageContext.request.contextPath}/images/ico_salvar.gif"
							hspace="5" border="0" />
					</a>
				</div>
				<div id="panelBtsEDITOR" style="display: none;">
					<span class="bt_novos"> <a isEdicao="true"
						href="javascript:;"
						onclick="descontoEditorController.popup_editor();"
						rel="tipsy" title="Incluir Novo"> <img
							src="${pageContext.request.contextPath}/images/ico_salvar.gif"
							hspace="5" border="0" />
					</a>
				</div>
				<div id="panelBtsPRODUTO" style="display: none;">
					<span class="bt_novos"> <a isEdicao="true"
						href="javascript:;"
						onclick="descontoProdutoController.popup_produto();"
						rel="tipsy" title="Incluir Novo"> <img
							src="${pageContext.request.contextPath}/images/ico_salvar.gif"
							hspace="5" border="0" />
					</a>
					</span>
				</div>
				
				<div id="idExportacaoESPECIFICO" style="display: none;">
					</span> <span class="bt_arq"> <a
						href="${pageContext.request.contextPath}/financeiro/tipoDescontoCota/exportar?fileType=XLS&tipoDesconto=ESPECIFICO"
						rel="tipsy" title="Gerar Arquivo"> <img
							src="${pageContext.request.contextPath}/images/ico_excel.png"
							hspace="5" border="0" />
					</a>
					</span> <span class="bt_arq"> <a
						href="${pageContext.request.contextPath}/financeiro/tipoDescontoCota/exportar?fileType=PDF&tipoDesconto=ESPECIFICO"
						rel="tipsy"> <img
							src="${pageContext.request.contextPath}/images/ico_impressora.gif"
							alt="Imprimir" hspace="5" border="0" />
					</a>
					</span>
				</div>
				
				<div id="idExportacaoGERAL" style="display: none;">
					<span class="bt_arq"> <a
						href="${pageContext.request.contextPath}/financeiro/tipoDescontoCota/exportar?fileType=XLS&tipoDesconto=GERAL"
						rel="tipsy" title="Gerar Arquivo"> <img
							src="${pageContext.request.contextPath}/images/ico_excel.png"
							hspace="5" border="0" />
					</a>
					</span>
					<span class="bt_arq"> <a
						href="${pageContext.request.contextPath}/financeiro/tipoDescontoCota/exportar?fileType=PDF&tipoDesconto=GERAL"
						rel="tipsy"> <img
							src="${pageContext.request.contextPath}/images/ico_impressora.gif"
							alt="Imprimir" hspace="5" border="0" />
					</a>
					</span></div>
				
				<div id="idExportacaoPRODUTO" style="display: none;">
					<span class="bt_arq"> <a
						href="${pageContext.request.contextPath}/financeiro/tipoDescontoCota/exportar?fileType=XLS&tipoDesconto=PRODUTO"
						rel="tipsy" title="Gerar Arquivo"> <img
							src="${pageContext.request.contextPath}/images/ico_excel.png"
							hspace="5" border="0" />
					</a>
					</span>
					<span class="bt_arq"> <a
						href="${pageContext.request.contextPath}/financeiro/tipoDescontoCota/exportar?fileType=PDF&tipoDesconto=PRODUTO"
						rel="tipsy"> <img
							src="${pageContext.request.contextPath}/images/ico_impressora.gif"
							alt="Imprimir" hspace="5" border="0" />
					</a>
					</span></div>



			</div>
		</div>
		<div class="linha_separa_fields">&nbsp;</div>
		<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
			<legend> Pesquisar Tipo de Desconto Cota</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1"
				class="filtro">
				<tr>
					<td width="20"><input type="radio" name="radio"
						id="radioGeral" value="radio"
						onclick="tipoDescontoController.mostra_geral();" /></td>
					<td width="47">Geral</td>
					<td width="20"><input type="radio" name="radio"
						id="radioEditor" value="radio"
						onclick="tipoDescontoController.mostra_editor();" /></td>
					<td width="65">Editor</td>
					<td width="20"><input type="radio" name="radio"
						id="radioProduto" value="radio"
						onclick="tipoDescontoController.mostra_produto();" /></td>
					<td width="48">Produto</td>
					<td width="20"><input type="radio" name="radio"
						id="radioEspecifico" value="radio"
						onclick="tipoDescontoController.mostra_especifico();" /></td>
					<td width="65">Específico</td>
					<td width="585">
						<div class="especifico" style="display: none">

							<label style="width: auto !important;">Cota:</label> <input
								name="numCotaPesquisa" id="numCotaPesquisa" type="text"
								maxlength="11" style="width: 70px; float: left;"
								onchange="pesquisaCotaTipoDescontoCota.pesquisarPorNumeroCota('#numCotaPesquisa', '#descricaoCotaPesquisa',false,
			           	  											null, 
			           	  											null);" />
							<label style="width: auto !important;">Nome:</label> <input
								name="descricaoCotaPesquisa" id="descricaoCotaPesquisa"
								type="text" class="nome_jornaleiro" maxlength="255"
								style="width: 200px; float: left;"
								onkeyup="pesquisaCotaTipoDescontoCota.autoCompletarPorNome('#descricaoCotaPesquisa');"
								onblur="pesquisaCotaTipoDescontoCota.pesquisarPorNomeCota('#numCotaPesquisa', '#descricaoCotaPesquisa', false,
													      			null,
													      			null);" />

						</div>
						
						<div class="editor" style="display: none">

							<label style="width: auto !important;">Editor:</label> <input
								name="codigoEditorPesquisa" id="codigoEditorPesquisa" type="text"
								maxlength="11" style="width: 70px; float: left;"
								onchange="pesquisaEditorTipoDescontoCota.pesquisarPorCodigoEditor('#codigoEditorPesquisa', '#descricaoEditorPesquisa', false,
			           	  											null, 
			           	  											null);" />
							<label style="width: auto !important;">Nome:</label> <input
								name="descricaoEditorPesquisa" id="descricaoEditorPesquisa"
								type="text" class="nome_jornaleiro" maxlength="255"
								style="width: 200px; float: left;"
								onkeyup="pesquisaEditorTipoDescontoCota.autoCompletarPorNome('#descricaoEditorPesquisa');"
								onblur="pesquisaEditorTipoDescontoCota.pesquisarPorNomeEditor('#codigoEditorPesquisa', '#descricaoEditorPesquisa', false,
													      			null,
													      			null);" />

						</div>

						<div class="produto" style="display: none">
							<label style="width: auto !important;">Código:</label> <input
								type="text" name="codigoPesquisa" id="codigoPesquisa"
								maxlength="255" style="width: 80px; float: left;"
								onchange="pesquisaProdutoTipoDescontoCota.pesquisarPorCodigoProduto('#codigoPesquisa', '#produtoPesquisa', false,
								   undefined,
								   undefined);" />
							<label style="width: auto !important;">Produto:</label> <input
								type="text" name="produtoPesquisa" id="produtoPesquisa"
								maxlength="255" style="width: 160px; float: left;"
								onkeydown="pesquisaProdutoTipoDescontoCota.autoCompletarPorNomeProduto('#produtoPesquisa', false);"
								onchange="pesquisaProdutoTipoDescontoCota.pesquisarPorNomeProduto('#codigoPesquisa', '#produtoPesquisa', false,
								   undefined,
								   undefined);" />

						</div>
					</td>
					<td width="104"><span class="bt_novos"><a
							href="javascript:;" onclick="tipoDescontoController.pesquisar();"><img
								src="${pageContext.request.contextPath}/images/ico_pesquisar.png"
								border="0" /></a></span></td>
				</tr>
			</table>

		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>

		<div class="grids" style="display: none;">
			<fieldset class="fieldGrid" id="tpoGERAL" style="display: none;">
				<legend>Tipos de Desconto Geral</legend>
				<div id="gridGeral">
					<table class="tiposDescGeralGrid"></table>
				</div>
			</fieldset>
			<fieldset class="fieldGrid" id="tpoESPECIFICO" style="display: none;">
				<legend>Tipos de Desconto Específico</legend>
				<div id="gridEspecifico">
					<table class="tiposDescEspecificoGrid"></table>
				</div>
			</fieldset>
			<fieldset class="fieldGrid" id="tpoEDITOR" style="display: none;">
				<legend>Tipos de Desconto Editor</legend>
				<div id="gridEditor">
					<table class="tiposDescEditorGrid"></table>
				</div>
			</fieldset>
			<fieldset class="fieldGrid" id="tpoPRODUTO" style="display: none;">
				<legend>Tipos de Desconto Produto</legend>
				<div id="gridProduto">
					<table class="tiposDescProdutoGrid"></table>
				</div>
			</fieldset>
		</div>
	</form>

</body>