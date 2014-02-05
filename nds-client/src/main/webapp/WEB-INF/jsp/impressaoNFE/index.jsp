<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" src="scripts/impressaoNfe.js"></script>

<script language="javascript" type="text/javascript">
	$(function() {
		impressaoNfeController.init();
	});
</script>

</head>

<body>

<div class="areaBts">
	<div class="area">
		<span class="bt_novos"><a isEdicao="true" href="javascript:;" id="impressaoNfe-btnGerar" rel="tipsy" title="Confirma  Gera&ccedil;&atilde;o de Nf-e?" ><img src="${pageContext.request.contextPath}/images/ico_check.gif" width="16" height="16" border="0" hspace="5" /></a></span>

        <span class="bt_arq"><a href="javascript:;" id="impressaoNfe-btnImprimirXLS" title="Gerar Arquivo" onclick="impressaoNfeController.imprimir('XLS');" rel="bandeira"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a></span>

        <span class="bt_arq"><a href="javascript:;" id="impressaoNfe-btnImprimirPDF" title="Imprimir" onclick="impressaoNfeController.imprimir('PDF');"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" /></a></span>	
	</div>
</div>

<div class="container">

	<fieldset class="classFieldset">
		<legend>Pesquisar NF-e</legend>
		<table width="100%" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="91">Destinatário:</td>
   				<td width="180">
   					<c:forEach items="${tiposDestinatarios}" var="tipoDestinatario" varStatus="status" >
   						<input type="radio" name="tipoDestinatario" id="tipoDestinatario${status.index}" value="${tipoDestinatario}" <c:if test="${status.index == 0}">checked="checked"</c:if> onchange="impressaoNfeController.verificarTipoDestinatario(this);" /> ${tipoDestinatario.descricao}
   					</c:forEach>
				</td>
   				<td colspan=2>
   					<select id="impressaoNfe-filtro-selectFornecedoresDestinatarios" name="selectFornecedores" multiple="multiple" style="width:300px">
						<c:forEach items="${fornecedoresDestinatarios}" var="fornecedor">
							<option value="${fornecedor.key }">${fornecedor.value }</option>
						</c:forEach>
					</select>
   				</td>
				<td width="80"></td>
				<td width="212"></td>
			</tr>
			<tr>
				<td width="91">Natureza Op:</td>
   				<td width="204">
					<select id="impressaoNfe-filtro-naturezaOperacao" name="naturezaOperacao" style="width:200px; font-size:11px!important" title="">
						<option value="">Todos</option>
						
					</select>
				</td>
				<td width="97">Data Movimento:</td>
				<td width="238">
					<input name="dataMovimentoInicial" type="text" id="dataMovimentoInicial" style="width: 76px;" maxlength="10" />
					&nbsp;&nbsp;Até&nbsp; 
					<input name="dataMovimentoFinal" type="text" id="dataMovimentoFinal" style="width: 76px;" maxlength="10" />
				</td>
				<td width="83">Data Emissão:</td>
				<td width="210">
					<input name="dataEmissao" type="text" id="dataEmissao" style="width: 80px;" value="${dataAtual}" maxlength="10" />
				</td>
			</tr>
			<tr>
				<td>Roteiro:</td>
				<td>
					<select name="idRoteiro" id="idRoteiro" style="width: 200px; font-size: 11px !important" onchange="impressaoNfeController.carregarRotas();">
						<option value="-1">Selecione...</option>
						<c:forEach items="${roteiros}" var="roteiro">
							<option value="${roteiro.key }">${roteiro.value }</option>
						</c:forEach>
					</select>
				</td>
				<td>Rota:</td>
				<td>
					<div id="rotaContainer">
						<select name="idRota" id="idRota" style="width: 200px; font-size: 11px !important">
							<option value="-1">Selecione...</option>
							<c:forEach items="${rotas}" var="rota">
								<option value="${rota.key }">${rota.value }</option>
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
					</td>
			</tr>
			<tr>
				<td>Produtos:</td>
				<td><a href="javascript:;" id="selProdutos">Clique e Selecione os Produtos</a>
					<div id="menuProdutos" class="menu_produtos" style="display: none;">
						<span class="bt_sellAll"> 
							<input type="checkbox" name="selecionarTodosProd" id="selecionarTodosProd" onclick="impressaoNfeController.checkTodosProdutos();" style="float: left;" />
							<label for="sel">Selecionar Todos</label>
						</span> 
						<br clear="all" />
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
				<td>
					<span class="bt_pesquisar"> 
						<a href="javascript:;" onclick="impressaoNfeController.pesquisar();">Pesquisar</a>
					</span>
				</td>
			</tr>
		</table>
	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="classFieldset">
		<legend>Impressão NF-e</legend>
		<div class="grids" style="display: none;">
		
			<table class="impressaoGrid"></table>

			<span class="bt_sellAll" style="float: right;" id="btSel"><label for="sel">Selecionar Todos</label><input
				type="checkbox" id="selTodasAsNotas" name="Todos" onclick="impressaoNfeController.checkTodasAsNotas();" style="float: left; margin-right: 30px;" /></span>
		</div>

	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>

	<div id="msgBoxDataMovimentoInvalida" title="Data de Movimento inválida.">
	    <p>É necessário informar uma Data de Movimento válida.</p>
	</div>
	
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
					<td width="15"><span class="classPesquisar"> <a href="#" onclick="impressaoNfeController.filtrarProdutos($('#dialog-pesqProdutos-codigoProduto', this.workspace).val(), $('#dialog-pesqProdutos-nomeProduto', this.workspace).val());"> </a>
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
