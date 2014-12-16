<head>
<title>Relatório de Vendas</title>
<style type="text/css">
.linhaCota, .linhaProduto, .linhaSegmentacao{display:none;}
.filtro label{width:auto!important; margin-bottom: 0px!important;  margin-top: 4px!important; margin-left: 0px!important; margin-right: 0px;!important}
</style>
<script language="javascript" type="text/javascript" src='scripts/jquery.numeric.js'></script>
<script language="javascript" type="text/javascript" src='scripts/relatorioVendas.js'></script>
<script language="javascript" type="text/javascript" src='scripts/jquery.justInput.js'></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produtoEdicao.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>

<script language="javascript" type="text/javascript">

$(function(){
	relatorioVendasController.init();

});

var pesquisaCotaFiltroConsulta = new PesquisaCota(relatorioVendasController.workspace);

</script>
</head>
<body>
	
	<input id="pathExportarRelatorioVendas" type="hidden" value="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?"/>

	<form id="form_dialog-editor">
		<div id="dialog-editor" title="Histórico de Produtos" style="display:none;">
		<fieldset style="width:810px;">
			<legend>Editor: <span name="nomeEditorPopUp" id="nomeEditorPopUp"></span></legend>
		    <table class="popEditorGrid"></table>
		        <span class="bt_novos" title="Gerar Arquivo"><a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=XLS&tipoRelatorio=5"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
				<span class="bt_novos" title="Imprimir"><a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=PDF&tipoRelatorio=5"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
		</fieldset>
		</div>
	</form>
	
	<div class="container">

	<div class="areaBts">
		<div class="area">

			<span class="bt_arq" title="Gerar Arquivo">
			<a class="impressaoXLSRelatorioVendas" href="" rel="tipsy" title="Gerar Arquivo">
				<img src="${pageContext.request.contextPath}/images/ico_excel.png"
				hspace="5" border="0" />
			</a> 
			</span> 

			<span class="bt_arq" title="Imprimir"> 
			<a class="impressaoPDFRelatorioVendas" href="" rel="tipsy" title="Imprimir">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif"
				hspace="5" border="0" /> 
			</a>
			</span>
		</div>
	</div>

	<br/><br/>

	<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
			<legend> Relatório de Vendas</legend>
			<table width="100%" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="20"><input type="radio" name="filtro" id="filtro_distrib" onclick="relatorioVendasController.mostra_distrib();relatorioVendasController.limparFiltros();" value="radio" /></td>
					<td><label for="filtro_distrib">Curva ABC Distribuidor</label></td>
					<td width="20"><input type="radio" name="filtro" id="filtro_editor" value="radio" onclick="relatorioVendasController.mostra_editor();relatorioVendasController.limparFiltros();" /></td>
					<td><label for="filtro_editor">Curva ABC Editor</label></td>
					<td width="20"><input type="radio" name="filtro" id="filtro_produto" onclick="relatorioVendasController.mostra_produto();relatorioVendasController.limparFiltros();" value="radio" /></td>
					<td><label for="filtro_produto">Curva ABC Produto</label></td>
					<td width="20" align="right"><input type="radio" name="filtro" id="filtro_cota" value="radio" onclick="relatorioVendasController.mostra_cota();relatorioVendasController.limparFiltros();" /></td>
					<td><label for="filtro_cota">Curva ABC Cota</label></td>
					
					<td width="20" align="right"><input type="radio" name="filtro" id="filtro_segmentacao" value="radio" onclick="relatorioVendasController.mostra_segmentacao();relatorioVendasController.limparFiltros();" /></td>
					<td><label for="filtro_segmentacao">Segmento</label></td>
										
					
					<td width="45">Período:</td>
					<td><input type="text" name="datepickerDe" id="datepickerDe" style="width: 60px;" /></td>
					<td width="25">Até:</td>
					<td><input type="text" name="datepickerAte" id="datepickerAte" style="width: 60px;" /></td>
					<td width="95" rowspan="3" valign="top"><span class="bt_pesquisar"><a href="javascript:;" onclick="relatorioVendasController.pesquisar();">Pesquisar</a></span></td>
					<td width="20" rowspan="3" align="center" valign="top"><a
						href="javascript:;" onclick="relatorioVendasController.mostra_pesq_avancada();"><img src="${pageContext.request.contextPath}/images/ico_pesq_avancada.jpg" alt="Pesquisa Avançada" width="20" height="20" vspace="10" border="0" /></a></td>
				</tr>
				<tr class="linhaCota">
					<td>&nbsp;</td>
					<td colspan="3">&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td colspan="7">
						<label>Cota:</label> 
							<input type="text" style="width: 80px; float: left; margin: 5px;" 
								   name="numeroCotaListaCota" id="numeroCotaListaCota"
								   onchange="pesquisaCotaFiltroConsulta.pesquisarPorNumeroCota('#numeroCotaListaCota', '#nomeCotaListaCota',false,null,null);" /> 
						<label>Nome:</label>
						<input type="text" style="width: 180px; float: left; margin: 5px;" name="nomeCotaListaCota" id="nomeCotaListaCota" />
					</td>
				</tr>
				<tr class="linhaProduto">
					<td>&nbsp;</td>
					<td colspan="3">&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td colspan="8">
						<label>Código:</label> 
							<input type="text" style="width: 80px; float: left; margin: 5px;" 
								   id="codigoProdutoListaProduto" name="codigoProdutoListaProduto"
								   onchange="produtoEdicaoController.pesquisarPorCodigoProduto('#codigoProdutoListaProduto', '#nomeProdutoListaProduto', false,
											undefined,
											undefined);" /> 
						<label>Produto:</label> <input type="text" style="width: 200px; float: left; margin: 5px;" id="nomeProdutoListaProduto" name="nomeProdutoListaProduto" />
					</td>
				</tr>
				
				<tr class="linhaSegmentacao">
					<td>&nbsp;</td>
					<td colspan="3">&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td colspan="9">
						<div style="float:right;">
							<label>Segmento:</label> 
							<select id="selectSegmentacao">
								<c:forEach items="${segmentacoes}" var="segmentacao">
									<option value="${segmentacao.id}">${segmentacao.descricao}</option>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr>
			</table>
		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset fieldFiltroItensNaoBloqueados" id="pesquisaAvancada" style="display: none;">
			<legend>Busca Avançada</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="69">Fornecedor:</td>
					<td width="255">
						<select name="select" id="selectFornecedor" style="width: 240px;">
							<option>Todos</option>
                    		<c:forEach items="${fornecedores}" var="fornecedor">
								<option value="${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</option>
                   			</c:forEach> 
						</select>
					</td>
					<td width="47" colspan="-1">Código:</td>
					<td width="108">
						<input type="text" style="width: 80px;" name="rel-vendas-codigoProduto" id="rel-vendas-codigoProduto"
							   onchange="produtoEdicaoController.pesquisarPorCodigoProduto('#rel-vendas-codigoProduto', '#rel-vendas-nomeProduto', false,
											undefined,
											undefined);" />
					</td>
					<td width="52">Produto:</td>
					<td width="213">
						<input type="text" style="width: 200px;" name="rel-vendas-nomeProduto" id="rel-vendas-nomeProduto" />
					</td>
					<td width="41">Edição:</td>
					<td>
						<input type="text" style="width: 100px;" name="edicaoProduto" id="edicaoProduto" />
					</td>
					<td>
						<a href="javascript:;" onclick="relatorioVendasController.esconde_pesq_avancada();">
							<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Fechar" width="15" height="15" border="0" />
						</a>
					</td>
				</tr>
				<tr>
					<td>Editor:</td>
					<td><select name="select2" id="selectEditor" style="width: 240px;">
							<option>Todos</option>
                    		<c:forEach items="${editores}" var="editor">
								<option value="${editor.id}">${editor.pessoaJuridica.razaoSocial}</option>
                   			</c:forEach> 
					</select></td>
					<td colspan="-1">Cota:</td>
					<td>
						<input type="text" name="rel-vandas-numeroCota" id="rel-vandas-numeroCota" style="width: 80px;"
							   onchange="pesquisaCotaFiltroConsulta.pesquisarPorNumeroCota('#rel-vendas-numeroCota', '#rel-vendas-nomeCota',false,null,null);" />
					</td>
					<td>Nome:</td>
					<td><input type="text" style="width: 200px;" id="rel-vendas-nomeCota" name="rel-vendas-nomeCota" /></td>
					<td>&nbsp;</td>
					<td width="104">&nbsp;</td>
					<td width="15">&nbsp;</td>
				</tr>
				<tr>
					<td>Municipio:</td>
					<td><select name="selectMunicipio" id="selectMunicipio" style="width: 240px;">
							<option selected="selected">Todos</option>
                    		<c:forEach items="${municipios}" var="municipio">
								<option value="${municipio}">${municipio}</option>
                   			</c:forEach> 
						</select>
					</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td><span class="bt_pesquisar"><a href="javascript:;" onclick="relatorioVendasController.pesquisarAvancada();">Pesquisar</a></span></td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

		<div class="grids" style="display: none;">
			<fieldset class="classFieldset" id="relatorioDistribuidor" style="display: none;">
				<legend>Curva ABC Distribuidor</legend>
				<table class="abcDistribuidorGrid"></table>

				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="441">&nbsp;</td>
						<td width="121"><strong>Total:</strong></td>
						<td width="130"><span id="qtdeTotalVendaExemplaresDistribuidor"></span></td>
						<td width="258"><span id="totalFaturamentoCapaDistribuidor"></span></td>
					</tr>
				</table>
			</fieldset>

			<fieldset class="classFieldset" id="relatorioEditor" style="display: none;">
				<legend>Curva ABC Editor</legend>
				<table class="abcEditorGrid"></table>
				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="200">&nbsp;</td>
						<td width="80"><strong>Total:</strong></td>
						<td width="145"><span id="qtdeTotalVendaExemplaresEditor"></span></td>
						<td width="326"><span id="totalFaturamentoCapaEditor"></span></td>
					</tr>
				</table>
			</fieldset>

			<fieldset class="classFieldset" id="relatorioProduto" style="display: none;">
				<legend>Curva ABC Produto</legend>
				<table class="abcProdutoGrid"></table>
				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="441">&nbsp;</td>
						<td width="151"><strong>Total:</strong></td>
						<td width="114"><span id="qtdeTotalVendaExemplaresProduto"></span></td>
						<td width="244"><span id="totalFaturamentoCapaProduto"></span></td>
					</tr>
				</table>
			</fieldset>

			<fieldset class="classFieldset" id="relatorioCota"
				style="display: none;">
				<legend>Curva ABC Cota</legend>
				<table class="abcCotaGrid"></table>
				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="432">&nbsp;</td>
						<td width="73"><strong>Total:</strong></td>
						<td width="205"><span id="qtdeTotalVendaExemplaresCota"></span></td>
						<td width="240"><span id="totalFaturamentoCapaCota"></td>
					</tr>
				</table>
			</fieldset>
			
			<fieldset class="classFieldset" id="relatorioSegmentacao" style="display: none;">
				<legend>Segmentação</legend>
				<table class="segmentacaoGrid"></table>
				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="432">&nbsp;</td>
						<td width="73"><strong>Total:</strong></td>
						<td width="205"></td>
						<td width="240">R$ <span id="totalFaturamentoCapaSegmento"></td>
					</tr>
				</table>
			</fieldset>
			
			<div class="linha_separa_fields">&nbsp;</div>

			<div class="linha_separa_fields">&nbsp;</div>
		</div>

	</div>

</body>
</html>
