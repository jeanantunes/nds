<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/transferenciaEstoqueParcial.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.justInput.js"></script>

	<script language="javascript" type="text/javascript">
		var pesquisaProdutoTransferenciaEstoqueParcial = new PesquisaProduto();
	</script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
</head>
<body>
	
	<div class="areaBts">
		<div class="area">
			<span class="bt_novos">
				<a href="#" 
				   onclick="transferenciaEstoqueParcialController.transferir();" 
				   rel="tipsy"
				   title="Efetuar transferência">
					
					<img src="${pageContext.request.contextPath}/images/ico_negociar.png" border="0" />
				</a>
			</span>
		</div>
	</div>
	
	<div class="linha_separa_fields">&nbsp;</div>
	<div class="linha_separa_fields">&nbsp;</div>
	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset>
   		<legend>Produto</legend>
   		<table class="gridTransferenciaEstoqueParcial" border="0" cellspacing="1" cellpadding="1" style="width:550px;">
    		<tr>
				<td width="55" bgcolor="#F5F5F5">
					<strong>Código</strong>
				</td>
				<td width="130" bgcolor="#F5F5F5">
					<strong>Produto</strong>
				</td>
				<td width="96" align="center" bgcolor="#F5F5F5">
					<strong>Edição de Origem</strong>
				</td>
				<td bgcolor="#F5F5F5" align="center">
					<strong>Estoque</strong>
				</td>
				<td width="96" align="center" bgcolor="#F5F5F5">
					<strong>Edição de Destino</strong>
				</td>
			</tr>
			<tr>
				<td>
					<input type="text" id="codigoProdutoInput" style="width:75px;" maxlength="30" onchange="transferenciaEstoqueParcialController.limparProduto()"
						onblur="pesquisaProdutoTransferenciaEstoqueParcial.pesquisarPorCodigoProduto(codigoProdutoInput, nomeProdutoInput, edicaoProdutoInput, true);"/>
				</td>
				<td>
					<input type="text" id="nomeProdutoInput" style="width:130px;" maxlength="60" onchange="transferenciaEstoqueParcialController.limparProduto()"
						onkeyup="pesquisaProdutoTransferenciaEstoqueParcial.autoCompletarPorNomeProduto(nomeProdutoInput, true);"
						onblur="pesquisaProdutoTransferenciaEstoqueParcial.pesquisarPorNomeProduto(codigoProdutoInput, nomeProdutoInput, edicaoProdutoInput, true);"/>
				</td>
				<td align="center">
					<input type="text" id="edicaoProdutoInput" onchange="transferenciaEstoqueParcialController.buscarEstoqueProdutoEdicaoOrigem();" style="width:80px;" maxlength="255" />
				</td>
				<td align="center" id="estoqueProdutoInput"></td>
				<td align="center">
					<input type="text" id="edicaoProdutoDestinoInput" style="width:120px;" maxlength="255" />
				</td>
			</tr>
   		</table>
   	</fieldset>

</body>