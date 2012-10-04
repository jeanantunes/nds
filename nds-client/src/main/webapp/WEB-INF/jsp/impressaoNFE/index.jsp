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
			<table width="950" border="0" cellpadding="2" cellspacing="1"
				class="filtro">
				<tr>
					<td width="82">Tipo de Nota:</td>
					<td width="209"><select name="tipoNotaFiscal" id="tipoNotaFiscal"
						style="width: 200px; font-size: 11px !important">
							<option value="-1">Selecione...</option>
							<c:forEach items="${tipoNotas}" var="tipoNota">
								<option value="${tipoNota.key}">${tipoNota.value}</option>
							</c:forEach>
					</select></td>
					<td width="97">Data Movimento:</td>
					<td width="238"><input name="dataInicialMovimento" type="text"
						id="dataInicialMovimento" style="width: 76px;" />
						&nbsp;&nbsp;Até&nbsp; <input name="dataFinalMovimento" type="text"
						id="dataFinalMovimento" style="width: 76px;" /></td>
					<td width="83">Data Emissão:</td>
					<td width="210"><input name="dataEmissao"
						type="text" id="dataEmissao" style="width: 80px;"
						value="${dataAtual}" /></td>
				</tr>
				<tr>
					<td>Roteiro:</td>
					<td><select name="idRoteiro" id="idRoteiro"
						style="width: 200px; font-size: 11px !important">
							<option value="-1">Selecione...</option>
							<c:forEach items="${roteiros}" var="roteiro">
								<option value="${roteiro.id}">${roteiro.descricaoRoteiro}</option>
							</c:forEach>
					</select></td>
					<td>Rota:</td>
					<td><select name="idRota" id="idRota"
						style="width: 200px; font-size: 11px !important">
							<option>Selecione...</option>
							<c:forEach items="${rotas}" var="rota">
								<option value="${rota.id}">${rota.descricaoRota}</option>
							</c:forEach>
					</select></td>
					<td>Tipo Emissão:</td>
					<td><select name="select5" id="select5"
						style="width: 210px; font-size: 11px !important">
							<option selected="selected">Selecione...</option>
							<c:forEach items="${tipoEmissao}" var="tipoEmissao">
								<option value="${tipoEmissao}">${tipoEmissao.descricao}</option>
							</c:forEach>

					</select></td>
				</tr>
				<tr>
					<td>Cota de:</td>
					<td><input type="text" style="width: 80px;" />
						&nbsp;Até&nbsp; <input type="text" style="width: 80px;" /></td>
					<td>Intervalo Box:</td>
					<td><input type="text" style="width: 76px;" /> &nbsp;Até
						&nbsp; <input type="text" style="width: 76px;" /></td>
					<td>Fornecedor:</td>
					<td><a href="#" id="selFornecedor">Clique e Selecione o
							Fornecedor</a>
						<div class="menu_fornecedor" style="display: none;">
							<span class="bt_sellAll"> <input type="checkbox" id="sel"
								name="Todos1" onclick="checkAll_fornecedor();"
								style="float: left;" /> <label for="sel">Selecionar
									Todos</label>
							</span> <br clear="all" />
							<c:forEach items="${fornecedores}" var="fornecedor">
								<input id="fornecedor_${fornecedor.id}" value="${fornecedor.id}"
									name="checkGroupFornecedores"
									onclick="verifyCheck($('#checkBoxSelecionarTodosFornecedores'));"
									type="checkbox" />
								<label for="fornecedor_${fornecedor.id}">${fornecedor.juridica.razaoSocial}</label>
								<br clear="all" />
							</c:forEach>
						</div></td>
				</tr>
				<tr>
					<td>Produtos:</td>
					<td><a href="#" id="selProdutos">Clique e Selecione os
							Produtos</a>
						<div class="menu_produtos" style="display: none;">
							<span class="bt_sellAll"> <input type="checkbox" id="sel"
								name="Todos1" onclick="checkAll_produtos();"
								style="float: left;" /> <label for="sel">Selecionar
									Todos</label>
							</span> <br clear="all" />
							<c:forEach items="${produtos}" var="produto">
								<input id="produto_${produto.id}" value="${produto.id}"
									name="checkGroupProduto"
									onclick="verifyCheck($('#checkBoxSelecionarTodosProduto'));"
									type="checkbox" />
								<label for="produto_${produto.id}">${produto.nome}</label>
								<br clear="all" />
							</c:forEach>
						</div></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td><span class="bt_pesquisar"> <a href="javascript:;"
							onclick="impressaoNfeController.pesquisar();">Pesquisar</a>
					</span></td>
				</tr>
				<td colspan="3"></td>
			</table>
		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
			<legend>Impressão NF-e</legend>
			<div class="grids" style="display: none;">
				<table class="impressaoGrid"></table>

				<span class="bt_novos" title="Gerar Arquivo"><a
					href="javascript:;"><img src="images/ico_excel.png" hspace="5"
						border="0" />Arquivo</a></span> <span class="bt_novos" title="Imprimir"><a
					href="../nota_envio.html" target="_blank"><img
						src="images/ico_impressora.gif" alt="Imprimir" hspace="5"
						border="0" />Imprimir Documento</a></span> <span class="bt_sellAll"
					style="float: right;" id="btSel"><label for="sel">Selecionar
						Todos</label><input type="checkbox" id="sel" name="Todos"
					onclick="checkAll();" style="float: left; margin-right: 30px;" /></span>
			</div>

		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>

	</div>

</body>
</html>
