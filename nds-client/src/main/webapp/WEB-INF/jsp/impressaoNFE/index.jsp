<head>
<script type="text/javascript" src="scripts/impressaoNfe.js"></script>
<script language="javascript" type="text/javascript">
	$(function() {
		impressaoNfeController.init();
	});
</script>

</head>

<body>

	<div class="container">

		<fieldset class="classFieldset">
			<legend> Pesquisar NF-e</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="82">Tipo de Nota:</td>
					<td width="209"><select name="tipoNFe" id="tipoNFe" style="width: 200px; font-size: 11px !important">
							<option value="-1">Selecione...</option>
							<c:forEach items="${tipoNotas}" var="tipoNota">
								<option value="${tipoNota.key}">${tipoNota.value}</option>
							</c:forEach>
					</select></td>
					<td width="97">Data Movimento:</td>
					<td width="238"><input name="dataMovimentoInicial" type="text" id="dataMovimentoInicial" style="width: 76px;" />
						&nbsp;&nbsp;Até&nbsp; <input name="dataMovimentoFinal" type="text" id="dataMovimentoFinal" style="width: 76px;" /></td>
					<td width="83">Data Emissão:</td>
					<td width="210"><input name="dataEmissao" type="text" id="dataEmissao" style="width: 80px;"
						value="${dataAtual}" /></td>
				</tr>
				<tr>
					<td>Roteiro:</td>
					<td><select name="idRoteiro" id="idRoteiro" style="width: 200px; font-size: 11px !important"
						onchange="impressaoNfeController.carregarRotas();">
							<option value="-1">Selecione...</option>
							<c:forEach items="${roteiros}" var="roteiro">
								<option value="${roteiro.id}">${roteiro.descricaoRoteiro}</option>
							</c:forEach>
					</select></td>
					<td>Rota:</td>
					<td>
						<div id="rotaContainer">
							<select name="idRota" id="idRota" style="width: 200px; font-size: 11px !important">
								<option value="-1">Selecione...</option>
								<c:forEach items="${rotas}" var="rota">
									<option value="${rota.id}">${rota.descricaoRota}</option>
								</c:forEach>
							</select>
						</div>
					</td>
					<td>Tipo Emissão:</td>
					<td><select name="tipoEmissao" id="tipoEmissao" style="width: 210px; font-size: 11px !important">
							<option selected="selected">Selecione...</option>
							<c:forEach items="${tipoEmissao}" var="tipoEmissao">
								<option value="${tipoEmissao}">${tipoEmissao.descricao}</option>
							</c:forEach>

					</select></td>
				</tr>
				<tr>
					<td>Cota de:</td>
					<td><input type="text" name="idCotaInicial" id="idCotaInicial" style="width: 80px;" /> &nbsp;Até&nbsp; <input
						type="text" name="idCotaFinal" id="idCotaFinal" style="width: 80px;" /></td>
					<td>Intervalo Box:</td>
					<td><input type="text" name="idBoxInicial" id="idBoxInicial" style="width: 76px;" /> &nbsp;Até &nbsp; <input
						type="text" name="idBoxFinal" id="idBoxFinal" style="width: 76px;" /></td>
					<td>Fornecedor:</td>
					<td><a href="#" id="selFornecedor">Clique e Selecione o Fornecedor</a>
						<div id="menuFornecedores" class="menu_fornecedor" style="display: none;">
							<span class="bt_sellAll"> <input type="checkbox" name="selecionarTodosFornecedores"
								id="selecionarTodosFornecedores" onclick="impressaoNfeController.checkTodosFornecedores();" style="float: left;" />
								<label for="sel">Selecionar Todos</label>
							</span> <br clear="all" />
							<c:forEach items="${fornecedores}" var="fornecedor">
								<input id="fornecedor_${fornecedor.id}" value="${fornecedor.id}" name="idsFornecedores"
									onclick="verifyCheck($('#checkBoxSelecionarTodosFornecedores'));" type="checkbox" />
								<label for="fornecedor_${fornecedor.id}">${fornecedor.juridica.razaoSocial}</label>
								<br clear="all" />
							</c:forEach>
						</div></td>
				</tr>
				<tr>
					<td>Produtos:</td>
					<td><a href="#" id="selProdutos">Clique e Selecione os Produtos</a>
						<div id="menuProdutos" class="menu_produtos" style="display: none;">
							<span class="bt_sellAll"> <input type="checkbox" name="selecionarTodosProd" id="selecionarTodosProd"
								onclick="impressaoNfeController.checkTodosProdutos();" style="float: left;" /> <label for="sel">Selecionar
									Todos</label>
							</span> <br clear="all" />
							<c:forEach items="${produtos}" var="produto">
								<input id="produto_${produto.codigoProduto}" value="${produto.codigoProduto}" name="codigosProdutos"
									onclick="verifyCheck($('#checkBoxSelecionarTodosProduto'));" type="checkbox" />
								<label for="produto_${produto.codigoProduto}">${produto.nomeProduto}</label>
								<br clear="all" />
							</c:forEach>
						</div></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td><span class="bt_pesquisar"> <a href="javascript:;" onclick="impressaoNfeController.pesquisar();">Pesquisar</a>
					</span></td>
				</tr>
			</table>
		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
			<legend>Impressão NF-e</legend>
			<div class="grids" style="display: none;">
				<table class="impressaoGrid"></table>

				<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="images/ico_excel.png"
						hspace="5" border="0" />Arquivo</a></span> <span class="bt_novos" title="Imprimir"><a href="../nota_envio.html"
					target="_blank"><img src="images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir Documento</a></span>
				<span class="bt_sellAll" style="float: right;" id="btSel"><label for="sel">Selecionar Todos</label><input
					type="checkbox" id="selTodasAsCotas" name="Todos" onclick="impressaoNfeController.checkTodasAsCotas();" style="float: left; margin-right: 30px;" /></span>
			</div>

		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>

		<div id="form-pesqProdutos">
		<div id="dialog-pesqProdutos" title="Pesquisar Produtos" style="display: none;">
			<fieldset style="width: 400px !important;">
				<legend>Pesquisar Produto</legend>
				<table width="380" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="36">Código:</td>
						<td width="88"><input type="text" id="dialog-pesqProdutos-codigoProduto" name="dialog-pesqProdutos-codigoProduto" style="width: 80px;" /></td>
						<td width="45">Produto:</td>
						<td width="180"><input type="text" id="dialog-pesqProdutos-nomeProduto" name="dialog-pesqProdutos-nomeProduto"  style="width: 180px;" /></td>
						<td width="15"><span class="classPesquisar"> <a href="javascript:this.filtrarProdutos($('#dialog-pesqProdutos-codigoProduto', this.workspace).val(), $('#dialog-pesqProdutos-nomeProduto', this.workspace).val());"> </a>
						</span></td>
					</tr>
				</table>
			</fieldset>

			<fieldset style="width: 400px !important; margin-top: 5px;">
				<legend>Produtos Cadastrados</legend>
				<table class="produtosPesqGrid"></table>
				<table width="175" border="0" align="right" cellpadding="0" cellspacing="0">
				    <tr>
				        <td width="140" align="right"><label for="selTodos">Selecionar Todos</label></td>
				        <td width="65" align="left" valign="top"><span class="bt_sellAll"><input type="checkbox" id="selecionarTodosProdutosCheck" name="selecionarTodosProdutosCheck" onclick="impressaoNfeController.checkTodosProdutos(this.checked);"/></span></td>
				    </tr>
				</table>
			</fieldset>
			<fieldset style="width: 400px !important; margin-top: 5px;">
			<legend>Produtos Filtrados</legend>
				<table class="produtosAdicionadosPesqGrid"></table>
			</fieldset>
			
		</div>
		</div>

	</div>

</body>
</html>
