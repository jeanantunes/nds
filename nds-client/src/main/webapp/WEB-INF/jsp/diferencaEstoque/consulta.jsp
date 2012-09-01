<head>
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
			
	<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

	<script type="text/javascript"
			src="${pageContext.request.contextPath}/scripts/diferencaEstoque.js"></script>

	<script type="text/javascript">
	
		var pesquisaProdutoConsultaFaltasSobras = new PesquisaProduto();
	
	</script>
</head>

<body>
	<fieldset class="classFieldset">
		<legend>Pesquisar Faltas e Sobras</legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td id="teste" width="59" title="tooltip teste">Código:</td>
				<td colspan="3">
					<input type="text" name="codigo" id="codigo"
						   style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
						   onchange="pesquisaProdutoConsultaFaltasSobras.pesquisarPorCodigoProduto('#codigo', '#produto', '#edicao', false,
								   									   pesquisarProdutosSuccessCallBack,
								   									   pesquisarProdutosErrorCallBack);" />
					
				</td>
				<td width="60">Produto:</td>
				<td width="220">
					<input type="text" name="produto" id="produto" style="width: 200px;" maxlength="255"
					       onkeyup="pesquisaProdutoConsultaFaltasSobras.autoCompletarPorNomeProduto('#produto', false);"
					       onblur="pesquisaProdutoConsultaFaltasSobras.pesquisarPorNomeProduto('#codigo', '#produto', '#edicao', false,
					       											  pesquisarProdutosSuccessCallBack,
					       											  pesquisarProdutosErrorCallBack);"/>
				</td>
				
				<td width="50" align="right">Edição:</td>
				<td width="90">
					<input type="text" style="width:70px;" name="edicao" id="edicao" maxlength="20" disabled="disabled"
						   onchange="pesquisaProdutoConsultaFaltasSobras.validarNumEdicao('#codigo', '#edicao', false);"/>
				</td>
				
				<td width="73">Fornecedor:</td>
				<td width="230" colspan="2">
					<select name="fornecedor" id="fornecedor" style="width: 200px;">
						<option selected="selected" value="">Todos</option>
						<c:forEach var="fornecedor" items="${listaFornecedores}">
							<option value="${fornecedor.key}">${fornecedor.value}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
		<table width="950" border="0" cellspacing="2" cellpadding="2"
			class="filtro">
			<tr>
				<td width="100">Período de Data:</td>
				<td width="108">
					<input type="text" name="dataInicial" id="dataInicial" style="width: 80px;" value="${dataAtual}" />
				</td>
				<td width="33" align="center">Até</td>
				<td width="147">
					<input type="text" name="dataFinal" id="dataFinal" style="width: 80px;" value="${dataAtual}" />
				</td>
				<td width="134" align="right">Tipo de Diferença:</td>
				<td width="169">
					<select name="tipoDiferenca" id="tipoDiferenca" style="width: 120px;">
						<option selected="selected" value="">Todos</option>
						<c:forEach var="tipoDiferenca" items="${listaTiposDiferenca}">
							<option value="${tipoDiferenca.key}">${tipoDiferenca.value}</option>
						</c:forEach>
					</select>
				</td>
				<td width="137">
					<span class="bt_pesquisar" title="Pesquisar">
						<a href="javascript:;" onclick="diferencaEstoqueController.pesquisar();">Pesquisar</a>
					</span>
				</td>
			</tr>
		</table>
	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="classFieldset">
		<legend>Faltas e Sobras Cadastradas</legend>
		<div class="grids" style="display: none;">
			<table class="consultaFaltasSobrasGrid"></table>
			
			<table width="100%" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="70%">
						<span class="bt_novos" title="Gerar Arquivo">
							<a href="${pageContext.request.contextPath}/estoque/diferenca/exportar?fileType=XLS">
								<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
								Arquivo
							</a>
						</span>
						<span class="bt_novos" title="Imprimir">
							<a href="${pageContext.request.contextPath}/estoque/diferenca/exportar?fileType=PDF">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
								Imprimir
							</a>
						</span>
					</td>
					<td width="5%"><strong>Total:</strong></td>
				    <td id="qtdeTotalDiferencas" width="7%" align="right" />
				    <td id="valorTotalDiferencas" width="16%" align="right" />
				    <td width="2%">&nbsp;</td>
				</tr>
			</table>
		</div>

	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>
</body>