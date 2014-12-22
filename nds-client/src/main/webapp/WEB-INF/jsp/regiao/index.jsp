<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
<script type="text/javascript" src="scripts/regiao.js"></script>

<script language="javascript" type="text/javascript">

var pesquisaCota = new PesquisaCota();
var pesquisaProduto = new PesquisaProduto();

$(function(){
	regiaoController.init();
});

var produtosEscolhidosArray=new Array();
var validatorProdEscolhidos=new Array();

var cotasRankingNMaioresArray=new Array();
var numCotasRankingNM=new Array();

function removeProdutoEscohido(idx){
	produtosEscolhidosArray.splice(parseInt(idx),1);
	
	var data = {
	        total: produtosEscolhidosArray.length,    
	        page:1,
	        rows: produtosEscolhidosArray
	};
	
	
	$("#nMaioresGrid").flexAddData(data);
	
	$(".btnExcluir").each(function(idx,value){
		$(this).val(idx);
	});
}

</script>

<style type="text/css">
.porCep,.porNMaiores,.porSegmento {
	display: none;
}

.gridfaixaCep,.gridNMaiores,.gridsegmentos {
	display: none;
}

/* #row5 {
	display: none;
} */
</style>
</head>

<body>
<div class="areaBts">
	<div class="area">
		<span class="bt_novos">
			<a href="javascript:;" isEdicao="true" onclick="regiaoController.addNovaRegiao();" rel="tipsy" title="Nova Região">
				<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />
			</a>
		</span>
		<div class="grids" style="display:none;">
			<span class="bt_novos">
				<a href="javascript:;" isEdicao="true" onclick="regiaoController.addCotasRegAutomatica();" rel="tipsy" title="Região Automática">
					<img src="${pageContext.request.contextPath}/images/ico_distribuicao_bup.gif" hspace="5" border="0" />
				</a>
			</span>

			<span class="bt_novos">
				<a href="javascript:;" isEdicao="true" onclick="regiaoController.cotasLote();" rel="tipsy" title="Adicionar em Lote">
					<img src="${pageContext.request.contextPath}/images/ico_integrar.png" hspace="5" border="0" />
				</a>
			</span>

			<span class="bt_novos">
				<a href="javascript:;" isEdicao="true" onclick="regiaoController.manutencaoRegiao();" rel="tipsy" title="Manutenção">
					<img src="${pageContext.request.contextPath}/images/bt_administracao.png" hspace="5" border="0" />
				</a>
			</span>

			<span class="bt_novos">
				<a href="javascript:;" isEdicao="true" onclick="regiaoController.popupAddCotaRegiao();" rel="tipsy" title="Adicionar Cota">
					<img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" />
				</a>
			</span>
			<span class="bt_arq" id="spanArquivoRegiaoCadastradas">
				<a href="${pageContext.request.contextPath}/distribuicao/regiao/exportar?fileType=XLS" rel="tipsy" title="Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
			</span>

			<span class="bt_arq" id="spanImprimirRegiaoCadastradas">
				<a href="${pageContext.request.contextPath}/distribuicao/regiao/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
				</a>
			</span>
		</div>
	</div>
</div>

<div class="corpo">
	<br clear="all" />
	<br />

	<div class="container">

		<form action="/produto" id="excluir_form">
			<div id="dialog-confirmacao" title="Inserir cota" style="display: none;">
				<p>Confirma a inserção destas cotas na região?</p>
			</div>
		</form>

		<div id="dialog-AddProdutos" title="Incluir produtos para pesquisa de cotas." style="display: none;">
			<p>Confirma a inserção destes produtos?</p>
		</div>

		<div id="dialog-detalhes" title="Visualizando Produto" style="margin-right:0px!important; float:right!important;">
			<img id="imagemCapaEdicao" width="235" height="314" />
		</div>

		<div id="dialog-pesqCotas" title="Selecionar Cotas"
		style="display: none;">
			<fieldset style="width: 600px !important;">
				<legend>Pesquisar Cotas</legend>
				<table width="550" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="31">Cota:</td>
						<td width="99">
							<input type="text" name="textfield"
						id="textfield" style="width: 80px;" />
						</td>

						<td width="32">Nome:</td>
						<td width="250">
							<input type="text" name="textfield5"
						id="textfield6" style="width: 200px;" />
						</td>

						<td width="106">
							<span class="bt_pesquisar">
								<a
							href="javascript:;">Pesquisar</a>
							</span>
						</td>

					</tr>
				</table>
			</fieldset>

			<fieldset style="width: 600px !important; margin-top: 10px !important;">
				<legend>Selecionar Cotas</legend>
				<table class="lstCotasGrid"></table>
				<span class="bt_sellAll" style="float: right;">
					<label	for="sel">Selecionar Todos</label>
					<input type="checkbox" id="selCEP"
					name="checkAllCEP" onclick="regiaoController.checkAll();"
					style="float: left; margin-right: 25px;" />
				</span>
			</fieldset>

		</div>

		<!-- ADD POR N-MAIORES, ADD PRODUTOS -->

		<div id="dialog-addNMaiores" title="Adicionar Produtos"
		style="display: none;">
			<fieldset style="width: 680px !important;">
				<legend>Pesquisar Produtos</legend>
				<table width="588" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="36">Código:</td>
						<td width="77">
							<input type="text" name="idCodigo"	id="idCodigo" style="width: 60px;" onchange="pesquisaProduto.pesquisarPorCodigoProduto('#idCodigo','#nomeProduto', false, undefined, undefined);" />
						</td>
						<td width="40">Produto:</td>
						<td width="129">
							<input type="text" name="nomeProduto" id="nomeProduto" style="width: 120px;"
							   onkeyup="pesquisaProduto.autoCompletarPorNomeProduto('#nomeProduto');" 
		 	   		   		   onblur="pesquisaProduto.pesquisarPorNomeProduto('#idCodigo', '#nomeProduto');" />
						</td>
						<td width="68">Classificação:</td>
						<td width="150">
							<select name="select" id="comboClassificacao" style="width:140px;">
								<option selected="selected">Selecione...</option>
								<c:forEach items="${listaClassificacao}" var="classificacao">
									<option value="${classificacao.key}">${classificacao.value}</option>
								</c:forEach>
							</select>
						</td>
						<td width="20">
							<span class="bt_pesquisar">
								<a href="javascript:;" onclick="regiaoController.filtroNMaiores();">Pesquisar</a>
							</span>
						</td>
					</tr>
				</table>
			</fieldset>

			<fieldset
			style="width: 680px !important; margin-top: 10px !important;">
				<legend>Produtos</legend>
				<table id="lstProdutosGrid"></table>
			</fieldset>

		</div>

		<!-- ADD POR NMAIORES, RANKING DE COTAS -->

		<div id="dialog-rankingCotas" title="Seleção de Cotas"
		style="display: none;">
			<fieldset style="width: 600px !important;">
				<legend>Seleção de Cotas</legend>
				<table width="588" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="36">Cota:</td>
						<td width="77">
							<input type="text" name="regiao-numeroCota" id="regiao-numeroCota" style="width: 60px;" />
						</td>
						<td width="40">Nome:</td>
						<td width="129">
							<input type="text" name="regiao-nomeCota" id="regiao-nomeCota" style="width: 120px;" />
						</td>
						<td width="106">
							<span class="bt_pesquisar">
								<a href="javascript:;" onclick="regiaoController.filtroCotaRanking();">Pesquisar</a>
							</span>
						</td>
					</tr>
				</table>
			</fieldset>

			<fieldset
			style="width: 600px !important; margin-top: 10px !important;">
				<legend>Cotas</legend>

				<table id="lstCotasRankingGrid"></table>
				<span class="bt_sellAll" style="float: right;">
					<label for="sel">Selecionar Todos</label>
					<input type="checkbox" id="selTodasCotas" name="Todos" onclick="regiaoController.checkAllRankingNMaiores();"
				style="float: left; margin-right: 25px;" checked />
				</span>
			</fieldset>

		</div>

		<!-- REGIÃO AUTOMÁTICA -->
		<div id="dialog-cotas" title="Montagem de Regão Automática"
		style="display: none;">
			<fieldset style="width: 600px !important;">
				<legend>Montar Região por:</legend>

				<table width="550" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="20">
							<input type="radio" name="radio" id="radio"
						value="radio" onclick="regiaoController.filtroPorCep();" />
						</td>
						<td width="40">CEP</td>

						<td width="20">
							<input type="radio" name="radio" id="radio2"
						value="radio" onclick="regiaoController.filtroPorNMaiores();" />
						</td>
						<td width="74">N Maiores</td>

						<td width="20">
							<input type="radio" name="radio" id="radio3"
						value="radio" onclick="regiaoController.filtroPorSegmento();" />
						</td>
						<td width="336">Segmento</td>
					</tr>
				</table>

				<!-- REGIAO AUTOMÁTICA - POR CEP -->

				<div class="porCep">
					<table width="550" border="0" cellspacing="2" cellpadding="2">
						<tr>
							<td width="86">Faixa de CEP:</td>
							<td width="133">
								<input type="text" name="textfield" id="cepInicialPart1" onkeydown='onlyNumeric(event);' size="5" maxlength="5" style="width: 50px;" />
								<input type="text" name="textfield3" id="cepInicialPart2" onkeydown='onlyNumeric(event);' size="3" maxlength="3" style="width: 30px;" />
							</td>
							<td width="26">Até:</td>
							<td width="186">
								<input type="text" name="textfield4" id="cepFinalPart1" onkeydown='onlyNumeric(event);' size="5" maxlength="5" style="width: 50px;" />
								<input type="text" name="textfield4" id="cepFinalPart2" onkeydown='onlyNumeric(event);' size="3" maxlength="3" style="width: 30px;" />
							</td>
							<td width="87">
								<span class="bt_pesquisar">
									<a href="javascript:;" onclick="regiaoController.mostrarPorCep();">Pesquisar</a>
								</span>
							</td>
						</tr>
					</table>
				</div>

				<!-- REGIAO AUTOMÁTICA - POR SEGMENTO -->

				<div class="porSegmento">
					<table width="550" border="0" cellspacing="2" cellpadding="2">
						<tr>
							<td width="51">Segmento:</td>
							<td width="205">

								<select name="comboSegmento" id="comboSegmento" style="width: 180px;">
									<option option selected="selected">Selecione...</option>
									<c:forEach items="${listaSegmento}" var="segmento">
										<option value="${segmento.key}">${segmento.value}</option>
									</c:forEach>
								</select>

							</td>
							<td width="92">Qtde de Cotas:</td>
							<td width="83">
								<input type="text" onkeydown='onlyNumeric(event);' name="qtdCotas"
							id="qtdCotas" style="width: 80px;" />
							</td>
							<td width="87">
								<span class="bt_pesquisar">
									<a
								href="javascript:;"
								onclick="regiaoController.mostrarPorSegmento();">Pesquisar</a>
								</span>
							</td>
						</tr>
					</table>
				</div>
			</fieldset>

			<!-- REGIAO AUTOMÁTICA - POR CEP - GRID -->

			<fieldset style="width: 600px !important; margin-top: 10px;" class="gridfaixaCep">
				<legend>Faixa de Cep</legend>
				<table class="faixaGrid" id="faixaGrid"></table>

				<span class="bt_sellAll" style="float: right;">
					<label for="todos">Selecionar Todos</label>
					<input type="checkbox" id="todos" name="todos" onclick="regiaoController.checkAll();" style="float: left; margin-right: 25px;" checked/>
				</span>

				<span class="bt_novos">
					<a	href="javascript:;" onclick="regiaoController.add_cotas();">
						<img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" />
						Incluir
					</a>
				</span>

			</fieldset>

			<!-- REGIAO AUTOMÁTICA - POR N-MAIORES -->

			<fieldset style="width: 600px !important; margin-top: 10px;"
			class="gridNMaiores">
				<legend>N Maiores</legend>
				<table id="nMaioresGrid"></table>

				<span class="bt_novos">
					<a href="javascript:;"
				onclick="regiaoController.add_produtos();">
						<img
					src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" />
						Incluir
					</a>
				</span>
				<span style="float: right; margin-top: 5px; margin-right: 50px;">
					Qtde de Cotas: &nbsp;&nbsp;
					<input name="qtdCotasRanking" id="qtdCotasRanking" type="text" onkeydown='onlyNumeric(event);' style="width: 60px;" />

					<a href="javascript:;" onclick="regiaoController.validarDadosParaRanking();">
						<img src="${pageContext.request.contextPath}/images/ico_check.gif" border="0" />
					</a>
				</span>
			</fieldset>

			<!-- REGIAO AUTOMÁTICA - POR SEGMENTO -->

			<fieldset style="width: 600px !important; margin-top: 10px;"
			class="gridsegmentos">
				<legend>Segmento</legend>
				<table class="segmentosGrid" id="segmentosGrid"></table>

				<span class="bt_sellAll" style="float: right;">
					<label for="sel">Selecionar Todos</label>
					<input type="checkbox" id="todosSegmento"	name="todosSegmento" onchange='regiaoController.checkAllSegmento();'
						   style="float: left; margin-right: 25px;" checked />
				</span>

				<span class="bt_novos">
					<a href="javascript:;" onclick="regiaoController.add_cotas_Segmento();">
						<img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" />
						Incluir
					</a>
				</span>

			</fieldset>
		</div>

		<!-- ADICIONAR EM LOTE -->

		<div id="dialog-lote" title="Adicionar em Lote" style="display: none;">
			<fieldset style="width: 300px;">
				<legend>Adicionar em Lote</legend>
				<form name="arquivoUpLoad" id="arquivoUpLoad" method="post" enctype="multipart/form-data">
					<table width="200" border="0" cellspacing="2" cellpadding="2">
						<tr>
							<p>
								Utilize o modelo de exemplo para fazer upload para o sistema:
							</p>
							<p >
								<span class="bt_novos" title="Download Modelo">
									<a href="${pageContext.request.contextPath}/modelos/modelo_regiao.xls">
										<img align="center" src="images/ico_excel.png" hspace="5" border="0" />
										Modelo de exemplo
									</a>
								</span>
							</p>
							<br>
							<br>
							<br>
							<hr>
							<p>Selecione um arquivo para upload:</p>
								<input type="file" id="xls" name="xls" />
						</tr>
					</table>
				</form>
				<div id="example2grid" class="dataTable" style="background: #FFF;"></div>
			</fieldset>
		</div>

		<!-- REGIÃO AUTOMÁTICA - MONTAGEM DA REGIAO -->

		<div id="dialog-regiaoAutomatica" title="Montagem Região Automática"
		style="display: none;">
			<fieldset style="width: 400px !important;">
				<legend>Adicionar Cotas</legend>
				<table class="regioesCadastradasGrid"></table>
			</fieldset>

			<fieldset style="width: 400px !important; margin-top: 5px;">
				<legend>Selecionar Cotas</legend>
				<table class="addCotasGrid"></table>
				<span class="bt_novos">
					<a href="javascript:;"
				onclick="regiaoController.addNovaRegiao();">
						<img
					src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" />
						Nova Região
					</a>
				</span>
			</fieldset>
		</div>

		<!-- REGIAO -->

		<div id="dialog-novo" title="Regiões Cadastradas" style="display: none;">
			<fieldset style="width: 600px !important;">
				<legend>Regiões Cadastradas</legend>
				<table class="regioesCadastradasGrid"></table>
			</fieldset>
		</div>

		<!-- NOVA REGIAO -->

		<div id="dialog-addRegiao" title="Nova Região" style="display: none;">
			<fieldset style="width: 300px !important;">
				<legend>Região</legend>
				<table width="270" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="44">Nome:</td>
						<td width="212">
							<input name="nomeRegiao" id="nomeRegiao"
						type="text" style="width: 200px;" />
						</td>
					</tr>
					<tr>
						<td align="right">
							<input name="regiaoIsFixa" id="regiaoIsFixa"
						type="checkbox" value="checked" />
						</td>
						<td>Região Fixa</td>
					</tr>
				</table>
			</fieldset>
		</div>
		<form id="cadastroCotaNaRegiao" >
			<div id="dialog-addCota" title="Nova Cota" style="display: none;">
				<table width="500" border="0" cellpadding="2" cellspacing="1" class="filtro" id="idCotas"></table>
				<div class="linha_separa_fields">&nbsp;</div>
			</form>
		</div>

		<!-- EXCLUIR REGIAO -->

		<div id="dialog-excluir" title="Excluir Região">
			<p>Confirma a exclusão desta Região?</p>
		</div>

		<div id="dialog-excluirCota" title="Excluir Cota" style="display:none;">
			<p>Confirma a exclusão desta Cota?</p>
		</div>

		<div id="dialog-alterarRegiao" title="Alterar Região" style="display:none;">
			<p>Confirma a alteração desta Região?</p>
		</div>

		<!-- EDITAR REGIAO -->

		<div id="dialog-editar" title="Editar região"></div>

		<!-- COMBO DE REGIÕES -->

		<fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
			<legend>Pesquisar Região</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="48">Região:</td>
					<td width="752">
						<select name="comboRegioes" id="comboRegioes" style="width: 250px;"
									onchange="$('.grids').toggle(); regiaoController.carregarRegiao()">
							<option selected="selected">Selecione...</option>
							<c:forEach items="${listaRegiao}" var="regiao">
								<option value="${regiao.key}" label="${regiao.value}" />
							</c:forEach>
						</select>
					</td>

					<td width="134"></td>
				</tr>
			</table>
		</fieldset>

		<!-- COTAS DA REGIAO -->

		<div class="linha_separa_fields">&nbsp;</div>
		<div class="grids" style="display: none;">
			<fieldset class="classFieldset">
				<legend>Cotas por Região</legend>

				<table class="cotasRegiaoGrid"></table>

			</fieldset>
		</div>
	</div>
</body>